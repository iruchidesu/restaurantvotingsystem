package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.error.VoteUpdateTimeException;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.UserRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.notFoundException;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.notFoundRestaurantException;

@Service
public class VoteService {
    private static final LocalTime VOTE_UPDATE_TIME = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Vote create(int restaurantId, int userId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundRestaurantException(restaurantId));
        User user = userRepository.getById(userId);
        Vote vote = new Vote();
        vote.setRestaurant(restaurant);
        vote.setUser(user);
        return voteRepository.save(vote);
    }

    public Vote get(int id) {
        return voteRepository.findById(id).orElseThrow(notFoundException("vote with id = " + id));
    }

    @Transactional
    public void update(int restaurantId, LocalTime time, int userId) {
        if (time.isAfter(VOTE_UPDATE_TIME)) {
            throw new VoteUpdateTimeException("it's too late to change your vote");
        }
        Vote vote = getVoteByUser(userId, LocalDate.now());
        User user = userRepository.getById(userId);
        vote.setUser(user);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundRestaurantException(restaurantId));
        vote.setRestaurant(restaurant);
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.getVoteByUserIdOrderByVotingDateDesc(userId);
    }

    public Vote getVoteByUser(int userId, LocalDate date) {
        return voteRepository.getVoteByUserIdAndVotingDate(userId, date)
                .orElseThrow(notFoundException("vote with userId = " + userId));
    }
}
