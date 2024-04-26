package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User addFriend(User user, User friend) {
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

    public Set<Integer> addGenresToFilm(Film film) {
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into PUBLIC.GENRE_FILM (FILM_ID, GENRE_ID) values (?, ?)",
                    film.getId(), genre.getId());
        }
        return film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
    }
}
