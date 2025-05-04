package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendUserId}")
    public void addToFriends(@PathVariable("id") Long id, @PathVariable("friendUserId") Long friendUserId) {
        log.info("Пользователь с ID: {} добавил в друзья пльзователя с ID: {}", id, friendUserId);
        userService.addToFriends(id, friendUserId);
    }

    @DeleteMapping("/{id}/friends/{friendUserId}")
    public void removeFromFriends(@PathVariable("id") Long id, @PathVariable("friendUserId") Long friendUserId) {
        log.info("Пользователь с ID: {} убрал из друзей пльзователя с ID: {}", id, friendUserId);
        userService.removeFromFriends(id, friendUserId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long id) {
        log.info("Получен список дурзей пользователя с ID: {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/mutual/{otherUserId}")
    public List<User> getMutualFriends(@PathVariable("id") Long id, @PathVariable("otherUserId") Long otherUserId) {
        log.info("Получен список общих друзей пользователя с ID: {} и {}", id, otherUserId);
        return userService.getMutualFriends(id, otherUserId);
    }
}