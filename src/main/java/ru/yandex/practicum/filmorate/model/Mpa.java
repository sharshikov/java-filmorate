package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class Mpa {
    @Max(5)
    @Min(1)
    private int id;

    private String name;
}
