package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
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
import sri.sysint.sri_starter_back.model.DWorkDay;
import sri.sysint.sri_starter_back.model.DWorkDayHours;
import sri.sysint.sri_starter_back.model.DWorkDayHoursSpesific;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.model.WorkDay; // Import your WorkDay model
import sri.sysint.sri_starter_back.repository.DWorkDayHoursSpecificRepo;
import sri.sysint.sri_starter_back.repository.DWorkDayRepo;
import sri.sysint.sri_starter_back.repository.WorkDayRepo;
import sri.sysint.sri_starter_back.service.WorkDayServiceImpl; // Import your WorkDay service

@CrossOrigin(maxAge = 3600)
@RestController
public class WorkDayController {

    private Response response;

    @Autowired
    private WorkDayServiceImpl workDayServiceImpl;
    
    @Autowired
    private WorkDayRepo workDayRepo;
    
    @Autowired
    private DWorkDayHoursSpecificRepo detailWorkDayRepo;
    
    @Autowired
    private DWorkDayRepo reasonWorkDay;

    @PersistenceContext
    private EntityManager em;

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @GetMapping("/getAllWorkDays")
    public Response getAllWorkDays(final HttpServletRequest req) throws ResourceNotFoundException {

        List<WorkDay> workDays = workDayServiceImpl.getAllWorkDays();

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            workDays
        );

        return response;
    }
    
    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/getAllWorkDaysByDateRange")
    public Response getAllWorkDaysByDateRange(final HttpServletRequest req, 
                                            @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {
        try {

            String startDateStr = requestBody.get("startDate");
            String endDateStr = requestBody.get("endDate");

            if (startDateStr == null || endDateStr == null) {
                throw new ResourceNotFoundException("Missing required fields: 'startDate' and 'endDate'");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedStartDate;
            Date parsedEndDate;
            try {
                parsedStartDate = dateFormat.parse(startDateStr);
                parsedEndDate = dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Invalid date format, expected format is dd-MM-yyyy");
            }

            List<WorkDay> workDays = workDayServiceImpl.getAllWorkDaysByDateRange(parsedStartDate, parsedEndDate);

            return new Response(
                
                HttpStatus.OK.value(),
                null,
                HttpStatus.OK.getReasonPhrase(),
                req.getRequestURI(),
                workDays
            );

        } catch (Exception e) {
            throw new ResourceNotFoundException("Error processing request: " + e.getMessage());
        }
    }



    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/getWorkDayByDate")
    public Response getWorkDayByDate(final HttpServletRequest req, @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {

        String date = requestBody.get("date"); // Extract date from body

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate;
		try {
			parsedDate = dateFormat.parse(date);
			
			Optional<WorkDay> workDay = workDayServiceImpl.getWorkDayByDate(parsedDate);
			
			return new Response(
					
					HttpStatus.OK.value(),
					null,
					HttpStatus.OK.getReasonPhrase(),
					req.getRequestURI(),
					workDay
					);
			
		} catch (ParseException e) {
			e.printStackTrace();
			return new Response(
					
					HttpStatus.CONFLICT.value(),
					null,
					HttpStatus.CONFLICT.getReasonPhrase(),
					req.getRequestURI(),
					null
					);
		}

    }


    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/saveWorkDay")
    public Response saveWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {

        WorkDay savedWorkDay = workDayServiceImpl.saveWorkDay(workDay);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            savedWorkDay
        );

        return response;
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/updateWorkDay")
    public Response updateWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {

        WorkDay updatedWorkDay;
		try {
			updatedWorkDay = workDayServiceImpl.updateWorkDay(workDay);
	        response = new Response(
	                
	                HttpStatus.OK.value(),
	                null,
	                HttpStatus.OK.getReasonPhrase(),
	                req.getRequestURI(),
	                updatedWorkDay
	            );
		} catch (Exception e) {
			
	        response = new Response(
	                
	                HttpStatus.CONFLICT.value(),
	                null,
	                HttpStatus.CONFLICT.getReasonPhrase(),
	                req.getRequestURI(),
	                null
            );
		}


        return response;
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/deleteWorkDay")
    public Response deleteWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {

        WorkDay deletedWorkDay = workDayServiceImpl.deleteWorkDay(workDay);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            deletedWorkDay
        );

        return response;
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/restoreWorkDay")
    public Response restoreWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {

        WorkDay restoredWorkDay = workDayServiceImpl.restoreWorkDay(workDay);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            restoredWorkDay
        );

        return response;
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/saveWorkDaysExcel")
    public Response saveWorkDaysExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {

        if (file.isEmpty()) {
            return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
        }

        workDayServiceImpl.deleteAllWorkDays();

        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<WorkDay> workDays = new ArrayList<>();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Cell dateCell = row.getCell(0);
                if (dateCell == null || (dateCell.getCellType() != CellType.STRING && dateCell.getCellType() != CellType.NUMERIC)) {
                    return new Response( HttpStatus.BAD_REQUEST.value(), null, "Invalid date format in file", req.getRequestURI(), null);
                }

                ZonedDateTime zonedDateTime = null;

                try {
                    if (dateCell.getCellType() == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(dateCell)) {
                            LocalDate localDate = dateCell.getDateCellValue().toInstant()
                                    .atZone(ZoneId.systemDefault()) 
                                    .toLocalDate();
                            zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
                        } else {
                            return new Response( HttpStatus.BAD_REQUEST.value(), null, "Invalid date format in file: " + dateCell.toString(), req.getRequestURI(), null);
                        }
                    } else if (dateCell.getCellType() == CellType.STRING) {
                        LocalDate parsedDate = LocalDate.parse(dateCell.getStringCellValue().trim(), dateFormatter);
                        zonedDateTime = parsedDate.atStartOfDay(ZoneId.of("UTC"));
                    }
                } catch (Exception e) {
                    return new Response( HttpStatus.BAD_REQUEST.value(), null, "Error parsing date: " + dateCell.toString(), req.getRequestURI(), null);
                }

                Date date = Date.from(zonedDateTime.toInstant());

                WorkDay workDay = new WorkDay();
                workDay.setDATE_WD(date);

                for (int j = 1; j <= 10; j++) {
                    Cell cell = row.getCell(j);
                    String shiftName = sheet.getRow(0).getCell(j).getStringCellValue();
                    int status = 0;

                    if (cell != null) {
                        if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue().trim();
                            if ("v".equalsIgnoreCase(cellValue)) {
                                status = 1;
                            }
                        } else if (cell.getCellType() == CellType.BLANK) {
                            status = 0;
                        }
                    }

                    switch (shiftName.toUpperCase()) {
                        case "WD_SHIFT_1":
                            workDay.setIWD_SHIFT_1(BigDecimal.valueOf(status));
                            break;
                        case "WD_SHIFT_2":
                            workDay.setIWD_SHIFT_2(BigDecimal.valueOf(status));
                            break;
                        case "WD_SHIFT_3":
                            workDay.setIWD_SHIFT_3(BigDecimal.valueOf(status));
                            break;
                        case "OT_TL_1":
                            workDay.setIOT_TL_1(BigDecimal.valueOf(status));
                            break;
                        case "OT_TL_2":
                            workDay.setIOT_TL_2(BigDecimal.valueOf(status));
                            break;
                        case "OT_TL_3":
                            workDay.setIOT_TL_3(BigDecimal.valueOf(status));
                            break;
                        case "OT_TT_1":
                            workDay.setIOT_TT_1(BigDecimal.valueOf(status));
                            break;
                        case "OT_TT_2":
                            workDay.setIOT_TT_2(BigDecimal.valueOf(status));
                            break;
                        case "OT_TT_3":
                            workDay.setIOT_TT_3(BigDecimal.valueOf(status));
                            break;
                        case "OFF":
                            workDay.setOFF(BigDecimal.valueOf(status));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown shift name: " + shiftName);
                    }
                    workDay.setSEMI_OFF(BigDecimal.ZERO);
                }

                workDayServiceImpl.saveWorkDay(workDay);
                workDays.add(workDay);
            }

            return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), workDays);

        } catch (IOException e) {
            return new Response( HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
        }
    }

  
    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; 
            }
        }
        return true; 
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")    
    @PostMapping("/turnOnOvertime")
    public Response turnOnOvertime(final HttpServletRequest req, @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {

                String dateWd = requestBody.get("dateWd");

                WorkDay updatedWorkDay;
				try {
					updatedWorkDay = workDayServiceImpl.turnOnOvertime(dateWd);
					return new Response(
							
							HttpStatus.OK.value(),
							null,
							HttpStatus.OK.getReasonPhrase(),
							req.getRequestURI(),
							updatedWorkDay
							);
				} catch (Exception e) {
//					e.printStackTrace();
					return new Response(
							
							HttpStatus.CONFLICT.value(),
							null,
							HttpStatus.CONFLICT.getReasonPhrase(),
							req.getRequestURI(),
							null
							);
				}

    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/turnOnShift/{dateWd}/{shift}")
    public Response turnOnShift(final HttpServletRequest req, @PathVariable String dateWd, @PathVariable String shift) throws ResourceNotFoundException {

        WorkDay updatedWorkDay;
		try {
			updatedWorkDay = workDayServiceImpl.turnOnShift(dateWd, shift);
			response = new Response(
					
					HttpStatus.OK.value(),
					null,
					HttpStatus.OK.getReasonPhrase(),
					req.getRequestURI(),
					updatedWorkDay
					);
		} catch (Exception e) {
//			e.printStackTrace();
			response = new Response(
					
					HttpStatus.CONFLICT.value(),
					null,
					HttpStatus.CONFLICT.getReasonPhrase(),
					req.getRequestURI(),
					null
					);

		}


        return response;
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @PostMapping("/turnOffShift/{dateWd}/{shift}")
    public Response turnOffShift(final HttpServletRequest req, @PathVariable String dateWd, @PathVariable String shift) throws ResourceNotFoundException {

        WorkDay updatedWorkDay;
		try {
			updatedWorkDay = workDayServiceImpl.turnOffShift(dateWd, shift);
			response = new Response(
					
					HttpStatus.OK.value(),
					null,
					HttpStatus.OK.getReasonPhrase(),
					req.getRequestURI(),
					updatedWorkDay
					);
		} catch (Exception e) {
			e.printStackTrace();
	        response = new Response(
	                
	                HttpStatus.CONFLICT.value(),
	                null,
	                HttpStatus.CONFLICT.getReasonPhrase(),
	                req.getRequestURI(),
	                null
	            );
		}


        return response;
    }
    
    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
    @GetMapping("/exportWorkDaysExcel/{year}/{month}")
    public ResponseEntity<byte[]> exportTemplateExcel(
            @PathVariable("month") int month,
            @PathVariable("year") int year) throws IOException {

        ByteArrayInputStream in = workDayServiceImpl.exportTemplateExcel(
                BigDecimal.valueOf(month), BigDecimal.valueOf(year));

        byte[] bytes = in.readAllBytes();

        String fileName = "WorkDayTemplate_" + year + "_" + month + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
    
    private Object getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    	@PreAuthorize("isAuthenticated() && hasRole('PPC')")
	@PostMapping("/importWDExcel")
	public Response importWorkDaysExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
	    if (file.isEmpty()) {
	        return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
	    }
	
	    try (InputStream inputStream = file.getInputStream()) {
            ZipSecureFile.setMinInflateRatio(0.001);
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        XSSFSheet sheet = workbook.getSheetAt(0);
	
	        // Define expected vertical headers in column A
	        String[] expectedRows = {
    		    "DATE_WD",
    		    "S1 OFF NORMAL", 
                "S1 START NORMAL", "S1 END NORMAL", "S1 REASON NORMAL",
    		    "S1 OFF TT",
    		    "S1 START OT_TT", "S1 END OT_TT", "S1 REASON OT_TT",
    		    "S1 OFF TL",
    		    "S1 START OT_TL", "S1 END OT_TL", "S1 REASON OT_TL",
    		    "S2 OFF NORMAL",
    		    "S2 START NORMAL", "S2 END NORMAL", "S2 REASON NORMAL",
    		    "S2 OFF TT", 
    		    "S2 START OT_TT", "S2 END OT_TT", "S2 REASON OT_TT",
    		    "S2 OFF TL",
    		    "S2 START OT_TL", "S2 END OT_TL", "S2 REASON OT_TL",
    		    "S3 OFF NORMAL",
    		    "S3 START NORMAL", "S3 END NORMAL", "S3 REASON NORMAL",
    		    "S3 OFF TT",
    		    "S3 START OT_TT", "S3 END OT_TT", "S3 REASON OT_TT",
    		    "S3 OFF TL",
    		    "S3 START OT_TL", "S3 END OT_TL", "S3 REASON OT_TL",

    		};
            List<String> errors = new ArrayList<>();
            List<String> successes = new ArrayList<>();

	        // Check if vertical headers in column A match
	        for (int i = 0; i < expectedRows.length; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null || row.getCell(0) == null) {
	                return new Response( HttpStatus.BAD_REQUEST.value(), null, "Missing or incomplete header in column A at row " + (i + 1), req.getRequestURI(), null);
	            }
	            String cellValue = row.getCell(0).getStringCellValue().trim();
	            if (!cellValue.equals(expectedRows[i])) {
	                return new Response( HttpStatus.BAD_REQUEST.value(), null, "Invalid header: expected '" + expectedRows[i] + "' but found '" + cellValue + "' at row " + (i + 1), req.getRequestURI(), null);
	            }
	        }
	
	        // Check if cells in column B contain valid dates
	        Row rowh = sheet.getRow(0); // Only check the first row
	        int lastColumn = rowh.getLastCellNum(); // Total columns used in this row
	        if (rowh != null) {
                int real = 0;
	            for (int col = 1; col < lastColumn; col++) {
	                Cell cell = rowh.getCell(col);
	                System.out.print("Column " + (col + 1) + ": ");
	
	                if (cell == null) {
	                    System.out.println("Cell is null");
	                    return new Response(
	                        
	                        HttpStatus.BAD_REQUEST.value(),
	                        null,
	                        "Missing cell at column " + (col + 1) + " in row 1",
	                        req.getRequestURI(),
	                        null
	                    );
	                }
	
	                if (cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
	                    System.out.println("Invalid date");
                        break;
	                }
	
                    real++;
	                System.out.println("Date: " + cell.getDateCellValue() + real);
	            }
                lastColumn = real + 1;
	        }
            
	        List<Map<String, Object>> resultTTList = new ArrayList<>();
	        List<Map<String, Object>> resultTLList = new ArrayList<>();
	        List<Map<String, Object>> resultNORMALList = new ArrayList<>();

	        for (int col = 1; col < lastColumn; col++) {
	            Map<String, Object> ttData = new HashMap<>();
	            Map<String, Object> tlData = new HashMap<>();
	            Map<String, Object> normalData = new HashMap<>();

	            // DATE_WD
	            Cell dateCell = sheet.getRow(0).getCell(col);
	            Date workDate = dateCell.getDateCellValue();
	            ttData.put("DATE_WD", workDate);
	            tlData.put("DATE_WD", workDate);
	            normalData.put("DATE_WD", workDate);

	            // SHIFTS: S1, S2, S3
	            for (int shift = 1; shift <= 3; shift++) {
	            	int baseRow = (shift - 1) * 12 + 1;
	            	
	                Map<String, Object> shiftNormal = new HashMap<>();
	                shiftNormal.put("OFF", getCellValue(sheet.getRow(baseRow ).getCell(col)));
	                shiftNormal.put("START", getCellValue(sheet.getRow(baseRow + 1).getCell(col)));
	                shiftNormal.put("END", getCellValue(sheet.getRow(baseRow + 2).getCell(col)));
	                shiftNormal.put("REASON", getCellValue(sheet.getRow(baseRow + 3).getCell(col)));
	                normalData.put("S" + shift + "_NORMAL", shiftNormal);

	                // TT Data
	                Map<String, Object> shiftTT = new HashMap<>();
	                shiftTT.put("OFF", getCellValue(sheet.getRow(baseRow + 4).getCell(col)));
	                shiftTT.put("START", getCellValue(sheet.getRow(baseRow + 5).getCell(col)));
	                shiftTT.put("END", getCellValue(sheet.getRow(baseRow + 6).getCell(col)));
	                shiftTT.put("REASON", getCellValue(sheet.getRow(baseRow + 7).getCell(col)));
	                ttData.put("S" + shift + "_TT", shiftTT);

	                // TL Data
	                Map<String, Object> shiftTL = new HashMap<>();
	                shiftTL.put("OFF", getCellValue(sheet.getRow(baseRow + 8).getCell(col)));
	                shiftTL.put("START", getCellValue(sheet.getRow(baseRow + 9).getCell(col)));
	                shiftTL.put("END", getCellValue(sheet.getRow(baseRow + 10).getCell(col)));
	                shiftTL.put("REASON", getCellValue(sheet.getRow(baseRow + 11).getCell(col)));
	                tlData.put("S" + shift + "_TL", shiftTL);
	            }

	            resultTTList.add(ttData);
	            resultTLList.add(tlData);
	            resultNORMALList.add(normalData);
	        }
	        
	        for (int i = 0; i < resultTTList.size(); i++) {
                boolean canUpdateDetail = false;
                boolean isInsert = false;
	            Map<String, Object> ttData = resultTTList.get(i);
	            Map<String, Object> tlData = resultTLList.get(i);
	            Map<String, Object> normalData = resultNORMALList.get(i);

	            Map<String, Object> s1NORMAL = (Map<String, Object>) normalData.get("S1_NORMAL");
	            Map<String, Object> s1TT = (Map<String, Object>) ttData.get("S1_TT");
	            Map<String, Object> s1TL = (Map<String, Object>) tlData.get("S1_TL");
                Map<String, Object> s2NORMAL = (Map<String, Object>) normalData.get("S2_NORMAL");
                Map<String, Object> s2TT = (Map<String, Object>) ttData.get("S2_TT");
	            Map<String, Object> s2TL = (Map<String, Object>) tlData.get("S2_TL");
                Map<String, Object> s3NORMAL = (Map<String, Object>) normalData.get("S3_NORMAL");
                Map<String, Object> s3TT = (Map<String, Object>) ttData.get("S3_TT");
	            Map<String, Object> s3TL = (Map<String, Object>) tlData.get("S3_TL");

		        Date wdIsDate = (Date) normalData.get("DATE_WD");

			     // Example: querying workDayRepo using the date
		        Optional<WorkDay> workDayOpt = workDayRepo.findById(wdIsDate);
                WorkDay updateWorkDay = null;
                if (workDayOpt.isPresent()) {
                    canUpdateDetail = true;
                    System.out.println("Data ada");
                    System.out.println(workDayOpt.get().getDATE_WD());
                    updateWorkDay = workDayOpt.get();
                    System.out.println("data lama udah masuk");

                }else{
                    System.out.println("Data ga ada, buat baru");
                    updateWorkDay = new WorkDay();
                    
                    updateWorkDay.setDATE_WD(wdIsDate);
                    updateWorkDay.setSTATUS(BigDecimal.ONE);
                    updateWorkDay.setCREATION_DATE(new Date());
                }
                
                Date date = (Date) ttData.get("DATE_WD");
                LocalDate dayWeek = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String offValue = s1NORMAL.get("OFF") != null ? s1NORMAL.get("OFF").toString() : "";
                String reasonValue = s1NORMAL.get("REASON") != null ? s1NORMAL.get("REASON").toString() : "";

                System.out.println("🔍 Parsing SHIFT 1 | OFF: \"" + offValue + "\" | REASON: \"" + reasonValue + "\" | Date: " + wdIsDate);

                // Set NORMAL shifts
                updateWorkDay.setIWD_SHIFT_1(yesNoToBigDecimal(
                    getStringFromMap(s1NORMAL, "OFF"),
                    getStringFromMap(s1NORMAL, "REASON"),
                    "SHIFT 1", wdIsDate
                ));

                updateWorkDay.setIWD_SHIFT_2(yesNoToBigDecimal(
                    getStringFromMap(s2NORMAL, "OFF"),
                    getStringFromMap(s2NORMAL, "REASON"),
                    "SHIFT 2", wdIsDate
                ));

                updateWorkDay.setIWD_SHIFT_3(yesNoToBigDecimal(
                    getStringFromMap(s3NORMAL, "OFF"),
                    getStringFromMap(s3NORMAL, "REASON"),
                    "SHIFT 3", wdIsDate
                ));

                // Set TL shifts
                updateWorkDay.setIOT_TL_1(yesNoToBigDecimal(
                    getStringFromMap(s1TL, "OFF"),
                    getStringFromMap(s1TL, "REASON"),
                    "OT TL SHIFT 1", wdIsDate
                ));

                updateWorkDay.setIOT_TL_2(yesNoToBigDecimal(
                    getStringFromMap(s2TL, "OFF"),
                    getStringFromMap(s2TL, "REASON"),
                    "OT TL SHIFT 2", wdIsDate
                ));

                updateWorkDay.setIOT_TL_3(yesNoToBigDecimal(
                    getStringFromMap(s3TL, "OFF"),
                    getStringFromMap(s3TL, "REASON"),
                    "OT TL SHIFT 3", wdIsDate
                ));

                // Set TT shifts
                updateWorkDay.setIOT_TT_1(yesNoToBigDecimal(
                    getStringFromMap(s1TT, "OFF"),
                    getStringFromMap(s1TT, "REASON"),
                    "OT TT SHIFT 1", wdIsDate
                ));

                updateWorkDay.setIOT_TT_2(yesNoToBigDecimal(
                    getStringFromMap(s2TT, "OFF"),
                    getStringFromMap(s2TT, "REASON"),
                    "OT TT SHIFT 2", wdIsDate
                ));

                updateWorkDay.setIOT_TT_3(yesNoToBigDecimal(
                    getStringFromMap(s3TT, "OFF"),
                    getStringFromMap(s3TT, "REASON"),
                    "OT TT SHIFT 3", wdIsDate
                ));
                System.out.println("data lama udah ganti baru");

                if(dayWeek.getDayOfWeek() == DayOfWeek.SATURDAY || dayWeek.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("Weekend detected: " + dayWeek.getDayOfWeek());
                    updateWorkDay.setOFF(BigDecimal.ONE);
                    updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                } else if(i < resultTTList.size() -1) {
                    System.out.println("Using list index for next day's shift3 (i+1): " + (i + 1)+" "+ resultTTList.size());

                    Map<String, Object> normalDatannext = resultNORMALList.get(i+1);
                    Map<String, Object> s3NORMALNext = (Map<String, Object>) normalDatannext.get("S3_NORMAL");

                    BigDecimal shift1 = updateWorkDay.getIWD_SHIFT_1();
                    BigDecimal shift2 = updateWorkDay.getIWD_SHIFT_2();
                    BigDecimal shift3Next = yesNoToBigDecimal(s3NORMALNext.get("OFF").toString(), null,null,null);

                    System.out.println("Shift1 (today): " + shift1);
                    System.out.println("Shift2 (today): " + shift2);
                    System.out.println("Shift3 (next): " + shift3Next);

                    boolean s1 = shift1.compareTo(BigDecimal.ONE) == 0;
                    boolean s2 = shift2.compareTo(BigDecimal.ONE) == 0;
                    boolean s3 = shift3Next.compareTo(BigDecimal.ONE) == 0;

                    System.out.println("Shift flags -> S1: " + s1 + ", S2: " + s2 + ", S3: " + s3);

                    if (s1 && s2 && s3) {
                        System.out.println("All shifts are ON -> OFF: 0, SEMI_OFF: 0");
                        updateWorkDay.setOFF(BigDecimal.ZERO);
                        updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                    } else if (!s1 && !s2 && !s3) {
                        System.out.println("All shifts are OFF -> OFF: 1, SEMI_OFF: 0");
                        updateWorkDay.setOFF(BigDecimal.ONE);
                        updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                    } else {
                        System.out.println("Mixed shift states -> OFF: 0, SEMI_OFF: 1");
                        updateWorkDay.setOFF(BigDecimal.ZERO);
                        updateWorkDay.setSEMI_OFF(BigDecimal.ONE);
                    }
                } else {
                    LocalDate nextDateLocal = dayWeek.plusDays(1);
                    Date nextDate = Date.from(nextDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    System.out.println("No more list data. Querying DB for next date: " + nextDate);

                    Optional<WorkDay> workDayNextDay = workDayRepo.findById(nextDate);
                    if(workDayNextDay.isPresent()) {
                        WorkDay nextDay = workDayNextDay.get();

                        BigDecimal shift1 = updateWorkDay.getIWD_SHIFT_1();
                        BigDecimal shift2 = updateWorkDay.getIWD_SHIFT_2();
                        BigDecimal shift3Next = nextDay.getIWD_SHIFT_3();

                        System.out.println("Shift1 (today): " + shift1);
                        System.out.println("Shift2 (today): " + shift2);
                        System.out.println("Shift3 (next from DB): " + shift3Next);

                        boolean s1 = shift1.compareTo(BigDecimal.ONE) == 0;
                        boolean s2 = shift2.compareTo(BigDecimal.ONE) == 0;
                        boolean s3 = shift3Next.compareTo(BigDecimal.ONE) == 0;

                        System.out.println("Shift flags -> S1: " + s1 + ", S2: " + s2 + ", S3: " + s3);

                        if (s1 && s2 && s3) {
                            System.out.println("All shifts are ON -> OFF: 0, SEMI_OFF: 0");
                            updateWorkDay.setOFF(BigDecimal.ZERO);
                            updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                        } else if (!s1 && !s2 && !s3) {
                            System.out.println("All shifts are OFF -> OFF: 1, SEMI_OFF: 0");
                            updateWorkDay.setOFF(BigDecimal.ONE);
                            updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                        } else {
                            System.out.println("Mixed shift states -> OFF: 0, SEMI_OFF: 1");
                            updateWorkDay.setOFF(BigDecimal.ZERO);
                            updateWorkDay.setSEMI_OFF(BigDecimal.ONE);
                        }
                    } else {
                        System.out.println("Next day's data not found in DB, defaulting to OFF: 0, SEMI_OFF: 0");
                        updateWorkDay.setOFF(BigDecimal.ZERO);
                        updateWorkDay.setSEMI_OFF(BigDecimal.ZERO);
                    }
                }


                DWorkDayHoursSpesific updateTT = null;
                DWorkDayHoursSpesific updateTL = null;
                DWorkDayHoursSpesific updateNORMAL = null;
		        Optional<List<DWorkDayHoursSpesific>> detailWDData = detailWorkDayRepo.findByDATEWD(wdIsDate);
		        if (!detailWDData.isEmpty()) {
		            System.out.println("Data Detail ada");
		            for (DWorkDayHoursSpesific item : detailWDData.get()) {
		            	if(item.getDESCRIPTION().equals("OT_TT")) {
                            updateTT = item;	            		
		            	}else if (item.getDESCRIPTION().equals("OT_TL")){
                            updateTL = item;
                        }else if (item.getDESCRIPTION().equals("WD_NORMAL")){
                            updateNORMAL = item;
                        }
		            }
		        } else {
                    System.out.println("Data Detail tidak ada, membuat baru");
                    
                    long currentCount = detailWorkDayRepo.count();

                    BigDecimal baseIdNormal = BigDecimal.valueOf(currentCount + 1);
                    BigDecimal baseIdTT = BigDecimal.valueOf(currentCount + 2);
                    BigDecimal baseIdTL = BigDecimal.valueOf(currentCount + 3);

                    updateTT = new DWorkDayHoursSpesific();
                    updateTT.setDETAIL_WD_HOURS_SPECIFIC_ID(baseIdTT);
                    updateTT.setDESCRIPTION("OT_TT");
                    updateTT.setDATE_WD(wdIsDate);
                    updateTT.setSTATUS(BigDecimal.ONE);
                    updateTT.setCREATION_DATE(new Date());
                    // set other fields if needed

                    updateTL = new DWorkDayHoursSpesific();
                    updateTL.setDETAIL_WD_HOURS_SPECIFIC_ID(baseIdTL);
                    updateTL.setDESCRIPTION("OT_TL");
                    updateTL.setDATE_WD(wdIsDate);
                    updateTL.setSTATUS(BigDecimal.ONE);
                    updateTL.setCREATION_DATE(new Date());
                    // set other fields if needed

                    updateNORMAL = new DWorkDayHoursSpesific();
                    updateNORMAL.setDETAIL_WD_HOURS_SPECIFIC_ID(baseIdNormal);
                    updateNORMAL.setDESCRIPTION("WD_NORMAL");
                    updateNORMAL.setDATE_WD(wdIsDate);
                    updateNORMAL.setSTATUS(BigDecimal.ONE);
                    updateNORMAL.setCREATION_DATE(new Date());                    
                    // set other fields if needed
                    
                    canUpdateDetail = true;

                }

                if (updateTT != null && updateTL != null && updateNORMAL != null && canUpdateDetail) {
                    System.out.println("Siap update");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = dateFormat.format(wdIsDate);
                    boolean isFriday = dayWeek.getDayOfWeek() == DayOfWeek.FRIDAY;

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    // === NORMAL Updates ===
                    if (updateShift(updateNORMAL, "NORMAL", s1NORMAL, 1, formatter,isFriday)) {
                        errors.add("Shift 1 NORMAL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 1 NORMAL updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateNORMAL, "NORMAL", s2NORMAL, 2, formatter, isFriday)) {
                        errors.add("Shift 2 NORMAL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 2 NORMAL updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateNORMAL, "NORMAL", s3NORMAL, 3, formatter, isFriday)) {
                        errors.add("Shift 3 NORMAL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 3 NORMAL updated successfully on date: " + dateStr);
                    }

                    // === TT Updates ===
                    if (updateShift(updateTT, "TT", s1TT, 1, formatter, isFriday)) {
                        errors.add("Shift 1 TT has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 1 TT updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateTT, "TT", s2TT, 2, formatter, isFriday)) {
                        errors.add("Shift 2 TT has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 2 TT updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateTT, "TT", s3TT, 3, formatter, isFriday)) {
                        errors.add("Shift 3 TT has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 3 TT updated successfully on date: " + dateStr);
                    }

                    // === TL Updates ===
                    if (updateShift(updateTL, "TL", s1TL, 1, formatter, isFriday)) {
                        errors.add("Shift 1 TL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 1 TL updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateTL, "TL", s2TL, 2, formatter, isFriday)) {
                        errors.add("Shift 2 TL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 2 TL updated successfully on date: " + dateStr);
                    }

                    if (updateShift(updateTL, "TL", s3TL, 3, formatter, isFriday)) {
                        errors.add("Shift 3 TL has an error on date: " + dateStr);
                    } else {
                        successes.add("Shift 3 TL updated successfully on date: " + dateStr);
                    }

                }
                if(canUpdateDetail){
                    updateTT.setLAST_UPDATE_DATE(new Date());
                    updateTL.setLAST_UPDATE_DATE(new Date());
                    updateNORMAL.setLAST_UPDATE_DATE(new Date());

                    workDayRepo.save(updateWorkDay);
                    detailWorkDayRepo.save(updateNORMAL);
                    System.out.println("UPDATEUPDATEUPDATEUPDATEUPDATE"+updateWorkDay.getDATE_WD()+updateTT.getSHIFT1_END_TIME()+" ID"+updateTT.getDETAIL_WD_HOURS_SPECIFIC_ID());
                    detailWorkDayRepo.save(updateTT);
                    System.out.println("ddd");
                    detailWorkDayRepo.save(updateTL);
                    System.out.println("f");
                }

	        }
	        
	        if(errors.isEmpty()) {
	        	return new Response( HttpStatus.OK.value(), null, "File processed successfully", req.getRequestURI(),null);
	        }
	        
	        String errorSummary;
	        if (errors.size() > 3) {
	            errorSummary = String.join(", ", errors.subList(0, 3)) + ", and more";
	        } else {
	            errorSummary = String.join(", ", errors);
	        }

	        return new Response( HttpStatus.OK.value(), errorSummary, "File processed successfully With error", req.getRequestURI(),errors);

	    } catch (IOException e) {
	        return new Response( HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage(), req.getRequestURI(), null);
	    }
	}

    private String extractHourMinute(Object timeValue) {
        if (timeValue == null) return null;

        if (timeValue instanceof String) {
            String str = (String) timeValue;
            try {
                // Try to parse full datetime
                SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date date = fullFormat.parse(str);
                SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
                return hourMinuteFormat.format(date);
            } catch (ParseException e) {
                // Try parsing HH:mm format
                if (str.matches("\\d{2}:\\d{2}")) {
                    return str;
                } else {
                    System.out.println("Invalid time format: " + str);
                    return "00:00";
                }
            }
        } else if (timeValue instanceof Date) {
            SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
            return hourMinuteFormat.format((Date) timeValue);
        }

        return null;
    }

    private BigDecimal calculateDurationInMinutes(String start, String end, DateTimeFormatter formatter, boolean isFriday,int shift) {
        try {
            LocalTime startTime = LocalTime.parse(start, formatter);
            LocalTime endTime = LocalTime.parse(end, formatter);

            // Assign an arbitrary base date
            LocalDate baseDate = LocalDate.of(2000, 1, 1);
            LocalDateTime startDateTime = LocalDateTime.of(baseDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(baseDate, endTime);

            // Handle overnight shift by adding 1 day to endDateTime
            if (endTime.isBefore(startTime)) {
                endDateTime = endDateTime.plusDays(1);
            }

            // Calculate total duration
            long minutes = Duration.between(startDateTime, endDateTime).toMinutes();

            // Define break time (you can customize per Friday if needed)
            LocalTime breakStart = LocalTime.of(12, 40);
            LocalTime breakEnd = LocalTime.of(13, 40);

            LocalDateTime breakStartDateTime = LocalDateTime.of(baseDate, breakStart);
            LocalDateTime breakEndDateTime = LocalDateTime.of(baseDate, breakEnd);

            // If break is before start time in an overnight shift, adjust to the next day
            if (endTime.isBefore(startTime) && breakStart.isBefore(startTime)) {
                breakStartDateTime = breakStartDateTime.plusDays(1);
                breakEndDateTime = breakEndDateTime.plusDays(1);
            }

            // Calculate overlap with break
            long breakMinutes = 0;
            if(isFriday && shift == 1){
            	breakMinutes =  calculateOverlapMinutes(startDateTime, endDateTime, breakStartDateTime, breakEndDateTime);
            	System.out.println(String.format(
            		    "Friday detected with shift = %d. Break overlap duration = %d minutes. Start = %s, End = %s, Total duration = %d, Final duration = %d",
            		    shift,
            		    breakMinutes,
            		    start,
            		    end,
            		    minutes,
            		    minutes - breakMinutes
            		));

            }

            return BigDecimal.valueOf(minutes - breakMinutes);
        } catch (DateTimeParseException e) {
            System.err.println("Time parse error: " + e.getMessage());
            return null;
        }
    }

    // Helper to calculate overlap between work period and break
    private long calculateOverlapMinutes(LocalDateTime start, LocalDateTime end,
        LocalDateTime breakStart, LocalDateTime breakEnd) {
		LocalDateTime latestStart = start.isAfter(breakStart) ? start : breakStart;
		LocalDateTime earliestEnd = end.isBefore(breakEnd) ? end : breakEnd;
		
		if (earliestEnd.isAfter(latestStart)) {
			return Duration.between(latestStart, earliestEnd).toMinutes();
		}
		return 0;
	}
	


    // Utility methods
    private LocalTime max(LocalTime t1, LocalTime t2) {
        return t1.isAfter(t2) ? t1 : t2;
    }

    private LocalTime min(LocalTime t1, LocalTime t2) {
        return t1.isBefore(t2) ? t1 : t2;
    }


    private boolean updateShift(DWorkDayHoursSpesific updateObj, String label, Map<String, Object> shiftMap, int shiftNumber, DateTimeFormatter formatter, boolean isFriday) {
        boolean hasError = false;

        // Default time and allowed range per shift
        Map<Integer, String[]> defaultShifts = Map.of(
            1, new String[]{"07:10", "15:50"},
            2, new String[]{"15:50", "23:30"},
            3, new String[]{"23:30", "07:10"} // crosses midnight
        );

        String[] defaultTimes = defaultShifts.get(shiftNumber);
        String defaultStart = defaultTimes[0];
        String defaultEnd = defaultTimes[1];

        String rawStart = extractHourMinute(shiftMap.get("START"));
        String rawEnd = extractHourMinute(shiftMap.get("END"));

        System.out.printf("%s Shift %d RAW: start = %s, end = %s%n", label, shiftNumber, rawStart, rawEnd);

        boolean validStart = isValidTime(shiftMap.get("START").toString(), formatter);
        boolean validEnd = isValidTime(shiftMap.get("END").toString(), formatter);

        if (!validStart) {
            System.out.printf("%s Shift %d WARNING: start '%s' is invalid, using default '%s'%n", label, shiftNumber, rawStart, defaultStart);
            hasError = true;
        }

        if (!validEnd) {
            System.out.printf("%s Shift %d WARNING: end '%s' is invalid, using default '%s'%n", label, shiftNumber, rawEnd, defaultEnd);
            hasError = true;
        }

        boolean inRangeStart = validStart && (rawStart.equals("00:00") || isWithinShiftRange(rawStart, defaultStart, defaultEnd, formatter));
        boolean inRangeEnd = validEnd && (rawEnd.equals("00:00") || isWithinShiftRange(rawEnd, defaultStart, defaultEnd, formatter));

        if (validStart && !inRangeStart && !rawStart.equals("00:00")) {
            System.out.printf("%s Shift %d WARNING: start '%s' is out of range (%s - %s), using default '%s'%n",
                    label, shiftNumber, rawStart, defaultStart, defaultEnd, defaultStart);
            hasError = true;
        }

        if (validEnd && !inRangeEnd && !rawEnd.equals("00:00")) {
            System.out.printf("%s Shift %d WARNING: end '%s' is out of range (%s - %s), using default '%s'%n",
                    label, shiftNumber, rawEnd, defaultStart, defaultEnd, defaultEnd);
            hasError = true;
        }

        String start = (validStart && inRangeStart) ? rawStart : defaultStart;
        String end = (validEnd && inRangeEnd) ? rawEnd : defaultEnd;

        System.out.printf("%s Shift %d FINAL: start = %s, end = %s%n", label, shiftNumber, start, end);

        BigDecimal dur = calculateDurationInMinutes(start, end, formatter,isFriday, shiftNumber);
        System.out.printf("%s Shift %d DURATION: %s minutes%n", label, shiftNumber, dur);
        if(!hasError) {
        	switch (shiftNumber) {
	        	case 1:
	        		updateObj.setSHIFT1_START_TIME(start);
	        		updateObj.setSHIFT1_END_TIME(end);
	        		updateObj.setSHIFT1_TOTAL_TIME(dur);
	        		break;
	        	case 2:
	        		updateObj.setSHIFT2_START_TIME(start);
	        		updateObj.setSHIFT2_END_TIME(end);
	        		updateObj.setSHIFT2_TOTAL_TIME(dur);
	        		break;
	        	case 3:
	        		updateObj.setSHIFT3_START_TIME(start);
	        		updateObj.setSHIFT3_END_TIME(end);
	        		updateObj.setSHIFT3_TOTAL_TIME(dur);
	        		break;
        	}
        	
        }

        return hasError;
    }



    
    private BigDecimal yesNoToBigDecimal(String value, String reason, String parent, Date date) {
        System.out.println("🔍 Parsing " + parent + " | OFF: \"" + value + "\" | REASON: \"" + reason + "\" | Date: " + date);

        if ("Yes".equalsIgnoreCase(value)) {
            if (parent != null && date != null) {
                DWorkDay existing = reasonWorkDay.findByParentAndStatusAndDateWDTruncated(parent, date);

                if (existing != null) {
                    if (reason != null && !reason.trim().isEmpty()) {
                        System.out.println("🔄 Found existing record. Updating DESCRIPTION to: \"" + reason + "\"");
                        existing.setDESCRIPTION(reason);
                        existing.setSTATUS(BigDecimal.ONE); // Mark as active
                    } else {
                        System.out.println("🗑️ Reason is empty. Marking STATUS = 0 for existing record.");
                        existing.setSTATUS(BigDecimal.ZERO);
                    }

                    existing.setLAST_UPDATE_DATE(new Date());
                    existing.setLAST_UPDATED_BY("System");
                    reasonWorkDay.save(existing);
                } else {
                    if (reason != null && !reason.trim().isEmpty()) {
                        long countReason = reasonWorkDay.count();
                        BigDecimal newIDReason = BigDecimal.valueOf(countReason + 1);

                        DWorkDay newEntry = new DWorkDay();
                        newEntry.setDETAIL_WD_ID(newIDReason);
                        newEntry.setPARENT(parent);
                        newEntry.setDATE_WD(date);
                        newEntry.setDESCRIPTION(reason);
                        newEntry.setSTATUS(BigDecimal.ONE);
                        newEntry.setCREATION_DATE(new Date());
                        newEntry.setCREATED_BY("System");
                        newEntry.setLAST_UPDATE_DATE(new Date());
                        newEntry.setLAST_UPDATED_BY("System");

                        reasonWorkDay.save(newEntry);
                        System.out.println("➕ No existing record found. Creating new entry with reason: \"" + reason + "\"");
                    } else {
                        System.out.println("⚠️ No existing record and reason is empty. Skipping insert.");
                    }
                }
            } else {
                System.out.println("⚠️ OFF = Yes, but parent or date is missing.");
            }

            return BigDecimal.ZERO;
        } else {
            // NEW LOGIC: OFF is not "Yes" (No, null, etc.)
            if (parent != null && date != null) {
                DWorkDay existing = reasonWorkDay.findByParentAndStatusAndDateWDTruncated(parent, date);

                if (existing != null) {
                    System.out.println("🚫 OFF is not 'Yes'. Marking existing record STATUS = 0");
                    existing.setSTATUS(BigDecimal.ZERO);
                    existing.setLAST_UPDATE_DATE(new Date());
                    existing.setLAST_UPDATED_BY("System");
                    reasonWorkDay.save(existing);
                } else {
                    System.out.println("ℹ️ OFF is not 'Yes' and no existing record found. Nothing to update.");
                }
            }

            System.out.println("🔁 OFF = No or other value. Returning 0.");
            return BigDecimal.ONE;
        }
    }

    private String getStringFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }

    private boolean isValidTime(String timeStr, DateTimeFormatter formatter) {
        if (timeStr == null) return false;
        try {
            LocalTime.parse(timeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isWithinShiftRange(String timeStr, String rangeStartStr, String rangeEndStr, DateTimeFormatter formatter) {
        try {
            LocalTime time = LocalTime.parse(timeStr, formatter);
            LocalTime rangeStart = LocalTime.parse(rangeStartStr, formatter);
            LocalTime rangeEnd = LocalTime.parse(rangeEndStr, formatter);

            if (rangeEnd.isAfter(rangeStart)) {
                // Normal range (e.g., 07:10 - 15:50)
                return !time.isBefore(rangeStart) && !time.isAfter(rangeEnd);
            } else {
                // Crosses midnight (e.g., 23:30 - 07:10)
                return !time.isBefore(rangeStart) || !time.isAfter(rangeEnd);
            }
        } catch (DateTimeParseException e) {
            return false;
        }
    }


}
