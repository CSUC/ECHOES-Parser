package org.csuc.rest.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.Morphia.Core.entities.UserToken;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.utils.Auth;
import org.csuc.rest.api.utils.Credentials;
import org.csuc.rest.api.utils.Secured;
import org.csuc.rest.api.utils.UnauthorizedToken;

@Path("/authentication")
public class Authentication {

	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Secured({ Role.Admin, Role.User })
	@Consumes(MediaType.APPLICATION_JSON)
	public String isAuthenticate() {
		return securityContext.getUserPrincipal().getName();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Credentials credentials) {
		try {
			String username = credentials.getUsername();
			String password = credentials.getPassword();

			// Authenticate the user using the credentials provided
			Auth auth = new Auth(username, password);
			auth.authenticate();

			// Issue a token for the user
			UserToken token = auth.issueToken();
			
			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED)
				.entity(new UnauthorizedToken(
						Response.Status.UNAUTHORIZED.getReasonPhrase(), 
						"[AUTHENTICATION_FAILURE] Authentication failed due to invalid authentication credentials.", 
						Response.Status.UNAUTHORIZED.getStatusCode()))
				.build();
		}
	}

	@GET
	@Path("/logout")
	@Secured({ Role.Admin, Role.User })
	@Produces("text/xml")
	public Response logout() {
		servletRequest.getSession().invalidate();
		return Response.status(Response.Status.ACCEPTED).entity("logout").build();
	}
}
