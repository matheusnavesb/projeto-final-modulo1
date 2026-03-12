package org.acme.ada.dto.lesson;

import org.acme.ada.model.Course;
import org.acme.ada.model.Lesson;

public record LessonResponseDTO(
        Long id,
        String name
) {
    public static LessonResponseDTO valueOf(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getId(),
                lesson.getName()
        );
    }
}
