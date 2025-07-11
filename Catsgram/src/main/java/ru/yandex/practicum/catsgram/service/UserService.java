package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(long userId) {
        if(users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public User create(User user) {
        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        boolean notMatchEmail = users.values().stream()
                .map(User::getEmail)
                .noneMatch(email -> Objects.equals(email, user.getEmail()));
        if (!notMatchEmail) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        List<String> emails = users.values().stream()
                .map(User::getEmail)
                .toList();
        User newUser = users.get(user.getId());
        if (user.getEmail() != null && !newUser.getEmail().equals(user.getEmail()) && emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        } else if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }

        if (user.getUsername() != null) {
            newUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null) {
            newUser.setPassword(user.getPassword());
        }

        users.put(newUser.getId(), newUser);
        return newUser;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<User> findUserById(Long authorId) {
        if(users.containsKey(authorId)) {
            return Optional.of(users.get(authorId));
        } else {
            return Optional.empty();
        }
    }
}
