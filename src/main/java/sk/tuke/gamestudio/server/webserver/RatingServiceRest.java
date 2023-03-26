package sk.tuke.gamestudio.server.webserver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}")
    public List<Rating> getComments(@PathVariable String game) {
        return ratingService.getRating(game);
    }

    @PostMapping
    public void addComment(@RequestBody Rating rating) {
        ratingService.addRating(rating);
    }

}
