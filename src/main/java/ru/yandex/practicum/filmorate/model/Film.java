package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.datevalidators.IsBefore;
import ru.yandex.practicum.filmorate.stringvalidators.MaxLengthString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

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
    @MaxLengthString(value = 200)
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @IsBefore(value = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
