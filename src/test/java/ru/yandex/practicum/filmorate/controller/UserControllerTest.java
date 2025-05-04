package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserStorage userStorage;
    private User user;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        user = new User(1L, "neo@matrix.com", "neo", "Neo", LocalDate.of(1990, 3, 11));
    }

    @Test
    void shouldAdduser() {
        User added = userStorage.addUser(user);
        assertNotNull(added.getId());
        assertEquals("Neo", added.getName());
    }

    @Test
    void shouldSetNameToLoginIfNameIsNull() {
        user.setName(null);
        User added = userStorage.addUser(user);
        assertEquals("neo", added.getName());
    }

    @Test
    void shouldSetNameToLoginIfNameIsBlank() {
        user.setName("   ");
        User added = userStorage.addUser(user);
        assertEquals("neo", added.getName());
    }

    @Test
    void shouldUpdateExistingUser() {
        User added = userStorage.addUser(user);
        added.setName("Thomas Anderson");
        User updated = userStorage.updateUser(added);

        assertEquals("Thomas Anderson", updated.getName());
        assertEquals(added.getId(), updated.getId());
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistentUser() {
        user.setId(999L); // Несуществующий ID
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userStorage.updateUser(user));
        assertEquals("Пользователь с ID: 999 не найден", ex.getMessage());
    }

    @Test
    void shouldReturnAllUsers() {
        userStorage.addUser(user);
        Collection<User> users = userStorage.getAllUsers();
        assertEquals(1, users.size());
    }
}
