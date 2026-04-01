package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component("moodLogFakeRepository")
public class MoodLogFakeRepository implements MoodLogRepository {

    protected final Map<Long, MoodLog> memory = new ConcurrentHashMap<>();
    private long nextId = 1L;

    @Override
    public <S extends MoodLog> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(nextId++);
        }
        memory.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends MoodLog> Iterable<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<MoodLog> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return memory.containsKey(id);
    }

    @Override
    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<MoodLog> findAllById(Iterable<Long> ids) {
        List<MoodLog> result = new ArrayList<>();
        for (Long id : ids) {
            if (memory.containsKey(id)) {
                result.add(memory.get(id));
            }
        }
        return result;
    }

    @Override
    public long count() {

        return memory.size();
    }

    @Override
    public void deleteById(Long id) {

        memory.remove(id);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        for (Long id : ids) {
            memory.remove(id);
        }
    }

    @Override
    public void delete(MoodLog entity) {
        if (entity.getId() != null) {
            memory.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll(Iterable<? extends MoodLog> entities) {
        for (MoodLog entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {

        memory.clear();
    }

    @Override
    public List<MoodLog> findByUser(User user) {
        return memory.values().stream()
                .filter(log -> user.getId() != null &&
                        log.getUser() != null &&
                        log.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoodLog> findByUserAndCreatedAtAfter(User user, Long timestamp) {
        return memory.values().stream()
                .filter(log -> user.getId() != null &&
                        log.getUser() != null &&
                        log.getUser().getId().equals(user.getId()))
                .filter(log -> log.getCreatedAt() != null && log.getCreatedAt() > timestamp)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {

        return new ArrayList<>();
    }
}

