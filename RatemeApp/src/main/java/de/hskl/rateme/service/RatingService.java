package de.hskl.rateme.service;

import com.google.gson.Gson;
import de.hskl.rateme.db.RatingDB;
import de.hskl.rateme.model.Poi;
import de.hskl.rateme.model.RatemeDbException;
import de.hskl.rateme.model.Rating;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class RatingService {
    @Inject
    PoiService poiService;
    @Inject
    RatingDB ratingDB;
    @Inject
    KafkaService kafkaService;

    public void createRating(Rating rating) {
        Poi poi = poiService.getPoi(rating.getOsmId());
        if(poi == null) {
            throw new RatemeDbException("Poi doesn't exist!");
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
}
