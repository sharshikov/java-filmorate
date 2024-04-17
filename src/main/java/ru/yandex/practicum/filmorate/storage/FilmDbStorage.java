package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundDataException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Qualifier("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertIntoFilm;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDao genreDao, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        insertIntoFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("PUBLIC")
                .withTableName("FILM")
                .usingGeneratedKeyColumns("id");
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
    }

    @Override
    public Film addFilm(Film film) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("RELEASE_DATE", film.getReleaseDate());
        parameters.put("DURATION", film.getDuration());
        parameters.put("RATING_MPA_ID", film.getMpa().getId());
        var id = insertIntoFilm.executeAndReturnKey(parameters).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            genreDao.addGenresToFilm(film);
        }
        log.info(String.format("Добавлен фильм %s", id));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            jdbcTemplate.queryForObject("select id from film where id = ?", Integer.class, film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("Не найден фильм с таким id " + film.getId());
        }
        jdbcTemplate.update("update film set name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ? where id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        genreDao.updateGenresToFilm(film);
        log.info(String.format("Обновлен фильм %s", film.getId()));
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film getFilm(int id) {
        String sql = "select * from film where id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("Не найден фильм с таким id: " + id);
        }
        if (film != null) {
            film.setGenres(genreDao.getGenresByFilm(id));
            film.getMpa().setName(mpaDao.getMpaById(film.getMpa().getId()).getName());
        }
        return film;
    }
}