package sri.sysint.sri_starter_back.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.MachineProduct;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.MachineProductServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class MachineProductController {
	@Autowired
    private MachineProductServiceImpl machineProductServiceImpl;

    public MachineProductController(MachineProductServiceImpl machineProductServiceImpl) {
        this.machineProductServiceImpl = machineProductServiceImpl;
    }
    
    private Response response;
    
    @PostMapping("/saveMachineProduct")
    public Response saveMachineProducts(
            HttpServletRequest req,
            @RequestBody String jsonInput) {

    	machineProductServiceImpl.saveMachineProducts(jsonInput);

        return new Response(
                new Date(),
                HttpStatus.OK.value(),
                null,
                HttpStatus.OK.getReasonPhrase(),
                req.getRequestURI(),
                "Data berhasil disimpan."
        );
    }

    
    @GetMapping("/getMachineProductsmoByVersion")
    public Response getMachineProductsByVersion(
            HttpServletRequest req,
            @RequestParam("moId1") String moId1,
            @RequestParam("moId2") String moId2,
            @RequestParam("verCheating") BigDecimal verCheating) {
        
        List<MachineProduct> filteredProducts = machineProductServiceImpl
                .findCheatingMacProdByMoIdAndVcheating(moId1, moId2, verCheating);

        return new Response(
                new Date(),
                HttpStatus.OK.value(),
                null,
                HttpStatus.OK.getReasonPhrase(),
                req.getRequestURI(),
                filteredProducts
        );
    }

    @GetMapping("/getMachineProductsmoByCuring")
    public Response getMachineProductsByCuring(
            HttpServletRequest req, 
            @RequestParam("moId1") String moId1,
            @RequestParam("moId2") String moId2,
            @RequestParam("verCheating") BigDecimal verCheating,
            @RequestParam("itemCuring") String itemCuring) {
        
        List<MachineProduct> filteredProducts = machineProductServiceImpl
                .findCheatingMacProdByMoIdVcheatingAndItemCuring(moId1, moId2, verCheating, itemCuring);

        return new Response(
                new Date(),
                HttpStatus.OK.value(),
                null,
                HttpStatus.OK.getReasonPhrase(),
                req.getRequestURI(),
                filteredProducts
        );
    }

}
