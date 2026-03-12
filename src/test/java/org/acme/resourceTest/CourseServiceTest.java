package org.acme.resourceTest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    PanacheQuery<Course> coursePanacheQuery;

    @Mock
    PanacheQuery<Lesson> lessonPanacheQuery;

    @InjectMocks
    CourseServiceImpl service;

    @Test
    void create_devePersistirCursoERetornarResponseDTO() {
        CourseDTO dto = new CourseDTO("Java Básico");

        CourseResponseDTO response = service.create(dto);

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).persist(captor.capture());

        Course cursoPersistido = captor.getValue();
        assertEquals("Java Básico", cursoPersistido.getName());

        assertNotNull(response);
        assertEquals("Java Básico", response.name());
    }

    @Test
    void list_deveRetornarListaDeCourseResponseDTO() {
        Course c1 = new Course();
        c1.setId(1L);
        c1.setName("Java");

        Course c2 = new Course();
        c2.setId(2L);
        c2.setName("Spring");

        when(courseRepository.findAll()).thenReturn(coursePanacheQuery);
        when(coursePanacheQuery.page(any(Page.class))).thenReturn(coursePanacheQuery);
        when(coursePanacheQuery.list()).thenReturn(List.of(c1, c2));

        List<CourseResponseDTO> response = service.list(0, 10);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Java", response.get(0).name());
        assertEquals("Spring", response.get(1).name());

        verify(courseRepository).findAll();
        verify(coursePanacheQuery).page(any(Page.class));
        verify(coursePanacheQuery).list();
    }

    @Test
    void findById_deveRetornarCursoQuandoExistir() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Quarkus");

        when(courseRepository.findById(1L)).thenReturn(course);

        CourseResponseDTO response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Quarkus", response.name());
    }

    @Test
    void findById_deveLancarNotFoundQuandoCursoNaoExistir() {
        when(courseRepository.findById(99L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.findById(99L));

        assertEquals("Course not found: 99", ex.getMessage());
    }

    @Test
    void update_deveAtualizarNomeDoCursoQuandoExistir() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Nome Antigo");

        CourseDTO dto = new CourseDTO("Nome Novo");

        when(courseRepository.findById(1L)).thenReturn(course);

        CourseResponseDTO response = service.update(1L, dto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Nome Novo", response.name());
        assertEquals("Nome Novo", course.getName());
    }

    @Test
    void update_deveLancarNotFoundQuandoCursoNaoExistir() {
        CourseDTO dto = new CourseDTO("Novo Nome");

        when(courseRepository.findById(10L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.update(10L, dto));

        assertEquals("Course not found: 10", ex.getMessage());
    }

    @Test
    void delete_deveRemoverCursoQuandoExistir() {
        when(courseRepository.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.delete(1L));

        verify(courseRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarNotFoundQuandoCursoNaoExistir() {
        when(courseRepository.deleteById(55L)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.delete(55L));

        assertEquals("Course not found: 55", ex.getMessage());
    }

    @Test
    void addLesson_devePersistirLessonEVincularAoCurso() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java");

        LessonDTO dto = new LessonDTO("Aula 01");

        when(courseRepository.findById(1L)).thenReturn(course);

        LessonResponseDTO response = service.addLesson(1L, dto);

        ArgumentCaptor<Lesson> captor = ArgumentCaptor.forClass(Lesson.class);
        verify(lessonRepository).persist(captor.capture());

        Lesson lessonPersistida = captor.getValue();
        assertEquals("Aula 01", lessonPersistida.getName());
        assertEquals(course, lessonPersistida.getCourse());

        assertNotNull(response);
        assertEquals("Aula 01", response.name());
    }

    @Test
    void addLesson_deveLancarNotFoundQuandoCursoNaoExistir() {
        LessonDTO dto = new LessonDTO("Aula inexistente");

        when(courseRepository.findById(99L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.addLesson(99L, dto));

        assertEquals("Course not found: 99", ex.getMessage());

        verify(lessonRepository, never()).persist(any(Lesson.class));
    }

    @Test
    void listLessons_deveRetornarListaDeLessonsQuandoCursoExistir() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java");

        Lesson l1 = new Lesson();
        l1.setId(1L);
        l1.setName("Introdução");
        l1.setCourse(course);

        Lesson l2 = new Lesson();
        l2.setId(2L);
        l2.setName("POO");
        l2.setCourse(course);

        when(courseRepository.findById(1L)).thenReturn(course);
        when(lessonRepository.find(eq("course.id"), eq(1L))).thenReturn(lessonPanacheQuery);
        when(lessonPanacheQuery.list()).thenReturn(List.of(l1, l2));

        List<LessonResponseDTO> response = service.listLessons(1L);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Introdução", response.get(0).name());
        assertEquals("POO", response.get(1).name());

        verify(courseRepository).findById(1L);
        verify(lessonRepository).find("course.id", 1L);
        verify(lessonPanacheQuery).list();
    }

    @Test
    void listLessons_deveLancarNotFoundQuandoCursoNaoExistir() {
        when(courseRepository.findById(77L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.listLessons(77L));

        assertEquals("Course not found: 77", ex.getMessage());

        verify(lessonRepository, never()).find(anyString(), Optional.ofNullable(any()));
    }
}