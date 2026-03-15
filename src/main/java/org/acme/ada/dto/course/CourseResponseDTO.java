package org.acme.ada.dto.course;

import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.model.Course;

import java.util.List;

public record CourseResponseDTO(
        Long id,
        String name,
        List<LessonResponseDTO> lessons
) {
    public static CourseResponseDTO valueOf(Course course) {

        List<LessonResponseDTO> lessonsDTO = course.getLessons() == null
                ? List.of()
                : course.getLessons()
                .stream()
                .map(LessonResponseDTO::valueOf)
                .toList();

        return new CourseResponseDTO(
                course.getId(),
                course.getName(),
                lessonsDTO
        );
    }
}
