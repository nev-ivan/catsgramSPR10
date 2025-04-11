package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Image {
    private long postId;
    private long id;
    private String originalFileName;
    private String filePath;
}
