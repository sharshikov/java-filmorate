package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet mpaRows, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(mpaRows.getInt("id"))
                .name(mpaRows.getString("name")).build();
    }
}