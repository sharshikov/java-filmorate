package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(newUser);

        // вызываем тестируемый метод
        User savedUser = userStorage.getUser(1);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGetUsers() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User firstUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User secondUser = User.builder()
                .id(2)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);
        userStorage.addUser(firstUser);
        userStorage.addUser(secondUser);

        // проверяем утверждения
        assertThat(users)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(userStorage.getUsers());        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateUsers() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User firstUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User testUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya12")
                .name("Ivan Petro")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userStorage.addUser(firstUser);
        userStorage.updateUser(testUser);

        // проверяем утверждения
        assertThat(userStorage.getUser(1))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(testUser);        // и сохраненного пользователя - совпадают
    }
}