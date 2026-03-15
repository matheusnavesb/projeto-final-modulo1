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
public class PersistenceTests {

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
    void shouldPersistCreatedCourse() {
        Integer id =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                      "name": "Persistent Course"
                    }
                    """)
                        .when()
                        .post("/courses")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .when()
                .get("/courses/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Persistent Course"));
    }

    @Test
    void shouldPersistLessonForCourse() {
        Integer courseId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                      "name": "Course Persistence"
                    }
                    """)
                        .when()
                        .post("/courses")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Stored Lesson"
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
                .body("$", hasSize(1))
                .body("[0].name", is("Stored Lesson"));
    }
}
