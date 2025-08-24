package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {
    PostService postService;

    @GetMapping("/posts")
    public Collection<Post> findAll(@RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "desc") String sort,
                                    @RequestParam(defaultValue = "1") int from) {
        return postService.findAll(size, sort, from);
    }

    @GetMapping("posts/{postId}")
        public Post findById(@PathVariable long postId) {
            return postService.findById(postId);
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping("/posts")
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}