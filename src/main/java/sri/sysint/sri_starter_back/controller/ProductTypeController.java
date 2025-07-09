package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.ProductType;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.ProducTypeServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class ProductTypeController {
	private Response response;	

	@Autowired
	private ProducTypeServiceImpl producTypeServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/getAllProductType")
	public Response getAllProductType(final HttpServletRequest req) throws ResourceNotFoundException {
		List<ProductType> productTypes = new ArrayList<>();
		productTypes = producTypeServiceImpl.getAllProductType();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			productTypes
		);
	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@GetMapping("/getProductTypeById/{id}")
	public Response getProductTypeById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {

		Optional<ProductType> poductType = Optional.of(new ProductType());
		poductType = producTypeServiceImpl.getProductTypeById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			poductType
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/saveProductType")
	public Response saveProductType(final HttpServletRequest req, @RequestBody ProductType productType) throws ResourceNotFoundException {

		ProductType savedProductType = producTypeServiceImpl.saveProductType(productType);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			savedProductType
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/updateProductType")
	public Response updateProductType(final HttpServletRequest req, @RequestBody ProductType productType) throws ResourceNotFoundException {

		ProductType updatedProductType = producTypeServiceImpl.updateProductType(productType);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			updatedProductType
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/deleteProductType")
	public Response deleteProductType(final HttpServletRequest req, @RequestBody ProductType productType) throws ResourceNotFoundException {

		ProductType deletedProductType = producTypeServiceImpl.deleteProductType(productType);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			deletedProductType
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/restoreProductType")
	public Response activateProductType(final HttpServletRequest req, @RequestBody ProductType productType) throws ResourceNotFoundException {

		ProductType activatedProductType = producTypeServiceImpl.activateProductType(productType);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			activatedProductType
		);

	    return response;
	}
	
		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/saveProductTypeExcel")
	@Transactional
	public Response saveProductTypeExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
		if (file.isEmpty()) {
			return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
		}

		try (InputStream inputStream = file.getInputStream()) {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<ProductType> productTypes = new ArrayList<>();
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

					ProductType productType = new ProductType();
					Cell productMerkCell = row.getCell(2);
					Cell productTypeCell = row.getCell(3);
					Cell categoryCell = row.getCell(4);

					if (productMerkCell == null || productMerkCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 3 (Product Merk)");
						continue;
					}

					if (productTypeCell == null || productTypeCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 4 (Product Type)");
						continue;
					}

					if (categoryCell == null || categoryCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 5 (Category)");
						continue;
					}

					productType.setPRODUCT_TYPE_ID(producTypeServiceImpl.getNewId());
					productType.setPRODUCT_MERK(productMerkCell.getStringCellValue());
					productType.setPRODUCT_TYPE(productTypeCell.getStringCellValue());
					productType.setCATEGORY(categoryCell.getStringCellValue());
					productType.setSTATUS(BigDecimal.valueOf(1));
					productType.setCREATION_DATE(new Date());
					productType.setLAST_UPDATE_DATE(new Date());

					productTypes.add(productType);
				}
			}

			if (!errorMessages.isEmpty()) {
				return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
			}

			producTypeServiceImpl.deleteAllProductType();
			for (ProductType productType : productTypes) {
				producTypeServiceImpl.saveProductType(productType);
			}

			return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), productTypes);

		} catch (IOException e) {
			throw new RuntimeException("Error processing file", e);
		}
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @RequestMapping("/exportProductTypesExcel")
    public ResponseEntity<InputStreamResource> exportProductTypesExcel() throws IOException {
        String filename = "EXPORT_MASTER_PRODUCT_TYPE.xlsx";

        ByteArrayInputStream data = producTypeServiceImpl.exportProductTypesExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @RequestMapping("/layoutProductTypesExcel")
    public ResponseEntity<InputStreamResource> layoutProductTypesExcel() throws IOException {
        String filename = "LAYOUT_MASTER_PRODUCT_TYPE.xlsx";

        ByteArrayInputStream data = producTypeServiceImpl.layoutProductTypesExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}
