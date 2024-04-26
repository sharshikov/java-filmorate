package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikeService {
    public Film addLike(int id, int userId);

    public Film deleteLike(int id, int userId);

    public List<Film> getMostLikedFilms(int count);
}