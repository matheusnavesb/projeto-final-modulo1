package com.ada.challenge.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CRUDOperationsTests extends BaseTest {

    @Test
    void shouldCreateCourse() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Java Fundamentals"
                        }
                        """)
                .when()
                .post("/courses")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("name", is("Java Fundamentals"))
                .body("lessons", anyOf(nullValue(), empty()));
    }

    @Test
    void shouldListCourses() {
        createCourse("Course A");
        createCourse("Course B");

        given()
                .when()
                .get("/courses")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("name", hasItems("Course A", "Course B"));
    }

    @Test
    void shouldFindCourseById() {
        Integer id = createCourse("Quarkus API");

        given()
                .when()
                .get("/courses/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Quarkus API"));
    }

    @Test
    void shouldUpdateCourse() {
        Integer id = createCourse("Old Name");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "New Name"
                        }
                        """)
                .when()
                .put("/courses/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("New Name"));
    }

    @Test
    void shouldDeleteCourse() {
        Integer id = createCourse("Delete Me");

        given()
                .when()
                .delete("/courses/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/courses/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404WhenCourseNotFound() {
        given()
                .when()
                .get("/courses/{id}", 999999L)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingCourse() {
        given()
                .when()
                .delete("/courses/{id}", 999999L)
                .then()
                .statusCode(404);
    }

}
