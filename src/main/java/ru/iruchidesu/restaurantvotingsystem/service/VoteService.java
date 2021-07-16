package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.UserRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.VoteRepository;
import ru.iruchidesu.restaurantvotingsystem.util.exception.VoteUpdateTimeException;

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

    public Vote create(Vote vote, int restaurantId, int userId) {
        Assert.notNull(vote, "vote must not be null");
        Restaurant restaurant = restaurantRepository.get(restaurantId);
        User user = userRepository.get(userId);
        vote.setRestaurant(restaurant);
        vote.setUser(user);
        return voteRepository.save(vote);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.delete(id, userId), id);
    }

    public Vote get(int id) {
        return checkNotFoundWithId(voteRepository.get(id), id);
    }

    public List<Vote> getAll() {
        return voteRepository.getAll();
    }

    public void update(Vote vote) {
        Assert.notNull(vote, "vote must not be null");
        if (LocalTime.now().isAfter(VOTE_UPDATE_TIME)) {
            throw new VoteUpdateTimeException("it's too late to change your vote");
        }
        checkNotFoundWithId(voteRepository.save(vote), vote.id());
    }

    public List<Vote> getAllVoteByUser(int userId) {
        return voteRepository.getAllVoteByUser(userId);
    }
}
