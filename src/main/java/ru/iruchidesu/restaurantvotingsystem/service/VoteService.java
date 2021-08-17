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
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundException("restaurant with id = " + restaurantId));
        User user = userRepository.findById(userId).orElseThrow(notFoundException("user with id = " + userId));
        Vote vote = new Vote();
        vote.setRestaurant(restaurant);
        vote.setUser(user);
        if (!vote.isNew() && vote.getUser().id() != userId) {
            return null;
        }
        return voteRepository.save(vote);
    }

    public void deleteToday(int userId) {
        if (voteRepository.delete(userId, LocalDate.now()) == 0) {
            notFoundException("vote with userId = " + userId).get();
        }
    }

    public Vote get(int id) {
        return voteRepository.findById(id).orElseThrow(notFoundException("vote with id = " + id));
    }

    @Transactional
    public void update(int restaurantId, LocalTime time, int userId) {
        if (time.isAfter(VOTE_UPDATE_TIME)) {
            throw new VoteUpdateTimeException("it's too late to change your vote");
        }
        Vote vote = getTodayVoteByUser(userId);
        User user = userRepository.findById(userId).orElse(null);
        vote.setUser(user);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundException("restaurant with id = " + restaurantId));
        vote.setRestaurant(restaurant);
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.getVoteByUserIdOrderByVotingDateDesc(userId);
    }

    public Vote getTodayVoteByUser(int userId) {
        return voteRepository.getVoteByUserIdAndVotingDate(userId, LocalDate.now())
                .orElseThrow(notFoundException("vote with userId = " + userId));
    }
}
