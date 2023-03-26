package sk.tuke.gamestudio.entity;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.commentDate DESC")
@NamedQuery( name = "Comment.resetComments",
        query = "DELETE FROM Comment ")
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private long ident;

    private String player;

    private String game;

    private String comment;

    private Date commentDate;

    public Comment() {
    }

    public Comment(String player, String game, String comment, Date commentedAt) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentDate = commentedAt;
    }

    public Long getIdent() {
        return ident;
    }

    public void setIdent(Long ident) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedAt=" + commentDate +
                '}';
    }
}


