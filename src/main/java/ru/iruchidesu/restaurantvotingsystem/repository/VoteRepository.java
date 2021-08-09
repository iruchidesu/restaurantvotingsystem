package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    // null if not found, when updated
    Vote save(Vote vote, int userId);

    // false if not found
    boolean deleteByDate(int userId, LocalDate votingDate);

    // null if not found
    Vote get(int id);

    // null if not found
    Vote getTodayVoteUserById(int userId);

    List<Vote> getAllVoteByUser(int userId);
}
