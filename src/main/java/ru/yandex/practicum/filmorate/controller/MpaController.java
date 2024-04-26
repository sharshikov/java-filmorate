package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaDao mpaDao;

    public MpaController(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @GetMapping()
    public List<Mpa> getMpa() {
        return mpaDao.getMpa();
    }

    @GetMapping("{id}")
    public Mpa getMpaById(@PathVariable("id") int id) {
        return mpaDao.getMpaById(id);
    }
}
