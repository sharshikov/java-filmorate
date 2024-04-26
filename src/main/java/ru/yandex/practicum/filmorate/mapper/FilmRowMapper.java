package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet filmRows, int rowNum) throws SQLException {
        return Film.builder()
                .id(filmRows.getInt("id"))
                .name(filmRows.getString("name"))
                .description(filmRows.getString("description"))
                .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                .duration(filmRows.getInt("duration"))
                .mpa(Mpa.builder().id(filmRows.getInt("rating_mpa_id")).build()).build();
    }
}
