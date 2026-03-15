package org.acme.ada.service.course;

import org.acme.ada.dto.course.CourseDTO;
import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;

import java.util.List;

public interface CourseService {

    CourseResponseDTO create(CourseDTO dto);

    List<CourseResponseDTO> list(int page, int size);

    List<CourseResponseDTO> listAll();

    CourseResponseDTO findById(Long id);

    CourseResponseDTO update(Long id, CourseDTO dto);

    void delete(Long id);

    // PLUS
    LessonResponseDTO addLesson(Long courseId, LessonDTO dto);

    // PLUS
    List<LessonResponseDTO> listLessons(Long courseId);
}