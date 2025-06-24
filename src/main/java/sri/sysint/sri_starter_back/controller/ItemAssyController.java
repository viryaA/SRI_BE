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
import sri.sysint.sri_starter_back.model.ItemAssy;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.ItemAssyServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class ItemAssyController {
    
    private Response response;    

    @Autowired
    private ItemAssyServiceImpl itemAssyServiceImpl;
    
    @PersistenceContext    
    private EntityManager em;
    
    @PreAuthorize("isAuthenticated()")    
    @GetMapping("/getAllItemAssy")
    public Response getAllPlant(final HttpServletRequest req) throws ResourceNotFoundException {

        List<ItemAssy> itemAssies = new ArrayList<>();
        itemAssies = itemAssyServiceImpl.getAllItemAssy();

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            itemAssies
        );


        return response;
    }

    @PreAuthorize("isAuthenticated()")    
    @GetMapping("/getItemAssyById/{id}")
    public Response getPlantById(final HttpServletRequest req, @PathVariable String id) throws ResourceNotFoundException {

        Optional<ItemAssy> itemAssy = Optional.of(new ItemAssy());
        itemAssy = itemAssyServiceImpl.getItemAssyById(id);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            itemAssy
        );


        return response;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/saveItemAssy")
    public Response savePlant(final HttpServletRequest req, @RequestBody ItemAssy itemAssy) throws ResourceNotFoundException {

        ItemAssy savedItemAssy = itemAssyServiceImpl.saveItemAssy(itemAssy);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            savedItemAssy
        );

        return response;
    }

    @PreAuthorize("isAuthenticated()")    
    @PostMapping("/updateItemAssy")
    public Response updatePlant(final HttpServletRequest req, @RequestBody ItemAssy itemAssy) throws ResourceNotFoundException {

        ItemAssy updatedItemAssy = itemAssyServiceImpl.updateItemAssy(itemAssy);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            updatedItemAssy
        );

        return response;
    }

    @PreAuthorize("isAuthenticated()")    
    @PostMapping("/deleteItemAssy")
    public Response deletePlant(final HttpServletRequest req, @RequestBody ItemAssy itemAssy) throws ResourceNotFoundException {

        ItemAssy deletedItemAssy = itemAssyServiceImpl.deleteItemAssy(itemAssy);

        response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            deletedItemAssy
        );

        return response;
    }

    @PreAuthorize("isAuthenticated()")    
    @PostMapping("/restoreItemAssy")
    public Response restoreItemAssy(final HttpServletRequest req, @RequestBody ItemAssy itemAssy) throws ResourceNotFoundException {

        ItemAssy restoredItemAssy = itemAssyServiceImpl.restoreItemAssy(itemAssy);

        Response response = new Response(
            
            HttpStatus.OK.value(),
            null,
            HttpStatus.OK.getReasonPhrase(),
            req.getRequestURI(),
            restoredItemAssy
        );
        return response;

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/saveItemAssyExcel")
    @Transactional
    public Response saveItemAssyExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {

        if (file.isEmpty()) {
            return new Response( HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
        }

        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<ItemAssy> itemAssies = new ArrayList<>();
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

                    ItemAssy itemAssy = new ItemAssy();
                    Cell itemAssyCell = row.getCell(1);

                    if (itemAssyCell == null || itemAssyCell.getCellType() == CellType.BLANK) {
                        errorMessages.add("Data Tidak Valid, Terdapat Data Kosong pada Baris " + (i + 1) + " Kolom 2 (Item Assy)");
                        continue;
                    }

                    itemAssy.setITEM_ASSY(itemAssyCell.getStringCellValue());
                    itemAssy.setSTATUS(BigDecimal.valueOf(1));
                    itemAssy.setCREATION_DATE(new Date());
                    itemAssy.setLAST_UPDATE_DATE(new Date());

                    itemAssies.add(itemAssy);
                }
            }

            if (!errorMessages.isEmpty()) {
                return new Response( HttpStatus.BAD_REQUEST.value(), null, String.join("; ", errorMessages), req.getRequestURI(), null);
            }

            itemAssyServiceImpl.deleteAllItemAssy();
            for (ItemAssy itemAssy : itemAssies) {
                itemAssyServiceImpl.saveItemAssy(itemAssy);
            }

            return new Response( HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), itemAssies);

        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exportItemAssyExcel")
    public ResponseEntity<InputStreamResource> exportItemAssyExcel() throws IOException {
        String filename = "EXPORT_MASTER_ITEM_ASSY.xlsx";

        ByteArrayInputStream data = itemAssyServiceImpl.exportItemAssysExcel(); 
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/layoutItemAssyExcel")
    public ResponseEntity<InputStreamResource> layoutItemAssyExcel() throws IOException {
        String filename = "LAYOUT_MASTER_ITEM_ASSY.xlsx";

        ByteArrayInputStream data = itemAssyServiceImpl.layoutItemAssysExcel(); 
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

}
