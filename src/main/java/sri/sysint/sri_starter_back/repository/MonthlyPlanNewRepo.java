package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sri.sysint.sri_starter_back.model.DetailDailyMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.DetailMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.MonthlyPlanningNew;
import sri.sysint.sri_starter_back.model.MonthlyPlanningCuring;

public interface MonthlyPlanNewRepo extends JpaRepository<MonthlyPlanningNew, String>{
	
	List<MonthlyPlanningNew> findByMoIdInAndVersion(List<String> moIds, BigDecimal version);
	
	List<MonthlyPlanningNew> findByMoIdIn(List<String> moIds);
	
	@Query(
		    value = "SELECT " +
		            "mp.ITEM_CURING, " +
		            "LISTAGG(DISTINCT mp.WCT, ', ') WITHIN GROUP (ORDER BY mp.WCT) AS WCT_List, " +
		            "SUM(mp.TOTAL_HARIAN) AS TOTAL, " +
		            "MAX(tp.TOTAL_GROSS) AS Gross, " +
		            "MAX(tp.TOTAL_PLAN) AS Net, " +
		            "(MAX(tp.TOTAL_GROSS) - MAX(tp.TOTAL_PLAN)) AS Selisih " +
		            "FROM SRI_IMPP_T_MONTHLYPLAN1 mp " +
		            "JOIN SRI_IMPP_D_TOTALPLAN tp ON mp.ITEM_CURING = tp.ITEM_CURING " +
		            "WHERE mp.MO_ID IN (:moIds) " +
		            "AND tp.ID_MO IN (:moIds) " +
		            "GROUP BY mp.ITEM_CURING " +
		            "ORDER BY mp.ITEM_CURING",
		    nativeQuery = true
		)
		List<Map<String, Object>> getMonthlyPlanSummaryByMoIds(@Param("moIds") List<String> moIds);

	
	
	@Query(value = "SELECT MAX(VERSION) FROM SRI_IMPP_T_MONTHLYPLAN1 WHERE MO_ID IN (:moIds)", nativeQuery = true)
	BigDecimal findLatestVersionsByMoIds(@Param("moIds") List<String> moIds);

	
	@Query(value = "SELECT COUNT(*) FROM SRI_IMPP_T_MONTHLYPLAN", nativeQuery = true)
    BigDecimal getNewId();
	
	@Query(value = "SELECT * FROM SRI_IMPP_T_MONTHLYPLAN WHERE MO_ID = :id", nativeQuery = true)
    List<MonthlyPlanningNew> findByMpCuringId(@Param("id") String id);

    @Modifying
    @Query(value = "BEGIN SP_GENERATE_MP_10(:jsonInput); END;", nativeQuery = true)
    void callGenerateMp(@Param("jsonInput") String jsonInput);
    
    @Modifying
    @Query(value = "BEGIN SP_SAVETOTALPLAN(:jsonInput); END;", nativeQuery = true)
    void saveTotalPlan(@Param("jsonInput") String jsonInput);
    
    @Modifying
    @Query(value = "BEGIN SP_CalculateMouldNeeded(:jsonInput); END;", nativeQuery = true)
    void hitungMould(@Param("jsonInput") String jsonInput);
    
    @Modifying
    @Query(value = "BEGIN SP_AFTER_GENERATE(:moId, :cheatingId); END;", nativeQuery = true)
    void callAfterGenerate(@Param("moId") String moId, @Param("cheatingId") BigDecimal cheatingId);


//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM SRI_IMPP_D_TOTALPLAN", nativeQuery = true)
//    void deleteFromTotalPlan();
//
//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM SRI_IMPP_T_MONTHLYPLAN1", nativeQuery = true)
//    void deleteFromMonthlyPlan1();
}