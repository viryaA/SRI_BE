package sri.sysint.sri_starter_back.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.store.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import sri.sysint.sri_starter_back.model.CTCuring;
import sri.sysint.sri_starter_back.model.DetailDailyMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.DetailMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.MachineCuring;
import sri.sysint.sri_starter_back.model.TotalPlan;
import sri.sysint.sri_starter_back.model.MachineProduct;
import sri.sysint.sri_starter_back.model.MarketingOrder;
import sri.sysint.sri_starter_back.model.ShiftMonthlyPlan;
import sri.sysint.sri_starter_back.model.WorkDay;
import sri.sysint.sri_starter_back.model.MonthlyPlanningNew;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.model.transaksi.ViewMonthlyPlanning;
import sri.sysint.sri_starter_back.repository.CTCuringRepo;
import sri.sysint.sri_starter_back.repository.DWorkDayHoursSpecificRepo;
import sri.sysint.sri_starter_back.repository.DetailMarketingOrderRepo;
import sri.sysint.sri_starter_back.repository.MachineCuringRepo;
import sri.sysint.sri_starter_back.repository.MachineProductRepo;
import sri.sysint.sri_starter_back.repository.MarketingOrderRepo;
import sri.sysint.sri_starter_back.repository.MonthlyPlanRepo;
import sri.sysint.sri_starter_back.repository.SettingRepo;
import sri.sysint.sri_starter_back.repository.TotalPlanRepo;
import sri.sysint.sri_starter_back.repository.MonthlyPlanNewRepo;
import sri.sysint.sri_starter_back.repository.ShiftMonthlyPlanRepo;
import sri.sysint.sri_starter_back.repository.StatusMPRepo;
import sri.sysint.sri_starter_back.repository.WorkDayRepo;


@Service
@Transactional
public class MonthlyPlanServiceImpl {
	
	@Autowired
    private MarketingOrderRepo marketingOrderRepo;
	
	@Autowired
    private MonthlyPlanRepo monthlyPlanningRepo;
	
	@Autowired
    private DetailMarketingOrderRepo detailMarketingOrderRepo;
	
	@Autowired
    private CTCuringRepo ctCuringRepo;
	
	@Autowired
    private ShiftMonthlyPlanRepo shiftMonthlyRepo;
	
	@Autowired
    private MachineCuringRepo machineCuringRepo;
	
	@Autowired
    private WorkDayRepo workDayRepo;
	
	@Autowired
    private SettingRepo settingRepo;
	
	@Autowired
    private MonthlyPlanNewRepo monthlyPlanNewRepo;

	@Autowired
    private TotalPlanRepo totalPlanRepo;
	
	@Autowired 
	private MachineProductRepo machineProductRepo;
	
	@Autowired
	private StatusMPRepo statusMPRepo;
	
	@Autowired
    private DWorkDayHoursSpecificRepo dWorkDayHourSpecificRepo;
	
    @Autowired
    private ObjectMapper objectMapper;
	
    
    private List<MachineCuring> machineCuringList = new ArrayList<>();

	private List<MachineCuring> machineCuringListTemp = new ArrayList<>();
	
	private List<ShiftMonthlyPlan> newShiftMonthlyPlan = new ArrayList<>();
	
	private List<ShiftMonthlyPlan> newShiftListPartNum = new ArrayList<>();
	
	private List<WorkDay> workDayList = new ArrayList<>();
	
	private List<ShiftMonthlyPlan> newShiftList = new ArrayList<>();
	
	private List<Map<String, Object>> detailMarkOrderList = new ArrayList<>();
	
	private BigDecimal smallOrderLimit;
	
	private List<ShiftMonthlyPlan> oldShiftPlan = new ArrayList<>();
	
	private List<TempOrder> tempOrderList = new ArrayList<>();
	
	private List<MachineProduct> machineProductList = new ArrayList<>();
	
	private List<MachineProduct> machineProductListperProduct = new ArrayList<>();
	
	private List<MachineCuring> machineCuringOrderList = new ArrayList<>();
	
	private List<MachineCuring> machineCuringUsedList = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderList = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListAB = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListBOM = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListDual = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListABFrontRear = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListBOMFrontRear = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderListDualFrontRear = new ArrayList<>();
	
	private List<DetailMo> detailMarketingOrderFrontRear = new ArrayList<>();
	
	private List<ChangeMould> changeMouldList = new ArrayList<>();
	
	private List<ChangeMould> endProductList = new ArrayList<>();
	
	private List<TempChangeMould> tempChangeMouldList = new ArrayList<>();
	
	private BigDecimal order;
	
	private BigDecimal percentagePlusMinus;
	
	private int maxChangeMould;
	
	private BigDecimal minProduction;
	
	public List<ShiftMonthlyPlan> MonthlyPlan(int month, int year, int limitChange, BigDecimal minA, BigDecimal maxA, BigDecimal minB, BigDecimal maxB, BigDecimal minC, BigDecimal maxC, BigDecimal minD, BigDecimal maxD) {
//		percentagePlusMinus = percentage;
		maxChangeMould = limitChange;
		getDataHeader(month, year);
        boolean tempShift = false;
        
        machineProductList.clear();
//        List<Object[]> results = machineProductRepo.findAllWct();
//
//        for (Object[] row : results) {
//            BigDecimal partNumber = (BigDecimal) row[0];
//            String workCenterText = (String) row[1];
//            
//            MachineProduct machineProduct = new MachineProduct(partNumber, workCenterText);
//            machineProductList.add(machineProduct);
//        }
        
		if(machineProductList != null) {
			for(MachineProduct mn : machineProductList) {
				
//	        	for(DetailMo dtMo : detailMarketingOrderListAB) {
//					
//	        		if(dtMo.getPartNumber().equals(mn.getPART_NUMBER())) {
//	        			minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
//	        			order = dtMo.getTotalAR();
//	        			
//	        			System.out.println("loop 2 isi mesin untuk produk" + mn.getPART_NUMBER() + "yaitu" + mn.getWORK_CENTER_TEXT());
//
//	        			tempShift = generateFromManualMapping(mn.getPART_NUMBER(), month, year, dtMo.getItemCuring(), mn.getWORK_CENTER_TEXT());
//	        			if(tempShift == true) {
//	        				dtMo.setMoMonth0(order);
//	        			}
//	        		}
//	        	}
//	        	
//	        	for(DetailMo dtMo : detailMarketingOrderListDual) {
//	        		
//	        		if(dtMo.getPartNumber().equals(mn.getPART_NUMBER())) {
//	        			order = dtMo.getTotalAR();
//	        			minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
//        			    
//	        			System.out.println("loop 3 isi mesin untuk produk" + mn.getPART_NUMBER() + "yaitu" + mn.getWORK_CENTER_TEXT());
//
//	        			tempShift = generateFromManualMapping(mn.getPART_NUMBER(), month, year, dtMo.getItemCuring(), mn.getWORK_CENTER_TEXT());
//	        			
//	        			if(tempShift == true) {
//	        				dtMo.setMoMonth0(order);
//	        			}
//	        		}
//	        	}
//	        	
//	        	for(DetailMo dtMo : detailMarketingOrderListBOM) {
//	        		
//	        		if(dtMo.getPartNumber().equals(mn.getPART_NUMBER())) {
//	        			order = dtMo.getTotalAR();
//	        			minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
//	        			
//	        			System.out.println("loop 4 isi mesin untuk produk" + mn.getPART_NUMBER() + "yaitu" + mn.getWORK_CENTER_TEXT());
//
//	        			tempShift = generateFromManualMapping(mn.getPART_NUMBER(), month, year, dtMo.getItemCuring(), mn.getWORK_CENTER_TEXT());
//	        			if(tempShift == true) {
//	        				dtMo.setMoMonth0(order);
//	        			}
//	        		}
//	        	}
	        }
		}
		
		for(DetailMo dtMo : detailMarketingOrderListABFrontRear) {
		System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    			minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListBOMFrontRear) {
			System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    	    	minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListDualFrontRear) {
		System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    	    	minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    	System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListABFrontRear) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
           System.out.println("test 13.1");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);	
		}
		
		for(DetailMo dtMo : detailMarketingOrderListBOMFrontRear) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
           System.out.println("test 13.2");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);
			
		}
		
		for(DetailMo dtMo : detailMarketingOrderListDualFrontRear) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
           System.out.println("test 13.3");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);
			
		}
		
		for(DetailMo dtMo : detailMarketingOrderListAB) {
		System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    			minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListBOM) {
			System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    	    	minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListDual) {
		System.out.println("test 2");
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 3");
		    order = dtMo.getTotalAR();
		   System.out.println("test 4");
    	    oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), dtMo.getItemCuring());
    	   System.out.println("test 5");
    	    if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) { // if 1
    	    	minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
    	    	while (order.intValue() > minProduction.intValue()) {
        	    System.out.println("test 6");
        	    	if(!checkAllActiveMachine()) {
        	    		break;
        	    	}
        	    	System.out.println("test 7");

    	            tempShift = generateFromOldShift(month, year);
    	            
        	       System.out.println("test 8");
        	        if(!tempShift){
        	        	break;
        	        }
        	    }
        	   System.out.println("test 9");
        	    dtMo.setMoMonth0(order);
	        }
    	    machineCuringList = machineCuringListTemp;
		}
		
		for(DetailMo dtMo : detailMarketingOrderListAB) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			int statusPrioritasMesin = 0;
			tempShift = false;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
           System.out.println("test 13.4");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);	
		}
		
		for(DetailMo dtMo : detailMarketingOrderListBOM) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			tempShift = false;
			int statusPrioritasMesin = 0;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
           System.out.println("test 13.5");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);
			
		}
		
		for(DetailMo dtMo : detailMarketingOrderListDual) {
		System.out.println("test 11");
			if(!checkAllActiveMachine()) {
				break;
			}
			tempShift = false;
			int statusPrioritasMesin = 0;
            statusPrioritasMesin = getStatusPrioritasMesin(dtMo, smallOrderLimit);
           System.out.println("test 12");
            order = dtMo.getTotalAR();
			System.out.println("INI TOTAL ORDER AR"+order);
           System.out.println("test 13");
            minProduction = getMinimalProduction(dtMo.getTotalAR(), minA, minB, minC, minD);
	    	while (order.intValue() > minProduction.intValue()) {
            System.out.println("test 14");
            	List<CTCuring> ctCurList = getMachine(statusPrioritasMesin, dtMo.getItemCuring());
	            if(ctCurList.size() > 0) {
	            System.out.println("test 15");
    	            tempShift = generateFromMidMonth(dtMo.getPartNumber(), month, year,  ctCurList);
	            }
	           System.out.println("test 16");
	            if(!tempShift) {
	            System.out.println("test 17");
	            	clearShift(dtMo.getPartNumber());
	            System.out.println("test 18");
	            	if (statusPrioritasMesin == 1) {
    	        		statusPrioritasMesin = 2;
    	            } else if (statusPrioritasMesin == 2) {
    	            	statusPrioritasMesin = 3;
    	            } else if (statusPrioritasMesin == 3) {
    	            	statusPrioritasMesin = 4;
    	            } else if (statusPrioritasMesin == 4) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 12) {
    	            	statusPrioritasMesin = 13;
    	            } else if (statusPrioritasMesin == 13) {
    	            	statusPrioritasMesin = 14;
    	            } else if (statusPrioritasMesin == 14) {
    	            	statusPrioritasMesin = 15;
    	            } else if(statusPrioritasMesin == 15) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 22) {
    	        		statusPrioritasMesin = 23;
    	            } else if (statusPrioritasMesin == 23) {
    	            	statusPrioritasMesin = 24;
    	            } else if (statusPrioritasMesin == 24) {
    	            	statusPrioritasMesin = 25;
    	            } else if (statusPrioritasMesin == 25) {
    	            	statusPrioritasMesin = 99;
    	            } else if (statusPrioritasMesin == 32) {
    	            	statusPrioritasMesin = 33;
    	            } else if (statusPrioritasMesin == 33) {
    	            	statusPrioritasMesin = 34;
    	            } else if (statusPrioritasMesin == 34) {
    	            	statusPrioritasMesin = 35;
    	            } else if(statusPrioritasMesin == 35) {
    	            	statusPrioritasMesin = 2;
    	            }  else if(statusPrioritasMesin == 99) {
    	            	break;
    	            } 
	            }	
            }
	    	machineCuringList = machineCuringListTemp;
            dtMo.setMoMonth0(order);
			
		}
		
		for(ChangeMould obj : changeMouldList) {
			System.out.println(obj.getPartNum() + " " + obj.getChangeDate() + " " + obj.getWct() + " " + obj.getShift());
		}
		System.out.println("ini ab doang");
		for(DetailMo dtm : detailMarketingOrderListAB) {
			if(dtm.getTotalAR().intValue() > 0) {
				System.out.println(dtm.getPartNumber() + " " + dtm.getTotalAR());
			}
		}
		
		System.out.println("ini bom doang");
		for(DetailMo dtm : detailMarketingOrderListBOM) {
			if(dtm.getTotalAR().intValue() > 0) {
				System.out.println(dtm.getPartNumber() + " " + dtm.getTotalAR());
			}
		}
		
		System.out.println("ini dual ");
		for(DetailMo dtm : detailMarketingOrderListDual) {
			if(dtm.getTotalAR().intValue() > 0) {
				System.out.println(dtm.getPartNumber() + " " + dtm.getTotalAR());
			}
		}
		
		return newShiftMonthlyPlan;
	}
	
	public BigDecimal getMinimalProduction(BigDecimal mo, BigDecimal minA, BigDecimal minB, BigDecimal minC, BigDecimal minD) {
	    BigDecimal percentage;
        System.out.println("INI NILAI TOTAL AR"+mo);
	    if (mo.compareTo(BigDecimal.valueOf(2001)) < 0) {
	        percentage = minA;
	    } else if (mo.compareTo(BigDecimal.valueOf(10001)) < 0) {
	        percentage = minB;
	    } else if (mo.compareTo(BigDecimal.valueOf(100001)) < 0) {
	        percentage = minC;
	    } else {
	        percentage = minD;
	    }

	    if (percentage.compareTo(BigDecimal.ZERO) != 0) {
	        return mo.multiply(percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
	    }

	    return BigDecimal.ZERO;
	}
	
	public BigDecimal getMaxProduction(BigDecimal mo, BigDecimal maxA, BigDecimal maxB, BigDecimal maxC, BigDecimal maxD) {
	    BigDecimal percentage;

	    if (mo.compareTo(BigDecimal.valueOf(2001)) < 0) {
	        percentage = maxA;
	    } else if (mo.compareTo(BigDecimal.valueOf(10001)) < 0) {
	        percentage = maxB;
	    } else if (mo.compareTo(BigDecimal.valueOf(100001)) < 0) {
	        percentage = maxC;
	    } else {
	        percentage = maxD;
	    }
	    
	    BigDecimal extra = mo.add(mo.multiply(percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
	    if (percentage.compareTo(BigDecimal.ZERO) != 0) {
	        return extra;
	    }

	    return BigDecimal.ZERO;
	}
	
	public boolean validateChangeMould(Date dateChange, int shift) {
		int i = 0;
		for(ChangeMould obj : endProductList) {
			if(obj.getChangeDate().equals(dateChange) && obj.getShift() == shift) {
				i++;
			}
		}
		if(i < maxChangeMould) {
			return true;
		}
		return false;
	}
	//4338
	
	public boolean generateFromManualMapping(BigDecimal partNum, int month, int year, String itemCuring, String wct) {
		int cav = 0;
	System.out.println("ini minimal bikin nya " + minProduction);
		for (MachineCuring machineCuring : machineCuringListTemp) {
			if(machineCuring.getWORK_CENTER_TEXT().equals(wct)) {
				cav = machineCuring.getCAVITY().intValue();
				List<Map<String, Object>> list = new ArrayList<>();
				if(checkOldShift(itemCuring)) {
				System.out.println("masuk old shift");
					list = dWorkDayHourSpecificRepo.getCuringCapacity(itemCuring , wct, cav, month, year);
				}else {
				System.out.println("masuk old shift2");
					list = dWorkDayHourSpecificRepo.getCuringCapacityChangeMouldFirstDate(itemCuring , wct, cav, month, year);
					addChangeMould(workDayList.get(0).getDATE_WD(), partNum , 1, machineCuring.getWORK_CENTER_TEXT() +  " dari manual mapping");
				}
				if (list != null && !list.isEmpty()) {
					for(Map<String, Object> capacityData : list) {
		    			System.out.println("masuk 8 " + new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()) + " tanggal " + capacityData.get("DATE_WD").toString());
						if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
							BigDecimal tempShift = BigDecimal.ZERO;
							ShiftMonthlyPlan shift = new ShiftMonthlyPlan();
		                    shift.setKAPA_SHIFT_1(BigDecimal.ZERO);
		                    shift.setKAPA_SHIFT_2(BigDecimal.ZERO);
		                    shift.setKAPA_SHIFT_3(BigDecimal.ZERO);
		                    shift.setDATE(parseDate(capacityData.get("DATE_WD").toString()));
		                    shift.setPART_NUMBER(partNum);
		                    shift.setCAVITY(new BigDecimal(cav));
		                    shift.setWORK_CENTER_TEXT(wct);
		                    shift.setCAVITY_USAGE(new BigDecimal(cav));
		                    shift.setSTATUS(BigDecimal.ONE);
		                    shift.setCAVITY_EXIST(BigDecimal.ZERO);
		                    shift.setITEM_CURING(itemCuring);
		                    if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
		                    	shift.setWH_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_HOUR").toString()));
		                        shift.setKAPA_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()));	
		                        order = order.subtract(shift.getKAPA_SHIFT_1());
		                        tempShift = BigDecimal.ONE;
		                    }
		                    if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
		                    	shift.setWH_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_HOUR").toString()));
		                        shift.setKAPA_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_CAPACITY").toString()));	
		                        order = order.subtract(shift.getKAPA_SHIFT_2());
		                        tempShift = BigDecimal.valueOf(2);
		                    }
		                    if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
		                    	shift.setWH_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_HOUR").toString()));
		                        shift.setKAPA_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_CAPACITY").toString()));	
		                        order = order.subtract(shift.getKAPA_SHIFT_3());
		                        tempShift = BigDecimal.valueOf(3);
		                    }
		                    BigDecimal totalKapasitasShift = shift.getKAPA_SHIFT_1()
		                            .add(shift.getKAPA_SHIFT_2())
		                            .add(shift.getKAPA_SHIFT_3());
		                    shift.setTOTAL_KAPA(totalKapasitasShift);
		                    newShiftMonthlyPlan.add(shift);
		        			System.out.println("masuk 9");
		        			if(order.intValue() < minProduction.intValue() && validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
		        				machineCuring.setSTATUS_USAGE(tempShift);
		        				machineCuring.setLAST_UPDATE_DATE(parseDate(capacityData.get("DATE_WD").toString()));
		        				addEndMould(parseDate(capacityData.get("DATE_WD").toString()), partNum , tempShift.intValue(), machineCuring.getWORK_CENTER_TEXT(), 0);
		                    	return true;
		                    }
						}
		    			System.out.println("masuk 10");
					}
					machineCuring.setSTATUS(BigDecimal.ZERO);
					return true;
		        }
				break;
			}
		}
		return false;
	}
	
	public boolean checkOldShift(String itemCuring) {
		oldShiftPlan = shiftMonthlyRepo.findYesterdaysShiftPlan(formatDateToString(workDayList.get(0).getDATE_WD()), itemCuring);
		if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean generateFromMidMonth(BigDecimal partNum, int month, int year, List<CTCuring> ctCurList) {
	System.out.println("Masuk ke mid month0");
		for (CTCuring ctCur : ctCurList) {
		System.out.println("Masuk ke mid month1");
            for (MachineCuring machineCuring : machineCuringListTemp) {
                if (machineCuring.getWORK_CENTER_TEXT().equals(ctCur.getOPERATION_SHORT_TEXT()) && machineCuring.getSTATUS().equals(BigDecimal.ONE)) {
                System.out.println("mesin " + machineCuring.getLAST_UPDATE_DATE() + " " + machineCuring.getSTATUS().intValue());
                	//if(validateChangeMould(machineCuring.getLAST_UPDATE_DATE(), machineCuring.getSTATUS_USAGE().intValue())) {
                		List<Map<String, Object>> list = new ArrayList<>();
                		if(machineCuring.getLAST_UPDATE_DATE() == null) {
                			list = dWorkDayHourSpecificRepo.getCuringCapacityChangeMouldFirstDate(ctCur.getWIP() , ctCur.getOPERATION_SHORT_TEXT(), machineCuring.getCAVITY().intValue(), month, year);
                		}else {
                			list = dWorkDayHourSpecificRepo.getCuringCapacityMidMonth(ctCur.getWIP() , ctCur.getOPERATION_SHORT_TEXT(), machineCuring.getCAVITY().intValue(), formatDateToString(machineCuring.getLAST_UPDATE_DATE()), machineCuring.getSTATUS_USAGE().intValue());	
                		}
                	System.out.println("ukuran list " + list.size() + " " + ctCur.getWIP() + " " + ctCur.getOPERATION_SHORT_TEXT());
    					if (list != null && !list.isEmpty()) {
    					System.out.println("masuk list tidak sama dengan null " + ctCur.getWIP());
    						addChangeMould(machineCuring.getLAST_UPDATE_DATE(), partNum , machineCuring.getSTATUS_USAGE().intValue(), machineCuring.getWORK_CENTER_TEXT() + " dari mid month");
    						for(Map<String, Object> capacityData : list) {
    	            			System.out.println("masuk 8 " + new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()) + " tanggal " + capacityData.get("DATE_WD").toString());
    							if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
    								BigDecimal tempShift = BigDecimal.ZERO;
    								ShiftMonthlyPlan shift = new ShiftMonthlyPlan();
    		                        shift.setKAPA_SHIFT_1(BigDecimal.ZERO);
    		                        shift.setKAPA_SHIFT_2(BigDecimal.ZERO);
    		                        shift.setKAPA_SHIFT_3(BigDecimal.ZERO);
    		                        shift.setDATE(parseDate(capacityData.get("DATE_WD").toString()));
    		                        shift.setPART_NUMBER(partNum);
    		                        shift.setCAVITY(machineCuring.getCAVITY());
    		                        shift.setWORK_CENTER_TEXT(machineCuring.getWORK_CENTER_TEXT());
    		                        shift.setCAVITY_USAGE(machineCuring.getCAVITY());
    		                        shift.setCAVITY_EXIST(BigDecimal.ZERO);
    		                        shift.setITEM_CURING(ctCur.getWIP());
    		                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
    		                        	shift.setWH_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_HOUR").toString()));
    			                        shift.setKAPA_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()));	
    			                        order = order.subtract(shift.getKAPA_SHIFT_1());
    			                        tempShift = BigDecimal.ONE;
    		                        }
    		                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
    		                        	shift.setWH_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_HOUR").toString()));
    			                        shift.setKAPA_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_CAPACITY").toString()));	
    			                        order = order.subtract(shift.getKAPA_SHIFT_2());
    			                        tempShift = BigDecimal.valueOf(2);
    		                        }
    		                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
    		                        	shift.setWH_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_HOUR").toString()));
    			                        shift.setKAPA_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_CAPACITY").toString()));	
    			                        order = order.subtract(shift.getKAPA_SHIFT_3());
    			                        tempShift = BigDecimal.valueOf(3);
    		                        }
    		                        BigDecimal totalKapasitasShift = shift.getKAPA_SHIFT_1()
    		                                .add(shift.getKAPA_SHIFT_2())
    		                                .add(shift.getKAPA_SHIFT_3());
    		                        shift.setTOTAL_KAPA(totalKapasitasShift);
    		                        newShiftMonthlyPlan.add(shift);
    		            			System.out.println("masuk 9");
    		            			if(order.intValue() < minProduction.intValue() && validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
    		            				machineCuring.setSTATUS_USAGE(tempShift);
    		            				machineCuring.setLAST_UPDATE_DATE(parseDate(capacityData.get("DATE_WD").toString()));
    		            				addEndMould(parseDate(capacityData.get("DATE_WD").toString()), partNum , tempShift.intValue(), machineCuring.getWORK_CENTER_TEXT(), 1);
    		                        	return true;
    		                        }
    							}
    	            			System.out.println("masuk 10");
    						}
    					System.out.println("habis " + machineCuring.getWORK_CENTER_TEXT());
							machineCuring.setSTATUS(BigDecimal.ZERO);
    						return true;
                        //}
                	}else {
                		machineCuring.setSTATUS(BigDecimal.ZERO);
                	}
                	
                }
            }
        }
		
		return false;
	}
	
	public boolean generateFromOldShift(int month, int year) {
		if (oldShiftPlan != null && !oldShiftPlan.isEmpty()) {
			for (ShiftMonthlyPlan shiftPlan : oldShiftPlan) {
                for (MachineCuring machineCuring : machineCuringListTemp) {
                    if (shiftPlan.getWORK_CENTER_TEXT().equals(machineCuring.getWORK_CENTER_TEXT()) && machineCuring.getSTATUS().equals(BigDecimal.ONE) && (machineCuring.getLAST_UPDATE_DATE() == null)) {
                    	List<Map<String, Object>> list = dWorkDayHourSpecificRepo.getCuringCapacity(shiftPlan.getITEM_CURING() , shiftPlan.getWORK_CENTER_TEXT(), shiftPlan.getCAVITY().intValue(), month, year);
						if (list != null && !list.isEmpty()) {
							for(Map<String, Object> capacityData : list) {
		            			System.out.println("masuk 8 " + new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()) + " tanggal " + capacityData.get("DATE_WD").toString());
								if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
									BigDecimal tempShift = BigDecimal.ZERO;
									ShiftMonthlyPlan shift = new ShiftMonthlyPlan();
			                        shift.setKAPA_SHIFT_1(BigDecimal.ZERO);
			                        shift.setKAPA_SHIFT_2(BigDecimal.ZERO);
			                        shift.setKAPA_SHIFT_3(BigDecimal.ZERO);
			                        shift.setDATE(parseDate(capacityData.get("DATE_WD").toString()));
			                        shift.setPART_NUMBER(shiftPlan.getPART_NUMBER());
			                        shift.setCAVITY(shiftPlan.getCAVITY());
			                        shift.setWORK_CENTER_TEXT(shiftPlan.getWORK_CENTER_TEXT());
			                        shift.setCAVITY_USAGE(shiftPlan.getCAVITY());
			                        shift.setSTATUS(BigDecimal.ONE);
			                        shift.setCAVITY_EXIST(BigDecimal.ZERO);
			                        shift.setITEM_CURING(shiftPlan.getITEM_CURING());
			                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
			                        	shift.setWH_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_HOUR").toString()));
				                        shift.setKAPA_SHIFT_1(new BigDecimal(capacityData.get("SHIFT1_CAPACITY").toString()));	
				                        order = order.subtract(shift.getKAPA_SHIFT_1());
				                        tempShift = BigDecimal.ONE;
			                        }
			                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
			                        	shift.setWH_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_HOUR").toString()));
				                        shift.setKAPA_SHIFT_2(new BigDecimal(capacityData.get("SHIFT2_CAPACITY").toString()));	
				                        order = order.subtract(shift.getKAPA_SHIFT_2());
				                        tempShift = BigDecimal.valueOf(2);
			                        }
			                        if(order.intValue() > minProduction.intValue() || !validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
			                        	shift.setWH_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_HOUR").toString()));
				                        shift.setKAPA_SHIFT_3(new BigDecimal(capacityData.get("SHIFT3_CAPACITY").toString()));	
				                        order = order.subtract(shift.getKAPA_SHIFT_3());
				                        tempShift = BigDecimal.valueOf(3);
			                        }
			                        BigDecimal totalKapasitasShift = shift.getKAPA_SHIFT_1()
			                                .add(shift.getKAPA_SHIFT_2())
			                                .add(shift.getKAPA_SHIFT_3());
			                        shift.setTOTAL_KAPA(totalKapasitasShift);
			                        newShiftMonthlyPlan.add(shift);
			            			System.out.println("masuk 9");
			            			if(order.intValue() < minProduction.intValue() && validateChangeMould(parseDate(capacityData.get("DATE_WD").toString()), 1)) {
			            				machineCuring.setSTATUS_USAGE(tempShift);
			            				machineCuring.setLAST_UPDATE_DATE(parseDate(capacityData.get("DATE_WD").toString()));
			            				addEndMould(parseDate(capacityData.get("DATE_WD").toString()), shiftPlan.getPART_NUMBER() , tempShift.intValue(), machineCuring.getWORK_CENTER_TEXT(), 0);
			                        	return true;
			                        }
								}
		            			System.out.println("masuk 10");
							}
							machineCuring.setSTATUS(BigDecimal.ZERO);
							return true;
	                    }
                    }
                }
            }
		}
		
		return false;
	}
  	
	public void addChangeMould(Date cahangeDate, BigDecimal partNum, int shift, String wct) {
		ChangeMould obj = new ChangeMould();
		obj.setChangeDate(cahangeDate);
		obj.setPartNum(partNum);
		obj.setShift(shift);
		obj.setWct(wct);
		changeMouldList.add(obj);
	}
	
	public void addEndMould(Date cahangeDate, BigDecimal partNum, int shift, String wct, int status) {
		ChangeMould obj = new ChangeMould();
		obj.setChangeDate(cahangeDate);
		obj.setPartNum(partNum);
		obj.setShift(shift);
		obj.setWct(wct);
		obj.setStatus(status);
		endProductList.add(obj);
	}
	
	public boolean checkAllActiveMachine() {
		for(MachineCuring mc : machineCuringList) {
			if(mc.getSTATUS().equals(BigDecimal.ONE)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void getDataHeader(int month, int year) {
		machineCuringList = machineCuringRepo.findMachineCuringActive();
		machineCuringListTemp = machineCuringRepo.findMachineCuringActive();
		machineProductList = machineProductRepo.findAll();
		
		System.out.println("ukuran list mesin " + machineCuringList.size() + " " + machineCuringListTemp.size());
		 
    	System.out.println(machineCuringList.size());
    	workDayList = workDayRepo.findByMonthYear(month, year); //Flowchart 3
		
    	smallOrderLimit = new BigDecimal(settingRepo.findSmallOrderLimit().getSETTING_VALUE());
    	
    	List<MarketingOrder> marketingOrderList = marketingOrderRepo.findByMonthYear(month, year); //flowchart 4

    	//flowchart 5 6 7 8
    	System.out.println("Done flow 5");
    	System.out.println(marketingOrderList.get(0).getMoId() + " " + marketingOrderList.get(1).getMoId());
    	
    	System.out.println("check");
    	List<Map<String, Object>> detailMarkOrderListAB = new ArrayList<>();
    	System.out.println("check1");
    	List<Map<String, Object>> detailMarkOrderListBOM = new ArrayList<>();
    	System.out.println("check2");
    	List<Map<String, Object>> detailMarkOrderListDual = new ArrayList<>();
    	System.out.println("check3");
    	List<Map<String, Object>> detailMarkOrderListABFrontRear = new ArrayList<>();
    	System.out.println("check1");
    	List<Map<String, Object>> detailMarkOrderListBOMFrontRear = new ArrayList<>();
    	System.out.println("check2");
    	List<Map<String, Object>> detailMarkOrderListDualFrontRear = new ArrayList<>();

    	detailMarkOrderListAB = detailMarketingOrderRepo.findByMoIdSortProductTypeAbNotFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
    	System.out.println("check4");
    	detailMarkOrderListBOM = detailMarketingOrderRepo.findByMoIdSortProductTypeBomNotFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
    	System.out.println("check5.1");
    	detailMarkOrderListDual = detailMarketingOrderRepo.findByMoIdSortProductTypeBomAbNotFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
    	
    	detailMarkOrderListABFrontRear = detailMarketingOrderRepo.findByMoIdSortProductTypeAbFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
        System.out.println("check4.2");
    	detailMarkOrderListBOMFrontRear = detailMarketingOrderRepo.findByMoIdSortProductTypeBomAbFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
    	System.out.println("check5");
    	detailMarkOrderListDualFrontRear = detailMarketingOrderRepo.findByMoIdSortProductTypeBomFrontRear(marketingOrderList.get(0).getMoId(), marketingOrderList.get(1).getMoId());
    	System.out.println("check6");
    	System.out.println("ukuran  " + detailMarkOrderListAB.size() + " " + detailMarkOrderListBOM.size() + " " + detailMarkOrderListDual.size()+ " " + detailMarkOrderListABFrontRear.size() + " " + detailMarkOrderListBOMFrontRear.size() + " " + detailMarkOrderListDualFrontRear.size());
    	System.out.println("Done flow 6");
        for (Map<String, Object> map : detailMarkOrderListAB) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list ab");
            System.out.println(obj);
            detailMarketingOrderListAB.add(obj);
        }
        
        for (Map<String, Object> map : detailMarkOrderListBOM) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list bom");
            System.out.println(obj);
            detailMarketingOrderListBOM.add(obj);
        }
        
        for (Map<String, Object> map : detailMarkOrderListDual) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list dual");
            System.out.println(obj);
            detailMarketingOrderListDual.add(obj);
        }
        
        for (Map<String, Object> map : detailMarkOrderListABFrontRear) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list ab frontrear");
            System.out.println(obj);
            detailMarketingOrderListABFrontRear.add(obj);
        }
        
        for (Map<String, Object> map : detailMarkOrderListBOMFrontRear) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list bom front rear");
            System.out.println(obj);
            detailMarketingOrderListBOMFrontRear.add(obj);
        }
        
        for (Map<String, Object> map : detailMarkOrderListDualFrontRear) {
        	DetailMo obj = new DetailMo();
            obj.setPpd((BigDecimal) map.get("PPD"));
            obj.setLowerConstant((BigDecimal) map.get("LOWER_CONSTANT"));
            obj.setMoMonth0((BigDecimal) map.get("MO_MONTH_0"));
            obj.setProductCategory((String) map.get("PRODUCT_CATEGORY"));
            obj.setMachineType((String) map.get("MACHINE_TYPE"));
            obj.setItemExt((String) map.get("ITEM_EXT"));
            obj.setExtDescription((String) map.get("EXT_DESCRIPTION"));
            obj.setMinOrder((BigDecimal) map.get("MIN_ORDER"));
            obj.setSizeId((String) map.get("SIZE_ID"));
            obj.setDetailId((BigDecimal) map.get("DETAIL_ID"));
            obj.setRim((BigDecimal) map.get("RIM"));
            obj.setItemCuring((String) map.get("ITEM_CURING"));
			obj.setTotalAR((BigDecimal) map.get("TOTAL_AR"));
            obj.setWibTube((String) map.get("WIB_TUBE"));
            obj.setPatternId((String) map.get("PATTERN_ID"));
            obj.setItemAssy((String) map.get("ITEM_ASSY"));
            obj.setCategory((String) map.get("CATEGORY"));
            obj.setCav((BigDecimal) map.get("CAV"));
            obj.setQtyPerRak((BigDecimal) map.get("QTY_PER_RAK"));
            obj.setDescription((String) map.get("DESCRIPTION"));
            obj.setQtyPerMould((BigDecimal) map.get("QTY_PER_MOULD"));
            obj.setMoId((String) map.get("MO_ID"));
            obj.setInitialStock((BigDecimal) map.get("INITIAL_STOCK"));
            obj.setCapacity((BigDecimal) map.get("CAPACITY"));
            obj.setProductType((String) map.get("PRODUCT_TYPE"));
            obj.setPartNumber((BigDecimal) map.get("PART_NUMBER"));
            obj.setUpperConstant((BigDecimal) map.get("UPPER_CONSTANT"));
            obj.setMaxCapMonth0((BigDecimal) map.get("MAX_CAP_MONTH_0"));
            System.out.println("Done list dual frontrear");
            System.out.println(obj);
            detailMarketingOrderListDualFrontRear.add(obj);
        }
	}
	    
	public void clearShift(BigDecimal partNum) {
	System.out.println("masuk clear shift");
		List<String> listWct = new ArrayList<>(); // Gunakan ArrayList untuk penambahan elemen dinamis
		Iterator<ChangeMould> iterator = changeMouldList.iterator();
		while (iterator.hasNext()) {
		    ChangeMould cm = iterator.next();
		    if (cm.getPartNum().equals(partNum)) {
		        listWct.add(cm.getWct());
		        iterator.remove(); // Gunakan iterator untuk menghapus elemen dengan aman
		    }
		}
		
		for (int i = 0; i < changeMouldList.size(); i++) {
		    ChangeMould cm = changeMouldList.get(i);
		    if (cm.getPartNum().equals(partNum)) {
		        listWct.add(cm.getWct());
		        changeMouldList.remove(i);
		        i--; // Kurangi indeks karena elemen dihapus
		    }
		}
		
		for (int i = 0; i < endProductList.size(); i++) {
		    ChangeMould cm = endProductList.get(i);
		    if (cm.getPartNum().equals(partNum) && cm.getStatus() == 1) {
		        changeMouldList.remove(i);
		        i--; // Kurangi indeks karena elemen dihapus
		    }
		}
		
		machineCuringListTemp = machineCuringList;
		
		
		Iterator<ShiftMonthlyPlan> iteratorrr = newShiftMonthlyPlan.iterator();
		while (iterator.hasNext()) {
		    ShiftMonthlyPlan shf = iteratorrr.next();
		    if (shf.getPART_NUMBER().equals(partNum) && shf.getSTATUS().compareTo(BigDecimal.ONE) != 0) {
		    	order = order.add(shf.getTOTAL_KAPA());
		        iterator.remove(); // Gunakan iterator untuk menghapus elemen
		    }
		}
	}

    
    public void saveShift(List<ShiftMonthlyPlan> newShiftList) {
    	for(ShiftMonthlyPlan sf : newShiftList) {
    		shiftMonthlyRepo.save(sf);
    	}
    }
    
    public List<ShiftMonthlyPlan> flowTwentyFive(List<ShiftMonthlyPlan> sfList, List<MachineCuring> machineCuringList){
    	
    	
    	return sfList;
    }
    
    public List<CTCuring> getMachine(int status, String itemCuring) {
        List<CTCuring> ctCuringList = new ArrayList<>();

        if (status == 1) {
            ctCuringList = ctCuringRepo.findCBuildingMachineByItemCuring(itemCuring);
        } else if (status == 2) {
            ctCuringList = ctCuringRepo.findMachineByGCDBuilding(itemCuring);
        } else if (status == 3) {
            ctCuringList = ctCuringRepo.findMachineByABBuilding(itemCuring);
        } else if(status == 4) {
            ctCuringList = ctCuringRepo.findMachineByHBuilding(itemCuring);
        } else if(status == 12) { 
            ctCuringList = ctCuringRepo.findMachineABByGCDBuilding(itemCuring);
        } else if(status == 13) { 
            ctCuringList = ctCuringRepo.findMachineABByABBuilding(itemCuring);
        } else if(status == 14) { 
            ctCuringList = ctCuringRepo.findMachineABByHBuilding(itemCuring);
        } else if(status == 15) { // ALL A/B
        	ctCuringList = ctCuringRepo.findMachineABAllBuilding(itemCuring);
        } else if(status == 22) { 
            ctCuringList = ctCuringRepo.findMachineBOMByGCDBuilding(itemCuring);
        } else if(status == 23) { 
            ctCuringList = ctCuringRepo.findMachineBOMByABBuilding(itemCuring);
        } else if(status == 24) { 
            ctCuringList = ctCuringRepo.findMachineBOMByHBuilding(itemCuring);
        } else if(status == 25) { // ALL A/B
            ctCuringList = ctCuringRepo.findMachineBOMAllBuilding(itemCuring);
        } else if(status == 32) { 
            ctCuringList = ctCuringRepo.findMachine2CavByGCDBuilding(itemCuring);
        } else if(status == 33) { 
            ctCuringList = ctCuringRepo.findMachine2CavByABBuilding(itemCuring);
        } else if(status == 34) { 
            ctCuringList = ctCuringRepo.findMachine2CavByHBuilding(itemCuring);
        } else if(status == 35) { // ALL 2CAV
        	ctCuringList = ctCuringRepo.findMachine2CavAllBuilding(itemCuring);
        } else if(status == 99) {
        	ctCuringList = ctCuringRepo.findMachineByItemCuring(itemCuring);
        }

        return ctCuringList;
    }
    
    public int getStatusPrioritasMesin(DetailMo dtMo, BigDecimal smallOrderLimit) {
    	if ("TT".equals(dtMo.getProductType()) && dtMo.getRim().compareTo(BigDecimal.valueOf(14)) == 0) { // flowchart 11
            System.out.println("Done flow 11");
            return 1;
        } else if ("TT".equals(dtMo.getProductType())) { // Flowchart 14
            System.out.println("Done flow 14");
            return 12;
        } else if ("SINGLE COMPOUND".equals(dtMo.getExtDescription())) { // Flowchart 12
            System.out.println("Done flow 12");
            return 2;
        } else if ("TL".equals(dtMo.getProductType())) { // Flowchart 13
            System.out.println("Done flow 13");
            return 22;
        }
    	
        if (dtMo.getTotalAR().compareTo(smallOrderLimit) <= 0) { // flowchart 15
            System.out.println("Done flow 14");
            return 32;
        }
    	return 0;
    }
    
    public Date parseDate(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = dateFormat.parse(dateString); 
        } catch (Exception e) {
            e.printStackTrace();  
        }
        System.out.println("ini date " + date);
        return date;
    }
    
    public Date normalizeDate(Date date) {
        if (date == null) {
            return null; // Kembalikan null jika input null
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return calendar.getTime(); // Kembalikan tanggal yang dinormalisasi
    }
    	
    public String getDayNameFromDate(Date date) {
        if (date == null) {
            return null; // Kembalikan null jika input null
        }
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
       System.out.println("hari " + dayFormat.format(date));
        return dayFormat.format(date); // Kembalikan nama hari dalam string
    }
    
    public Date getNextDay(Date date) {
        // Inisialisasi Calendar dengan tanggal yang diberikan
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Tambah satu hari
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Mengembalikan tanggal berikutnya
        return calendar.getTime();
    }
    
    public Date getPreviousDay(Date date) {
        // Inisialisasi Calendar dengan tanggal yang diberikan
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Kurangi satu hari
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        // Mengembalikan tanggal sebelumnya
        return calendar.getTime();
    }
    
    public String formatDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
    
    class PartWct{
    	private String wct;
    	private BigDecimal partNum;
		public String getWct() {
			return wct;
		}
		public void setWct(String wct) {
			this.wct = wct;
		}
		public BigDecimal getPartNum() {
			return partNum;
		}
		public void setPartNum(BigDecimal partNum) {
			this.partNum = partNum;
		}
    }
    
    public class ChangeMould{
    	private BigDecimal partNum;
    	private String wct;
    	private Date changeDate;
    	private int shift;
    	private int status;
    	
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public BigDecimal getPartNum() {
			return partNum;
		}
		public void setPartNum(BigDecimal partNum) {
			this.partNum = partNum;
		}
		public String getWct() {
			return wct;
		}
		public void setWct(String wct) {
			this.wct = wct;
		}
		public Date getChangeDate() {
			return changeDate;
		}
		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}
		public int getShift() {
			return shift;
		}
		public void setShift(int shift) {
			this.shift = shift;
		}
    	
    }
    
    class TempChangeMould{
    	private Date changeDate;
    	private int shift;
    	private int total;
		public Date getChangeDate() {
			return changeDate;
		}
		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}
		public int getShift() {
			return shift;
		}
		public void setShift(int shift) {
			this.shift = shift;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
    }
    
    class TempOrder{
    	private BigDecimal pratNum;
    	private BigDecimal marketingOrder;
    	private String itemCur;
		public TempOrder() {
		}
		
		public TempOrder(BigDecimal pratNum, BigDecimal marketingOrder, String itemCur) {
			this.pratNum = pratNum;
			this.marketingOrder = marketingOrder;
			this.itemCur = itemCur;
		}

		public BigDecimal getPratNum() {
			return pratNum;
		}
		public void setPratNum(BigDecimal pratNum) {
			this.pratNum = pratNum;
		}
		public BigDecimal getMarketingOrder() {
			return marketingOrder;
		}
		public void setMarketingOrder(BigDecimal marketingOrder) {
			this.marketingOrder = marketingOrder;
		}
		public String getItemCur() {
			return itemCur;
		}
		public void setItemCur(String itemCur) {
			this.itemCur = itemCur;
		}
    	
    }
    
    public class DetailMo {
        private BigDecimal ppd;
        private BigDecimal lowerConstant;
        private BigDecimal moMonth0;
        private BigDecimal totalAR;
        private String productCategory;
        private String machineType;
        private String itemExt;
        private String extDescription;
        private BigDecimal minOrder;
        private String sizeId;
        private BigDecimal detailId;
        private BigDecimal rim;
        private String itemCuring;
        private String wibTube;
        private String patternId;
        private String itemAssy;
        private String category;
        private BigDecimal cav;
        private BigDecimal qtyPerRak;
        private String description;
        private BigDecimal qtyPerMould;
        private String moId;
        private BigDecimal initialStock;
        private BigDecimal capacity;
        private String productType;
        private BigDecimal partNumber;
        private BigDecimal upperConstant;
        private BigDecimal maxCapMonth0;
        private BigDecimal productionLimit;

        // Getter dan Setter untuk semua properti
        public BigDecimal getPpd() { return ppd; }
        public void setPpd(BigDecimal ppd) { this.ppd = ppd; }
        public BigDecimal getLowerConstant() { return lowerConstant; }
        public void setLowerConstant(BigDecimal lowerConstant) { this.lowerConstant = lowerConstant; }
        public BigDecimal getMoMonth0() { return moMonth0; }
        public void setMoMonth0(BigDecimal moMonth0) { this.moMonth0 = moMonth0; }
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        public String getMachineType() { return machineType; }
        public void setMachineType(String machineType) { this.machineType = machineType; }
        public String getItemExt() { return itemExt; }
        public void setItemExt(String itemExt) { this.itemExt = itemExt; }
        public String getExtDescription() { return extDescription; }
        public void setExtDescription(String extDescription) { this.extDescription = extDescription; }
        public BigDecimal getMinOrder() { return minOrder; }
        public void setMinOrder(BigDecimal minOrder) { this.minOrder = minOrder; }
        public String getSizeId() { return sizeId; }
        public void setSizeId(String sizeId) { this.sizeId = sizeId; }
        public BigDecimal getDetailId() { return detailId; }
        public void setDetailId(BigDecimal detailId) { this.detailId = detailId; }
        public BigDecimal getRim() { return rim; }
        public void setRim(BigDecimal rim) { this.rim = rim; }
        public String getItemCuring() { return itemCuring; }
        public void setItemCuring(String itemCuring) { this.itemCuring = itemCuring; }
        public String getWibTube() { return wibTube; }
        public void setWibTube(String wibTube) { this.wibTube = wibTube; }
        public String getPatternId() { return patternId; }
        public void setPatternId(String patternId) { this.patternId = patternId; }
        public String getItemAssy() { return itemAssy; }
        public void setItemAssy(String itemAssy) { this.itemAssy = itemAssy; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public BigDecimal getCav() { return cav; }
        public void setCav(BigDecimal cav) { this.cav = cav; }
        public BigDecimal getQtyPerRak() { return qtyPerRak; }
        public void setQtyPerRak(BigDecimal qtyPerRak) { this.qtyPerRak = qtyPerRak; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getQtyPerMould() { return qtyPerMould; }
        public void setQtyPerMould(BigDecimal qtyPerMould) { this.qtyPerMould = qtyPerMould; }
        public String getMoId() { return moId; }
        public void setMoId(String moId) { this.moId = moId; }
        public BigDecimal getInitialStock() { return initialStock; }
        public void setInitialStock(BigDecimal initialStock) { this.initialStock = initialStock; }
        public BigDecimal getCapacity() { return capacity; }
        public void setCapacity(BigDecimal capacity) { this.capacity = capacity; }
        public String getProductType() { return productType; }
        public void setProductType(String productType) { this.productType = productType; }
        public BigDecimal getPartNumber() { return partNumber; }
        public void setPartNumber(BigDecimal partNumber) { this.partNumber = partNumber; }
        public BigDecimal getUpperConstant() { return upperConstant; }
        public void setUpperConstant(BigDecimal upperConstant) { this.upperConstant = upperConstant; }
        public BigDecimal getMaxCapMonth0() { return maxCapMonth0; }
        public void setMaxCapMonth0(BigDecimal maxCapMonth0) { this.maxCapMonth0 = maxCapMonth0; }
        
        public BigDecimal getTotalAR() {
			return totalAR;
		}
		public void setTotalAR(BigDecimal totalAR) {
			this.totalAR = totalAR;
		}
		public BigDecimal getProductionLimit() {
			return productionLimit;
		}
		public void setProductionLimit(BigDecimal productionLimit) {
			this.productionLimit = productionLimit;
		}
		@Override
        public String toString() {
            return "CustomObject{" +
                    "ppd=" + ppd +
                    ", lowerConstant='" + lowerConstant + '\'' +
                    ", moMonth0=" + moMonth0 +
                    ", productCategory='" + productCategory + '\'' +
                    ", machineType='" + machineType + '\'' +
                    ", itemExt='" + itemExt + '\'' +
                    ", extDescription='" + extDescription + '\'' +
                    ", minOrder=" + minOrder +
                    ", sizeId='" + sizeId + '\'' +
                    ", detailId=" + detailId +
                    ", rim=" + rim +
                    ", itemCuring='" + itemCuring + '\'' +
                    ", wibTube='" + wibTube + '\'' +
                    ", patternId='" + patternId + '\'' +
                    ", itemAssy='" + itemAssy + '\'' +
                    ", category='" + category + '\'' +
                    ", cav=" + cav +
                    ", qtyPerRak=" + qtyPerRak +
                    ", description='" + description + '\'' +
                    ", qtyPerMould=" + qtyPerMould +
                    ", moId='" + moId + '\'' +
                    ", initialStock=" + initialStock +
                    ", capacity=" + capacity +
                    ", productType='" + productType + '\'' +
                    ", partNumber='" + partNumber + '\'' +
                    ", upperConstant='" + upperConstant + '\'' +
                    ", totalAR='" + totalAR + '\'' +
                    ", maxCapMonth0=" + maxCapMonth0 +
                    '}';
        }
    }

    private EntityManager entityManager;

    public boolean callSpBuatMp9WithOutput(String jsonInput) {
        try {
            // Enable DBMS_OUTPUT with a large buffer
            entityManager.createNativeQuery("BEGIN DBMS_OUTPUT.ENABLE(1000000); END;").executeUpdate();
            
            // Call your procedure
            entityManager.createNativeQuery("BEGIN SP_BUAT_MP_9(:jsonInput); END;")
                .setParameter("jsonInput", jsonInput)
                .executeUpdate();
            
            // Check if there's any DBMS_OUTPUT
            StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("GET_DBMS_OUTPUT")
                .registerStoredProcedureParameter(1, Class.class, ParameterMode.REF_CURSOR);
            
            query.execute();
            
            // Get the result set from the stored procedure
            List<?> results = query.getResultList();
            
            // If there are results, return true
            return !results.isEmpty();
            
        } catch (Exception e) {
            // Log the error if needed
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Map<String, Object>> notificationMp(String inputJson) throws Exception {
        JsonNode root = objectMapper.readTree(inputJson);
        System.out.println("Parsed JSON: " + inputJson);
		ObjectNode transformed = objectMapper.createObjectNode();

		// Transform MO_ID array into individual keys
		JsonNode moIds = root.get("MO_ID");
		if (moIds != null && moIds.isArray()) {
			for (int i = 0; i < moIds.size(); i++) {
				transformed.put("MO_ID" + (i + 1), moIds.get(i).asText());
			}
		}
		
        List<String> moIdsS = new ArrayList<>();
        for (JsonNode moIdNode : root.path("MO_ID")) {
            if (moIdNode != null && !moIdNode.isNull()) {
                moIdsS.add(moIdNode.asText());
            }
        }
    	return monthlyPlanNewRepo.findTotalPlanByMoIds(moIdsS);
    }
    
    public List<Map<String, Object>> generateMp(String inputJson) throws Exception {
        try {
            JsonNode root = objectMapper.readTree(inputJson);
            System.out.println("Parsed JSON: " + inputJson);
			ObjectNode transformed = objectMapper.createObjectNode();

			// Transform MO_ID array into individual keys
			JsonNode moIds = root.get("MO_ID");
			if (moIds != null && moIds.isArray()) {
				for (int i = 0; i < moIds.size(); i++) {
					transformed.put("MO_ID" + (i + 1), moIds.get(i).asText());
				}
			}
			
            List<String> moIdsS = new ArrayList<>();
            for (JsonNode moIdNode : root.path("MO_ID")) {
                if (moIdNode != null && !moIdNode.isNull()) {
                    moIdsS.add(moIdNode.asText());
                }
            }

			   System.out.println("Deleting Log Start");
               monthlyPlanNewRepo.deleteLog();
			   System.out.println("Deleting Log Done");

			for (String moId : moIdsS) {
               System.out.println("Deleting Total Plan by MO_ID: " + moId);
               totalPlanRepo.deleteByMOID(moId);
			}
			
			String transformedJson = objectMapper.writeValueAsString(transformed);
			System.out.println("Transformed JSON: " + transformedJson);

			monthlyPlanNewRepo.saveTotalPlan(transformedJson);
			System.out.println("hitung mould");
			monthlyPlanNewRepo.hitungMould(transformedJson);
			JsonNode cheatingID = root.get("CHEATING_ID");
			transformed.put("CHEATING_ID",cheatingID.asText());
			String withCheatingMO = objectMapper.writeValueAsString(transformed);
			System.out.println("Transformed JSON with cheating: " + withCheatingMO );
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    // Future<?> future = executor.submit(() -> {
		    	monthlyPlanNewRepo.callGenerateMp1(withCheatingMO);
		    //     return null;
		    // });
		    
		    // try {
		    //     future.get(10, TimeUnit.MINUTES);
		    // } catch (TimeoutException e) {
		    //     future.cancel(true);
		    // } catch (ExecutionException | InterruptedException e) {
		    //     e.printStackTrace();
		    // } finally {
		    //     executor.shutdownNow();
		    // }
		
			BigDecimal version = totalPlanRepo.getNewestVersion(moIdsS.get(1).toString(),moIdsS.get(0).toString());


            // // Get summary
             System.out.println("get summary");
			List<Map<String, Object>> dataDetailMp = monthlyPlanNewRepo.getMonthlyPlanSummaryByMoIds(moIdsS,version);

            return dataDetailMp;

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            throw e;
        }
    }

    
    
    public List<Map<String, Object>> getSummaryByMoIds(List<String> moIds) {
//        return monthlyPlanNewRepo.getMonthlyPlanSummaryByMoIds(moIds);
    	return null;
    }

	public Response exportExcelR(int month, int year, int limitChange,BigDecimal versionMO,BigDecimal versionGenerate) {

    	YearMonth yearMonth = YearMonth.of(year,month);
    	LocalDate startLocal = yearMonth.atDay(1);
    	LocalDate endLocal = yearMonth.atEndOfMonth();

        String yearMonthStr = String.format("%04d%02d", year, month);
    	List<MarketingOrder> top2 = marketingOrderRepo
    	        .findTop2ByYearMonth(yearMonthStr,versionMO);

    	List<String> moids = new ArrayList<>(); 

    	for (MarketingOrder buffer : top2) {
    	    System.out.println(buffer.getMoId());	
    	    moids.add(buffer.getMoId());
    	}
    	
		BigDecimal version;
		if (versionGenerate.compareTo(BigDecimal.ZERO) == 0) {
			version = totalPlanRepo.getNewestVersion(moids.get(1).toString(), moids.get(0).toString());
		} else {
			version = versionGenerate;
		}
		System.out.println("Using version: " + version);

	   	List<MonthlyPlanningNew> shiftMonthlyPlan = monthlyPlanNewRepo.findByMoIdInAndVersion(moids, version);
	 // Step 1: Sort the list by getDateMp()
	   	shiftMonthlyPlan.sort(Comparator.comparing(MonthlyPlanningNew::getDateMp));

	   	// Step 2: Loop through the sorted list
	   	for (MonthlyPlanningNew plan : shiftMonthlyPlan) {
	   	    System.out.println("Date: " + plan.getDateMp());
	   	    // add your logic here
	   	}
	   	
	   	List<Map<String, Object>> resultList = new ArrayList<>();
	   	
	   	List<Map<String, Object>> groupedByDateAndWct = shiftMonthlyPlan.stream()
	   		    .collect(Collectors.groupingBy(plan -> {
	   		        LocalDate date = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	   		        String wct = plan.getWct();
	   		        return date + "|" + wct; // Composite key
	   		    }))
	   		    .entrySet().stream()
	   		    .map(entry -> {
	   		        String[] keyParts = entry.getKey().split("\\|");
	   		        Map<String, Object> map = new LinkedHashMap<>();
	   		        map.put("date", keyParts[0]);
	   		        map.put("wct", keyParts[1]);
	   		        map.put("entries", entry.getValue());
	   		        return map;
	   		    })
	   		    .sorted(Comparator.comparing(map ->
	   		        LocalDate.parse((String) map.get("date"))
	   		    ))
	   		    .collect(Collectors.toList());


	   	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    LocalDate currentDate = LocalDate.parse((String) grouped.get("date"), formatter);
	   	    String currentWct = (String) grouped.get("wct");
	   	    LocalDate nextDate = currentDate.plusDays(1);

	   	    List<MonthlyPlanningNew> nextEntries = shiftMonthlyPlan.stream()
	   	        .filter(plan -> {
	   	            LocalDate planDate = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	   	            return planDate.equals(nextDate) && plan.getWct().equals(currentWct);
	   	        })
	   	        .collect(Collectors.toList());

	   	    grouped.put("nextEntries", nextEntries);
	   	    
		   	 boolean coupled = false;
	
		   	if (nextEntries.size() > 1) {
		   	    String firstItem = nextEntries.get(0).getItemCuring();
		   	    coupled = nextEntries.stream()
		   	        .allMatch(e -> e.getItemCuring().equals(firstItem));
		   	}
	
		   	grouped.put("coupled", coupled);
	   	}
	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");
	   	    List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");
	   	    
	   	    Set<String> currentItems = entries.stream()
	   	        .map(MonthlyPlanningNew::getItemCuring)
	   	        .collect(Collectors.toSet());
	
	   	    Set<String> nextItems = nextEntries.stream()
	   	        .map(MonthlyPlanningNew::getItemCuring)
	   	        .collect(Collectors.toSet());
	
	   	    Set<String> sameItemCuring = new HashSet<>(currentItems);
	   	    sameItemCuring.retainAll(nextItems); // keep only common elements
	
	   	    Set<String> addedItemCuring = new HashSet<>(nextItems);
	   	    addedItemCuring.removeAll(currentItems); // items only in next
	
	   	    Set<String> removedItemCuring = new HashSet<>(currentItems);
	   	    removedItemCuring.removeAll(nextItems); // items only in current
	
	   	    // Status logic
	   	    String status;
	   	    if (nextItems.size() > currentItems.size()) {
	   	        status = "increase";
	   	    } else if (nextItems.size() < currentItems.size()) {
	   	        status = "decrease";
	   	    } else {
	   	        if (addedItemCuring.isEmpty() && removedItemCuring.isEmpty()) {
	   	            status = "same";
	   	        } else {
	   	            status = "same-but-different-item-curing";
	   	        }
	   	    }
	
	   	    grouped.put("changeStatus", status);
	   	    grouped.put("entryCount", currentItems.size());
	   	    grouped.put("nextEntryCount", nextItems.size());
	   	    grouped.put("sameItemCuring", sameItemCuring);
	   	    grouped.put("addedItemCuring", addedItemCuring);
	   	    grouped.put("removedItemCuring", removedItemCuring);
		   	 if ("decrease".equals(grouped.get("changeStatus"))) {
		   	    String currentWct = (String) grouped.get("wct");
		   	    LocalDate nextDate = LocalDate.parse((String) grouped.get("date")).plusDays(1);
	
//		   	    Set<String> sameItemCuring = (Set<String>) grouped.getOrDefault("sameItemCuring", Collections.emptySet());
//		   	    Set<String> addedItemCuring = (Set<String>) grouped.getOrDefault("addedItemCuring", Collections.emptySet());
	
		   	    Map<LocalDate, List<MonthlyPlanningNew>> futureMatches = shiftMonthlyPlan.stream()
		   	        .filter(plan -> {
		   	            LocalDate planDate = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		   	            return planDate.isAfter(nextDate) && plan.getWct().equals(currentWct);
		   	        })
		   	        .collect(Collectors.groupingBy(
		   	            plan -> plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
		   	            TreeMap::new,
		   	            Collectors.toList()
		   	        ));
	
		   	    if (!futureMatches.isEmpty()) {
		   	        // Get the first date it reappears
		   	        Map.Entry<LocalDate, List<MonthlyPlanningNew>> firstReuse = futureMatches.entrySet().iterator().next();
	
		   	        List<MonthlyPlanningNew> fullEntries = firstReuse.getValue();
		   	        // Optional: only new itemCuring
		   	        List<MonthlyPlanningNew> onlyNewItemCurings = fullEntries.stream()
		   	            .filter(entry ->
		   	                !sameItemCuring.contains(entry.getItemCuring()) &&
		   	                !addedItemCuring.contains(entry.getItemCuring())
		   	            )
		   	            .collect(Collectors.toList());
		   	        if(!onlyNewItemCurings.isEmpty()) {
		   	        	grouped.put("wctUsedAgainNewEntries", onlyNewItemCurings);		   	        	
		   	        }
	
		   	    }
		   	}

	   	}


//	   	for (MonthlyPlanningNew currentPlan : shiftMonthlyPlan) {
//	   	    LocalDate currentLocalDate = currentPlan.getDateMp().toInstant()
//	   	            .atZone(ZoneId.systemDefault())
//	   	            .toLocalDate();
//
//	   	    LocalDate nextLocalDate = currentLocalDate.plusDays(1);
//
//	   	    System.out.println("== Checking current plan ==");
//	   	    System.out.println("Current LocalDate : " + currentLocalDate);
//	   	    System.out.println("Next LocalDate    : " + nextLocalDate);
//	   	    System.out.println("WCT               : " + currentPlan.getWct());
//	   	    System.out.println("Item Curing       : " + currentPlan.getItemCuring());
//
//	   	    List<MonthlyPlanningNew> nextMatches = new ArrayList<>();
//
//	   	    for (MonthlyPlanningNew candidate : shiftMonthlyPlan) {
//	   	        LocalDate candidateDate = candidate.getDateMp().toInstant()
//	   	                .atZone(ZoneId.systemDefault())
//	   	                .toLocalDate();
//
//	   	        if (candidateDate.equals(nextLocalDate) &&
//	   	            candidate.getWct().equals(currentPlan.getWct())) {
//
//	   	            nextMatches.add(candidate);
//	   	        }
//	   	    }
//
//	   	    if (!nextMatches.isEmpty()) {
//	   	        Map<String, Object> row = new LinkedHashMap<>();
//	   	        row.put("current", currentPlan);
//	   	        row.put("next", nextMatches);
//	   	        resultList.add(row);
//
//	   	        System.out.println("   >>> MATCH FOUND for next day! Count: " + nextMatches.size());
//	   	        for (MonthlyPlanningNew match : nextMatches) {
//	   	            System.out.println("       -> " + match.getDateMp());
//	   	        }
//	   	    } else {
//	   	        System.out.println("   >>> No match on next day.");
//	   	    }
//
//	   	    System.out.println("==================================\n");
//	   	}


	   	List<Map<String, Object>> filteredResults = new ArrayList<>();

	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    String changeStatus = (String) grouped.get("changeStatus");

	   	    if (!"same".equals(changeStatus)) {
	   	        if ("same-but-different-item-curing".equals(changeStatus)) {
	   	            @SuppressWarnings("unchecked")
	   	            List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");

	   	            @SuppressWarnings("unchecked")
	   	            List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");

	   	            int maxIndex = Math.min(entries.size(), nextEntries.size());

	   	            for (int i = 0; i < maxIndex; i++) {
	   	                MonthlyPlanningNew original = entries.get(i);
	   	                MonthlyPlanningNew reused = nextEntries.get(i);
	   	                if(!original.getItemCuring().equals(reused.getItemCuring())) {
	   	                	Map<String, Object> data = new LinkedHashMap<>();
	   	                	data.put("ORIGINAL_DATE", formatDate(original.getDateMp()));
	   	                	data.put("ORIGINAL_ITEM_CURING", original.getItemCuring());
	   	                	data.put("SHIFT_STOP", getShiftName(original));
	   	                	data.put("WCT", original.getWct());
	   	                	
	   	                	data.put("REUSED_DATE", formatDate(reused.getDateMp()));
	   	                	data.put("REUSED_ITEM_CURING", reused.getItemCuring());
	   	                	data.put("SHIFT_START", getShiftName(reused));
	   	                	
	   	                	filteredResults.add(data);	   	                	
	   	                }

	   	            }
	   	        }
	   	     if ("increase".equals(changeStatus)) {
	   	        @SuppressWarnings("unchecked")
	   	        List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");

	   	        @SuppressWarnings("unchecked")
	   	        List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");

	   	        int entrySize = entries.size();
	   	        int nextEntrySize = nextEntries.size();

	   	        int maxIndex = Math.max(entrySize, nextEntrySize);

	   	        for (int i = 0; i < maxIndex; i++) {
	   	            Map<String, Object> data = new LinkedHashMap<>();

	   	            if (i < entrySize && i < nextEntrySize) {
	   	                // Matched index entries
	   	                MonthlyPlanningNew original = entries.get(i);
	   	                MonthlyPlanningNew reused = nextEntries.get(i);

	   	                if(!original.getItemCuring().equals(reused.getItemCuring())) {
	   	                	data.put("ORIGINAL_DATE", formatDate(original.getDateMp()));
	   	                	data.put("ORIGINAL_ITEM_CURING", original.getItemCuring());
	   	                	data.put("SHIFT_STOP", getShiftName(original));
	   	                	data.put("WCT", original.getWct());
	   	                	
	   	                	data.put("REUSED_DATE", formatDate(reused.getDateMp()));
	   	                	data.put("REUSED_ITEM_CURING", reused.getItemCuring());
	   	                	data.put("SHIFT_START", getShiftName(reused));
	   	                	filteredResults.add(data);
	   	                }
	   	            } else if (i >= entrySize && i < nextEntrySize) {
	   	                // Extra new items (added)
	   	                MonthlyPlanningNew added = nextEntries.get(i);

	   	                data.put("ORIGINAL_DATE", "Extend"); // No matching original
	   	                data.put("ORIGINAL_ITEM_CURING", "Extend");
	   	                data.put("SHIFT_STOP", "Extend"); // No shift stop info
	   	                data.put("WCT", added.getWct()); // From new

	   	                // New reused entry
	   	                data.put("REUSED_DATE", formatDate(added.getDateMp()));
	   	                data.put("REUSED_ITEM_CURING", added.getItemCuring());
	   	                data.put("SHIFT_START", getShiftName(added));
	   	                filteredResults.add(data);
	   	            }

	   	        }
	   	    }
	   	    }
	   	}



    	return new Response( HttpStatus.OK.value(), null, "File processed successfully", null,filteredResults);
	}

	public List<Map<String, Object>> changemouldR(List<MonthlyPlanningNew> shiftMonthlyPlan) {
	 // Step 1: Sort the list by getDateMp()
	   	shiftMonthlyPlan.sort(Comparator.comparing(MonthlyPlanningNew::getDateMp));

	   	// Step 2: Loop through the sorted list
	   	for (MonthlyPlanningNew plan : shiftMonthlyPlan) {
	   	    System.out.println("Date: " + plan.getDateMp());
	   	    // add your logic here
	   	}
	   	
	   	List<Map<String, Object>> resultList = new ArrayList<>();
	   	
	   	List<Map<String, Object>> groupedByDateAndWct = shiftMonthlyPlan.stream()
	   		    .collect(Collectors.groupingBy(plan -> {
	   		        LocalDate date = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	   		        String wct = plan.getWct();
	   		        return date + "|" + wct; // Composite key
	   		    }))
	   		    .entrySet().stream()
	   		    .map(entry -> {
	   		        String[] keyParts = entry.getKey().split("\\|");
	   		        Map<String, Object> map = new LinkedHashMap<>();
	   		        map.put("date", keyParts[0]);
	   		        map.put("wct", keyParts[1]);
	   		        map.put("entries", entry.getValue());
	   		        return map;
	   		    })
	   		    .sorted(Comparator.comparing(map ->
	   		        LocalDate.parse((String) map.get("date"))
	   		    ))
	   		    .collect(Collectors.toList());


	   	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    LocalDate currentDate = LocalDate.parse((String) grouped.get("date"), formatter);
	   	    String currentWct = (String) grouped.get("wct");
	   	    LocalDate nextDate = currentDate.plusDays(1);

	   	    List<MonthlyPlanningNew> nextEntries = shiftMonthlyPlan.stream()
	   	        .filter(plan -> {
	   	            LocalDate planDate = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	   	            return planDate.equals(nextDate) && plan.getWct().equals(currentWct);
	   	        })
	   	        .collect(Collectors.toList());

	   	    grouped.put("nextEntries", nextEntries);
	   	    
		   	 boolean coupled = false;
	
		   	if (nextEntries.size() > 1) {
		   	    String firstItem = nextEntries.get(0).getItemCuring();
		   	    coupled = nextEntries.stream()
		   	        .allMatch(e -> e.getItemCuring().equals(firstItem));
		   	}
	
		   	grouped.put("coupled", coupled);
	   	}
	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");
	   	    List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");
	   	    
	   	    Set<String> currentItems = entries.stream()
	   	        .map(MonthlyPlanningNew::getItemCuring)
	   	        .collect(Collectors.toSet());
	
	   	    Set<String> nextItems = nextEntries.stream()
	   	        .map(MonthlyPlanningNew::getItemCuring)
	   	        .collect(Collectors.toSet());
	
	   	    Set<String> sameItemCuring = new HashSet<>(currentItems);
	   	    sameItemCuring.retainAll(nextItems); // keep only common elements
	
	   	    Set<String> addedItemCuring = new HashSet<>(nextItems);
	   	    addedItemCuring.removeAll(currentItems); // items only in next
	
	   	    Set<String> removedItemCuring = new HashSet<>(currentItems);
	   	    removedItemCuring.removeAll(nextItems); // items only in current
	
	   	    // Status logic
	   	    String status;
	   	    if (nextItems.size() > currentItems.size()) {
	   	        status = "increase";
	   	    } else if (nextItems.size() < currentItems.size()) {
	   	        status = "decrease";
	   	    } else {
	   	        if (addedItemCuring.isEmpty() && removedItemCuring.isEmpty()) {
	   	            status = "same";
	   	        } else {
	   	            status = "same-but-different-item-curing";
	   	        }
	   	    }
	
	   	    grouped.put("changeStatus", status);
	   	    grouped.put("entryCount", currentItems.size());
	   	    grouped.put("nextEntryCount", nextItems.size());
	   	    grouped.put("sameItemCuring", sameItemCuring);
	   	    grouped.put("addedItemCuring", addedItemCuring);
	   	    grouped.put("removedItemCuring", removedItemCuring);
		   	 if ("decrease".equals(grouped.get("changeStatus"))) {
		   	    String currentWct = (String) grouped.get("wct");
		   	    LocalDate nextDate = LocalDate.parse((String) grouped.get("date")).plusDays(1);

		   	    Map<LocalDate, List<MonthlyPlanningNew>> futureMatches = shiftMonthlyPlan.stream()
		   	        .filter(plan -> {
		   	            LocalDate planDate = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		   	            return planDate.isAfter(nextDate) && plan.getWct().equals(currentWct);
		   	        })
		   	        .collect(Collectors.groupingBy(
		   	            plan -> plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
		   	            TreeMap::new,
		   	            Collectors.toList()
		   	        ));
	
		   	    if (!futureMatches.isEmpty()) {
		   	        // Get the first date it reappears
		   	        Map.Entry<LocalDate, List<MonthlyPlanningNew>> firstReuse = futureMatches.entrySet().iterator().next();
	
		   	        List<MonthlyPlanningNew> fullEntries = firstReuse.getValue();
		   	        // Optional: only new itemCuring
		   	        List<MonthlyPlanningNew> onlyNewItemCurings = fullEntries.stream()
		   	            .filter(entry ->
		   	                !sameItemCuring.contains(entry.getItemCuring()) &&
		   	                !addedItemCuring.contains(entry.getItemCuring())
		   	            )
		   	            .collect(Collectors.toList());
		   	        if(!onlyNewItemCurings.isEmpty()) {
		   	        	grouped.put("wctUsedAgainNewEntries", onlyNewItemCurings);		   	        	
		   	        }
	
		   	    }
		   	}

	   	}

	   	List<Map<String, Object>> filteredResults = new ArrayList<>();

	   	for (Map<String, Object> grouped : groupedByDateAndWct) {
	   	    String changeStatus = (String) grouped.get("changeStatus");

	   	    if (!"same".equals(changeStatus)) {
	   	        if ("same-but-different-item-curing".equals(changeStatus)) {
	   	            @SuppressWarnings("unchecked")
	   	            List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");

	   	            @SuppressWarnings("unchecked")
	   	            List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");

	   	            int maxIndex = Math.min(entries.size(), nextEntries.size());

	   	            for (int i = 0; i < maxIndex; i++) {
	   	                MonthlyPlanningNew original = entries.get(i);
	   	                MonthlyPlanningNew reused = nextEntries.get(i);

	   	                if(!original.getItemCuring().equals(reused.getItemCuring())) {
	   	                	Map<String, Object> data = new LinkedHashMap<>();
	   	                	data.put("ORIGINAL_DATE", formatDate(original.getDateMp()));
	   	                	data.put("ORIGINAL_ITEM_CURING", original.getItemCuring());
	   	                	data.put("SHIFT_STOP", getShiftName(original));
	   	                	data.put("WCT", original.getWct());
	   	                	
	   	                	data.put("REUSED_DATE", formatDate(reused.getDateMp()));
	   	                	data.put("REUSED_ITEM_CURING", reused.getItemCuring());
	   	                	data.put("SHIFT_START", getShiftName(reused));
	   	                	
	   	                	filteredResults.add(data);	   	                	
	   	                }
	   	            }
	   	        }
	   	     if ("increase".equals(changeStatus)) {
	   	        @SuppressWarnings("unchecked")
	   	        List<MonthlyPlanningNew> entries = (List<MonthlyPlanningNew>) grouped.get("entries");

	   	        @SuppressWarnings("unchecked")
	   	        List<MonthlyPlanningNew> nextEntries = (List<MonthlyPlanningNew>) grouped.get("nextEntries");

	   	        int entrySize = entries.size();
	   	        int nextEntrySize = nextEntries.size();

	   	        int maxIndex = Math.max(entrySize, nextEntrySize);

	   	        for (int i = 0; i < maxIndex; i++) {
	   	            Map<String, Object> data = new LinkedHashMap<>();

	   	            if (i < entrySize && i < nextEntrySize) {
	   	                // Matched index entries
	   	                MonthlyPlanningNew original = entries.get(i);
	   	                MonthlyPlanningNew reused = nextEntries.get(i);

	   	                if(!original.getItemCuring().equals(reused.getItemCuring())) {
	   	                	data.put("ORIGINAL_DATE", formatDate(original.getDateMp()));
	   	                	data.put("ORIGINAL_ITEM_CURING", original.getItemCuring());
	   	                	data.put("SHIFT_STOP", getShiftName(original));
	   	                	data.put("WCT", original.getWct());
	   	                	
	   	                	data.put("REUSED_DATE", formatDate(reused.getDateMp()));
	   	                	data.put("REUSED_ITEM_CURING", reused.getItemCuring());
	   	                	data.put("SHIFT_START", getShiftName(reused));
	   	                	filteredResults.add(data);       	
	   	                }
	   	            } else if (i >= entrySize && i < nextEntrySize) {
	   	                // Extra new items (added)
	   	                MonthlyPlanningNew added = nextEntries.get(i);

	   	                data.put("ORIGINAL_DATE", "Extend"); // No matching original
	   	                data.put("ORIGINAL_ITEM_CURING", "Extend");
	   	                data.put("SHIFT_STOP", "Extend"); // No shift stop info
	   	                data.put("WCT", added.getWct()); // From new

	   	                // New reused entry
	   	                data.put("REUSED_DATE", formatDate(added.getDateMp()));
	   	                data.put("REUSED_ITEM_CURING", added.getItemCuring());
	   	                data.put("SHIFT_START", getShiftName(added));
	   	                filteredResults.add(data);
	   	            }

	   	        }
	   	    }
	   	    }
	   	}

		return filteredResults;
	}
	
		private String getShiftName(MonthlyPlanningNew plan) {
		    int s1 = plan.getShift1() != null ? plan.getShift1().intValue() : 0;
		    int s2 = plan.getShift2() != null ? plan.getShift2().intValue() : 0;
		    int s3 = plan.getShift3() != null ? plan.getShift3().intValue() : 0;
	
		    if (s1 > 0) return "SHIFT_1";
		    if (s2 > 0) return "SHIFT_2";
		    if (s3 > 0) return "SHIFT_3";
		    return "NONE";
		}
	
		private String formatDate(Date date) {
		    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
		}

	    
    public ByteArrayInputStream exportExcel(int month, int year, int limitChange, BigDecimal minA, BigDecimal maxA, BigDecimal minB, BigDecimal maxB, BigDecimal minC, BigDecimal maxC, BigDecimal minD, BigDecimal maxD, BigDecimal versionMO,BigDecimal versionGenerate) throws IOException {
//    	List<ShiftMonthlyPlan> shiftMonthlyPlan = MonthlyPlan(month, year, limitChange, minA, maxA, minB, maxB, minC, maxC, minD, maxD);
//        if (!"Tidak Aktif".equals(statusMPRepo.findLatestStatusMP())) {
//            return null; // or throw new IllegalStateException("Status is not active");
//        }
//    	
    	YearMonth yearMonth = YearMonth.of(year,month);
    	LocalDate startLocal = yearMonth.atDay(1);
    	LocalDate endLocal = yearMonth.atEndOfMonth();

        String yearMonthStr = String.format("%04d%02d", year, month);
    	List<MarketingOrder> top2 = marketingOrderRepo
    	        .findTop2ByYearMonth(yearMonthStr,versionMO);

    	List<String> moids = new ArrayList<>(); 

    	for (MarketingOrder buffer : top2) {
    	    System.out.println(buffer.getMoId());	
    	    moids.add(buffer.getMoId());
    	}
    	
		BigDecimal version;
		if (versionGenerate.compareTo(BigDecimal.ZERO) == 0) {
			version = totalPlanRepo.getNewestVersion(moids.get(1).toString(), moids.get(0).toString());
		} else {
			version = versionGenerate;
		}
		System.out.println("Using version: " + version);

	   	List<MonthlyPlanningNew> shiftMonthlyPlan = monthlyPlanNewRepo.findByMoIdInAndVersion(moids, version);
	 // Step 1: Sort the list by getDateMp()
	   	shiftMonthlyPlan.sort(Comparator.comparing(MonthlyPlanningNew::getDateMp));

	   	System.out.println(shiftMonthlyPlan.size());
	   	// List<String> productDescription = new ArrayList<>();
		List<TotalPlan> totalPlanList = totalPlanRepo.findAll();
		List<String> uniqueItemCuring = shiftMonthlyPlan.stream()
			.map(MonthlyPlanningNew::getItemCuring) // Extract itemCuring values
			.distinct() // Remove duplicates
			.collect(Collectors.toList());
		// List<MonthlyPlanningNew> shiftMonthlyPlan = null;
		// List<String> productDescription = null;
		// List<TotalPlan> totalPlanList = null;

    	if (uniqueItemCuring.isEmpty()) {
			System.out.println("Tidak ada data pada itemcuirn unik");
			return null;
		
		}
		List<Object[]> results = shiftMonthlyRepo.findDescriptionsByItemCuring(new ArrayList<>(uniqueItemCuring));
		// Map ITEM_CURING to DESCRIPTION
		// Create map for descriptions
		Map<String, String> itemCuringToDescription = results.stream()
				.collect(Collectors.toMap(
					result -> (String) result[0], // ITEM_CURING
					result -> (String) result[1], // DESCRIPTION
					(existing, replacement) -> existing
				));

		// Create map for kapa per mould
		Map<String, Integer> itemCuringToKapaPerMould = results.stream()
				.collect(Collectors.toMap(
					result -> (String) result[0], // ITEM_CURING
					result -> Integer.parseInt(result[2].toString()), // KAPA_PER_MOULD
					(existing, replacement) -> existing
				));


		// Prepare product descriptions
		List<String> productDescription = shiftMonthlyPlan.stream()
				.map(plan -> {
					String partNumber = plan.getItemCuring();
					System.out.println("Item Curing: " + partNumber);

					String description = itemCuringToDescription.getOrDefault(partNumber, "N/A");
					System.out.println("Description: " + description);
					return description;
				})
				.collect(Collectors.toList());
				
		List<Integer> productKapaPerMould = shiftMonthlyPlan.stream()
				.map(plan -> itemCuringToKapaPerMould.getOrDefault(plan.getItemCuring(), 0))
				.collect(Collectors.toList());




        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
        	//style
        	// Font
            Font calibri11 = workbook.createFont();
            calibri11.setFontName("Calibri");
            calibri11.setFontHeightInPoints((short) 11);
            
            Font calibri12 = workbook.createFont();
            calibri12.setFontName("Calibri");
            calibri12.setFontHeightInPoints((short) 12);
            
            Font calibriBold11 = workbook.createFont();
            calibriBold11.setFontName("Calibri");
            calibriBold11.setFontHeightInPoints((short) 11);
            calibriBold11.setBold(true);
            
            Font calibriBold12 = workbook.createFont();
            calibriBold12.setFontName("Calibri");
            calibriBold12.setFontHeightInPoints((short) 12);
            calibriBold12.setBold(true);
            
            Font calibriBold14 = workbook.createFont();
            calibriBold14.setFontName("Calibri");
            calibriBold14.setFontHeightInPoints((short) 14);
            calibriBold14.setBold(true);
            // End Font
            
            // Border cell style
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            // End border cell style

            // Style untuk cell
            CellStyle title = workbook.createCellStyle();
            title.setFont(calibriBold14);
            title.setAlignment(HorizontalAlignment.CENTER);
            title.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11Left = workbook.createCellStyle();
            calibri11Left.setFont(calibri11);
            calibri11Left.setAlignment(HorizontalAlignment.LEFT);
            calibri11Left.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11Right = workbook.createCellStyle();
            calibri11Right.setFont(calibri11);
            calibri11Right.setAlignment(HorizontalAlignment.RIGHT);
            calibri11Right.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11Center = workbook.createCellStyle();
            calibri11Center.setFont(calibri11);
            calibri11Center.setAlignment(HorizontalAlignment.CENTER);
            calibri11Center.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11LeftBorder = workbook.createCellStyle();
            calibri11LeftBorder.cloneStyleFrom(borderStyle);
            calibri11LeftBorder.setFont(calibri11);
            calibri11LeftBorder.setAlignment(HorizontalAlignment.LEFT);
            calibri11LeftBorder.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibriBold11LeftBorder = workbook.createCellStyle();
            calibriBold11LeftBorder.cloneStyleFrom(borderStyle);
            calibriBold11LeftBorder.setFont(calibriBold11);
            calibriBold11LeftBorder.setAlignment(HorizontalAlignment.LEFT);
            calibriBold11LeftBorder.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11RightBorder = workbook.createCellStyle();
            calibri11RightBorder.cloneStyleFrom(borderStyle);
            calibri11RightBorder.setFont(calibri11);
            calibri11RightBorder.setAlignment(HorizontalAlignment.RIGHT);
            calibri11RightBorder.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibri11CenterBorder = workbook.createCellStyle();
            calibri11CenterBorder.cloneStyleFrom(borderStyle);
            calibri11CenterBorder.setFont(calibri11);
            calibri11CenterBorder.setAlignment(HorizontalAlignment.CENTER);
            calibri11CenterBorder.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle calibriBold11CenterBorder = workbook.createCellStyle();
            calibriBold11CenterBorder.cloneStyleFrom(borderStyle);
            calibriBold11CenterBorder.setFont(calibriBold11);
            calibriBold11CenterBorder.setAlignment(HorizontalAlignment.CENTER);
            calibriBold11CenterBorder.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle calibri11Date = workbook.createCellStyle();
            calibri11Date.cloneStyleFrom(borderStyle);
            calibri11Date.setFont(calibri11);
            calibri11Date.setAlignment(HorizontalAlignment.CENTER);
            calibri11Date.setVerticalAlignment(VerticalAlignment.CENTER);
            calibri11Date.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            //end style
            
            //mesin curing sheet
            Sheet curingSheet = workbook.createSheet("mesin curing");
            curingSheet.setColumnWidth(1, 3000);
            curingSheet.setColumnWidth(4, 10000);
            curingSheet.setColumnWidth(5, 3000);
            curingSheet.setColumnWidth(6, 10000);
            
            //header table
            Row tableHeadCuringRow = curingSheet.createRow(1);
            Cell tableHeadCuringCell;
            
            String[] curingHeaderLabels = {"Tanggal", "Nomor", "Cavity", "Work Center Text", "Item Curing", "Deskripsi", "Shift 1", "Shift 2", "Shift 3", "Total","Mould Use","Kapa Per Mould"};
            CellStyle[] curingHeaderStyles = {calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, 
            		calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder,calibriBold11CenterBorder};
            System.out.println("Check point 1");
            // Loop untuk kolom 0-5
            for (int col = 0; col < curingHeaderLabels.length; col++) {
            	tableHeadCuringCell = tableHeadCuringRow.createCell(col+1);
            	tableHeadCuringCell.setCellStyle(curingHeaderStyles[col]);
                if (!curingHeaderLabels[col].isEmpty()) {
                	tableHeadCuringCell.setCellValue(curingHeaderLabels[col]);
                }
            }
            
            //data tables
            
            int curingDatarow = 2;
            Row curingDataRow;
            Cell curingDataCell;
            for (int j = 0; j < shiftMonthlyPlan.size(); j++) {
            	
            	curingDataRow = curingSheet.createRow(curingDatarow);
            	curingDataCell = curingDataRow.createCell(1);
                curingDataCell.setCellStyle(calibri11Date);
                curingDataCell.setCellValue(shiftMonthlyPlan.get(j).getDateMp());
                
                
                curingDataCell = curingDataRow.createCell(2);
                curingDataCell.setCellStyle(calibriBold11CenterBorder);
                curingDataCell.setCellValue(j+1);
                
                curingDataCell = curingDataRow.createCell(3);
        		curingDataCell.setCellStyle(calibri11CenterBorder);
        		curingDataCell.setCellValue("C");
        		
        		curingDataCell = curingDataRow.createCell(4);
        		curingDataCell.setCellStyle(calibriBold11LeftBorder);
        		curingDataCell.setCellValue(shiftMonthlyPlan.get(j).getWct());
        		
        		curingDataCell = curingDataRow.createCell(5);
        		curingDataCell.setCellStyle(calibriBold11LeftBorder);
        		curingDataCell.setCellValue(shiftMonthlyPlan.get(j).getItemCuring());
        		
        		curingDataCell = curingDataRow.createCell(6);
        		curingDataCell.setCellStyle(calibri11LeftBorder);
        		curingDataCell.setCellValue(productDescription.get(j));
        		
        		BigDecimal shift1 = shiftMonthlyPlan.get(j).getShift1();
        		curingDataCell = curingDataRow.createCell(7);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellValue(shift1 != null ? shift1.doubleValue() : 0.0);

        		BigDecimal shift2 = shiftMonthlyPlan.get(j).getShift2();
        		curingDataCell = curingDataRow.createCell(8);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellValue(shift2 != null ? shift2.doubleValue() : 0.0);

        		BigDecimal shift3 = shiftMonthlyPlan.get(j).getShift3();
        		curingDataCell = curingDataRow.createCell(9);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellValue(shift3 != null ? shift3.doubleValue() : 0.0);
        		
        		curingDataCell = curingDataRow.createCell(10);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellFormula("SUM(H" + (curingDatarow + 1) + ":J" + (curingDatarow + 1) + ")");
        		
        		BigDecimal mouldUse = shiftMonthlyPlan.get(j).getMouldUse();
        		curingDataCell = curingDataRow.createCell(11);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellValue(mouldUse != null ? mouldUse.doubleValue() : 0.0);
        		
        		Integer kapaPerMould = productKapaPerMould.get(j);
        		curingDataCell = curingDataRow.createCell(12);
        		curingDataCell.setCellStyle(calibri11RightBorder);
        		curingDataCell.setCellValue(kapaPerMould != null ? kapaPerMould : 0);
        		
                curingDatarow++;
            }
            //end curing sheet
        	
            //prepare prod sheet
            Sheet prepareProdSheet = workbook.createSheet("PREPARE PRODE TIRE");
            
            // Set column width
            prepareProdSheet.setColumnWidth(1, 5000);
            prepareProdSheet.setColumnWidth(4, 8000);
            prepareProdSheet.setColumnWidth(5, 8000);

            //header
            Row monthRow = prepareProdSheet.createRow(8);
            Cell monthCell = monthRow.createCell(4);
            monthCell.setCellStyle(calibri11Right);
            monthCell.setCellValue("MONTH OF :");
            
            Row sectionRow = prepareProdSheet.createRow(9);
            Cell sectionCell = sectionRow.createCell(4);
            sectionCell.setCellStyle(calibri11Right);
            sectionCell.setCellValue("SECTION :");
            
            Row issueDateRow = prepareProdSheet.createRow(11);
            Cell issueDateCell = issueDateRow.createCell(4);
            issueDateCell.setCellStyle(calibri11Right);
            issueDateCell.setCellValue("ISSUE DATE :");
            
            prepareProdSheet.addMergedRegion(new CellRangeAddress(8, 9, 18, 23));
            Cell titleCell = monthRow.createCell(18);
            titleCell.setCellStyle(title);
            titleCell.setCellValue("M O N T H L Y      P L A N N I N G");
            
            Cell kadeptCell = monthRow.createCell(41);
            kadeptCell.setCellStyle(calibri11CenterBorder);
            kadeptCell.setCellValue("KADEPT");
            
            Cell kassieCell = monthRow.createCell(42);
            kassieCell.setCellStyle(calibri11CenterBorder);
            kassieCell.setCellValue("KASSIE PP");
            
            Cell docNumCell = monthRow.createCell(43);
            docNumCell.setCellStyle(calibri11LeftBorder);
            docNumCell.setCellValue("NO. DOK");
            
            //table head
            int i;
            Row tableHeadMpRow1 = prepareProdSheet.createRow(16);
            Row tableHeadMpRow2 = prepareProdSheet.createRow(17);
            Cell tableHeadMpCell;

            // Array untuk header teks dan gaya untuk kolom 0-5
            String[] headerMpLabels = {"", "ITEM CURING", "", "NO.", "SIZE", "PATTERN"};
            CellStyle[] headerMpStyles = {calibri11CenterBorder, calibriBold11CenterBorder, calibri11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder, calibriBold11CenterBorder};

            // Loop untuk kolom 0-5
            for (int col = 0; col < headerMpLabels.length; col++) {
            	prepareProdSheet.addMergedRegion(new CellRangeAddress(16, 17, col, col));
                tableHeadMpCell = tableHeadMpRow1.createCell(col);
                tableHeadMpCell.setCellStyle(headerMpStyles[col]);
                if (!headerMpLabels[col].isEmpty()) {
                    tableHeadMpCell.setCellValue(headerMpLabels[col]);
                }
                tableHeadMpCell = tableHeadMpRow2.createCell(col);
                tableHeadMpCell.setCellStyle(calibri11CenterBorder);
            }

            // Mendapatkan jumlah hari di bulan ini
//            YearMonth yearMonth = YearMonth.of(year, month);
            int jumlahHariBulanIni = yearMonth.lengthOfMonth();
            LocalDate localDate;
            Date date = new Date();

            // Loop untuk menambah tanggal (kolom 6 ke atas)
            System.out.println("Check point 2");
            for (i = 0; i < jumlahHariBulanIni; i++) {
                int col = i + 6;
                prepareProdSheet.addMergedRegion(new CellRangeAddress(16, 17, col, col));
                tableHeadMpCell = tableHeadMpRow1.createCell(col);
                tableHeadMpCell.setCellStyle(calibriBold11CenterBorder);
                tableHeadMpCell.setCellValue(i + 1);
                tableHeadMpCell = tableHeadMpRow2.createCell(col);
                tableHeadMpCell.setCellStyle(calibri11CenterBorder);
            }

            // Menambahkan kolom "TOTAL" setelah jumlah hari dalam bulan
            tableHeadMpCell = tableHeadMpRow1.createCell(i + 6);
            tableHeadMpCell.setCellStyle(calibriBold11CenterBorder);
            tableHeadMpCell.setCellValue("TOTAL");
            tableHeadMpCell = tableHeadMpRow2.createCell(i + 6);
            tableHeadMpCell.setCellStyle(calibri11CenterBorder);
            tableHeadMpCell.setCellValue(i);
            
            prepareProdSheet.addMergedRegion(new CellRangeAddress(16, 17, i + 7, i + 7));
            
            tableHeadMpCell = tableHeadMpRow1.createCell(i + 7);
            tableHeadMpCell.setCellStyle(calibriBold11CenterBorder);
            tableHeadMpCell.setCellValue("TOTAL PLAN");
            tableHeadMpCell = tableHeadMpRow2.createCell(i + 7);
            tableHeadMpCell.setCellStyle(calibri11CenterBorder);
            tableHeadMpCell = tableHeadMpRow2.createCell(i + 7);
            tableHeadMpCell.setCellStyle(calibri11CenterBorder);

			prepareProdSheet.addMergedRegion(new CellRangeAddress(16, 17, i + 8, i + 8));
			tableHeadMpCell = tableHeadMpRow1.createCell(i + 8);
			tableHeadMpCell.setCellStyle(calibriBold11CenterBorder);
			tableHeadMpCell.setCellValue("MOULD NEED"); 
			tableHeadMpCell = tableHeadMpRow2.createCell(i + 8);
			tableHeadMpCell.setCellStyle(calibri11CenterBorder);

            int mpDatarow = 18;
            Row mpDataRow;
            Cell mpDataCell;
            Set<String> addedPartNumbers = new HashSet<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Precompute the length of the month
            int monthLength = yearMonth.lengthOfMonth();

            // Create a Map to group shiftMonthlyPlan by itemCuring and date
            Map<String, Map<LocalDate, Integer>> capacityMap = new HashMap<>();
            for (MonthlyPlanningNew plan : shiftMonthlyPlan) {
                String itemCuring = String.valueOf(plan.getItemCuring());
                LocalDate planDate = plan.getDateMp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int shift1 = plan.getShift1() != null ? plan.getShift1().intValue() : 0;
                int shift2 = plan.getShift2() != null ? plan.getShift2().intValue() : 0;
                int shift3 = plan.getShift3() != null ? plan.getShift3().intValue() : 0;

                int capacity = shift1 + shift2 + shift3;
                capacityMap.computeIfAbsent(itemCuring, k -> new HashMap<>())
                           .merge(planDate, capacity, Integer::sum);
            }

            // Iterate through shiftMonthlyPlan to create rows
            for (int j = 0; j < shiftMonthlyPlan.size(); j++) {
                String itemCuring = String.valueOf(shiftMonthlyPlan.get(j).getItemCuring());
                String description = productDescription.get(j);

                if (itemCuring != null && !addedPartNumbers.contains(itemCuring)) {
                    addedPartNumbers.add(itemCuring);

                    // Create a new row
                    mpDataRow = prepareProdSheet.createRow(mpDatarow);
                    mpDataCell = mpDataRow.createCell(0);
                    mpDataCell.setCellValue("");
                    mpDataCell.setCellStyle(calibri11LeftBorder);

                    mpDataCell = mpDataRow.createCell(1);
                    mpDataCell.setCellValue(itemCuring);
                    mpDataCell.setCellStyle(calibri11CenterBorder);

                    mpDataCell = mpDataRow.createCell(2);
                    mpDataCell.setCellValue("");
                    mpDataCell.setCellStyle(calibri11LeftBorder);

                    mpDataCell = mpDataRow.createCell(3);
                    mpDataCell.setCellValue(mpDatarow - 17);
                    mpDataCell.setCellStyle(calibri11RightBorder);

                    mpDataCell = mpDataRow.createCell(4);
                    mpDataCell.setCellValue(description);
                    mpDataCell.setCellStyle(calibri11LeftBorder);

                    mpDataCell = mpDataRow.createCell(5);
                    mpDataCell.setCellValue(description);
                    mpDataCell.setCellStyle(calibri11LeftBorder);

                    int totalCapacity = 0;
                    Map<LocalDate, Integer> itemCapacity = capacityMap.getOrDefault(itemCuring, new HashMap<>());

                    // Loop through each day of the month
                    for (int day = 1; day <= monthLength; day++) {
                        LocalDate currentDate = yearMonth.atDay(day);
                        int intCapacity = itemCapacity.getOrDefault(currentDate, 0);
                        totalCapacity += intCapacity;

                        mpDataCell = mpDataRow.createCell(day + 5);
                        if (intCapacity > 0) {
                            mpDataCell.setCellValue((double) intCapacity);
                        } else {
                            mpDataCell.setCellValue("");
                        }
                        mpDataCell.setCellStyle(calibri11RightBorder);
                    }

                    // Set total capacity
                    mpDataCell = mpDataRow.createCell(monthLength + 6);
                    mpDataCell.setCellValue((double) totalCapacity);
                    mpDataCell.setCellStyle(calibri11RightBorder);

                    // Set total plan data
                    Optional<TotalPlan> totalPlanData = totalPlanList.stream()
                            .filter(tp -> tp.getITEM_CURING().equals(itemCuring))
                            .findFirst();
                    mpDataCell = mpDataRow.createCell(monthLength + 7);
                    if (totalPlanData.isPresent()) {
                        BigDecimal totalPlan = totalPlanData.get().getTOTAL_PLAN();
                        mpDataCell.setCellValue(totalPlan != null ? totalPlan.doubleValue() : null);
                    } else {
                        mpDataCell.setCellValue("");
                    }
                    mpDataCell.setCellStyle(calibri11RightBorder);

					mpDataCell = mpDataRow.createCell(monthLength + 8);
					if (totalPlanData.isPresent()) {
                        BigDecimal mouldneed = totalPlanData.get().getMOULD_NEEDED();
                        mpDataCell.setCellValue(mouldneed != null ? mouldneed.doubleValue() : null);
                    } else {
                        mpDataCell.setCellValue("");
                    }
                    mpDataCell.setCellStyle(calibri11RightBorder);


                    mpDatarow++;
                }
            }
			System.out.println("Check point 4");
	        List<Map<String, Object>> dataListDetail = totalPlanRepo.getDetailTotalPlan(moids.get(1).toString(),moids.get(0).toString(),version);
	        System.out.println("Check point 4.1");
	        List<Map<String, Object>> resultChangeMould = changemouldR(shiftMonthlyPlan);
	        System.out.println("Check point 5");
			List<Map<String, Object>> resultMouldUsed = monthlyPlanNewRepo.findDailyMouldUseSummaryAsMap(moids,version);
	        System.out.println("sudah dapetindata");
			Map<Object, Long> counts = resultChangeMould.stream()
				.collect(Collectors.groupingBy(
					row -> row.get("REUSED_DATE"),
					TreeMap::new,
					Collectors.collectingAndThen(
						Collectors.mapping(
							row -> Arrays.asList(row.get("WCT"), row.get("REUSED_ITEM_CURING")),
							Collectors.toSet()
						),
						set -> (long) set.size()
					)
				));


			System.out.println("uda ngitung");
			String[] headerObjName = { "TOTAL_HARIAN_PER_TANGGAL", 
									"TOTAL_HARIAN_TT", "TOTAL_HARIAN_TL", "PERSENTASE_TT", "PERSENTASE_TL"};
			String[] headersName = {"Total Mould Used per Day", "Total Day per Date", 
									"Total Day TT", "Total Day TL", "Percentage TT", "Percentage TL","Change Mould"};
			// Write Headers
			System.out.println("Check point 6");
			int headerRowIndex = mpDatarow;
			for (int j = 0; j < headersName.length; j++) {
				mpDataRow = prepareProdSheet.createRow(mpDatarow++);
				mpDataCell = mpDataRow.createCell(5);
				mpDataCell.setCellValue(headersName[j]);
				mpDataCell.setCellStyle(calibri11RightBorder);
			}

			// Write Data (Start below headers)
			int colOffset = 6;
			int dataStartRow = headerRowIndex;
			for (Map<String, Object> row : resultMouldUsed) {
				mpDataRow = prepareProdSheet.getRow(dataStartRow); // Get the existing row
				if (mpDataRow == null) {
					mpDataRow = prepareProdSheet.createRow(dataStartRow);
				}
				Object value = row.get("TOTAL_MOULD_USE_HARIAN");
				mpDataCell = mpDataRow.createCell(colOffset);
				mpDataCell.setCellValue(value != null ? value.toString() : "");
				mpDataCell.setCellStyle(calibri11RightBorder);
				colOffset++; // Move to the next column for the next Map
			}
			colOffset = 6; // Start writing from column 6

			for (Map<String, Object> row : dataListDetail) {
				int currentRow = dataStartRow + 1;
				for (String header : headerObjName) {
					mpDataRow = prepareProdSheet.getRow(currentRow); // Get the existing row
					if (mpDataRow == null) {
						mpDataRow = prepareProdSheet.createRow(currentRow);
					}
					Object value = row.get(header);
					mpDataCell = mpDataRow.createCell(colOffset);
					mpDataCell.setCellValue(value != null ? value.toString() : "");
					mpDataCell.setCellStyle(calibri11RightBorder);
					currentRow++; // Move to the next row for each header
				}
				colOffset++; // Move to the next column for the next Map
			}

			if (!counts.isEmpty()) {
				System.out.println("ini ada");

				int countRowIndex = headerRowIndex + 6; // The row where all count values will go

				Map<Integer, Long> countByDay = new TreeMap<>();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				// Determine year and month from the first date
				Integer targetYear = null;
				Integer targetMonth = null;

				for (Map.Entry<Object, Long> entry : counts.entrySet()) {
					Object originalDateObj = entry.getKey();
					Long count = entry.getValue();

					if (originalDateObj != null) {
						LocalDate dateM;
						if (originalDateObj instanceof java.sql.Date) {
							dateM = ((java.sql.Date) originalDateObj).toLocalDate();
						} else if (originalDateObj instanceof java.util.Date) {
							dateM = ((java.util.Date) originalDateObj).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
						} else if (originalDateObj instanceof String) {
							dateM = LocalDate.parse((String) originalDateObj, formatter);
						} else {
							continue; // Skip unknown type
						}

						if (targetYear == null || targetMonth == null) {
							targetYear = dateM.getYear();
							targetMonth = dateM.getMonthValue();
						}

						int dayOfMonth = dateM.getDayOfMonth();
						countByDay.put(dayOfMonth, count);
					}
				}

				// Ensure year and month were found
				if (targetYear != null && targetMonth != null) {
					int daysInMonth = java.time.YearMonth.of(targetYear, targetMonth).lengthOfMonth();

					Row countRow = prepareProdSheet.getRow(countRowIndex);
					if (countRow == null) countRow = prepareProdSheet.createRow(countRowIndex);

					int currentCol = 6;

					for (int day = 1; day <= daysInMonth; day++) {
						Cell countCell = countRow.createCell(currentCol);
						countCell.setCellValue(countByDay.getOrDefault(day, 0L));
						countCell.setCellStyle(calibri11RightBorder);
						currentCol++;
					}
				} else {
					System.out.println("No valid dates found to determine month.");
				}

			} else {
				System.out.println("No Change Mould data found.");
			}




            //end prepare prod sheet
            
            //change mould sheet
            // 1. Create the Sheet and Set Column Widths
			Sheet changeMouldSheet = workbook.createSheet("CHANGE MOULD");
			for (int ic = 1; i <= 7; ic++) {
				changeMouldSheet.setColumnWidth(ic, 5000); // Uniform column width, adjust as needed
			}

			// 2. Define Header Labels and Corresponding Map Keys
			String[] headerLabels = {
				"Original Item Curing", "Original Date", "Shift Stop", "Work Center Text", 
				"Reused Date", "Reused Item Curing", "Shift Start"
			};

			String[] mapKeys = {
				"ORIGINAL_ITEM_CURING", "ORIGINAL_DATE", "SHIFT_STOP", "WCT",
				"REUSED_DATE", "REUSED_ITEM_CURING", "SHIFT_START"
			};

			// 3. Create Header Row
			Row headerRow = changeMouldSheet.createRow(1);
			for (int col = 0; col < headerLabels.length; col++) {
				Cell cell = headerRow.createCell(col + 1); // Start from column 1	
				cell.setCellValue(headerLabels[col]);
				cell.setCellStyle(calibriBold11CenterBorder);
			}

			// 4. Populate Data
			int rowIndex = 2;
			for (Map<String, Object> row : resultChangeMould) {
				Row dataRow = changeMouldSheet.createRow(rowIndex++);
				for (int col = 0; col < mapKeys.length; col++) {
					Cell dataCell = dataRow.createCell(col + 1); // Start from column 1
					Object value = row.get(mapKeys[col]);
					dataCell.setCellValue(value != null ? value.toString() : "");
					dataCell.setCellStyle(calibri11CenterBorder); // Apply consistent style
				}
			}

            //end change mould sheet
            
            workbook.write(out); // Menulis data ke output stream
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
           System.out.println("Fail to export data");
            return null;
        } finally {
            out.close(); // Tutup output stream setelah selesai
        }
    }
    
    
    
    public ViewMonthlyPlanning getDetailMonthlyPlan(int month, int year, int limitChange, BigDecimal minA, BigDecimal maxA, BigDecimal minB, BigDecimal maxB, BigDecimal minC, BigDecimal maxC, BigDecimal minD, BigDecimal maxD) {
        List<ShiftMonthlyPlan> shiftMonthlyPlan = MonthlyPlan(month, year, limitChange, minA, maxA, minB, maxB, minC, maxC, minD, maxD);

        ViewMonthlyPlanning viewMonthlyPlanning = new ViewMonthlyPlanning();
        List<DetailMonthlyPlanCuring> detailList1 = new ArrayList<>();
        List<DetailDailyMonthlyPlanCuring> detailList2 = new ArrayList<>();
        List<Map<String, Object>> description = new ArrayList<>();
        List<Map<String, Object>> productDetails = new ArrayList<>();

        BigDecimal detailId = BigDecimal.ONE;
        BigDecimal detailDailyId = BigDecimal.ONE;
        
        Map<BigDecimal, BigDecimal> partNumberToDetailIdCuringMap = new HashMap<>();
        
        int totalKapa = 0;
        int totalPlanDaily = 0;

        // totalKapa per partNumber 
        for (int j = 0; j < shiftMonthlyPlan.size(); j++) {
            final BigDecimal partNumber = shiftMonthlyPlan.get(j).getPART_NUMBER(); 
            
            // Check if partNumber already exists in detailList1
            boolean isPartNumberExists = detailList1.stream()
                .anyMatch(detail -> detail.getPartNumber().equals(partNumber));
            
            if (isPartNumberExists) {
                continue; 
            }

            for (int k = 0; k < shiftMonthlyPlan.size(); k++) {
                if (partNumber.equals(shiftMonthlyPlan.get(k).getPART_NUMBER())) {
                    totalKapa += shiftMonthlyPlan.get(k).getTOTAL_KAPA().intValue(); 
                }
            }

            DetailMonthlyPlanCuring detail = new DetailMonthlyPlanCuring();
            detail.setDetailIdCuring(detailId);
            detail.setPartNumber(partNumber);
            detail.setTotal(BigDecimal.valueOf(totalKapa));
            
            detailId = detailId.add(BigDecimal.ONE);

            detailList1.add(detail);
            partNumberToDetailIdCuringMap.put(partNumber, detail.getDetailIdCuring());

            // Reset totalKapa 
            totalKapa = 0;
        }

        // totalPlanDaily for each partNumber per day 
        for (ShiftMonthlyPlan shift : shiftMonthlyPlan) {
            BigDecimal partNumber = shift.getPART_NUMBER();
            final Date planDate = shift.getDATE();
            
            BigDecimal detailIdCuring = partNumberToDetailIdCuringMap.get(partNumber);

            // Skip if the combination of detailIdCuring and planDate already exists in detailList2
            boolean isAlreadyProcessed = detailList2.stream()
                .anyMatch(detail -> detail.getDetailIdCuring().equals(detailIdCuring) && detail.getDateDailyMp().equals(planDate));
            
            if (isAlreadyProcessed) {
                continue; 
            }

            for (ShiftMonthlyPlan shiftInner : shiftMonthlyPlan) {
                if (partNumber.equals(shiftInner.getPART_NUMBER()) && planDate.equals(shiftInner.getDATE())) {
                    totalPlanDaily += shiftInner.getTOTAL_KAPA().intValue();
                }
            }

            DetailDailyMonthlyPlanCuring detail = new DetailDailyMonthlyPlanCuring();
            detail.setDetailDailyIdCuring(detailDailyId);
            detail.setDetailIdCuring(detailIdCuring);
            detail.setDateDailyMp(planDate);
            detail.setTotalPlan(BigDecimal.valueOf(totalPlanDaily));
            
            detailDailyId = detailDailyId.add(BigDecimal.ONE); 
            detailList2.add(detail);

            // Reset totalPlanDaily 
            totalPlanDaily = 0;
            BigDecimal partNum = shift.getPART_NUMBER();
            if (productDetails.stream().noneMatch(detail1 -> detail1.get("partNumber").equals(partNum))) {
                String description2 = shiftMonthlyRepo.findDescriptionByPartNum(partNum);
                Map<String, Object> detail2 = new HashMap<>();
                detail2.put("partNumber", partNum);
                detail2.put("description", description2 != null ? description2 : "N/A");

                productDetails.add(detail2);
            }
        }
        
        List<Map<String, Object>> descriptionWD = monthlyPlanningRepo.getDescriptionWD(month, year);

        viewMonthlyPlanning.setDetailMonthlyPlanCuring(detailList1);
        viewMonthlyPlanning.setDetailDailyMonthlyPlanCuring(detailList2);
        viewMonthlyPlanning.setShiftMonthlyPlan(shiftMonthlyPlan);
        viewMonthlyPlanning.setChangeMould(changeMouldList);
        viewMonthlyPlanning.setDescription(descriptionWD);
        viewMonthlyPlanning.setProductDetails(productDetails);
        
        System.out.println("Data shift: " + shiftMonthlyPlan.size());
        System.out.println("Data list1 (detailMonthlyPlanCuring): " + detailList1.size());
        System.out.println("Data list2 (detailDailyMonthlyPlanCuring): " + detailList2.size());

        return viewMonthlyPlanning;
    }

}