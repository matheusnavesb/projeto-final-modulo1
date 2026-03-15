package com.ada.challenge.tests;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import org.acme.ada.model.Course;
import org.acme.ada.model.Lesson;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class ModelDataTests {

    @Test
    void course_shouldHaveExpectedFields() throws Exception {
        Class<Course> clazz = Course.class;

        assertTrue(clazz.isAnnotationPresent(Entity.class));

        Field id = clazz.getDeclaredField("id");
        Field name = clazz.getDeclaredField("name");
        Field lessons = clazz.getDeclaredField("lessons");

        assertNotNull(id);
        assertNotNull(name);
        assertNotNull(lessons);

        assertTrue(id.isAnnotationPresent(Id.class));
        assertTrue(lessons.isAnnotationPresent(OneToMany.class));
    }

    @Test
    void lesson_shouldHaveExpectedFields() throws Exception {
        Class<Lesson> clazz = Lesson.class;

        assertTrue(clazz.isAnnotationPresent(Entity.class));

        Field id = clazz.getDeclaredField("id");
        Field name = clazz.getDeclaredField("name");
        Field course = clazz.getDeclaredField("course");

        assertNotNull(id);
        assertNotNull(name);
        assertNotNull(course);

        assertTrue(id.isAnnotationPresent(Id.class));
        assertTrue(course.isAnnotationPresent(ManyToOne.class));
    }
}
