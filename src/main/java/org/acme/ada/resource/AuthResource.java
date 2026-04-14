package org.acme.ada.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.ada.dto.user.LoginDTO;
import org.acme.ada.dto.user.TokenResponseDTO;
import org.acme.ada.service.auth.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/token")
    @PermitAll
    public Response token(@Valid LoginDTO dto) {
        TokenResponseDTO response = authService.generateToken(dto);
        return Response.ok(response).build();
    }
}
