package org.acme.ada.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.ada.dto.course.CourseDTO;
import org.acme.ada.dto.course.CourseResponseDTO;
import org.acme.ada.dto.lesson.LessonDTO;
import org.acme.ada.dto.lesson.LessonResponseDTO;
import org.acme.ada.service.course.CourseService;

import java.net.URI;
import java.util.List;

@Path("/courses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    @Inject
    CourseService service;

    @POST
    public Response create(@Valid CourseDTO dto, @Context UriInfo uriInfo) {

        CourseResponseDTO response = service.create(dto);

        URI location = uriInfo
                .getAbsolutePathBuilder()
                .path(response.id().toString())
                .build();

        return Response
                .created(location)
                .entity(response)
                .build();
    }

    @GET
    public Response list(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {

        List<CourseResponseDTO> courses;

        if (page != null && size != null) {
            courses = service.list(page, size);
        } else {
            courses = service.listAll();
        }

        return Response.ok(courses).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {

        CourseResponseDTO course = service.findById(id);

        return Response.ok(course).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid CourseDTO dto) {

        CourseResponseDTO course = service.update(id, dto);

        return Response.ok(course).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {

        service.delete(id);

        return Response.noContent().build();
    }

    // PLUS

    @POST
    @Path("/{courseId}/lessons")
    public Response addLesson(
            @PathParam("courseId") Long courseId,
            @Valid LessonDTO dto) {

        LessonResponseDTO lesson = service.addLesson(courseId, dto);

        return Response.status(Response.Status.CREATED)
                .entity(lesson)
                .build();
    }

    // PLUS

    @GET
    @Path("/{courseId}/lessons")
    public Response listLessons(@PathParam("courseId") Long courseId) {

        List<LessonResponseDTO> lessons = service.listLessons(courseId);

        return Response.ok(lessons).build();
    }
}
