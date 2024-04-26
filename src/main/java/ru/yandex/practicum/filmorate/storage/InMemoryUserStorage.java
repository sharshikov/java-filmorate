package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(user.getId(), user);
        log.info(String.format("Добавлен пользователь %s", id));
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundDataException("Не найден пользователь с таким id " + user.getId());
        }
        users.put(user.getId(), user);
        log.info(String.format("Обновлен пользователь %s", id));
        return user;
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundDataException("Не найден пользователь с таким id " + id);
        }
        return users.get(id);
    }
}
