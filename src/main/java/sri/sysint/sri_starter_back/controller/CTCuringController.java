package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pjfanning.xlsx.StreamingReader;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.CTAssy;
import sri.sysint.sri_starter_back.model.CTCuring;
import sri.sysint.sri_starter_back.model.ItemCuring;
import sri.sysint.sri_starter_back.model.MachineCuring;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.CTCuringServiceImpl;
import sri.sysint.sri_starter_back.repository.ItemCuringRepo;
import sri.sysint.sri_starter_back.repository.CTCuringRepo;
import sri.sysint.sri_starter_back.repository.MachineCuringRepo;

@CrossOrigin(maxAge = 3600)
@RestController
public class CTCuringController {
	
	private Response response;	

	@Autowired
	private CTCuringServiceImpl ctCuringServiceImpl;

	@Autowired
	private CTCuringRepo ctCuringRepo;
	
    @Autowired
    private ItemCuringRepo itemCuringRepo;

    @Autowired
    private MachineCuringRepo machineCuringRepo;

	@PersistenceContext	
	private EntityManager em;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping(value = "/ctcuring/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public StreamingResponseBody streamAll() {
        return outputStream -> {
            try (Stream<CTCuring> stream = ctCuringRepo.streamAll()) {
                ObjectMapper mapper = new ObjectMapper();

                stream.forEach(entity -> {
                    try {
                        String json = mapper.writeValueAsString(entity);
                        // write each JSON object followed by newline → NDJSON
                        outputStream.write(json.getBytes());
                        outputStream.write('\n');
                        outputStream.flush();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

	

	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/getAllCTCuring")
	public Response getAllCTCuring(final HttpServletRequest req) throws ResourceNotFoundException {

		List<CTCuring> ctCurings = new ArrayList<>();
		ctCurings = ctCuringServiceImpl.getAllCTCuring();

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			ctCurings
		);

	    return response;
	}

	@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@GetMapping("/getCTCuringById/{id}")
	public Response getCTCuringById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {

		Optional<CTCuring> ctCuring = Optional.of(new CTCuring());
		ctCuring = ctCuringServiceImpl.getCTCuringById(id);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			ctCuring
		);

	    return response;
	}

	@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/saveCTCuring")
	public Response saveCTCuring(final HttpServletRequest req, @RequestBody CTCuring ctCuring) throws ResourceNotFoundException {

		CTCuring savedCTCuring = ctCuringServiceImpl.saveCTCuring(ctCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			savedCTCuring
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/updateCTCuring")
	public Response updateCTCuring(final HttpServletRequest req, @RequestBody CTCuring ctCuring) throws ResourceNotFoundException {

		CTCuring updatedCTCuring = ctCuringServiceImpl.updateCTCuring(ctCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			updatedCTCuring
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/deleteCTCuring")
	public Response deleteCTCuring(final HttpServletRequest req, @RequestBody CTCuring ctCuring) throws ResourceNotFoundException {

		CTCuring deletedCTCuring = ctCuringServiceImpl.deleteCTCuring(ctCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			deletedCTCuring
		);

	    return response;
	}

		@PreAuthorize("isAuthenticated() && hasRole('PPC')")	
	@PostMapping("/restoreCTCuring")
	public Response activateCTCuring(final HttpServletRequest req, @RequestBody CTCuring ctCuring) throws ResourceNotFoundException {

		CTCuring activatedCTCuring = ctCuringServiceImpl.activateCTCuring(ctCuring);

		response = new Response(
			
			HttpStatus.OK.value(),
			null,
			HttpStatus.OK.getReasonPhrase(),
			req.getRequestURI(),
			activatedCTCuring
		);

	    return response;
	}
	
	@PostMapping("/saveCTCuringExcel")
	public Response saveCTCuringExcelFile(@RequestParam("file") MultipartFile file,
										final HttpServletRequest req) {

		if (file.isEmpty()) {
			return new Response(HttpStatus.BAD_REQUEST.value(), null,
					"No file uploaded", req.getRequestURI(), null);
		}

		try (InputStream inputStream = file.getInputStream();
			Workbook workbook = StreamingReader.builder()
					.rowCacheSize(100)     // hanya cache 100 row di memori
					.bufferSize(4096)      // buffer 4KB
					.open(inputStream)) {  // buka dengan streaming

			Sheet sheet = workbook.getSheetAt(0);

			List<CTCuring> batch = new ArrayList<>();
			List<String> errorMessages = new ArrayList<>();
			int batchSize = 500;

			// hapus data lama sebelum import
			ctCuringServiceImpl.deleteAllCTCuring();

			int rowIndex = 0;
			for (Row row : sheet) {
				if (rowIndex++ == 0) continue; // skip header

				boolean hasError = false;

				// validasi kosong
				for (int col = 0; col <= 34; col++) {
					Cell cell = row.getCell(col);
					if (cell == null || cell.getCellType() == CellType.BLANK) {
						errorMessages.add("Data kosong pada Baris " + (rowIndex) +
								" Kolom " + (col + 1));
						hasError = true;
						if (errorMessages.size() >= 100) {
							return new Response(HttpStatus.BAD_REQUEST.value(), null,
									"Terlalu banyak error, hanya tampilkan 100 pertama: "
											+ String.join("; ", errorMessages),
									req.getRequestURI(), null);
						}
					}
				}
				if (hasError) continue;

				// ambil key
				String wip = getStringFromCell(row.getCell(0));
				String operationShortText = getStringFromCell(row.getCell(5));

				Optional<ItemCuring> wipOpt = itemCuringRepo.findById(wip);
				Optional<MachineCuring> operationShortTextOpt = machineCuringRepo.findById(operationShortText);

				if (wipOpt.isEmpty() || operationShortTextOpt.isEmpty()) {
					errorMessages.add("WIP/Operation tidak ditemukan pada baris " + (rowIndex));
					continue;
				}

				// isi entity
				CTCuring ctCuring = new CTCuring();
				ctCuring.setCT_CURING_ID(ctCuringServiceImpl.getNewId());
				ctCuring.setWIP(wip);

						for (int col = 1; col <= 34; col++) {
							Cell cell = row.getCell(col);
							switch (col) {
								case 1:
									ctCuring.setGROUP_COUNTER(getStringFromCell(cell));
									break;
								case 2:
									ctCuring.setVAR_GROUP_COUNTER(getStringFromCell(cell));
									break;
								case 3:
									ctCuring.setSEQUENCE(getBigDecimalFromCell(cell));
									break;
								case 4:
									ctCuring.setWCT(getStringFromCell(cell));
									break;
								case 5:
									ctCuring.setOPERATION_SHORT_TEXT(getStringFromCell(cell));
									break;
								case 6:
									ctCuring.setOPERATION_UNIT(getStringFromCell(cell));
									break;
								case 7:
									ctCuring.setBASE_QUANTITY(getBigDecimalFromCell(cell));
									break;
								case 8:
									ctCuring.setSTANDART_VALUE_UNIT(getStringFromCell(cell));
									break;
								case 9:
									ctCuring.setCT_SEC1(getBigDecimalFromCell(cell));
									break;
								case 10:
									ctCuring.setCT_HR1000(getBigDecimalFromCell(cell));
									break;
								case 11:
									ctCuring.setWH_NORMAL_SHIFT_0(getBigDecimalFromCell(cell));
									break;
								case 12:
									ctCuring.setWH_NORMAL_SHIFT_1(getBigDecimalFromCell(cell));
									break;
								case 13:
									ctCuring.setWH_NORMAL_SHIFT_2(getBigDecimalFromCell(cell));
									break;
								case 14:
									ctCuring.setWH_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 15:
									ctCuring.setWH_TOTAL_NORMAL_SHIFT(getBigDecimalFromCell(cell));
									break;
								case 16:
									ctCuring.setWH_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 17:
									ctCuring.setALLOW_NORMAL_SHIFT_0(getBigDecimalFromCell(cell));
									break;
								case 18:
									ctCuring.setALLOW_NORMAL_SHIFT_1(getBigDecimalFromCell(cell));
									break;
								case 19:
									ctCuring.setALLOW_NORMAL_SHIFT_2(getBigDecimalFromCell(cell));
									break;
								case 20:
									ctCuring.setALLOW_TOTAL(getBigDecimalFromCell(cell));
									break;
								case 21:
									ctCuring.setOP_TIME_NORMAL_SHIFT_0(getBigDecimalFromCell(cell));
									break;
								case 22:
									ctCuring.setOP_TIME_NORMAL_SHIFT_1(getBigDecimalFromCell(cell));
									break;
								case 23:
									ctCuring.setOP_TIME_NORMAL_SHIFT_2(getBigDecimalFromCell(cell));
									break;
								case 24:
									ctCuring.setOP_TIME_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 25:
									ctCuring.setOP_TIME_NORMAL_SHIFT(getBigDecimalFromCell(cell));
									break;
								case 26:
									ctCuring.setOP_TIME_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 27:
									ctCuring.setKAPS_NORMAL_SHIFT_0(getBigDecimalFromCell(cell));
									break;
								case 28:
									ctCuring.setKAPS_NORMAL_SHIFT_1(getBigDecimalFromCell(cell));
									break;
								case 29:
									ctCuring.setKAPS_NORMAL_SHIFT_2(getBigDecimalFromCell(cell));
									break;
								case 30:
									ctCuring.setKAPS_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 31:
									ctCuring.setKAPS_TOTAL_NORMAL_SHIFT(getBigDecimalFromCell(cell));
									break;
								case 32:
									ctCuring.setKAPS_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(cell));
									break;
								case 33:
									ctCuring.setWAKTU_TOTAL_CT_NORMAL(getBigDecimalFromCell(cell));
									break;
								case 34:
									ctCuring.setWAKTU_TOTAL_CT_FRIDAY(getBigDecimalFromCell(cell));
									break;
							}
						}

				ctCuring.setSTATUS(BigDecimal.valueOf(1));
				ctCuring.setCREATION_DATE(new Date());
				ctCuring.setLAST_UPDATE_DATE(new Date());

				batch.add(ctCuring);

				// simpan batch per 500 row
				if (batch.size() >= batchSize) {
					ctCuringServiceImpl.saveAll(batch);
					batch.clear();
				}
			}

			// simpan sisa
			if (!batch.isEmpty()) {
				ctCuringServiceImpl.saveAll(batch);
			}

			if (!errorMessages.isEmpty()) {
				return new Response(HttpStatus.BAD_REQUEST.value(), null,
						String.join("; ", errorMessages), req.getRequestURI(), null);
			}

			return new Response(HttpStatus.OK.value(), null,
					"File processed and data saved", req.getRequestURI(), null);

		} catch (IOException e) {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
					"Error processing file", req.getRequestURI(), null);
		}
	}

	private boolean isRowEmpty(Row row) {
	    for (int j = 0; j < row.getLastCellNum(); j++) {
	        Cell cell = row.getCell(j);
	        if (cell != null && cell.getCellType() != CellType.BLANK) {
	            return false;
	        }
	    }
	    return true;
	}

	private BigDecimal getBigDecimalFromCell(Cell cell) {
	    if (cell == null) {
	        return null;
	    }
	    switch (cell.getCellType()) {
	        case STRING:
	            String value = cell.getStringCellValue();
	            try {
	                return new BigDecimal(value);
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number format in cell: " + value);
	                return null; 
	            }
	        case NUMERIC:
	            return BigDecimal.valueOf(cell.getNumericCellValue());
	        default:
	            return null; 
	    }
	}

	private String getStringFromCell(Cell cell) {
	    if (cell == null) {
	        return null;
	    }
	    return cell.getStringCellValue();
	}
	
//		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @GetMapping("/exportCTCuringExcel")
    public ResponseEntity<InputStreamResource> exportCTCuringExcel() throws IOException {
        String filename = "MASTER_CT_CURING_DATA.xlsx";

        // Generate the Excel data using the service method
        ByteArrayInputStream data = ctCuringServiceImpl.exportCTCuringsExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
    
//		@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@GetMapping("/layoutCTCuringsExcel")
    public ResponseEntity<InputStreamResource> layoutCTCuringsExcel() throws IOException {
        String filename = "LAYOUT_MASTER_CT_CURING.xlsx";
        ByteArrayInputStream data = ctCuringServiceImpl.layoutCTCuringsExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}
