package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

    private final CrudVoteRepository voteRepository;
    private final CrudUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository, CrudUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        User user = userRepository.getById(userId);
        return user.getRoles().contains(Role.ADMIN) && voteRepository.delete(id) != 0;
    }

    @Override
    public Vote get(int id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public Vote getTodayVoteUserById(int userId) {
        return voteRepository.getVoteByUserIdAndVotingDate(userId, LocalDate.now());
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.findVoteByUserId(userId);
    }
}
