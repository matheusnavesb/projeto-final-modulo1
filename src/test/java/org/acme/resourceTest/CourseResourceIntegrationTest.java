package org.acme.resourceTest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.model.Course;
import org.acme.ada.repository.CourseRepository;
import org.acme.ada.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//@QuarkusTest
//class CourseResourceIntegrationTest {
//
//    @Inject
//    CourseRepository courseRepository;
//
//    @Inject
//    LessonRepository lessonRepository;
//
//    @BeforeEach
//    @Transactional
//    void limparBase() {
//        lessonRepository.deleteAll();
//        courseRepository.deleteAll();
//    }
//
//    @Test
//    void create_deveRetornar201ECriarCurso() {
//        String body = """
//                {
//                  "name": "Java"
//                }
//                """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(body)
//                .when()
//                .post("/courses")
//                .then()
//                .statusCode(201)
//                .body("id", notNullValue())
//                .body("name", equalTo("Java"));
//    }
//
//    @Test
//    void list_deveRetornarCursosPaginados() {
//        criarCurso("Java");
//        criarCurso("Spring");
//
//        given()
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .when()
//                .get("/courses")
//                .then()
//                .statusCode(200)
//                .body("$.size()", is(2))
//                .body("[0].name", equalTo("Java"))
//                .body("[1].name", equalTo("Spring"));
//    }
//
//    @Test
//    void findById_deveRetornarCursoQuandoExistir() {
//        Long id = criarCurso("Quarkus");
//
//        given()
//                .pathParam("id", id)
//                .when()
//                .get("/courses/{id}")
//                .then()
//                .statusCode(200)
//                .body("id", equalTo(id.intValue()))
//                .body("name", equalTo("Quarkus"));
//    }
//
//    @Test
//    void findById_quandoNaoExistir_deveRetornar404() {
//        given()
//                .pathParam("id", 9999L)
//                .when()
//                .get("/courses/{id}")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    void update_deveAlterarNomeDoCurso() {
//        Long id = criarCurso("Java Básico");
//
//        String body = """
//                {
//                  "name": "Java Avançado"
//                }
//                """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .pathParam("id", id)
//                .body(body)
//                .when()
//                .put("/courses/{id}")
//                .then()
//                .statusCode(200)
//                .body("id", equalTo(id.intValue()))
//                .body("name", equalTo("Java Avançado"));
//    }
//
//    @Test
//    void delete_deveRemoverCurso() {
//        Long id = criarCurso("Curso para deletar");
//
//        given()
//                .pathParam("id", id)
//                .when()
//                .delete("/courses/{id}")
//                .then()
//                .statusCode(204);
//
//        given()
//                .pathParam("id", id)
//                .when()
//                .get("/courses/{id}")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    void addLesson_deveCriarAulaParaOCurso() {
//        Long courseId = criarCurso("Java");
//
//        String body = """
//                {
//                  "name": "Orientação a Objetos"
//                }
//                """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .pathParam("courseId", courseId)
//                .body(body)
//                .when()
//                .post("/courses/{courseId}/lessons")
//                .then()
//                .statusCode(201)
//                .body("id", notNullValue())
//                .body("name", equalTo("Orientação a Objetos"));
//    }
//
//    @Test
//    void listLessons_deveRetornarAulasDoCurso() {
//        Long courseId = criarCurso("Java");
//
//        given()
//                .contentType(ContentType.JSON)
//                .pathParam("courseId", courseId)
//                .body("""
//                            { "name": "POO" }
//                        """)
//                .when()
//                .post("/courses/{courseId}/lessons")
//                .then()
//                .statusCode(201);
//
//        given()
//                .contentType(ContentType.JSON)
//                .pathParam("courseId", courseId)
//                .body("""
//                            { "name": "Herança" }
//                        """)
//                .when()
//                .post("/courses/{courseId}/lessons")
//                .then()
//                .statusCode(201);
//
//        given()
//                .pathParam("courseId", courseId)
//                .when()
//                .get("/courses/{courseId}/lessons")
//                .then()
//                .statusCode(200)
//                .body("$.size()", is(2))
//                .body("[0].name", equalTo("POO"))
//                .body("[1].name", equalTo("Herança"));
//    }
//
//    private Long criarCurso(String nome) {
//        return given()
//                .contentType(ContentType.JSON)
//                .body("""
//                        {
//                          "name": "%s"
//                        }
//                        """.formatted(nome))
//                .when()
//                .post("/courses")
//                .then()
//                .statusCode(201)
//                .extract()
//                .jsonPath()
//                .getLong("id");
//    }
//}
