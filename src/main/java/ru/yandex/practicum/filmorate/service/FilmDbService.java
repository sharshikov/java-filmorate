package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("filmDbService")
public class FilmDbService implements FilmLikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addLike(int id, int userId) {
        var film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        jdbcTemplate.update("insert into PUBLIC.LIKE_FILM_BY_USER (FILM_ID, USER_ID) values (?, ?)", id, userId);
        return film;
    }

    @Override
    public Film deleteLike(int id, int userId) {
        var film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        jdbcTemplate.update("DELETE FROM PUBLIC.LIKE_FILM_BY_USER WHERE FILM_ID = ? AND USER_ID = ?", id, userId);
        return film;
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        var mapList = jdbcTemplate.queryForList("select FILM_ID, count(USER_ID) from PUBLIC.LIKE_FILM_BY_USER GROUP BY FILM_ID ORDER BY count(USER_ID) DESC limit ?", count);
        var topFilms = new ArrayList<Film>();
        for (Map<String, Object> map : mapList) {
            topFilms.add(filmStorage.getFilm((Integer) map.get("FILM_ID")));
        }
        return topFilms;
    }
}
