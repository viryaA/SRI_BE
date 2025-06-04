package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getAllWorkDays")
    public Response getAllWorkDays(final HttpServletRequest req) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                List<WorkDay> workDays = workDayServiceImpl.getAllWorkDays();

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    workDays
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }
    
    @PostMapping("/getAllWorkDaysByDateRange")
    public Response getAllWorkDaysByDateRange(final HttpServletRequest req, 
                                            @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {
        // Validate JWT token
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            if (user == null) {
                throw new ResourceNotFoundException("User not found");
            }

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
                new Date(),
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



    @PostMapping("/getWorkDayByDate")
    public Response getWorkDayByDate(final HttpServletRequest req, @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                String date = requestBody.get("date"); // Extract date from body

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date parsedDate = dateFormat.parse(date);

                Optional<WorkDay> workDay = workDayServiceImpl.getWorkDayByDate(parsedDate);

                return new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    workDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }
    }


    @PostMapping("/saveWorkDay")
    public Response saveWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay savedWorkDay = workDayServiceImpl.saveWorkDay(workDay);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    savedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }

    @PostMapping("/updateWorkDay")
    public Response updateWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay updatedWorkDay = workDayServiceImpl.updateWorkDay(workDay);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    updatedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }

    @PostMapping("/deleteWorkDay")
    public Response deleteWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay deletedWorkDay = workDayServiceImpl.deleteWorkDay(workDay);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    deletedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }

    @PostMapping("/restoreWorkDay")
    public Response restoreWorkDay(final HttpServletRequest req, @RequestBody WorkDay workDay) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay restoredWorkDay = workDayServiceImpl.restoreWorkDay(workDay);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    restoredWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }

//    @PostMapping("/saveWorkDaysExcel")
//    public Response saveWorkDaysExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
//        String header = req.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
//        }
//
//        String token = header.replace("Bearer ", "");
//        Response response;
//
//        try {
//            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
//                .build()
//                .verify(token)
//                .getSubject();
//
//            if (user != null) {
//                if (file.isEmpty()) {
//                    return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
//                }
//
//                workDayServiceImpl.deleteAllWorkDays();  
//
//                try (InputStream inputStream = file.getInputStream()) {
//                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//                    XSSFSheet sheet = workbook.getSheetAt(0);
//
//                    List<WorkDay> workDays = new ArrayList<>();
//
//                    Row headerRow = sheet.getRow(0);
//                    if (headerRow == null) {
//                        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Invalid file format", req.getRequestURI(), null);
//                    }
//
//                    // Date format for "Wed July 03, 2024"
//                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("E MMMM dd, yyyy", Locale.ENGLISH);
//                    // Date format for ZonedDateTime parsing
//                    DateTimeFormatter zonedDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//
//                    for (int j = 1; j < headerRow.getLastCellNum(); j++) {
//                        Cell dateCell = headerRow.getCell(j);
//                        if (dateCell != null) {
//                            ZonedDateTime zonedDateTime = null;
//
//                            try {
//                                if (dateCell.getCellType() == CellType.NUMERIC) {
//                                    if (DateUtil.isCellDateFormatted(dateCell)) {
//                                        LocalDate localDate = dateCell.getDateCellValue().toInstant()
//                                                .atZone(ZoneId.systemDefault()) 
//                                                .toLocalDate();
//                                        zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
//                                    } else {
//                                        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Invalid date format in file: " + dateCell.toString(), req.getRequestURI(), null);
//                                    }
//                                } else if (dateCell.getCellType() == CellType.STRING) {
//                                    try {
//                                        // Attempt to parse the string date
//                                        LocalDate parsedDate = LocalDate.parse(dateCell.getStringCellValue().trim(), dateFormatter);
//                                        zonedDateTime = parsedDate.atStartOfDay(ZoneId.of("UTC"));
//                                    } catch (Exception e) {
//                                        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Error parsing date: " + dateCell.toString(), req.getRequestURI(), null);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Error processing date cell: " + dateCell.toString(), req.getRequestURI(), null);
//                            }
//
//                            Date date = Date.from(zonedDateTime.toInstant());
//                            WorkDay workDay = new WorkDay();
//                            workDay.setDATE_WD(date);
//
//                            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                                Row row = sheet.getRow(i);
//                                if (row == null || isRowEmpty(row)) {
//                                    break; 
//                                }
//
//                                Cell cell = row.getCell(j);  
//                                String shiftName = row.getCell(0).getStringCellValue();  
//                                int status = 0;
//
//                                if (cell != null) {
//                                    System.out.println("Row: " + (i + 1) + ", Col: " + (j + 1) + ", Cell Type: " + cell.getCellType() + ", Value: " + cell.toString());
//
//                                    if (cell.getCellType() == CellType.STRING) {
//                                        String cellValue = cell.getStringCellValue().trim();
//                                        if ("v".equalsIgnoreCase(cellValue)) {
//                                            status = 1;
//                                        } else if (cellValue.isEmpty()) {
//                                            status = 0;
//                                        }
//                                    } else if (cell.getCellType() == CellType.BLANK) {
//                                        status = 0;
//                                    }
//                                } else {
//                                    status = 0; 
//                                }
//
//                                switch (shiftName.toUpperCase()) {
//                                    case "WD_SHIFT_1":
//                                        workDay.setIWD_SHIFT_1(BigDecimal.valueOf(status));
//                                        break;
//                                    case "WD_SHIFT_2":
//                                        workDay.setIWD_SHIFT_2(BigDecimal.valueOf(status));
//                                        break;
//                                    case "WD_SHIFT_3":
//                                        workDay.setIWD_SHIFT_3(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TL_1":
//                                        workDay.setIOT_TL_1(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TL_2":
//                                        workDay.setIOT_TL_2(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TL_3":
//                                        workDay.setIOT_TL_3(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TT_1":
//                                        workDay.setIOT_TT_1(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TT_2":
//                                        workDay.setIOT_TT_2(BigDecimal.valueOf(status));
//                                        break;
//                                    case "OT_TT_3":
//                                        workDay.setIOT_TT_3(BigDecimal.valueOf(status));
//                                        break;
//                                    default:
//                                        throw new IllegalArgumentException("Unknown shift name: " + shiftName);
//                                }
//                            }
//
//                            int iwdShift1Status = workDay.getIWD_SHIFT_1().intValue();
//                            int iwdShift2Status = workDay.getIWD_SHIFT_2().intValue();
//                            int iwdShift3Status = workDay.getIWD_SHIFT_3().intValue();
//
//                            if (iwdShift1Status == 0 && iwdShift2Status == 0 && iwdShift3Status == 0) {
//                                workDay.setOFF(BigDecimal.ONE); 
//                                workDay.setSEMI_OFF(BigDecimal.ZERO);
//                            } else if (iwdShift1Status == 1 && iwdShift2Status == 1 && iwdShift3Status == 1) {
//                                workDay.setOFF(BigDecimal.ZERO); 
//                                workDay.setSEMI_OFF(BigDecimal.ZERO);
//                            } else {
//                                workDay.setOFF(BigDecimal.ZERO); 
//                                workDay.setSEMI_OFF(BigDecimal.ONE);
//                            }
//
//                            workDayServiceImpl.saveWorkDay(workDay); 
//                            workDays.add(workDay);
//                        }
//                    }
//
//                    response = new Response(new Date(), HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), workDays);
//
//                } catch (IOException e) {
//                    response = new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
//                }
//            } else {
//                throw new ResourceNotFoundException("User not found");
//            }
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("JWT token is not valid or expired");
//        }
//
//        return response;
//    }
    
    @PostMapping("/saveWorkDaysExcel")
    public Response saveWorkDaysExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {

        if (file.isEmpty()) {
            return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
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
                    return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Invalid date format in file", req.getRequestURI(), null);
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
                            return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Invalid date format in file: " + dateCell.toString(), req.getRequestURI(), null);
                        }
                    } else if (dateCell.getCellType() == CellType.STRING) {
                        LocalDate parsedDate = LocalDate.parse(dateCell.getStringCellValue().trim(), dateFormatter);
                        zonedDateTime = parsedDate.atStartOfDay(ZoneId.of("UTC"));
                    }
                } catch (Exception e) {
                    return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Error parsing date: " + dateCell.toString(), req.getRequestURI(), null);
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

            return new Response(new Date(), HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), workDays);

        } catch (IOException e) {
            return new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
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

    @RequestMapping("/exportWorkDaysExcel")
    public ResponseEntity<InputStreamResource> exportWorkDaysExcel() throws IOException {
        String filename = "MASTER_WORK_DAY.xlsx";

        ByteArrayInputStream data = workDayServiceImpl.exportWDsExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
    
   @PostMapping("/turnOnOvertime")
    public Response turnOnOvertime(final HttpServletRequest req, @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                String dateWd = requestBody.get("dateWd"); // Extract dateWd from JSON request body

                WorkDay updatedWorkDay = workDayServiceImpl.turnOnOvertime(dateWd);

                return new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    updatedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }
    }

    
    @PostMapping("/turnOnShift/{dateWd}/{shift}")
    public Response turnOnShift(final HttpServletRequest req, @PathVariable String dateWd, @PathVariable String shift) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");
        Response response;

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay updatedWorkDay = workDayServiceImpl.turnOnShift(dateWd, shift);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    updatedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }

    @PostMapping("/turnOffShift/{dateWd}/{shift}")
    public Response turnOffShift(final HttpServletRequest req, @PathVariable String dateWd, @PathVariable String shift) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");
        Response response;

        try {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            if (user != null) {
                WorkDay updatedWorkDay = workDayServiceImpl.turnOffShift(dateWd, shift);

                response = new Response(
                    new Date(),
                    HttpStatus.OK.value(),
                    null,
                    HttpStatus.OK.getReasonPhrase(),
                    req.getRequestURI(),
                    updatedWorkDay
                );
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("JWT token is not valid or expired");
        }

        return response;
    }
    
    @GetMapping("/export-template-excel/{year}/{month}")
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


	@PostMapping("/importWDExcel")
	public Response importWorkDaysExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
	    if (file.isEmpty()) {
	        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
	    }
	
	    try (InputStream inputStream = file.getInputStream()) {
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        XSSFSheet sheet = workbook.getSheetAt(0);
	
	        // Define expected vertical headers in column A
	        String[] expectedRows = {
	            "DATE_WD",
	            "S1 OFF TT",
	            "S1 START OT_TT", "S1 END OT_TT", "S1 REASON OT_TT",
	            "S1 OFF TL",
	            "S1 START OT_TL", "S1 END OT_TL", "S1 REASON OT_TL",
	            "S2 OFF TT",
	            "S2 START OT_TT", "S2 END OT_TT", "S2 REASON OT_TT",
	            "S2 OFF TL",
	            "S2 START OT_TL", "S2 END OT_TL", "S2 REASON OT_TL",
	            "S3 OFF TT",
	            "S3 START OT_TT", "S3 END OT_TT", "S3 REASON OT_TT",
	            "S3 OFF TL",
	            "S3 START OT_TL", "S3 END OT_TL", "S3 REASON OT_TL"
	        };
	
	        // Check if vertical headers in column A match
	        for (int i = 0; i < expectedRows.length; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null || row.getCell(0) == null) {
	                return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Missing or incomplete header in column A at row " + (i + 1), req.getRequestURI(), null);
	            }
	            String cellValue = row.getCell(0).getStringCellValue().trim();
	            if (!cellValue.equals(expectedRows[i])) {
	                return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "Invalid header: expected '" + expectedRows[i] + "' but found '" + cellValue + "' at row " + (i + 1), req.getRequestURI(), null);
	            }
	        }
	
	        // Check if cells in column B contain valid dates
	        Row rowh = sheet.getRow(0); // Only check the first row
	        if (rowh != null) {
	            int lastColumn = rowh.getLastCellNum(); // Total columns used in this row
	
	            for (int col = 1; col < lastColumn; col++) {
	                Cell cell = rowh.getCell(col);
	                System.out.print("Column " + (col + 1) + ": ");
	
	                if (cell == null) {
	                    System.out.println("Cell is null");
	                    return new Response(
	                        new Date(),
	                        HttpStatus.BAD_REQUEST.value(),
	                        null,
	                        "Missing cell at column " + (col + 1) + " in row 1",
	                        req.getRequestURI(),
	                        null
	                    );
	                }
	
	                if (cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
	                    System.out.println("Invalid date");
	                    return new Response(
	                        new Date(),
	                        HttpStatus.BAD_REQUEST.value(),
	                        null,
	                        "Invalid or non-date cell at column " + (col + 1) + " in row 1",
	                        req.getRequestURI(),
	                        null
	                    );
	                }
	
	                System.out.println("Date: " + cell.getDateCellValue());
	            }
	        }
	        List<Map<String, Object>> resultTTList = new ArrayList<>();
	        List<Map<String, Object>> resultTLList = new ArrayList<>();

	        int lastColumn = sheet.getRow(0).getLastCellNum();

	        for (int col = 1; col < lastColumn; col++) {
	            Map<String, Object> ttData = new HashMap<>();
	            Map<String, Object> tlData = new HashMap<>();

	            // DATE_WD
	            Cell dateCell = sheet.getRow(0).getCell(col);
	            Date workDate = dateCell.getDateCellValue();
	            ttData.put("DATE_WD", workDate);
	            tlData.put("DATE_WD", workDate);

	            // SHIFTS: S1, S2, S3
	            for (int shift = 1; shift <= 3; shift++) {
	            	int baseRow = (shift - 1) * 8 + 1;

	                // TT Data
	                Map<String, Object> shiftTT = new HashMap<>();
	                shiftTT.put("OFF", getCellValue(sheet.getRow(baseRow).getCell(col)));
	                shiftTT.put("START", getCellValue(sheet.getRow(baseRow + 1).getCell(col)));
	                shiftTT.put("END", getCellValue(sheet.getRow(baseRow + 2).getCell(col)));
	                shiftTT.put("REASON", getCellValue(sheet.getRow(baseRow + 3).getCell(col)));
	                ttData.put("S" + shift + "_TT", shiftTT);

	                // TL Data
	                Map<String, Object> shiftTL = new HashMap<>();
	                shiftTL.put("OFF", getCellValue(sheet.getRow(baseRow + 4).getCell(col)));
	                shiftTL.put("START", getCellValue(sheet.getRow(baseRow + 5).getCell(col)));
	                shiftTL.put("END", getCellValue(sheet.getRow(baseRow + 6).getCell(col)));
	                shiftTL.put("REASON", getCellValue(sheet.getRow(baseRow + 7).getCell(col)));
	                tlData.put("S" + shift + "_TL", shiftTL);
	            }

	            resultTTList.add(ttData);
	            resultTLList.add(tlData);
	        }
	        
	        for (int i = 0; i < resultTTList.size(); i++) {
                boolean canUpdateDetail = false;
	            Map<String, Object> ttData = resultTTList.get(i);
	            Map<String, Object> tlData = resultTLList.get(i);

	            Map<String, Object> s1TT = (Map<String, Object>) ttData.get("S1_TT");
	            Map<String, Object> s1TL = (Map<String, Object>) tlData.get("S1_TL");
                Map<String, Object> s2TT = (Map<String, Object>) ttData.get("S2_TT");
	            Map<String, Object> s2TL = (Map<String, Object>) tlData.get("S2_TL");
                Map<String, Object> s3TT = (Map<String, Object>) ttData.get("S3_TT");
	            Map<String, Object> s3TL = (Map<String, Object>) tlData.get("S3_TL");

                String startTT = extractHourMinute(s1TT.get("START"));
                String startTL = extractHourMinute(s1TL.get("START"));

                System.out.println("S1 TT START: " + startTT + " | S1 TL START: " + startTL);

		        Date wdIsDate = (Date) ttData.get("DATE_WD");

			     // Example: querying workDayRepo using the date
		        Optional<WorkDay> workDayOpt = workDayRepo.findById(wdIsDate);
                WorkDay updateWorkDay = null;
                if (workDayOpt.isPresent()) {
                    canUpdateDetail = true;
                    System.out.println("Data ada");
                    System.out.println(workDayOpt.get().getDATE_WD());
                    updateWorkDay = workDayOpt.get();

                    // Convert and set TL
                    updateWorkDay.setIOT_TL_1(yesNoToBigDecimal(s1TL.get("OFF").toString(),s1TL));
                    updateWorkDay.setIOT_TL_2(yesNoToBigDecimal(s2TL.get("OFF").toString(),s2TL));
                    updateWorkDay.setIOT_TL_3(yesNoToBigDecimal(s3TL.get("OFF").toString(),s3TL));

                    // Convert and set TT
                    updateWorkDay.setIOT_TT_1(yesNoToBigDecimal(s1TT.get("OFF").toString(),s1TT));
                    updateWorkDay.setIOT_TT_2(yesNoToBigDecimal(s2TT.get("OFF").toString(),s2TT));
                    updateWorkDay.setIOT_TT_3(yesNoToBigDecimal(s3TT.get("OFF").toString(),s3TT));
                }

                DWorkDayHoursSpesific updateTT = null;
                DWorkDayHoursSpesific updateTL = null;
		        Optional<List<DWorkDayHoursSpesific>> detailWDData = detailWorkDayRepo.findByDATEWD(wdIsDate);
		        if (!detailWDData.isEmpty()) {
		            System.out.println("Data Detail ada");
		            for (DWorkDayHoursSpesific item : detailWDData.get()) {
		            	if(item.getDESCRIPTION().equals("OT_TT")) {
                            updateTT = item;	            		
		            	}else if (item.getDESCRIPTION().equals("OT_TL")){
                            updateTL = item;
                        }
		            }
		        }

                if (updateTT != null && updateTL != null && canUpdateDetail) {
                    System.out.println("Siap update");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                    // === TT Updates ===
                    updateShift(updateTT, "TT", s1TT, 1, formatter);
                    updateShift(updateTT, "TT", s2TT, 2, formatter);
                    updateShift(updateTT, "TT", s3TT, 3, formatter);

                    // === TL Updates ===
                    updateShift(updateTL, "TL", s1TL, 1, formatter);
                    updateShift(updateTL, "TL", s2TL, 2, formatter);
                    updateShift(updateTL, "TL", s3TL, 3, formatter);
                }
                if(canUpdateDetail){
                    System.out.println("UPDATEUPDATEUPDATEUPDATEUPDATE"+updateWorkDay.getDATE_WD()+updateTT.getSHIFT1_END_TIME()+" ID"+updateTT.getDETAIL_WD_HOURS_SPECIFIC_ID());
                    workDayRepo.save(updateWorkDay);
                    detailWorkDayRepo.save(updateTT);
                    detailWorkDayRepo.save(updateTL);
                }

	        }

	        return new Response(new Date(), HttpStatus.OK.value(), null, "File processed successfully", req.getRequestURI(), Map.of("tt", resultTTList, "tl", resultTLList));

//	        return new Response(new Date(), HttpStatus.OK.value(), null, "File processed and data validated successfully", req.getRequestURI(), null);
	
	    } catch (IOException e) {
	        return new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
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
                    return null;
                }
            }
        } else if (timeValue instanceof Date) {
            SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
            return hourMinuteFormat.format((Date) timeValue);
        }

        return null;
    }

    private BigDecimal calculateDurationInMinutes(String start, String end, DateTimeFormatter formatter) {
        try {
            LocalTime startTime = LocalTime.parse(start, formatter);
            LocalTime endTime = LocalTime.parse(end, formatter);

            long minutes = Duration.between(startTime, endTime).toMinutes();
            if (minutes < 0) {
                minutes += 24 * 60; // overnight shift
            }
            return BigDecimal.valueOf(minutes);
        } catch (DateTimeParseException e) {
            System.err.println("Time parse error: " + e.getMessage());
            return null;
        }
    }

    private void updateShift(DWorkDayHoursSpesific updateObj, String label, Map<String, Object> shiftMap, int shiftNumber, DateTimeFormatter formatter) {
        String start = extractHourMinute(shiftMap.get("START"));
        String end = extractHourMinute(shiftMap.get("END"));

        System.out.printf("%s Shift %d: start = %s, end = %s%n", label, shiftNumber, start, end);

        if (start != null && end != null) {
            BigDecimal dur = calculateDurationInMinutes(start, end, formatter);
            System.out.printf("%s Shift %d duration = %s%n", label, shiftNumber, dur);

            switch (shiftNumber) {
                case 1 : {
                    updateObj.setSHIFT1_START_TIME(start);
                    updateObj.setSHIFT1_END_TIME(end);
                    updateObj.setSHIFT1_TOTAL_TIME(dur);
                }
                case 2 : {
                    updateObj.setSHIFT2_START_TIME(start);
                    updateObj.setSHIFT2_END_TIME(end);
                    updateObj.setSHIFT2_TOTAL_TIME(dur);
                }
                case 3 : {
                    updateObj.setSHIFT3_START_TIME(start);
                    updateObj.setSHIFT3_END_TIME(end);
                    updateObj.setSHIFT3_TOTAL_TIME(dur);
                }
            }
        }
    }
    
    private BigDecimal yesNoToBigDecimal(String value, Map<String, Object> data) {
        if ("Yes".equalsIgnoreCase(value)) {
            Object reason = data.get("REASON");
            if (reason != null) {
                System.out.println("OFF = Yes, Reason: " + reason.toString());
                // You can also save this somewhere if needed
            } else {
                System.out.println("OFF = Yes, but REASON is missing.");
            }
            return BigDecimal.ONE;
        }
        return BigDecimal.ZERO;
    }


}
