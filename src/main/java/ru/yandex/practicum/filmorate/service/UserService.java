package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addToFriends(Long userId, Long friendUSerId) {
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendUSerId);

        user.getFriends().add(friendUser.getId());
        friendUser.getFriends().add(user.getId());
    }

    public void removeFromFriends(Long userId, Long friendUSerId) {
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendUSerId);

        user.getFriends().remove(friendUser.getId());
        friendUser.getFriends().remove(user.getId());
    }

    public List<User> getUserFriends(Long id) {
        Set<Long> userFriendsIds = userStorage.getUserById(id).getFriends();

        if (userFriendsIds == null || userFriendsIds.isEmpty()) {
            return new ArrayList<>();
        }

        return userFriendsIds.stream()
                .map(userStorage::getUserById)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        Set<Long> mutualFriends = userStorage.getUserById(userId).getFriends();

        if (mutualFriends == null || mutualFriends.isEmpty()) {
            return new ArrayList<>();
        }

        mutualFriends.retainAll(userStorage.getUserById(otherUserId).getFriends());

        return mutualFriends.stream()
                .map(userStorage::getUserById)
                .filter(Objects::nonNull)
                .toList();
    }
}