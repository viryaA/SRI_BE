package sri.sysint.sri_starter_back.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.CTCuring;
import sri.sysint.sri_starter_back.model.ItemCuring;
import sri.sysint.sri_starter_back.model.MachineCuring;
import sri.sysint.sri_starter_back.repository.CTCuringRepo;
import sri.sysint.sri_starter_back.repository.ItemCuringRepo;
import sri.sysint.sri_starter_back.repository.MachineCuringRepo;

@Service
@Transactional
public class CTCuringServiceImpl {
	@Autowired
    private CTCuringRepo ctCuringRepo;

    @Autowired
    private ItemCuringRepo itemCuringRepo;

    @Autowired
    private MachineCuringRepo machineCuringRepo;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 500;
	
    public CTCuringServiceImpl(CTCuringRepo ctCuringRepo){
        this.ctCuringRepo = ctCuringRepo;
    }
    
    @Transactional
    public List<CTCuring> processStream() {
        try (Stream<CTCuring> stream = ctCuringRepo.streamAll()) {
            return stream.toList(); // collect stream into list
        }
    }
    @Transactional(readOnly = true) // 👈 keeps transaction open
    public Stream<CTCuring> streamAll() {
        return ctCuringRepo.streamAll();
    }

    
    public BigDecimal getNewId() {
    	return ctCuringRepo.getNewId().add(BigDecimal.valueOf(1));
    }

    public List<CTCuring> getAllCTCuring() {
    	Iterable<CTCuring> ctCurings = ctCuringRepo.getDataOrderId();
        List<CTCuring> ctCuringList = new ArrayList<>();
        for (CTCuring item : ctCurings) {
            CTCuring ctCuringTemp = new CTCuring(item);
            ctCuringList.add(ctCuringTemp);
        }
        
        return ctCuringList;
    }
    
    public Optional<CTCuring> getCTCuringById(BigDecimal id) {
    	Optional<CTCuring> ctCuring = ctCuringRepo.findById(id);
    	return ctCuring;
    }

    @Transactional
    public void saveAll(List<CTCuring> list) {
        int i = 0;
        for (CTCuring entity : list) {

            entity.setCT_CURING_ID(getNewId());
            entity.setSTATUS(BigDecimal.valueOf(1));
            entity.setCREATION_DATE(new Date());
            entity.setLAST_UPDATE_DATE(new Date());
            
            entityManager.persist(entity);
            i++;

            if (i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // flush sisa data
        entityManager.flush();
        entityManager.clear();
    }


    public CTCuring saveCTCuring(CTCuring ctCuring) {
        try {
        	ctCuring.setCT_CURING_ID(getNewId());
        	ctCuring.setSTATUS(BigDecimal.valueOf(1));
        	ctCuring.setCREATION_DATE(new Date());
        	ctCuring.setLAST_UPDATE_DATE(new Date());
            return ctCuringRepo.save(ctCuring);
        } catch (Exception e) {
            System.err.println("Error saving CT CURING: " + e.getMessage());
            throw e;
        }
    }
    
    public CTCuring updateCTCuring(CTCuring ctCuring) {
        try {
            Optional<CTCuring> currentCTCuringOpt = ctCuringRepo.findById(ctCuring.getCT_CURING_ID());
            
            if (currentCTCuringOpt.isPresent()) {
                CTCuring currentCTCuring = currentCTCuringOpt.get();
                
             
                currentCTCuring.setWIP(ctCuring.getWIP());
                currentCTCuring.setGROUP_COUNTER(ctCuring.getGROUP_COUNTER());
                currentCTCuring.setVAR_GROUP_COUNTER(ctCuring.getVAR_GROUP_COUNTER());
                currentCTCuring.setSEQUENCE(ctCuring.getSEQUENCE());
                currentCTCuring.setWCT(ctCuring.getWCT());
                currentCTCuring.setOPERATION_SHORT_TEXT(ctCuring.getOPERATION_SHORT_TEXT());
                currentCTCuring.setOPERATION_UNIT(ctCuring.getOPERATION_UNIT());
                currentCTCuring.setBASE_QUANTITY(ctCuring.getBASE_QUANTITY());
                currentCTCuring.setSTANDART_VALUE_UNIT(ctCuring.getSTANDART_VALUE_UNIT());
                currentCTCuring.setCT_SEC1(ctCuring.getCT_SEC1());
                currentCTCuring.setCT_HR1000(ctCuring.getCT_HR1000());
                currentCTCuring.setWH_NORMAL_SHIFT_0(ctCuring.getWH_NORMAL_SHIFT_0());
                currentCTCuring.setWH_NORMAL_SHIFT_1(ctCuring.getWH_NORMAL_SHIFT_1());
                currentCTCuring.setWH_NORMAL_SHIFT_2(ctCuring.getWH_NORMAL_SHIFT_2());
                currentCTCuring.setWH_SHIFT_FRIDAY(ctCuring.getWH_SHIFT_FRIDAY()); 
                currentCTCuring.setWH_TOTAL_NORMAL_SHIFT(ctCuring.getWH_TOTAL_NORMAL_SHIFT());
                currentCTCuring.setWH_TOTAL_SHIFT_FRIDAY(ctCuring.getWH_TOTAL_SHIFT_FRIDAY()); 
                currentCTCuring.setALLOW_NORMAL_SHIFT_0(ctCuring.getALLOW_NORMAL_SHIFT_0());
                currentCTCuring.setALLOW_NORMAL_SHIFT_1(ctCuring.getALLOW_NORMAL_SHIFT_1());
                currentCTCuring.setALLOW_NORMAL_SHIFT_2(ctCuring.getALLOW_NORMAL_SHIFT_2());
                currentCTCuring.setALLOW_TOTAL(ctCuring.getALLOW_TOTAL());
                currentCTCuring.setOP_TIME_NORMAL_SHIFT_0(ctCuring.getOP_TIME_NORMAL_SHIFT_0());
                currentCTCuring.setOP_TIME_NORMAL_SHIFT_1(ctCuring.getOP_TIME_NORMAL_SHIFT_1());
                currentCTCuring.setOP_TIME_NORMAL_SHIFT_2(ctCuring.getOP_TIME_NORMAL_SHIFT_2());
                currentCTCuring.setOP_TIME_SHIFT_FRIDAY(ctCuring.getOP_TIME_SHIFT_FRIDAY());  
                currentCTCuring.setOP_TIME_NORMAL_SHIFT(ctCuring.getOP_TIME_NORMAL_SHIFT());
                currentCTCuring.setOP_TIME_TOTAL_SHIFT_FRIDAY(ctCuring.getOP_TIME_TOTAL_SHIFT_FRIDAY()); 
                currentCTCuring.setKAPS_NORMAL_SHIFT_0(ctCuring.getKAPS_NORMAL_SHIFT_0());
                currentCTCuring.setKAPS_NORMAL_SHIFT_1(ctCuring.getKAPS_NORMAL_SHIFT_1());
                currentCTCuring.setKAPS_NORMAL_SHIFT_2(ctCuring.getKAPS_NORMAL_SHIFT_2());
                currentCTCuring.setKAPS_SHIFT_FRIDAY(ctCuring.getKAPS_SHIFT_FRIDAY());
                currentCTCuring.setKAPS_TOTAL_NORMAL_SHIFT(ctCuring.getKAPS_TOTAL_NORMAL_SHIFT());
                currentCTCuring.setKAPS_TOTAL_SHIFT_FRIDAY(ctCuring.getKAPS_TOTAL_SHIFT_FRIDAY());  
                currentCTCuring.setWAKTU_TOTAL_CT_NORMAL(ctCuring.getWAKTU_TOTAL_CT_NORMAL());
                currentCTCuring.setWAKTU_TOTAL_CT_FRIDAY(ctCuring.getWAKTU_TOTAL_CT_FRIDAY());  
                
                currentCTCuring.setSTATUS(ctCuring.getSTATUS()); 
                currentCTCuring.setLAST_UPDATE_DATE(new Date());  
                currentCTCuring.setLAST_UPDATED_BY(ctCuring.getLAST_UPDATED_BY());
                
                return ctCuringRepo.save(currentCTCuring);
            } else {
                throw new RuntimeException("CT CURING with ID " + ctCuring.getCT_CURING_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating CT CURING: " + e.getMessage());
            throw e;
        }
    }

    
    public CTCuring deleteCTCuring(CTCuring ctCuring) {
        try {
            Optional<CTCuring> currentCTCuringOpt = ctCuringRepo.findById(ctCuring.getCT_CURING_ID());
            
            if (currentCTCuringOpt.isPresent()) {
            	CTCuring currentCTCuring = currentCTCuringOpt.get();
                
            	currentCTCuring.setSTATUS(BigDecimal.valueOf(0));
            	currentCTCuring.setLAST_UPDATE_DATE(new Date());
            	currentCTCuring.setLAST_UPDATED_BY(ctCuring.getLAST_UPDATED_BY());
                
                return ctCuringRepo.save(currentCTCuring);
            } else {
                throw new RuntimeException("CY CURING with ID " + ctCuring.getCT_CURING_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating CT CURING: " + e.getMessage());
            throw e;
        }
    }
    public CTCuring activateCTCuring(CTCuring ctCuring) {
        try {
            Optional<CTCuring> currentCTCuringOpt = ctCuringRepo.findById(ctCuring.getCT_CURING_ID());
            
            if (currentCTCuringOpt.isPresent()) {
            	CTCuring currentCTCuring = currentCTCuringOpt.get();
                
            	currentCTCuring.setSTATUS(BigDecimal.valueOf(1));
            	currentCTCuring.setLAST_UPDATE_DATE(new Date());
            	currentCTCuring.setLAST_UPDATED_BY(ctCuring.getLAST_UPDATED_BY());
                
                return ctCuringRepo.save(currentCTCuring);
            } else {
                throw new RuntimeException("CY CURING with ID " + ctCuring.getCT_CURING_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating CT CURING: " + e.getMessage());
            throw e;
        }
    }
    
    public void deleteAllCTCuring() {
    	ctCuringRepo.deleteAll();
    }
    
    public ByteArrayInputStream exportCTCuringsExcel() throws IOException {
        List<CTCuring> ctCurings = ctCuringRepo.getDataOrderId();
        return dataToExcelPlain(ctCurings);
    }
   
    public ByteArrayInputStream layoutCTCuringsExcel() throws IOException {
        ByteArrayInputStream byteArrayInputStream = layoutToExcel();
        return byteArrayInputStream;
    }

    private static final String[] COLUMNS = {
            "WIP","GROUP_COUNTER","VAR_GROUP_COUNTER","SEQUENCE","WCT","OPERATION_SHORT_TEXT",
            "OPERATION_UNIT","BASE_QUANTITY","STANDART_VALUE_UNIT","CT_SEC1","CT_HR1000",
            "WH_NORMAL_SHIFT_0","WH_NORMAL_SHIFT_1","WH_NORMAL_SHIFT_2","WH_SHIFT_FRIDAY",
            "WH_TOTAL_NORMAL_SHIFT","WH_TOTAL_SHIFT_FRIDAY","ALLOW_NORMAL_SHIFT_0","ALLOW_NORMAL_SHIFT_1",
            "ALLOW_NORMAL_SHIFT_2","ALLOW_TOTAL","OP_TIME_NORMAL_SHIFT_0","OP_TIME_NORMAL_SHIFT_1",
            "OP_TIME_NORMAL_SHIFT_2","OP_TIME_SHIFT_FRIDAY","OP_TIME_TOTAL_NORMAL_SHIFT",
            "OP_TIME_TOTAL_SHIFT_FRIDAY","KAPS_NORMAL_SHIFT_0","KAPS_NORMAL_SHIFT_1","KAPS_NORMAL_SHIFT_2",
            "KAPS_SHIFT_FRIDAY","KAPS_TOTAL_NORMAL_SHIFT","KAPS_TOTAL_SHIFT_FRIDAY",
            "WAKTU_TOTAL_CT_NORMAL","WAKTU_TOTAL_CT_FRIDAY"
    };

    // ========================================================================
    // Layout Only (pakai XSSFWorkbook agar auto-size bisa dipakai)
    // ========================================================================
    public ByteArrayInputStream layoutToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet("LAYOUT");

            // Style header
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Buat header row
            Row header = sheet.createRow(0);
            for (int i = 0; i < COLUMNS.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(COLUMNS[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }
            
            sheet.setColumnWidth(0, 20 * 256);  // WIP
            sheet.setColumnWidth(4, 20 * 256);  // WCT
            sheet.setColumnWidth(5, 35 * 256);  // OPERATION_SHORT_TEXT

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } finally {
            workbook.close();
            out.close();
        }
    }

    // ========================================================================
    // Data Export (hybrid: XSSF -> autoSize, lalu SXSSF -> streaming)
    // ========================================================================
    public ByteArrayInputStream dataToExcelPlain(List<CTCuring> ctCurings) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        Sheet xssfSheet = xssfWorkbook.createSheet("CT_CURING DATA");

        // Style header & data
        CellStyle headerStyle = createHeaderStyle(xssfWorkbook);
        CellStyle dataStyle = createDataStyle(xssfWorkbook);

        // Header
        Row header = xssfSheet.createRow(0);
        for (int i = 0; i < COLUMNS.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(COLUMNS[i]);
            cell.setCellStyle(headerStyle);
        }

        // Auto-size pakai XSSF
        for (int i = 0; i < COLUMNS.length; i++) {
            xssfSheet.autoSizeColumn(i);
        }
        
        xssfSheet.setColumnWidth(0, 20 * 256);  // WIP
        xssfSheet.setColumnWidth(4, 20 * 256);  // WCT
        xssfSheet.setColumnWidth(5, 35 * 256);  // OPERATION_SHORT_TEXT

        // Wrap ke SXSSF (streaming)
        SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook, 100);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.getSheetAt(0);

            // Isi data
            int rowIndex = 1;
            for (CTCuring c : ctCurings) {
                Row row = sheet.createRow(rowIndex++);
                int col = 0;

                createCell(row, col++, safe(c.getWIP()), dataStyle);
                createCell(row, col++, safe(c.getGROUP_COUNTER()), dataStyle);
                createCell(row, col++, safe(c.getVAR_GROUP_COUNTER()), dataStyle);
                createCell(row, col++, safeNum(c.getSEQUENCE()), dataStyle);
                createCell(row, col++, safe(c.getWCT()), dataStyle);
                createCell(row, col++, safe(c.getOPERATION_SHORT_TEXT()), dataStyle);
                createCell(row, col++, safe(c.getOPERATION_UNIT()), dataStyle);
                createCell(row, col++, safeNum(c.getBASE_QUANTITY()), dataStyle);
                createCell(row, col++, safe(c.getSTANDART_VALUE_UNIT()), dataStyle);
                createCell(row, col++, safeNum(c.getCT_SEC1()), dataStyle);
                createCell(row, col++, safeNum(c.getCT_HR1000()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_NORMAL_SHIFT_0()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_NORMAL_SHIFT_1()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_NORMAL_SHIFT_2()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_TOTAL_NORMAL_SHIFT()), dataStyle);
                createCell(row, col++, safeNum(c.getWH_TOTAL_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getALLOW_NORMAL_SHIFT_0()), dataStyle);
                createCell(row, col++, safeNum(c.getALLOW_NORMAL_SHIFT_1()), dataStyle);
                createCell(row, col++, safeNum(c.getALLOW_NORMAL_SHIFT_2()), dataStyle);
                createCell(row, col++, safeNum(c.getALLOW_TOTAL()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_NORMAL_SHIFT_0()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_NORMAL_SHIFT_1()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_NORMAL_SHIFT_2()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_NORMAL_SHIFT()), dataStyle);
                createCell(row, col++, safeNum(c.getOP_TIME_TOTAL_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_NORMAL_SHIFT_0()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_NORMAL_SHIFT_1()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_NORMAL_SHIFT_2()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_TOTAL_NORMAL_SHIFT()), dataStyle);
                createCell(row, col++, safeNum(c.getKAPS_TOTAL_SHIFT_FRIDAY()), dataStyle);
                createCell(row, col++, safeNum(c.getWAKTU_TOTAL_CT_NORMAL()), dataStyle);
                createCell(row, col++, safeNum(c.getWAKTU_TOTAL_CT_FRIDAY()), dataStyle);

                if (rowIndex % 2000 == 0) {
                    ((SXSSFSheet) sheet).flushRows(2000); // buang dari memory
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } finally {
            workbook.close();
            out.close();
        }
    }

    // ========================================================================
    // Helpers
    // ========================================================================
    private void createCell(Row row, int col, String val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int col, double val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val);
        cell.setCellStyle(style);
    }

    private String safe(String val) {
        return val != null ? val : "";
    }

    private double safeNum(Number num) {
        return num != null ? num.doubleValue() : 0;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = createBorderStyle(workbook);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        return createBorderStyle(workbook);
    }

    private CellStyle createBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

}
