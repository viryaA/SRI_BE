package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.PMStop;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.PMStopServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class PMStopController {

    @Autowired
    private PMStopServiceImpl pmStopService;

    private Response response;

    // Helper: JWT validation
    private void validateJWT(HttpServletRequest req) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");
        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @GetMapping("/getAllPMStops")
    public Response getAllPMStops(HttpServletRequest req) throws ResourceNotFoundException {
//        validateJWT(req);
        List<PMStop> list = pmStopService.getAllPMStops();
        return new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), list);
    }

    @GetMapping("/getActivePMStops")
    public Response getActivePMStops(HttpServletRequest req) throws ResourceNotFoundException {
//        validateJWT(req);
        List<PMStop> list = pmStopService.getActivePMStops();
        return new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), list);
    }

    @GetMapping("/getPMStopById/{id}")
    public Response getPMStopById(HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
//        validateJWT(req);
        Optional<PMStop> stop = pmStopService.getPMStopById(id);
        return new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), stop);
    }

    @GetMapping("/getPMStopByWorkCenter/{name}")
    public Response getPMStopByWorkCenter(HttpServletRequest req, @PathVariable String name) throws ResourceNotFoundException {
//        validateJWT(req);
        Optional<PMStop> stop = pmStopService.getPMStopByWorkCenter(name);
        return new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), stop);
    }

    @PostMapping("/insertPMStop")
    public Response insertPMStop(HttpServletRequest req, @RequestBody PMStop pmStop) throws ResourceNotFoundException {
//        validateJWT(req);
        System.out.println("Received PMStop: " + pmStop.getWORK_CENTER_TEXT());
        pmStopService.insertPMStop(
            pmStop.getWORK_CENTER_TEXT(),
            pmStop.getCREATED_BY(),
            pmStop.getLAST_UPDATED_BY(),
            pmStop.getDATE_STOP(),
            pmStop.getSTART_TIME(),
            pmStop.getEND_TIME()
        );
        return new Response(new Date(), HttpStatus.OK.value(), null, "PM Stop inserted successfully", req.getRequestURI(), null);
    }

    @PostMapping("/softDeletePMStop/{id}")
    public Response softDeletePMStop(HttpServletRequest req, @PathVariable BigDecimal id, @RequestParam String updatedBy) throws ResourceNotFoundException {
//        validateJWT(req);
        PMStop result = pmStopService.softDeletePMStop(id, updatedBy);
        return new Response(new Date(), HttpStatus.OK.value(), null, "PM Stop soft-deleted", req.getRequestURI(), result);
    }

    @PostMapping("/restorePMStop/{id}")
    public Response restorePMStop(HttpServletRequest req, @PathVariable BigDecimal id, @RequestParam String updatedBy) throws ResourceNotFoundException {
//        validateJWT(req);
        PMStop result = pmStopService.restorePMStop(id, updatedBy);
        return new Response(new Date(), HttpStatus.OK.value(), null, "PM Stop restored", req.getRequestURI(), result);
    }

    @PostMapping("/updatePMStop")
    public Response updatePMStop(HttpServletRequest req, @RequestBody PMStop pmStop) throws ResourceNotFoundException {
//        validateJWT(req);
        pmStopService.insertPMStop( 
            pmStop.getWORK_CENTER_TEXT(),
            pmStop.getCREATED_BY(),
            pmStop.getLAST_UPDATED_BY(),
            pmStop.getDATE_STOP(),
            pmStop.getSTART_TIME(),
            pmStop.getEND_TIME()
        );
        return new Response(new Date(), HttpStatus.OK.value(), null, "PM Stop updated or inserted successfully", req.getRequestURI(), null);
    }
}
