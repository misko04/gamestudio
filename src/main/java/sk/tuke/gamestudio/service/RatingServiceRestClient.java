package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;

public class RatingServiceRestClient implements RatingService{

    private final String url = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addRating(Rating rating) {
        restTemplate.postForEntity(url,rating,Rating.class);
    }

    @Override
    public List<Rating> getRating(String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + game,Rating[].class).getBody());
    }

    @Override
    public int getAverageRating(String game) {
        return 0;
    }

    @Override
    public int getYourRating(String game, String player) {
        return 0;
    }

    @Override
    public void resetRating() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
