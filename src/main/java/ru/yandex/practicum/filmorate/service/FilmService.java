package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film){
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms(){
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id){
        return filmStorage.getFilmByID(id);
    }

    public void addLikeToFilm(Long filmId, Long userId){
        Film film = filmStorage.getFilmByID(filmId);
        User user = userStorage.getUserById(userId);

        film.getLikes().add(user.getId());
        log.info("Пользователь с ID: {} поставил лайк фильму с ID: {}.", userId, filmId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId){
        Film film = filmStorage.getFilmByID(filmId);
        User user = userStorage.getUserById(userId);

        film.getLikes().remove(user.getId());
        log.info("Пользователь с ID: {} убрал лайк с фильма с ID: {}.", userId, filmId);
    }

    public List<Film> getPopularFilms(int count){
        log.info("Получен топ {} популярных фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
