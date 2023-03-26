package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entity.Rating;

import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addRating(Rating rating) {
        entityManager.persist(rating);
    }

    @Override
    public List<Rating> getRating(String game) {
        return entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setMaxResults(10).getResultList();
    }

    @Override
    public int getAverageRating(String game) {
        return entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game",game).getFirstResult();
    }

    @Override
    public int getYourRating(String game, String player) {
        return entityManager.createNamedQuery("Rating.getYourRating")
                .setParameter("game",game).getFirstResult();
    }

    @Override
    public void resetRating() {
        entityManager.createNativeQuery("DELETE FROM comment").executeUpdate();
    }
}
