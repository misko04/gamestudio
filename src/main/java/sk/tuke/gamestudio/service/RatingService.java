package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.util.List;

public interface RatingService {

    void addRating(Rating rating);

    List<Rating> getRating(String game);

    int getAverageRating(String game);

    public int getYourRating(String game, String player);

    void resetRating();
}
