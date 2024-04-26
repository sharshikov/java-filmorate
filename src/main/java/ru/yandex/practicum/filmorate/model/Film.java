package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    @PositiveOrZero
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Date(afterThis = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> usersIdWithLike;
    @Valid
    private Set<Genre> genres;
    @NotNull
    @Valid
    private Mpa mpa;
}
