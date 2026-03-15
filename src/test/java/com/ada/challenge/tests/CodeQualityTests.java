package com.ada.challenge.tests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.reflect.Constructor;

import org.acme.ada.dto.course.CourseDTO;
import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.resource.CourseResource;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

public class CodeQualityTests {

    @Test
    void courseResource_shouldHaveRestAnnotations() {
        assertTrue(CourseResource.class.isAnnotationPresent(jakarta.ws.rs.Path.class));
        assertTrue(CourseResource.class.isAnnotationPresent(jakarta.ws.rs.Consumes.class));
        assertTrue(CourseResource.class.isAnnotationPresent(jakarta.ws.rs.Produces.class));
    }

    @Test
    void courseDto_shouldHaveBeanValidation() {
        Constructor<?> constructor = CourseDTO.class.getDeclaredConstructors()[0];
        Parameter parameter = constructor.getParameters()[0];

        assertTrue(parameter.isAnnotationPresent(NotBlank.class));
        assertTrue(parameter.isAnnotationPresent(Size.class));
    }

    @Test
    void lessonDto_shouldHaveBeanValidation() {
        Constructor<?> constructor = LessonDTO.class.getDeclaredConstructors()[0];
        Parameter parameter = constructor.getParameters()[0];

        assertTrue(parameter.isAnnotationPresent(NotBlank.class));
    }

    @Test
    void resourceMethods_shouldUseValidAnnotation() throws Exception {
        Method createMethod = CourseResource.class.getMethod("create", CourseDTO.class, jakarta.ws.rs.core.UriInfo.class);
        Parameter createDtoParam = createMethod.getParameters()[0];
        assertTrue(createDtoParam.isAnnotationPresent(Valid.class));

        Method updateMethod = CourseResource.class.getMethod("update", Long.class, CourseDTO.class);
        Parameter updateDtoParam = updateMethod.getParameters()[1];
        assertTrue(updateDtoParam.isAnnotationPresent(Valid.class));

        Method addLessonMethod = CourseResource.class.getMethod("addLesson", Long.class, LessonDTO.class);
        Parameter lessonDtoParam = addLessonMethod.getParameters()[1];
        assertTrue(lessonDtoParam.isAnnotationPresent(Valid.class));
    }

    @Test
    void project_shouldUseResponseDtos() {
        assertNotNull(CourseResponseDTO.class);
        assertNotNull(LessonResponseDTO.class);
    }
}
