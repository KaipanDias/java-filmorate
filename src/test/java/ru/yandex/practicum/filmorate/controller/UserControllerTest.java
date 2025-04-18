package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = new User(1L, "neo@matrix.com", "neo", "Neo", LocalDate.of(1990, 3, 11));
    }

    @Test
    void shouldAdduser() {
        User added = userController.addUser(user);
        assertNotNull(added.getId());
        assertEquals("Neo", added.getName());
    }

    @Test
    void shouldSetNameToLoginIfNameIsNull() {
        user.setName(null);
        User added = userController.addUser(user);
        assertEquals("neo", added.getName());
    }

    @Test
    void shouldSetNameToLoginIfNameIsBlank() {
        user.setName("   ");
        User added = userController.addUser(user);
        assertEquals("neo", added.getName());
    }

    @Test
    void shouldUpdateExistingUser() {
        User added = userController.addUser(user);
        added.setName("Thomas Anderson");
        User updated = userController.updateUser(added);

        assertEquals("Thomas Anderson", updated.getName());
        assertEquals(added.getId(), updated.getId());
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistentUser() {
        user.setId(999L); // Несуществующий ID
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userController.updateUser(user));
        assertEquals("Пользователь с ID: 999 не найден", ex.getMessage());
    }

    @Test
    void shouldReturnAllUsers() {
        userController.addUser(user);
        Collection<User> users = userController.getUsers();
        assertEquals(1, users.size());
    }
}
