package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Post {
    private long authorId;
    private Long id;
    private String description;
    private Instant postDate;
}
