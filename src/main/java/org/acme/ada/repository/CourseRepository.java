package org.acme.ada.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.ada.model.Course;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<Course> {
}
