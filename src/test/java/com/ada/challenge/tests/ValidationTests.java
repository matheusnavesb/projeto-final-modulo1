package com.ada.challenge.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ValidationTests {

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
    void shouldReturn400WhenCourseNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": ""
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(400)
                .body("status", is(400));
    }

    @Test
    void shouldReturn400WhenCourseNameIsNull() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(400)
                .body("status", is(400));
    }

    @Test
    void shouldReturn400WhenCourseNameHasLessThan3Chars() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "ab"
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(400)
                .body("status", is(400));
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidCourseName() {
        Integer id = createCourse("Valid Name");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": ""
                }
                """)
                .when()
                .put("/courses/{id}", id)
                .then()
                .statusCode(400)
                .body("status", is(400));
    }

    @Test
    void shouldReturn400WhenLessonNameIsBlank() {
        Integer courseId = createCourse("Course Validation");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": ""
                }
                """)
                .when()
                .post("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(400)
                .body("status", is(400));
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
