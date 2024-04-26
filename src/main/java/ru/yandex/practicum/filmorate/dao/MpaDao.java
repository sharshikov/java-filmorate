package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundDataException;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        Mpa mpa;
        String sql = "select * from PUBLIC.RATING_MPA where id = ?";
        try {
            mpa = jdbcTemplate.queryForObject(sql, new MpaRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("Не найден рейтинг с таким id: " + id);
        }
        return mpa;
    }

    public List<Mpa> getMpa() {
        String sql = "select * from PUBLIC.RATING_MPA ORDER BY ID";
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }
}