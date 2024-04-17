package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("userDbService")
public class UserDbService implements UserFriendService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public UserDbService(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addFriend(int firstId, int secondId) {
        var user = userStorage.getUser(firstId);
        var friend = userStorage.getUser(secondId);
        String friendStatus = null;
        try {
            friendStatus = jdbcTemplate.queryForObject("select status from PUBLIC.FRIEND where USER_ID = ? and FRIEND_ID = ?", String.class, friend.getId(), user.getId());
        } catch (EmptyResultDataAccessException e) {
            friendRequest(user.getId(), friend.getId());
        }
        if (FriendStatus.UNCONFIRMED.equals(friendStatus)) {
            confirmFriendRequest(user.getId(), friend.getId());
        }
        return user;
    }

    @Override
    public User deleteFriend(int firstId, int secondId) {
        var user = userStorage.getUser(firstId);
        userStorage.getUser(secondId);
        jdbcTemplate.update("DELETE FROM PUBLIC.FRIEND WHERE USER_ID = ? and FRIEND_ID = ?", firstId, secondId);
        return user;
    }

    @Override
    public List<User> getFriends(int id) {
        userStorage.getUser(id);
        var idUsers = jdbcTemplate.queryForList("select FRIEND_ID from PUBLIC.FRIEND where USER_ID = ?", Integer.class, id);
        var users = new ArrayList<User>();
        for (Integer idUser : idUsers) {
            users.add(userStorage.getUser(idUser));
        }
        return users;
    }

    @Override
    public List<User> getCommonFriends(int firstId, int secondId) {
        var idUsersFirst = jdbcTemplate.queryForList("select FRIEND_ID from PUBLIC.FRIEND where USER_ID = ?", Integer.class, firstId);
        var idUsersSecond = jdbcTemplate.queryForList("select FRIEND_ID from PUBLIC.FRIEND where USER_ID = ?", Integer.class, secondId);
        var commonFriendsId = new ArrayList<Integer>();
        if (idUsersFirst.size() > idUsersSecond.size()) {
            idUsersFirst.retainAll(idUsersSecond);
            commonFriendsId.addAll(idUsersFirst);
        } else {
            idUsersSecond.retainAll(idUsersFirst);
            commonFriendsId.addAll(idUsersSecond);
        }
        var commonFriends = new ArrayList<User>();
        for (Integer id : commonFriendsId) {
            commonFriends.add(userStorage.getUser(id));
        }
        return commonFriends;
    }

    private void friendRequest(int id, int friendId) {
        jdbcTemplate.update("insert into PUBLIC.FRIEND (USER_ID, FRIEND_ID, STATUS) values (?, ?, ?)",
                id, friendId, FriendStatus.UNCONFIRMED);
    }

    private void confirmFriendRequest(int id, int friendId) {
        jdbcTemplate.update("insert into PUBLIC.FRIEND (USER_ID, FRIEND_ID, STATUS) values (?, ?, ?)",
                id, friendId, FriendStatus.CONFIRMED);
        jdbcTemplate.update("update PUBLIC.FRIEND set STATUS = ? where FRIEND_ID = ? and USER_ID = ?",
                FriendStatus.CONFIRMED, id, friendId);
    }
}
