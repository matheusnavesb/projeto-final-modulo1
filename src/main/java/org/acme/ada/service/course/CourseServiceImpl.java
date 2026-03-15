package org.acme.ada.service.course;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.exception.NotFoundException;
import org.acme.ada.dto.course.CourseDTO;
import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.model.Course;
import org.acme.ada.model.Lesson;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;

import java.util.List;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    @Inject
    CourseRepository courseRepository;

    @Inject
    LessonRepository lessonRepository;

    @Override
    @Transactional
    public CourseResponseDTO create(CourseDTO dto) {
        Course course = new Course();
        course.setName(dto.name());

        courseRepository.persist(course);

        return CourseResponseDTO.valueOf(course);
    }

    @Override
    @Transactional
    public List<CourseResponseDTO> list(int page, int size) {
        List<Course> courses = courseRepository.findAll()
                .page(Page.of(page, size))
                .list();

        return courses.stream()
                .map(CourseResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public List<CourseResponseDTO> listAll() {
        return courseRepository.listAll()
                .stream()
                .map(CourseResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponseDTO findById(Long id) {
        Course course = courseRepository.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found: " + id);
        }
        return CourseResponseDTO.valueOf(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO update(Long id, CourseDTO dto) {
        Course course = courseRepository.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found: " + id);
        }

        course.setName(dto.name());
        // dentro da transação o Hibernate atualiza
        return CourseResponseDTO.valueOf(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = courseRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Course not found: " + id);
        }
    }

    @Override
    @Transactional
    public LessonResponseDTO addLesson(Long courseId, LessonDTO dto) {
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new NotFoundException("Course not found: " + courseId);
        }

        Lesson lesson = new Lesson();
        lesson.setName(dto.name());
        lesson.setCourse(course);

        lessonRepository.persist(lesson);

        return LessonResponseDTO.valueOf(lesson);
    }

    @Override
    @Transactional
    public List<LessonResponseDTO> listLessons(Long courseId) {
        // garante 404 se o curso não existe (conforme requisito)
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new NotFoundException("Course not found: " + courseId);
        }

        return lessonRepository.find("course.id", courseId)
                .list()
                .stream()
                .map(LessonResponseDTO::valueOf)
                .toList();
    }
}
