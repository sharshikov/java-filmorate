package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet genreRows, int rowNum) throws SQLException {
        return Genre.builder()
                .id(genreRows.getInt("id"))
                .name(genreRows.getString("name")).build();
    }
}