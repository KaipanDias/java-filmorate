package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmStorage filmStorage;
    private FilmService filmService;
    private UserStorage userStorage;
    private Film film;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        film = new Film(1L, "The Matrix", "A sci-fi classic.", LocalDate.of(1999, 3, 31), 136L);
    }

    @Test
    void shouldAddFilm() {
        Film added = filmStorage.addFilm(film);
        assertNotNull(added.getId());
        assertEquals("The Matrix", added.getName());
    }

    @Test
    void shouldThrowValidationExceptionForTooOldDate() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        ValidationException ex = assertThrows(ValidationException.class,
                () -> filmService.addFilm(film));
        assertEquals("Год выпуска фильма не может быть раньше 28.12.1895", ex.getMessage());
    }

    @Test
    void shouldReturnAllFilms() {
        filmStorage.addFilm(film);
        Collection<Film> films = filmStorage.getAllFilms();
        assertEquals(1, films.size());
    }

    @Test
    void shouldUpdateExistingFilm() {
        Film added = filmStorage.addFilm(film);
        added.setName("The Matrix Reloaded");

        Film updated = filmStorage.updateFilm(added);
        assertEquals("The Matrix Reloaded", updated.getName());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentFilm() {
        film.setId(999L);
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> filmStorage.updateFilm(film));
        assertEquals("Фильм с ID:999 не найден", ex.getMessage());
    }
}