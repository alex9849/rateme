package de.hskl.rateme.service;

import de.hskl.rateme.db.RatingDB;
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

    public void createRating(Rating rating) {
        if(poiService.getPoi(rating.getOsmId()) == null) {
            throw new RatemeDbException("Poi doesn't exist!");
        }
        if(ratingDB.loadRatingsForPoi(rating.getOsmId()).stream().anyMatch(x -> x.getOsmId() == rating.getOsmId())) {
            throw new RatemeDbException("You can rate the same poi only once!");
        }
        ratingDB.createRating(rating);
    }

    public Collection<Rating> getRatingsByPoi(long osmID) {
        return ratingDB.loadRatingsForPoi(osmID);
    }

    public Collection<Rating> getRatingsByUser(int userID) {
        return ratingDB.loadRatingsForUser(userID);
    }
}
