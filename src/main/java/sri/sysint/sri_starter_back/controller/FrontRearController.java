package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.FrontRear;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.FrontRearServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class FrontRearController {

    @Autowired
    private FrontRearServiceImpl frontRearService;

    private void validateToken(HttpServletRequest req) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }
        String token = header.replace("Bearer ", "");
        JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token);
    }

    @GetMapping("/getAllfrontRear")
    public ResponseEntity<Response> getAllFrontRearItems(HttpServletRequest req) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getAllFrontRear();
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }

    @GetMapping("/getByIdfrontRear/{id}")
    public ResponseEntity<Response> getFrontRearItemById(HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
        validateToken(req);
        FrontRear frontRear = frontRearService.getFrontRearById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FrontRear with ID " + id + " not found"));
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRear));
    }

    @GetMapping("/getByParallelId/{id}")
    public ResponseEntity<Response> getFrontRearByParallelId(HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getFrontRearByParallelId(id);
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }
    
    @GetMapping("/getCheatingfrontRearbyMoId")
    public ResponseEntity<Response> getCheatingFrontRear(HttpServletRequest req, @RequestParam String moId1, @RequestParam String moId2) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getCheatingFrontRearByMoId(moId1, moId2);
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }

    @GetMapping("/getCheatingfrontRearbyMoIdandVcheating")
    public ResponseEntity<Response> getCheatingFrontRear(HttpServletRequest req, @RequestParam String moId1, @RequestParam String moId2, @RequestParam BigDecimal verCheating) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getCheatingFrontRear(moId1, moId2, verCheating);
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }

    @GetMapping("/getCheatingbyMoIdandVcheatingWithItemCuring")
    public ResponseEntity<Response> getCheatingFrontRearWithItemCuring(HttpServletRequest req, @RequestParam String moId1, @RequestParam String moId2, @RequestParam BigDecimal verCheating, @RequestParam String itemCuring) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getCheatingFrontRearWithItemCuring(moId1, moId2, verCheating, itemCuring);
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }

    @GetMapping("/getCheatingbyMoIdandVcheatingWithParallelId")
    public ResponseEntity<Response> getCheatingFrontRearWithParallelId(HttpServletRequest req, @RequestParam String moId1, @RequestParam String moId2, @RequestParam BigDecimal verCheating, @RequestParam BigDecimal parallelId) throws ResourceNotFoundException {
        validateToken(req);
        List<FrontRear> frontRearList = frontRearService.getCheatingFrontRearWithParallelId(moId1, moId2, verCheating, parallelId);
        return ResponseEntity.ok(new Response( HttpStatus.OK.value(), null, "Success", req.getRequestURI(), frontRearList));
    }

    @PostMapping("/saveFrontRear")
    public ResponseEntity<Response> saveFrontRear(HttpServletRequest req, @RequestBody String jsonInput) throws ResourceNotFoundException {
        validateToken(req);
        frontRearService.saveFrontRear(jsonInput);
        return ResponseEntity.ok(new Response( HttpStatus.CREATED.value(), null, "Saved Successfully", req.getRequestURI(), null));
    }
}
