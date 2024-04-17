package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet userRows, int rowNum) throws SQLException {
        return User.builder()
                .id(userRows.getInt("id"))
                .email(userRows.getString("email"))
                .login(userRows.getString("login"))
                .name(userRows.getString("name"))
                .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                .build();
    }
}
