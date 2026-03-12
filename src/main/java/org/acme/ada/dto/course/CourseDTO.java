package org.acme.ada.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseDTO(
        @NotBlank(message = "Course name is required")
        @Size(min = 3, message = "Course name must have at least 3 characters")
        String name
) {
}
