package org.acme.ada.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.ada.dto.user.UserDTO;
import org.acme.ada.dto.user.UserResponseDTO;
import org.acme.ada.service.user.UserService;
import org.eclipse.microprofile.jwt.JsonWebToken;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @POST
    @PermitAll
    public Response create(@Valid UserDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"ADMIN", "USER"})
    public Response me() {
        String email = jwt.getSubject();
        UserResponseDTO response = userService.getLoggedUser(email);
        return Response.ok(response).build();
    }
}
