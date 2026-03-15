package org.acme.resourceTest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.ada.dto.course.CourseDTO;
import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.exception.NotFoundException;
import org.acme.ada.model.Course;
import org.acme.ada.model.Lesson;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;
import org.acme.ada.service.course.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.quarkus.panache.common.Page;
import static org.mockito.Mockito.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//class CourseResourceTest {
//
//    @Mock
//    private CourseRepository courseRepository;
//
//    @Mock
//    private LessonRepository lessonRepository;
//
//    @Mock
//    private PanacheQuery<Course> coursePanacheQuery;
//
//    @Mock
//    private PanacheQuery<Lesson> lessonPanacheQuery;
//
//    @InjectMocks
//    private CourseServiceImpl service;
//
//    @Test
//    void create_devePersistirCursoERetornarResponseDTO() {
//        CourseDTO dto = new CourseDTO("Java");
//
//        CourseResponseDTO response = service.create(dto);
//
//        verify(courseRepository).persist(any(Course.class));
//        assertNotNull(response);
//        assertEquals("Java", response.name());
//    }
//
//    @Test
//    void list_deveRetornarListaDeCourseResponseDTO() {
//        Course course1 = new Course();
//        course1.setId(1L);
//        course1.setName("Java");
//
//        Course course2 = new Course();
//        course2.setId(2L);
//        course2.setName("Spring");
//
//        when(courseRepository.findAll()).thenReturn(coursePanacheQuery);
//        when(coursePanacheQuery.page(any(Page.class))).thenReturn(coursePanacheQuery);
//        when(coursePanacheQuery.list()).thenReturn(List.of(course1, course2));
//
//        List<CourseResponseDTO> response = service.list(0, 10);
//
//        assertNotNull(response);
//        assertEquals(2, response.size());
//        assertEquals("Java", response.get(0).name());
//        assertEquals("Spring", response.get(1).name());
//
//        verify(courseRepository).findAll();
//        when(coursePanacheQuery.page(org.mockito.ArgumentMatchers.any(Page.class)))
//                .thenReturn(coursePanacheQuery);
//        verify(coursePanacheQuery).list();
//    }
//
//    @Test
//    void findById_quandoExiste_deveRetornarCourseResponseDTO() {
//        Course course = new Course();
//        course.setId(1L);
//        course.setName("Java");
//
//        when(courseRepository.findById(1L)).thenReturn(course);
//
//        CourseResponseDTO response = service.findById(1L);
//
//        assertNotNull(response);
//        assertEquals(1L, response.id());
//        assertEquals("Java", response.name());
//
//        verify(courseRepository).findById(1L);
//    }
//
//    @Test
//    void findById_quandoNaoExiste_deveLancarNotFoundException() {
//        when(courseRepository.findById(1L)).thenReturn(null);
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> service.findById(1L)
//        );
//
//        assertEquals("Course not found: 1", exception.getMessage());
//    }
//
//    @Test
//    void update_quandoExiste_deveAtualizarNomeERetornarResponseDTO() {
//        Course course = new Course();
//        course.setId(1L);
//        course.setName("Java");
//
//        CourseDTO dto = new CourseDTO("Java Avancado");
//
//        when(courseRepository.findById(1L)).thenReturn(course);
//
//        CourseResponseDTO response = service.update(1L, dto);
//
//        assertNotNull(response);
//        assertEquals(1L, response.id());
//        assertEquals("Java Avancado", response.name());
//        assertEquals("Java Avancado", course.getName());
//    }
//
//    @Test
//    void update_quandoNaoExiste_deveLancarNotFoundException() {
//        CourseDTO dto = new CourseDTO("Java Avancado");
//
//        when(courseRepository.findById(1L)).thenReturn(null);
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> service.update(1L, dto)
//        );
//
//        assertEquals("Course not found: 1", exception.getMessage());
//    }
//
//    @Test
//    void delete_quandoExiste_deveExecutarSemErro() {
//        when(courseRepository.deleteById(1L)).thenReturn(true);
//
//        assertDoesNotThrow(() -> service.delete(1L));
//
//        verify(courseRepository).deleteById(1L);
//    }
//
//    @Test
//    void delete_quandoNaoExiste_deveLancarNotFoundException() {
//        when(courseRepository.deleteById(1L)).thenReturn(false);
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> service.delete(1L)
//        );
//
//        assertEquals("Course not found: 1", exception.getMessage());
//    }
//
//    @Test
//    void addLesson_quandoCursoExiste_devePersistirLessonERetornarResponseDTO() {
//        Course course = new Course();
//        course.setId(1L);
//        course.setName("Java");
//
//        LessonDTO dto = new LessonDTO("Orientacao a Objetos");
//
//        when(courseRepository.findById(1L)).thenReturn(course);
//
//        LessonResponseDTO response = service.addLesson(1L, dto);
//
//        verify(courseRepository).findById(1L);
//        verify(lessonRepository).persist(any(Lesson.class));
//
//        assertNotNull(response);
//        assertEquals("Orientacao a Objetos", response.name());
//    }
//
//    @Test
//    void addLesson_quandoCursoNaoExiste_deveLancarNotFoundException() {
//        LessonDTO dto = new LessonDTO("Orientacao a Objetos");
//
//        when(courseRepository.findById(1L)).thenReturn(null);
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> service.addLesson(1L, dto)
//        );
//
//        assertEquals("Course not found: 1", exception.getMessage());
//        verify(lessonRepository, never()).persist(any(Lesson.class));
//    }
//
//    @Test
//    void listLessons_quandoCursoExiste_deveRetornarListaDeLessons() {
//        Course course = new Course();
//        course.setId(1L);
//        course.setName("Java");
//
//        Lesson lesson1 = new Lesson();
//        lesson1.setId(1L);
//        lesson1.setName("POO");
//        lesson1.setCourse(course);
//
//        Lesson lesson2 = new Lesson();
//        lesson2.setId(2L);
//        lesson2.setName("Heranca");
//        lesson2.setCourse(course);
//
//        when(courseRepository.findById(1L)).thenReturn(course);
//        when(lessonRepository.find(eq("course.id"), eq(1L))).thenReturn(lessonPanacheQuery);
//        when(lessonPanacheQuery.list()).thenReturn(List.of(lesson1, lesson2));
//
//        List<LessonResponseDTO> response = service.listLessons(1L);
//
//        assertNotNull(response);
//        assertEquals(2, response.size());
//        assertEquals("POO", response.get(0).name());
//        assertEquals("Heranca", response.get(1).name());
//
//        verify(courseRepository).findById(1L);
//        verify(lessonRepository).find(eq("course.id"), eq(1L));
//        verify(lessonPanacheQuery).list();
//    }
//
//    @Test
//    void listLessons_quandoCursoNaoExiste_deveLancarNotFoundException() {
//        when(courseRepository.findById(1L)).thenReturn(null);
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> service.listLessons(1L)
//        );
//
//        assertEquals("Course not found: 1", exception.getMessage());
//        verify(lessonRepository, never()).find(eq("course.id"), eq(1L));
//    }
//}
