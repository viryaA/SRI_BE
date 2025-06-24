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
import sri.sysint.sri_starter_back.model.ItemCuring;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.ItemCuringServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class ItemCuringController {
		
	private Response response;	

	@Autowired
	private ItemCuringServiceImpl itemCuringServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getAllItemCuring")
	public Response getAllPlant(final HttpServletRequest req) throws ResourceNotFoundException {
		List<ItemCuring> itemCurings = new ArrayList<>();
		itemCurings = itemCuringServiceImpl.getAllItemCuring();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			itemCurings
		);
		return response;
	}

	@PreAuthorize("isAuthenticated()")	
	@GetMapping("/getItemCuringById/{id}")
	public Response getPlantById(final HttpServletRequest req, @PathVariable String id) throws ResourceNotFoundException {

		Optional<ItemCuring> itemCuring = Optional.of(new ItemCuring());
		itemCuring = itemCuringServiceImpl.getItemCuringById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			itemCuring
		);

		return response;
	}


	@PreAuthorize("isAuthenticated()")
	@PostMapping("/saveItemCuring")
	public Response savePlant(final HttpServletRequest req, @RequestBody ItemCuring itemCuring) throws ResourceNotFoundException {

		ItemCuring savedItemCuring = itemCuringServiceImpl.saveItemCuring(itemCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			savedItemCuring
		);

		return response;
	}

	@PreAuthorize("isAuthenticated()")	
	@PostMapping("/updateItemCuring")
	public Response updatePlant(final HttpServletRequest req, @RequestBody ItemCuring itemCuring) throws ResourceNotFoundException {

		ItemCuring updatedItemCuring = itemCuringServiceImpl.updateItemCuring(itemCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			updatedItemCuring
		);

		return response;
	}

	@PreAuthorize("isAuthenticated()")	
	@PostMapping("/deleteItemCuring")
	public Response deletetePlant(final HttpServletRequest req, @RequestBody ItemCuring itemCuring) throws ResourceNotFoundException {

		ItemCuring deletedItemCuring = itemCuringServiceImpl.deleteItemCuring(itemCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			deletedItemCuring
		);

		return response;
	}

	@PreAuthorize("isAuthenticated()")	
	@PostMapping("/restoreItemCuring")
	public Response restoreItemCuring(final HttpServletRequest req, @RequestBody ItemCuring itemCuring) throws ResourceNotFoundException {

		ItemCuring restoredItemCuring = itemCuringServiceImpl.restoreItemCuring(itemCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			restoredItemCuring
		);

		return response;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/saveItemCuringExcel")
	@Transactional
	public Response saveItemCuringExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {

		if (file.isEmpty()) {
			return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
		}

		try (InputStream inputStream = file.getInputStream()) {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<ItemCuring> itemCurings = new ArrayList<>();
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

					ItemCuring itemCuring = new ItemCuring();
					Cell itemCuringCell = row.getCell(1);
					Cell kapaPerMouldCell = row.getCell(2);
					Cell numberOfMouldCell = row.getCell(3);
					Cell machineTypeCell = row.getCell(4);
					Cell spareMould = row.getCell(5);
					Cell mouldPlan = row.getCell(6);

					if (itemCuringCell == null || itemCuringCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 2 (Item Curing)");
						continue;
					}

					if (kapaPerMouldCell == null || kapaPerMouldCell.getCellType() != CellType.NUMERIC) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Tidak Valid pada Baris " + (i + 1) + " Kolom 3 (Kapa Per Mould)");
						continue;
					}

					if (numberOfMouldCell == null || numberOfMouldCell.getCellType() != CellType.NUMERIC) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Tidak Valid pada Baris " + (i + 1) + " Kolom 4 (Number of Mould)");
						continue;
					}

					if (machineTypeCell == null || machineTypeCell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 5 (Machine Type)");
						continue;
					}

					if (spareMould == null || spareMould.getCellType() != CellType.NUMERIC) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Tidak Valid pada Baris " + (i + 1) + " Kolom 6 (Spare Mould)");
						continue;
					}

					if (mouldPlan == null || mouldPlan.getCellType() != CellType.NUMERIC) {
						errorMessages.add("Data Tidak Valid, Terdapat Data Tidak Valid pada Baris " + (i + 1) + " Kolom 7 (Mould Monthly Plan)");
						continue;
					}

					itemCuring.setITEM_CURING(itemCuringCell.getStringCellValue());
					itemCuring.setKAPA_PER_MOULD(BigDecimal.valueOf(kapaPerMouldCell.getNumericCellValue()));
					itemCuring.setNUMBER_OF_MOULD(BigDecimal.valueOf(numberOfMouldCell.getNumericCellValue()));
					itemCuring.setMACHINE_TYPE(machineTypeCell.getStringCellValue());
					itemCuring.setSPARE_MOULD(BigDecimal.valueOf(spareMould.getNumericCellValue()));
					itemCuring.setMOULD_MONTHLY_PLAN(BigDecimal.valueOf(mouldPlan.getNumericCellValue()));

					itemCuring.setSTATUS(BigDecimal.valueOf(1));
					itemCuring.setCREATION_DATE(new Date());
					itemCuring.setLAST_UPDATE_DATE(new Date());

					itemCurings.add(itemCuring);
				}
			}

			if (!errorMessages.isEmpty()) {
				return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
			}

			itemCuringServiceImpl.deleteAllItemCuring();
			for (ItemCuring itemCuring : itemCurings) {
				itemCuringServiceImpl.saveItemCuring(itemCuring);
			}

			return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), itemCurings);

		} catch (IOException e) {
			throw new RuntimeException("Error processing file", e);
		}
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/exportItemCuringExcel")
	public ResponseEntity<InputStreamResource> exportItemCuringExcel() throws IOException {
		String filename = "EXPORT_MASTER_ITEM_CURING.xlsx";

		ByteArrayInputStream data = itemCuringServiceImpl.exportItemCuringsExcel(); 
		InputStreamResource file = new InputStreamResource(data);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) 
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(file); 
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/layoutItemCuringExcel")
	public ResponseEntity<InputStreamResource> layoutItemCuringExcel() throws IOException {
		String filename = "LAYOUT_MASTER_ITEM_CURING.xlsx";

		ByteArrayInputStream data = itemCuringServiceImpl.layoutItemCuringsExcel(); 
		InputStreamResource file = new InputStreamResource(data);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) 
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(file); 
	}

}


