package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    Vote getVoteByUserIdAndVotingDate(int userId, LocalDate date);

    List<Vote> findVoteByUserIdOrderByVotingDateDesc(int userId);
}
