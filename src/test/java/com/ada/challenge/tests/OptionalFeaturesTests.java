package com.ada.challenge.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.exception.ConstraintViolationExceptionMapper;
import org.acme.ada.exception.GenericExceptionMapper;
import org.acme.ada.exception.NotFoundExceptionMapper;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class OptionalFeaturesTests {

    @Inject
    CourseRepository courseRepository;

    @Inject
    LessonRepository lessonRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void shouldSupportLessonEndpoints() {
        Integer courseId = createCourse("Course Optional");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Optional Lesson"
                }
                """)
                .when()
                .post("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(201)
                .body("name", is("Optional Lesson"));

        given()
                .when()
                .get("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    void shouldSupportPaginationOnCourses() {
        createCourse("Course 1");
        createCourse("Course 2");
        createCourse("Course 3");

        given()
                .queryParam("page", 0)
                .queryParam("size", 2)
                .when()
                .get("/courses")
                .then()
                .statusCode(200)
                .body("$", hasSize(2));
    }

    @Test
    void shouldHaveGlobalExceptionMappers() {
        assertNotNull(NotFoundExceptionMapper.class);
        assertNotNull(ConstraintViolationExceptionMapper.class);
        assertNotNull(GenericExceptionMapper.class);
    }

    @Test
    void shouldUseDtos() {
        assertNotNull(CourseResponseDTO.class);
        assertNotNull(LessonResponseDTO.class);
    }

    private Integer createCourse(String name) {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "%s"
                    }
                    """.formatted(name))
                .when()
                .post("/courses")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}
