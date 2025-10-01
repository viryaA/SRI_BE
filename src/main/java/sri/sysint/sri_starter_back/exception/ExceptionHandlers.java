package sri.sysint.sri_starter_back.exception;

import java.util.Date;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;

import sri.sysint.sri_starter_back.model.Response;

@RestControllerAdvice
public class ExceptionHandlers  {
	
   
   @ExceptionHandler(ResourceNotFoundException.class)
   @ResponseStatus(value = HttpStatus.NOT_FOUND)
   public @ResponseBody Response handleResourceNotFound(final ResourceNotFoundException exception, final HttpServletRequest req) {
	   
	   Response error = new Response();
	   error.setStatus(HttpStatus.NOT_FOUND.value());
	   error.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
	   error.setMessage(exception.getMessage());	  
	   error.setPath(req.getRequestURI());
	   
	   return error;
   }
   
    @ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Response handleException(final Exception exception,
			final HttpServletRequest req) {

		Response error = new Response();
		   error.setStatus(HttpStatus.NOT_FOUND.value());
		   error.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
		   error.setMessage(exception.getMessage());	  
		   error.setPath(req.getRequestURI());
		   
		return error;
	}
    
    @ExceptionHandler(TokenExpiredException.class)
   	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
   	public @ResponseBody Response handleTokenExpiredException(final TokenExpiredException exception,
   			final HttpServletRequest req) {

   		Response error = new Response();
   		   error.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
   		   error.setError(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
   		   error.setMessage(exception.getMessage());	  
   		   error.setPath(req.getRequestURI());
   		   
   		return error;
   	}
    
    @ExceptionHandler(ServletException.class)
   	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
   	public @ResponseBody Response handleServletException(final ServletException exception,
   			final HttpServletRequest req) {

   		Response error = new Response();
   		   error.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
   		   error.setError(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
   		   error.setMessage(exception.getMessage());	  
   		   error.setPath(req.getRequestURI());
   		   
   		return error;
   	}
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Just get the first validation error message
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Invalid input");

        return new Response(
                HttpStatus.BAD_REQUEST.value(),
                null,
                errors,
                request.getRequestURI(),
                null
        );
    }


    
}