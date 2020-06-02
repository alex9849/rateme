package de.hskl.rateme.service;

import com.google.gson.Gson;
import de.hskl.rateme.db.RatingDB;
import de.hskl.rateme.model.Poi;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.Rating;
import de.hskl.rateme.util.ImageUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;

@Singleton
public class RatingService {
    @Inject
    PoiService poiService;
    @Inject
    RatingDB ratingDB;
    @Inject
    KafkaService kafkaService;

    public void createRating(Rating rating) throws IOException {
        Poi poi = poiService.getPoi(rating.getOsmId());
        if(poi == null) {
            throw new RatemeDbException("Poi doesn't exist!");
        }
        if(rating.getImage() != null) {
            BufferedImage img = ImageUtils.readBase64Image(rating.getImage());
            double hight = 60;
            double witdh = img.getWidth() / (img.getHeight() / hight);
            img = ImageUtils.resizeImage(img, Math.min(200, (int) witdh), (int) hight);
            rating.setImage(ImageUtils.toBase64Image(img));
        }
        ratingDB.createRating(rating);
        Gson gson = new Gson();
        kafkaService.produce(rating.getId() + "", gson.toJson(rating));
    }

    public Collection<Rating> getRatingsByPoi(long osmID) {
        return ratingDB.loadRatingsForPoi(osmID);
    }

    public Collection<Rating> getRatingsByUser(int userID) {
        return ratingDB.loadRatingsForUser(userID);
    }

    public Rating getRating(int ratingid) {
        return ratingDB.loadRating(ratingid);
    }
}
