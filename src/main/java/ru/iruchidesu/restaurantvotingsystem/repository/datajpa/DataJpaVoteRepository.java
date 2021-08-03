package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

    private final CrudVoteRepository voteRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote save(Vote vote, int userId) {
        if (!vote.isNew() && vote.getUser().id() != userId) {
            return null;
        }
        return voteRepository.save(vote);
    }

    @Override
    @Transactional
    public boolean deleteByDate(int userId, LocalDate votingDate) {
        return voteRepository.delete(userId, votingDate) != 0;
    }

    @Override
    public Vote get(int id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll(Sort.by(Sort.Direction.DESC, "votingDate"));
    }

    @Override
    public Vote getTodayVoteUserById(int userId) {
        return voteRepository.getVoteByUserIdAndVotingDate(userId, LocalDate.now());
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.getVoteByUserIdOrderByVotingDateDesc(userId);
    }
}
