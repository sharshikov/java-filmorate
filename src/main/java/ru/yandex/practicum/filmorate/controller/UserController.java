package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private static int id = 1;

    @GetMapping()
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundDataException("Не найден пользователь с таким id " + user.getId());
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь");
        return user;
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        log.info("Добавлен пользователь");
        return user;
    }
}
