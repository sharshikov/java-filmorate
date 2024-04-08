package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundDataException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(id);
        films.put(film.getId(), film);
        log.info(String.format("Добавлен фильм %s", id));
        id++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundDataException("Не найден фильм с таким id " + film.getId());
        }
        films.put(film.getId(), film);
        log.info(String.format("Обновлен фильм %s", id));
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundDataException("Не найден фильм с таким id " + id);
        }
        return films.get(id);
    }
}
