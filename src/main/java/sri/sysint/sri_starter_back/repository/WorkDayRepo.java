package sri.sysint.sri_starter_back.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.DWorkDayHours;
import sri.sysint.sri_starter_back.model.WorkDay;

public interface WorkDayRepo extends JpaRepository<WorkDay, Date> {
    
	@Query(value = "SELECT * FROM SRI_IMPP_M_WD WHERE TRUNC(DATE_WD) = TO_DATE(:id, 'DD-MM-YYYY')", nativeQuery = true)
	Optional<WorkDay> findByDDateWd(@Param("id") String id);

    @Query(value = "SELECT * FROM SRI_IMPP_M_WD ORDER BY DATE_WD ASC", nativeQuery = true)
    List<WorkDay> getDataOrderByDateWd();
    
	@Query(value = "SELECT * FROM SRI_IMPP_M_WD WHERE STATUS = 1", nativeQuery = true)
	List<WorkDay> findWorkDayActive();
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_WD WHERE DATE_WD BETWEEN TO_DATE(:startDate, 'DD-MM-YYYY') AND TO_DATE(:endDate, 'DD-MM-YYYY') ORDER BY DATE_WD ASC", nativeQuery = true)
	List<WorkDay> findAllByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
    @Query(value = "SELECT * " +
	        "FROM SRI_IMPP_M_WD " +
	        "WHERE EXTRACT(MONTH FROM DATE_WD) = :month " +
	        "AND EXTRACT(YEAR FROM DATE_WD) = :year", nativeQuery = true)
	List<WorkDay> findByMonthYear(@Param("month") int month,@Param("year") int year);
    
    @Query(value = 
    	    "SELECT " +
    	    "   HS.DATE_WD, " +
    	    "   D3.DATE_WD AS D3_DATE_WD, " +
    	    "   HS.SHIFT1_START_TIME, " +
    	    "   HS.SHIFT1_END_TIME, " +
    	    "   D1.DESCRIPTION AS SHIFT1_DESCRIPTION, " +
    	    "   CASE " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TL' THEN WD.IOT_TL_1 " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TT' THEN WD.IOT_TT_1 " +
    	    "       ELSE NULL " +
    	    "   END AS SHIFT1_WD, " +

    	    "   HS.SHIFT2_START_TIME, " +
    	    "   HS.SHIFT2_END_TIME, " +
    	    "   D2.DESCRIPTION AS SHIFT2_DESCRIPTION, " +
    	    "   CASE " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TL' THEN WD.IOT_TL_2 " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TT' THEN WD.IOT_TT_2 " +
    	    "       ELSE NULL " +
    	    "   END AS SHIFT2_WD, " +

    	    "   HS.SHIFT3_START_TIME, " +
    	    "   HS.SHIFT3_END_TIME, " +
    	    "   D3.DESCRIPTION AS SHIFT3_DESCRIPTION, " +
    	    "   CASE " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TL' THEN WD.IOT_TL_3 " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TT' THEN WD.IOT_TT_3 " +
    	    "       ELSE NULL " +
    	    "   END AS SHIFT3_WD, " +

    	    "   HS.DESCRIPTION " +

    	    "FROM " +
    	    "   SRI_IMPP_D_WD_HOURS_SPECIFIC HS " +

    	    "LEFT JOIN ( " +
    	    "   SELECT * FROM ( " +
    	    "       SELECT D1.*, ROW_NUMBER() OVER (PARTITION BY TRUNC(D1.DATE_WD), D1.PARENT ORDER BY D1.LAST_UPDATE_DATE) rn " +
    	    "       FROM SRI_IMPP_D_WD D1 " +
    	    "   ) WHERE rn = 1 " +
    	    ") D1 ON TRUNC(D1.DATE_WD) = TRUNC(HS.DATE_WD) " +
    	    "   AND D1.PARENT = REPLACE(HS.DESCRIPTION, '_', ' ') || ' SHIFT 1' " +

    	    "LEFT JOIN ( " +
    	    "   SELECT * FROM ( " +
    	    "       SELECT D2.*, ROW_NUMBER() OVER (PARTITION BY TRUNC(D2.DATE_WD), D2.PARENT ORDER BY D2.LAST_UPDATE_DATE) rn " +
    	    "       FROM SRI_IMPP_D_WD D2 " +
    	    "   ) WHERE rn = 1 " +
    	    ") D2 ON TRUNC(D2.DATE_WD) = TRUNC(HS.DATE_WD) " +
    	    "   AND D2.PARENT = REPLACE(HS.DESCRIPTION, '_', ' ') || ' SHIFT 2' " +

    	    "LEFT JOIN ( " +
    	    "   SELECT * FROM ( " +
    	    "       SELECT D3.*, ROW_NUMBER() OVER (PARTITION BY TRUNC(D3.DATE_WD), D3.PARENT ORDER BY D3.LAST_UPDATE_DATE) rn " +
    	    "       FROM SRI_IMPP_D_WD D3 " +
    	    "   ) WHERE rn = 1 " +
    	    ") D3 ON TRUNC(D3.DATE_WD) = TRUNC(HS.DATE_WD) " +
    	    "   AND D3.PARENT = REPLACE(HS.DESCRIPTION, '_', ' ') || ' SHIFT 3' " +

    	    "JOIN SRI_IMPP_M_WD WD ON TRUNC(WD.DATE_WD) = TRUNC(HS.DATE_WD) " +

    	    "WHERE " +
    	    "   HS.DESCRIPTION IN ('OT_TT', 'OT_TL') " +
    	    "   AND TRUNC(HS.DATE_WD) BETWEEN :startDate AND :endDate " +

    	    "ORDER BY " +
    	    "   HS.DATE_WD, " +
    	    "   CASE " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TT' THEN 1 " +
    	    "       WHEN HS.DESCRIPTION = 'OT_TL' THEN 2 " +
    	    "   END "
    	, nativeQuery = true)
    	List<Object[]> findShiftDetailsByDateRange(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);


}
