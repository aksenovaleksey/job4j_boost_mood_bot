package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    @Override
    List<MoodLog> findAll();

    List<MoodLog> findByUser(User user);

    List<MoodLog> findByUserAndCreatedAtAfter(User user, Long timestamp);

    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);
}