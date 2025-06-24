package sri.sysint.sri_starter_back.controller;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.CustomPrincipal;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.model.Roles;
import sri.sysint.sri_starter_back.model.UserLogin;
import sri.sysint.sri_starter_back.model.Users;
import sri.sysint.sri_starter_back.model.view.ViewUsers;
import sri.sysint.sri_starter_back.security.SecurityConstants;
import sri.sysint.sri_starter_back.service.UserDetailsServiceImpl;
import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;
import static sri.sysint.sri_starter_back.security.SecurityConstants.HEADER_STRING;
import static sri.sysint.sri_starter_back.security.SecurityConstants.TOKEN_PREFIX;

@CrossOrigin(maxAge = 3600)
@RestController
public class UserController {
		
	private Response response;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;

	
	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/getAllUser")
	public Response getAllUser(final HttpServletRequest req) throws ResourceNotFoundException {
        try {
            List<Users> users = userDetailsServiceImpl.getAllUser();
            response = new Response( HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), users);
        } catch (Exception e) {
            response = new Response( HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage(), req.getRequestURI(), null);
        }

	    return response;
	}

//	@PreAuthorize("isAuthenticated() && #userName == principal.username")
	@GetMapping("/getUsername/{userName}")
	public Response getUserByUsername(@PathVariable String userName, HttpServletRequest req) throws ResourceNotFoundException {
		
	    try {
	    	Users user = userDetailsServiceImpl.getUserByUsername(userName);
	    	if(user != null) {
	    		Roles role = userDetailsServiceImpl.getRoleByUserId(user.getId());
	    		if(role != null) {
	    			Roles dataRole = new Roles(role);
	    			ViewUsers dataUser = new ViewUsers(user, dataRole);
	    			response = new Response( HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), dataUser);
	    		}
	    	}else {
	    		response = new Response(HttpStatus.NOT_FOUND.value(), null, HttpStatus.NOT_FOUND.getReasonPhrase(), req.getRequestURI(), null);
	    	}
	    }catch(Exception e) {
	    	response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage(), req.getRequestURI(), null);
	    }

	    return response;
	}
	
	@PostMapping("/loginUser")
	public Response signin(final HttpServletRequest req, @Valid @RequestBody UserLogin user) throws ResourceNotFoundException {
	    Users existingUser = userDetailsServiceImpl.getUser(user.getUserName(), user.getPassword());
	    if (existingUser != null) {
	        // 🔹 Get role using service
	        Roles role = userDetailsServiceImpl.getRoleByUserId(existingUser.getId());
	        String roleName = role != null ? role.getRole_name() : "USER"; // default fallback

	        // 🔐 Create JWT with custom claims
	        String token = JWT.create()
	            .withSubject(existingUser.getUserName())  // standard: sub = username
	            .withClaim("userId", existingUser.getId()) // custom claim: user ID
	            .withClaim("role", roleName)               // custom claim: role
	            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
	            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

	        response = new Response(
	            HttpStatus.OK.value(),
	            null,
	            HttpStatus.OK.getReasonPhrase(),
	            req.getRequestURI(),
	            token
	        );
	    } else {
	        response = new Response(
	            HttpStatus.NOT_FOUND.value(),
	            null,
	            "User not found",
	            req.getRequestURI(),
	            null
	        );
	    }

	    return response;
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public Response whoAmI(final HttpServletRequest req, Principal principal) {
	    CustomPrincipal custom = (CustomPrincipal) ((Authentication) principal).getPrincipal();

	    String message = "You are: " + custom.getUsername() +
	                     ", Role: " + custom.getRole() +
	                     ", ID: " + custom.getUserId();

	    return new Response(
	        HttpStatus.OK.value(),
	        null,
	        message,
	        req.getRequestURI(),
	        null
	    );
	}

}
