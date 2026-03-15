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
public class EndpointsTests {

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
    void postCourses_shouldExist() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Endpoint Test"
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(201);
    }

    @Test
    void getCourses_shouldExist() {
        given()
                .when()
                .get("/courses")
                .then()
                .statusCode(200);
    }

    @Test
    void getCourseById_shouldExist() {
        Integer id = createCourse("Course Endpoint");

        given()
                .when()
                .get("/courses/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id));
    }

    @Test
    void putCourse_shouldExist() {
        Integer id = createCourse("Before Update");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "After Update"
                }
                """)
                .when()
                .put("/courses/{id}", id)
                .then()
                .statusCode(200)
                .body("name", is("After Update"));
    }

    @Test
    void deleteCourse_shouldExist() {
        Integer id = createCourse("To Delete");

        given()
                .when()
                .delete("/courses/{id}", id)
                .then()
                .statusCode(204);
    }

    @Test
    void postLessons_shouldExist() {
        Integer courseId = createCourse("Course with Lesson");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Lesson 1"
                }
                """)
                .when()
                .post("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("Lesson 1"));
    }

    @Test
    void getLessons_shouldExist() {
        Integer courseId = createCourse("Course Lessons");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Lesson A"
                }
                """)
                .when()
                .post("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/courses/{courseId}/lessons", courseId)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
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
