package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundDataException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Integer> addGenresToFilm(Film film) {
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into PUBLIC.GENRE_FILM (FILM_ID, GENRE_ID) values (?, ?)",
                    film.getId(), genre.getId());
        }
        return film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
    }

    public Set<Integer> updateGenresToFilm(Film film) {
        jdbcTemplate.update("DELETE FROM PUBLIC.GENRE_FILM WHERE id = ?", film.getId());
        if (film.getGenres() == null) {
            return new HashSet<>();
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into PUBLIC.GENRE_FILM (FILM_ID, GENRE_ID) values (?, ?)",
                    film.getId(), genre.getId());
        }
        return film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
    }

    public Set<Genre> getGenresByFilm(int id) {
        var idGenres = jdbcTemplate.queryForList("select GENRE_ID from PUBLIC.GENRE_FILM where FILM_ID = ? ORDER BY GENRE_ID", Integer.class, id);
        var genres = new HashSet<Genre>();
        for (Integer idGenre : idGenres) {
            genres.add(getGenreById(idGenre));
        }
        return genres;
    }

    public Genre getGenreById(int id) {
        Genre genre;
        String sql = "select * from genre where id = ?";
        try {
            genre = jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("Не найден жанр с таким id: " + id);
        }
        return genre;
    }

    public List<Genre> getGenres() {
        String sql = "select * from genre order by id";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }
}
