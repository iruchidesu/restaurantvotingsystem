package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
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
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.votingDate=:votingDate")
    int delete(@Param("userId") int userId, @Param("votingDate") LocalDate votingDate);

    Vote getVoteByUserIdAndVotingDate(int userId, LocalDate date);

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Vote> getVoteByUserIdOrderByVotingDateDesc(int userId);
}
