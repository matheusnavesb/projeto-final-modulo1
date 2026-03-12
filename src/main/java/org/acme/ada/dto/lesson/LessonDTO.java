package org.acme.ada.dto.lesson;

import jakarta.validation.constraints.NotBlank;

public record LessonDTO(
        @NotBlank(message = "Lesson name is required")
        String name
) {
}
