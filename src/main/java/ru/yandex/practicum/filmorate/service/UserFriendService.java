package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserFriendService {
    public User addFriend(int firstId, int secondId);

    public User deleteFriend(int firstId, int secondId);

    public List<User> getFriends(int id);

    public List<User> getCommonFriends(int firstId, int secondId);
}