package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.UserRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;
import ru.iruchidesu.restaurantvotingsystem.util.exception.VoteUpdateTimeException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;

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
        Restaurant restaurant = restaurantRepository.get(restaurantId);
        User user = userRepository.get(userId);
        Vote vote = new Vote();
        vote.setRestaurant(restaurant);
        vote.setUser(user);
        return voteRepository.save(vote, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.deleteByDate(userId, LocalDate.now()), id);
    }

    public Vote get(int id) {
        return checkNotFoundWithId(voteRepository.get(id), id);
    }

    @Transactional
    public void update(int restaurantId, LocalTime time, int userId) {
        if (time.isAfter(VOTE_UPDATE_TIME)) {
            throw new VoteUpdateTimeException("it's too late to change your vote");
        }
        Vote vote = getTodayVoteByUser(userId);
        Assert.notNull(vote, "Today vote not found");
        User user = userRepository.get(userId);
        vote.setUser(user);
        Restaurant restaurant = restaurantRepository.get(restaurantId);
        vote.setRestaurant(restaurant);
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.getAllVoteByUser(userId);
    }

    public Vote getTodayVoteByUser(int userId) {
        return voteRepository.getTodayVoteUserById(userId);
    }
}
