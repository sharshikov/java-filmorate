package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmMemoryService implements FilmLikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmMemoryService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage, @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int id, int userId) {
        var film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        if (film.getUsersIdWithLike() == null) {
            film.setUsersIdWithLike(new HashSet<>());
        }
        film.getUsersIdWithLike().add(userId);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        var film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.getUsersIdWithLike().remove(userId);
        return film;
    }

    public List<Film> getMostLikedFilms(int count) {
        var films = new ArrayList<>(filmStorage.getFilms());
        var topFilms = new LinkedList<Film>();
        if (count == 0) {
            count = 10;
        }
        if (count > films.size()) {
            count = films.size();
        }
        for (int i = 0; i < count; i++) {
            var max = films.stream().filter(x -> x.getUsersIdWithLike() != null).max(Comparator.comparing(x -> x.getUsersIdWithLike().size()));
            if (max.isPresent()) {
                topFilms.add(max.get());
                films.remove(max.get());
            } else {
                topFilms.addAll(films.subList(0, count - i));
                break;
            }
        }
        return topFilms;
    }
}
