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
import org.springframework.transaction.annotation.Transactional;
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
import sri.sysint.sri_starter_back.model.Pattern;
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.PatternServiceImpl;
import sri.sysint.sri_starter_back.service.PlantServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class PatternController {
		
	private Response response;	

	@Autowired
	private PatternServiceImpl patternServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getAllPattern")
	public Response getAllPattern(final HttpServletRequest req) throws ResourceNotFoundException {
		List<Pattern> patterns = new ArrayList<>();
		patterns = patternServiceImpl.getAllPattern();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			patterns
		);
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getPatternById/{id}")
	public Response getPatternById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {

		Optional<Pattern> pattern = Optional.of(new Pattern());
		pattern = patternServiceImpl.getPatternById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			pattern
		);
	    return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/savePattern")
	public Response savePattern(final HttpServletRequest req, @RequestBody Pattern pattern) throws ResourceNotFoundException {

		Pattern savedPattern = patternServiceImpl.savePattern(pattern);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			savedPattern
		);
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/updatePattern")
	public Response updatePattern(final HttpServletRequest req, @RequestBody Pattern pattern) throws ResourceNotFoundException {
	
		Pattern updatedPattern = patternServiceImpl.updatePattern(pattern);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			updatedPattern
		);
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deletePattern")
	public Response deletetePattern(final HttpServletRequest req, @RequestBody Pattern pattern) throws ResourceNotFoundException {

		Pattern deletedPattern = patternServiceImpl.deletePattern(pattern);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			deletedPattern
		);
		
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/restorePattern")
	public Response restorePattern(final HttpServletRequest req, @RequestBody Pattern pattern) throws ResourceNotFoundException {

		Pattern restoredPattern = patternServiceImpl.restorePattern(pattern);

		Response response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			restoredPattern
		);

		return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/savePatternsExcel")
	@Transactional
	public Response savePatternsExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
		if (file.isEmpty()) {
			return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
		}

		try (InputStream inputStream = file.getInputStream()) {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<Pattern> patterns = new ArrayList<>();
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

					Pattern pattern = new Pattern();
					Cell patternNameCell = row.getCell(2);

					if (patternNameCell == null || patternNameCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 3 (Pattern Name)");
						continue;
					}

					if (patternNameCell.getCellType() == CellType.STRING) {
						pattern.setPATTERN_ID(patternServiceImpl.getNewId());
						pattern.setPATTERN_NAME(patternNameCell.getStringCellValue());
						pattern.setSTATUS(BigDecimal.valueOf(1));
						pattern.setCREATION_DATE(new Date());
						pattern.setLAST_UPDATE_DATE(new Date());

						patterns.add(pattern);
					}
				}
			}

			if (!errorMessages.isEmpty()) {
				return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
			}

			patternServiceImpl.deleteAllPattern();
			for (Pattern pattern : patterns) {
				patternServiceImpl.savePattern(pattern);
			}

			return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), patterns);

		} catch (IOException e) {
			throw new RuntimeException("Error processing file", e);
		}
	}

	@PreAuthorize("isAuthenticated()")
    @RequestMapping("/exportPatternExcel")
    public ResponseEntity<InputStreamResource> exportPatternExcel() throws IOException {
        String filename = "EXPORT_MASTER_PATTERN.xlsx"; 

        ByteArrayInputStream data = patternServiceImpl.exportPatternsExcel();
        InputStreamResource file = new InputStreamResource(data); 

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file); 
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/layoutPatternExcel")
    public ResponseEntity<InputStreamResource> layoutPatternExcel() throws IOException {
        String filename = "LAYOUT_MASTER_PATTERN.xlsx"; 

        ByteArrayInputStream data = patternServiceImpl.layoutPatternsExcel();
        InputStreamResource file = new InputStreamResource(data); 

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file); 
    }

}


