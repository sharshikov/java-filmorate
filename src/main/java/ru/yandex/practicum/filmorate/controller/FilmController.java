package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmLikeService filmService;

    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("filmDbService") FilmLikeService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable("id") int id) {
        return filmStorage.getFilm(id);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int id, @PathVariable("userId") int friendId) {
        return filmService.deleteLike(id, friendId);
    }

    @GetMapping("popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false) int count) {
        return filmService.getMostLikedFilms(count);
    }
}
