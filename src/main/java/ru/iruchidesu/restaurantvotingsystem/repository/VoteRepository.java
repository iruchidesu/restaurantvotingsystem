package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.Vote;

import java.util.List;

public interface VoteRepository {
    // null if not found, when updated
    Vote save(Vote vote);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Vote get(int id);

    List<Vote> getAll();
}
