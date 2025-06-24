package sri.sysint.sri_starter_back.security;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import sri.sysint.sri_starter_back.model.CustomPrincipal;

/**
 * JwtAuthorizationFilter
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
 
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
        	
        	DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
        		    .build()
        		    .verify(token);

			String username = jwt.getSubject();
			String role = jwt.getClaim("role").asString();
			Long userId = jwt.getClaim("userId").asLong();
			
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

			CustomPrincipal principal = new CustomPrincipal(userId, username, role, authorities);

            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            
            UsernamePasswordAuthenticationToken authToken =
            	    new UsernamePasswordAuthenticationToken(principal, null, authorities);

        	SecurityContextHolder.getContext().setAuthentication(authToken);
            
//            if (user != null) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}