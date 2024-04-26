package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class Genre {
    @Max(6)
    @Min(1)
    private Integer id;
    private String name;
}
