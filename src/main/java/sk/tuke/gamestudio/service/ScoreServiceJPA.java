package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entity.Score;

import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) {
        entityManager.persist(score);
    }

    @Override
        public List<Score> getTopScores(String game){
            return entityManager.createNamedQuery("Score.getTopScores")
                    .setParameter("game", game).setMaxResults(10).getResultList();
        }

    @Override
    public void resetScore() {
        entityManager.createNativeQuery("DELETE FROM score").executeUpdate();
    }
}
