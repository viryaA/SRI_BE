package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.PlantServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class PlantController {
		
	private Response response;	

	@Autowired
	private PlantServiceImpl plantServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/getAllPlant")
	public Response getAllPlant(final HttpServletRequest req) throws ResourceNotFoundException {

		List<Plant> plants = new ArrayList<>();
		plants = plantServiceImpl.getAllPlant();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			plants
		);
	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/getPlantById/{id}")
	public Response getPlantById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {

		Optional<Plant> plant = Optional.of(new Plant());
		plant = plantServiceImpl.getPlantById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			plant
		);
	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/savePlant")
	public Response savePlant(final HttpServletRequest req, @RequestBody Plant plant) throws ResourceNotFoundException {

		Plant savedPlant = plantServiceImpl.savePlant(plant);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			savedPlant
		);
	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/updatePlant")
	public Response updatePlant(final HttpServletRequest req, @RequestBody Plant plant) throws ResourceNotFoundException {

		Plant updatedPlant = plantServiceImpl.updatePlant(plant);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			updatedPlant
		);
	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/deletePlant")
	public Response deletetePlant(final HttpServletRequest req, @RequestBody Plant plant) throws ResourceNotFoundException {

		Plant deletedPlant = plantServiceImpl.deletePlant(plant);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			deletedPlant
		);
	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/restorePlant")
	public Response restorePlant(final HttpServletRequest req, @RequestBody Plant plant) throws ResourceNotFoundException {

		Plant restoredPlant = plantServiceImpl.restorePlant(plant);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			restoredPlant
		);
	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/savePlantsExcel")
	public Response savePlantsExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {

			if (file.isEmpty()) {
				return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
			}

			try (InputStream inputStream = file.getInputStream()) {
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);

				List<Plant> plants = new ArrayList<>();
				List<String> errorMessages = new ArrayList<>();

				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						boolean isEmptyRow = true;

						for (int j = 0; j < row.getLastCellNum(); j++) {
							Cell cell = row.getCell(j);
							if (cell != null && cell.getCellType() != CellType.BLANK) {
								isEmptyRow = false;
								break;
							}
						}

						if (isEmptyRow) {
							continue;
						}

						Cell nameCell = row.getCell(2);

						if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
							errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 3 (Plant Name)");
							continue;
						}

						Plant plant = new Plant();
						plant.setPLANT_ID(plantServiceImpl.getNewId());
						plant.setPLANT_NAME(nameCell.getStringCellValue());
						plant.setSTATUS(BigDecimal.valueOf(1));
						plant.setCREATION_DATE(new Date());
						plant.setLAST_UPDATE_DATE(new Date());

						plants.add(plant);
					}
				}

				if (!errorMessages.isEmpty()) {
					return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
				}

				plantServiceImpl.deleteAllPlant();
				for (Plant plant : plants) {
					plantServiceImpl.savePlant(plant);
				}

				return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), plants);

			} catch (IOException e) {
				throw new RuntimeException("Error processing file", e);
			}
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@RequestMapping("/exportPlantsExcel")
	public ResponseEntity<InputStreamResource> exportPLantsExcel() throws IOException {
	    String filename = "EXPORT_MASTER_PLANT.xlsx";
	    
	    ByteArrayInputStream data = plantServiceImpl.exportPlantsExcel();
	    InputStreamResource file = new InputStreamResource(data);
	    
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@RequestMapping("/layoutPlantsExcel")
	public ResponseEntity<InputStreamResource> layoutPLantsExcel() throws IOException {
	    String filename = "LAYOUT_MASTER_PLANT.xlsx";
	    
	    ByteArrayInputStream data = plantServiceImpl.layoutPlantsExcel();
	    InputStreamResource file = new InputStreamResource(data);
	    
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	}
	
}


