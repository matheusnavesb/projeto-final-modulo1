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
public class HTTPRequirementsTests {

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
    void shouldAcceptApplicationJson() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "HTTP Course"
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(201)
                .contentType(containsString("application/json"));
    }

    @Test
    void shouldRejectUnsupportedMediaType() {
        given()
                .contentType("text/plain")
                .body("invalid")
                .when()
                .post("/courses")
                .then()
                .statusCode(415);
    }

    @Test
    void shouldReturnLocationHeaderOnPost() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Location Header"
                }
                """)
                .when()
                .post("/courses")
                .then()
                .statusCode(201)
                .header("Location", containsString("/courses/"));
    }

    @Test
    void shouldReturnCorrectStatusCodesForCrud() {
        Integer id =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                      "name": "Status Code Test"
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
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "name": "Updated Status Code Test"
                }
                """)
                .when()
                .put("/courses/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .delete("/courses/{id}", id)
                .then()
                .statusCode(204);
    }

    @Test
    void shouldReturn404ForNonExistingResource() {
        given()
                .when()
                .get("/courses/{id}", 999999L)
                .then()
                .statusCode(404);
    }
}
