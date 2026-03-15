package com.ada.challenge.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BaseTest {

    @Inject
    protected CourseRepository courseRepository;

    @Inject
    protected LessonRepository lessonRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
    }

    protected Integer createCourse(String name) {
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
