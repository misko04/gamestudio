package sk.tuke.gamestudio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Rating.getRating",
        query = "SELECT r FROM Rating r WHERE r.game=:game ORDER BY r.ratingDate DESC")
@NamedQuery( name = "Rating.getYourRating",
        query = "SELECT r FROM Rating r WHERE r.game=:game AND r.player=:player")
@NamedQuery( name = "Rating.getAverageRating",
        query = "SELECT AVG(r) FROM Rating r WHERE r.game=:game")
@NamedQuery( name = "Rating.resetRating",
        query = "DELETE FROM Rating ")
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private long ident;
    private String player;

    private String game;

    private int rating_num;

    private Date ratingDate;


    public Rating(String player, String game, int starsNumber, Date ratingDate) {
        this.player = player;
        this.game = game;
        this.rating_num = starsNumber;
        this.ratingDate = ratingDate;
    }

    public Rating() {
    }

    public long getIdent() {
        return ident;
    }

    public void setIdent(long ident) {
        this.ident = ident;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating_num;
    }

    public void setRating(int rating) {
        this.rating_num = rating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", rating='" + rating_num + '\'' +
                ", rated at=" + ratingDate +
                '}';
    }
}
