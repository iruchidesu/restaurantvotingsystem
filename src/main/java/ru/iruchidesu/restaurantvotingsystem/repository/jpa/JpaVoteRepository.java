package ru.iruchidesu.restaurantvotingsystem.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaVoteRepository implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Vote save(Vote vote) {
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            return em.merge(vote);
        }
    }
    //TODO implements Vote delete
    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Vote get(int id) {
        return em.find(Vote.class, id);
    }
    //TODO implements Vote getAll
    @Override
    public List<Vote> getAll() {
        return null;
    }
}
