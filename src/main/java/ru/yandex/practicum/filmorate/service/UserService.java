package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int firstId, int secondId) {
        var firstUser = userStorage.getUser(firstId);
        var secondUser = userStorage.getUser(secondId);
        if (firstUser.getFriendsId() == null) {
            firstUser.setFriendsId(new HashSet<>());
        }
        if (secondUser.getFriendsId() == null) {
            secondUser.setFriendsId(new HashSet<>());
        }
        firstUser.getFriendsId().add(secondId);
        secondUser.getFriendsId().add(firstId);
        log.info("Добавлен друг");
        return firstUser;
    }

    public User deleteFriend(int firstId, int secondId) {
        var firstUser = userStorage.getUser(firstId);
        var secondUser = userStorage.getUser(secondId);
        if (firstUser.getFriendsId() != null
                && !firstUser.getFriendsId().isEmpty()) {
            firstUser.getFriendsId().remove(secondId);
        }
        if (secondUser.getFriendsId() != null
                && !secondUser.getFriendsId().isEmpty()) {
            secondUser.getFriendsId().remove(firstId);
        }
        log.info("Удален друг");
        return firstUser;
    }

    public List<User> getFriends(int id) {
        var user = userStorage.getUser(id);
        var friends = new ArrayList<User>();
        if (user.getFriendsId() == null || user.getFriendsId().isEmpty()) {
            return List.of();
        }
        for (var friendId : user.getFriendsId()) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int firstId, int secondId) {
        var firstUser = userStorage.getUser(firstId);
        var secondUser = userStorage.getUser(secondId);
        if (firstUser.getFriendsId() == null
                || firstUser.getFriendsId().isEmpty()) {
            return List.of();
        }
        if (secondUser.getFriendsId() == null
                || secondUser.getFriendsId().isEmpty()) {
            return List.of();
        }
        var friendsFirstUser = new HashSet<>(firstUser.getFriendsId());
        friendsFirstUser.retainAll(secondUser.getFriendsId());
        var friends = new ArrayList<User>();
        for (var friendId : friendsFirstUser) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }
}
