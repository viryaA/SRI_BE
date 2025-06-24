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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.ItemCuring;
import sri.sysint.sri_starter_back.model.MachineCuringType;
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.model.Size;
import sri.sysint.sri_starter_back.service.ItemCuringServiceImpl;
import sri.sysint.sri_starter_back.service.MachineCuringTypeServiceImpl;
import sri.sysint.sri_starter_back.service.PlantServiceImpl;
import sri.sysint.sri_starter_back.service.SizeServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class SizeController {
		
	private Response response;	

	@Autowired
	private SizeServiceImpl sizeServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getAllSize")
	public Response getAllSize(final HttpServletRequest req) throws ResourceNotFoundException {
		List<Size> sizes = new ArrayList<>();
		sizes = sizeServiceImpl.getAllSize();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			sizes
		);
	    return response;
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getSizeById/{id}")
	public Response getSizeById(final HttpServletRequest req, @PathVariable String id) throws ResourceNotFoundException {
		Optional<Size> size = Optional.of(new Size());
		size = sizeServiceImpl.getSizeById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			size
		);
	    return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/saveSize")
	public Response saveSize(final HttpServletRequest req, @RequestBody Size size) throws ResourceNotFoundException {
	        	Size savedSize = sizeServiceImpl.saveSize(size);

	    	    response = new Response(
	    	        
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        savedSize
	    	    );

	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/updateSize")
	public Response updateSize(final HttpServletRequest req, @RequestBody Size size) throws ResourceNotFoundException {
	        	Size updatedSize = sizeServiceImpl.updateSize(size);

	    	    response = new Response(
	    	        
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        updatedSize
	    	    );

	    return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deleteSize")
	public Response deleteSize(final HttpServletRequest req, @RequestBody Size size) throws ResourceNotFoundException {
	        	Size deletedSize = sizeServiceImpl.deleteSize(size);

	    	    response = new Response(
	    	        
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        deletedSize
	    	    );
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/restoreSize")
	public Response restoreSize(final HttpServletRequest req, @RequestBody Size size) throws ResourceNotFoundException {
	            Size restoredSize = sizeServiceImpl.restoreSize(size);

	            Response response = new Response(
	                
	                HttpStatus.OK.value(),
	                null,
	                HttpStatus.OK.getReasonPhrase(),
	                req.getRequestURI(),
	                restoredSize
	            );
	            return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/saveSizeExcel")
	@Transactional
	public Response saveSizeExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
		try{
			if (file.isEmpty()) {
				return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
			}

			try (InputStream inputStream = file.getInputStream()) {
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);

				List<Size> sizes = new ArrayList<>();
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

						Size size = new Size();
						Cell sizeIdCell = row.getCell(1);
						Cell descriptionCell = row.getCell(2);

						if (sizeIdCell == null || sizeIdCell.getCellType() == CellType.BLANK) {
							errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 2 (Size ID)");
							continue;
						}

						if (descriptionCell == null || descriptionCell.getCellType() == CellType.BLANK) {
							errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 3 (Description)");
							continue;
						}

						size.setSIZE_ID(sizeIdCell.getStringCellValue());
						size.setDESCRIPTION(descriptionCell.getStringCellValue());
						size.setSTATUS(BigDecimal.valueOf(1));
						size.setCREATION_DATE(new Date());
						size.setLAST_UPDATE_DATE(new Date());

						sizes.add(size);
					}
				}

				if (!errorMessages.isEmpty()) {
					return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
				}

				sizeServiceImpl.deleteAllSize();
				for (Size size : sizes) {
					sizeServiceImpl.saveSize(size);
				}

				return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), sizes);

			} catch (IOException e) {
				throw new RuntimeException("Error processing file", e);
			}
	    } catch (IllegalArgumentException e) {
	        return new Response( HttpStatus.BAD_REQUEST.value(), null, e.getMessage(), req.getRequestURI(), null);
	    }
	}

	@PreAuthorize("isAuthenticated()")
    @GetMapping("/exportSizeexcel")
    public ResponseEntity<InputStreamResource> exportSizesExcel() throws IOException {
        String filename = "EXPORT_MASTER_SIZE.xlsx";

        ByteArrayInputStream data = sizeServiceImpl.exportSizesExcel();
        InputStreamResource file = new InputStreamResource(data); 

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) 
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) 
                .body(file); 
    }

	@PreAuthorize("isAuthenticated()")
    @GetMapping("/layoutSizeexcel")
    public ResponseEntity<InputStreamResource> layoutSizeexcel() throws IOException {
        String filename = "LAYOUT_MASTER_SIZE.xlsx";

        ByteArrayInputStream data = sizeServiceImpl.layoutSizesExcel();
        InputStreamResource file = new InputStreamResource(data); 

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) 
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) 
                .body(file); 
    }

}


