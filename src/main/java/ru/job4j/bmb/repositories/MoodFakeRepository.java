package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("moodFakeRepository")
public class MoodFakeRepository implements MoodRepository {

    protected final Map<Long, Mood> memory = new ConcurrentHashMap<>();
    private long nextId = 1L;

    @Override
    public <S extends Mood> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(nextId++);
        }
        memory.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends Mood> Iterable<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<Mood> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return memory.containsKey(id);
    }

    @Override
    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<Mood> findAllById(Iterable<Long> ids) {
        List<Mood> result = new ArrayList<>();
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
    public void delete(Mood entity) {
        if (entity.getId() != null) {
            memory.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Mood> entities) {
        for (Mood entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        memory.clear();
    }
}
