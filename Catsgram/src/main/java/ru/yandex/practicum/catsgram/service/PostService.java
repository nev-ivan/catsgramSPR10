package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(int size, String sort, int from) {
        List<Post> sortedList = posts.values().stream()
                .sorted(Comparator.comparing(Post::getPostDate))
                .toList();
        LinkedList<Post> sortedPosts = new LinkedList<>(sortedList);
        List<Post> responsePosts = new ArrayList<>();
        if(sort.equals("asc") || sort.equals("ascending")) {
            for (int i = 0; i < from; i++) {
                sortedPosts.removeFirst();
            }
            for (int i = 0; i < size; i++) {
                responsePosts.add(sortedPosts.removeFirst());
            }
        } else {
            for (int i = 0; i < from; i++) {
                sortedPosts.removeLast();
            }
            for (int i = 0; i < size; i++) {
                responsePosts.add(sortedPosts.removeLast());
            }
        }
        return responsePosts;
    }

    public Post findById(long postId) {
        if(posts.containsKey(postId)) {
            return posts.get(postId);
        } else {
           throw new NotFoundException("Пост не найден");
        }
    }

    public Post create(Post post) {
        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        if(userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId() + " не найден»");
        }
        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private Long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
