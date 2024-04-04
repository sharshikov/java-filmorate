package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User updateUser(User user);

    List<User> getUsers();

    User addUser(User user);

    User getUser(int id);
}
