package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        // Подготавливаем данные для теста
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.builder().id(6).name("Боевик").build());
        Film film = Film.builder()
                .id(1)
                .name("John Wick")
                .description("Keanu Reeves kills people")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 12, 12))
                .mpa(Mpa.builder().id(5).name("NC-17").build())
                .genres(genres)
                .build();
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, new GenreDao(jdbcTemplate), new MpaDao(jdbcTemplate));
        filmDbStorage.addFilm(film);

        // вызываем тестируемый метод
        Film savedFilm = filmDbStorage.getFilm(1);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGetFilms() {
        // Подготавливаем данные для теста
        Film film1 = Film.builder()
                .id(1)
                .name("John Wick")
                .description("Keanu Reeves kills people")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 12, 12))
                .mpa(Mpa.builder().id(5).build())
                .build();
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, new GenreDao(jdbcTemplate), new MpaDao(jdbcTemplate));
        Film film2 = Film.builder()
                .id(2)
                .name("John Wick")
                .description("Keanu Reeves kills people")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 12, 12))
                .mpa(Mpa.builder().id(5).build())
                .build();
        List<Film> films = new ArrayList<>();
        films.add(film1);
        films.add(film2);
        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        // проверяем утверждения
        assertThat(films)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(filmDbStorage.getFilms());        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateFilm() {
        // Подготавливаем данные для теста
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.builder().id(6).name("Боевик").build());
        Film film1 = Film.builder()
                .id(1)
                .name("John Wick")
                .description("Keanu Reeves kills people")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 12, 12))
                .mpa(Mpa.builder().id(5).name("NC-17").build())
                .genres(genres)
                .build();
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, new GenreDao(jdbcTemplate), new MpaDao(jdbcTemplate));
        Film film2 = Film.builder()
                .id(1)
                .name("John")
                .description("Keanu Reeves kills people")
                .duration(120)
                .releaseDate(LocalDate.of(2020, 12, 12))
                .mpa(Mpa.builder().id(5).name("NC-17").build())
                .genres(genres)
                .build();
        filmDbStorage.addFilm(film1);
        filmDbStorage.updateFilm(film2);

        // проверяем утверждения
        assertThat(filmDbStorage.getFilm(1))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film2);        // и сохраненного пользователя - совпадают
    }
}