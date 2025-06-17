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
                "AND mp.VERSION = :versionParam " +
                "AND tp.ID_MO IN (:moIds) " +
                "GROUP BY mp.ITEM_CURING " +
                "ORDER BY mp.ITEM_CURING",
        nativeQuery = true
    )
    List<Map<String, Object>> getMonthlyPlanSummaryByMoIds(@Param("moIds") List<String> moIds,@Param("versionParam") BigDecimal versionParam);

	
	
	@Query(value = "SELECT MAX(VERSION) FROM SRI_IMPP_T_MONTHLYPLAN1 WHERE MO_ID IN (:moIds)", nativeQuery = true)
	BigDecimal findLatestVersionsByMoIds(@Param("moIds") List<String> moIds);

	
	@Query(value = "SELECT COUNT(*) FROM SRI_IMPP_T_MONTHLYPLAN", nativeQuery = true)
    BigDecimal getNewId();
	
	@Query(value = "SELECT * FROM SRI_IMPP_T_MONTHLYPLAN WHERE MO_ID = :id", nativeQuery = true)
    List<MonthlyPlanningNew> findByMpCuringId(@Param("id") String id);

    @Modifying
    @Query(value = "BEGIN SP_SAVE_CHANGE_MOULD_RESULT (:jsonInput); END;", nativeQuery = true)
    void callChangeMouldResult(@Param("jsonInput") String jsonInput);
	
    @Modifying
    @Query(value = "BEGIN SP_BUAT_MP_15(:jsonInput); END;", nativeQuery = true)
    void callGenerateMp1(@Param("jsonInput") String jsonInput);

    @Modifying
    @Query(value = "BEGIN SP_BUAT_MP_9(:jsonInput); END;", nativeQuery = true)
    void callGenerateMp2(@Param("jsonInput") String jsonInput);

    @Modifying
    @Query(value = "BEGIN SP_SAVE_TOTALPLAN1(:jsonInput); END;", nativeQuery = true)
    void saveTotalPlan(@Param("jsonInput") String jsonInput);
    
    @Modifying
    @Query(value = "BEGIN SP_HITUNG_MOULD(:jsonInput); END;", nativeQuery = true)
    void hitungMould(@Param("jsonInput") String jsonInput);
    
    @Modifying
    @Query(value = "BEGIN SP_AFTER_GENERATE(:moId, :cheatingId); END;", nativeQuery = true)
    void callAfterGenerate(@Param("moId") String moId, @Param("cheatingId") BigDecimal cheatingId);

	@Query(value = "SELECT " +
            "ORIGINAL_ITEM_CURING, " +
            "ORIGINAL_DATE, " +
            "SHIFT_STOP, " +
            "WORK_CENTER_TEXT, " +
            "REUSED_DATE, " +
            "REUSED_ITEM_CURING, " +
            "SHIFT_START " +
            "FROM SRI_IMPP_CHANGE_MOULD " +
            "WHERE MO_IDS = :moId AND VERSION = :version", nativeQuery = true)
    List<Map<String, Object>> findMouldChangesByMoIdAndVersion(@Param("moId") String moId, @Param("version") int version);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SRI_IMPP_CHANGE_MOULD ( " +
            "    VERSION, ORIGINAL_ITEM_CURING, ORIGINAL_DATE, SHIFT_STOP, " +
            "    WORK_CENTER_TEXT, REUSED_DATE, REUSED_ITEM_CURING, SHIFT_START, MO_IDS) " +
            "WITH numbers AS ( " +
            "  SELECT LEVEL AS n FROM dual CONNECT BY LEVEL <= 100 " +
            "), " +
            "changemould_old AS ( " +
            "  SELECT ITEM_CURING, " +
            "         LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_OLD_LIST, " +
            "         MAX(MOULD_USE) AS MOULD_USE_OLD, " +
            "         DATE_VALID + 1 AS NEXT_DATE, " +
            "         CASE " +
            "           WHEN MAX(SHIFT_1) > 0 THEN 'SHIFT_1' " +
            "           WHEN MAX(SHIFT_2) > 0 THEN 'SHIFT_2' " +
            "           WHEN MAX(SHIFT_3) > 0 THEN 'SHIFT_3' " +
            "           ELSE 'NONE' " +
            "         END AS SHIFT_START, " +
            "         CASE " +
            "           WHEN MAX(SHIFT_1) = 0 THEN 'SHIFT_1' " +
            "           WHEN MAX(SHIFT_2) = 0 THEN 'SHIFT_2' " +
            "           WHEN MAX(SHIFT_3) = 0 THEN 'SHIFT_3' " +
            "           ELSE NULL " +
            "         END AS SHIFT_STOP " +
            "  FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "  WHERE MO_ID IN (:moId1, :moId2) " +
            "  GROUP BY ITEM_CURING, DATE_VALID " +
            "), " +
            "changemould_new AS ( " +
            "  SELECT ITEM_CURING, DATE_VALID, " +
            "         LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_NEW_LIST, " +
            "         MAX(MOULD_USE) AS MOULD_USE_NEW, " +
            "         CASE " +
            "           WHEN MAX(SHIFT_1) > 0 THEN 'SHIFT_1' " +
            "           WHEN MAX(SHIFT_2) > 0 THEN 'SHIFT_2' " +
            "           WHEN MAX(SHIFT_3) > 0 THEN 'SHIFT_3' " +
            "           ELSE 'NONE' " +
            "         END AS SHIFT_START, " +
            "         CASE " +
            "           WHEN MAX(SHIFT_1) = 0 THEN 'SHIFT_1' " +
            "           WHEN MAX(SHIFT_2) = 0 THEN 'SHIFT_2' " +
            "           WHEN MAX(SHIFT_3) = 0 THEN 'SHIFT_3' " +
            "           ELSE NULL " +
            "         END AS SHIFT_STOP " +
            "  FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "  WHERE MO_ID IN (:moId1, :moId2) " +
            "  GROUP BY ITEM_CURING, DATE_VALID " +
            "), " +
            "OldWCT AS ( " +
            "  SELECT o.ITEM_CURING, o.NEXT_DATE, o.SHIFT_STOP, " +
            "         TRIM(REGEXP_SUBSTR(o.WCT_OLD_LIST, '[^,]+', 1, n.n)) AS WCT " +
            "  FROM changemould_old o " +
            "  JOIN numbers n ON n.n <= REGEXP_COUNT(o.WCT_OLD_LIST, ',') + 1 " +
            "), " +
            "NewWCT AS ( " +
            "  SELECT n.ITEM_CURING, n.DATE_VALID, n.SHIFT_STOP, n.SHIFT_START, " +
            "         TRIM(REGEXP_SUBSTR(n.WCT_NEW_LIST, '[^,]+', 1, n2.n)) AS WCT " +
            "  FROM changemould_new n " +
            "  JOIN numbers n2 ON n2.n <= REGEXP_COUNT(n.WCT_NEW_LIST, ',') + 1 " +
            "), " +
            "OldNotInNewSameDate AS ( " +
            "  SELECT o.ITEM_CURING, o.NEXT_DATE, o.SHIFT_STOP, o.WCT " +
            "  FROM OldWCT o " +
            "  LEFT JOIN NewWCT n ON o.ITEM_CURING = n.ITEM_CURING AND o.NEXT_DATE = n.DATE_VALID AND o.WCT = n.WCT " +
            "  WHERE n.WCT IS NULL " +
            "), " +
            "ReusedOnOtherDateRanked AS ( " +
            "  SELECT o.ITEM_CURING AS ORIGINAL_ITEM_CURING, o.NEXT_DATE AS ORIGINAL_DATE, o.SHIFT_STOP, o.WCT, " +
            "         n.DATE_VALID AS REUSED_DATE, n.ITEM_CURING AS REUSED_ITEM_CURING, n.SHIFT_START, " +
            "         ROW_NUMBER() OVER (PARTITION BY o.ITEM_CURING, o.NEXT_DATE, o.WCT ORDER BY n.DATE_VALID) AS rn " +
            "  FROM OldNotInNewSameDate o " +
            "  JOIN NewWCT n ON o.WCT = n.WCT " +
            "  WHERE n.DATE_VALID > o.NEXT_DATE " +
            ") " +
            "SELECT :version, ORIGINAL_ITEM_CURING, ORIGINAL_DATE, SHIFT_STOP, WCT, REUSED_DATE, REUSED_ITEM_CURING, SHIFT_START, :moIds " +
            "FROM ReusedOnOtherDateRanked WHERE rn = 1 ORDER BY WCT, ORIGINAL_DATE, REUSED_DATE",
            nativeQuery = true)
    void runChangeMouldQuery(@Param("moId1") String moId1,@Param("moId2") String moId2,@Param("version") int version,@Param("moIds") String moIds);

    
    @Query(value = "SELECT NVL(MAX(VERSION), 0) + 1 " +
                "FROM SRI_IMPP_CHANGE_MOULD " +
                "WHERE MO_IDS = :moId", 
        nativeQuery = true)
    int getChangeMouldVersion(@Param("moId") String moId);
    @Modifying
    @Query(
        value = "WITH numbers AS ( " +
                "  SELECT LEVEL AS n FROM dual CONNECT BY LEVEL <= 100 " +
                "), " +
                "changemould_old AS ( " +
                "  SELECT ITEM_CURING, " +
                "         LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_OLD_LIST, " +
                "         MAX(MOULD_USE) AS MOULD_USE_OLD, " +
                "         DATE_VALID + 1 AS NEXT_DATE, " +
                "         CASE " +
                "           WHEN MAX(SHIFT_1) > 0 THEN 'SHIFT_1' " +
                "           WHEN MAX(SHIFT_2) > 0 THEN 'SHIFT_2' " +
                "           WHEN MAX(SHIFT_3) > 0 THEN 'SHIFT_3' " +
                "           ELSE 'NONE' " +
                "         END AS SHIFT_START, " +
                "         CASE " +
                "           WHEN MAX(SHIFT_1) = 0 THEN 'SHIFT_1' " +
                "           WHEN MAX(SHIFT_2) = 0 THEN 'SHIFT_2' " +
                "           WHEN MAX(SHIFT_3) = 0 THEN 'SHIFT_3' " +
                "           ELSE NULL " +
                "         END AS SHIFT_STOP " +
                "  FROM SRI_IMPP_T_MONTHLYPLAN1 " +
                "  WHERE MO_ID IN (:moIds) " +
                "  AND VERSION  = :version " +
                "  GROUP BY ITEM_CURING, DATE_VALID " +
                "), " +
                "changemould_new AS ( " +
                "  SELECT ITEM_CURING, DATE_VALID, " +
                "         LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_NEW_LIST, " +
                "         MAX(MOULD_USE) AS MOULD_USE_NEW, " +
                "         CASE WHEN MAX(SHIFT_1) > 0 THEN 'SHIFT_1' " +
                "              WHEN MAX(SHIFT_2) > 0 THEN 'SHIFT_2' " +
                "              WHEN MAX(SHIFT_3) > 0 THEN 'SHIFT_3' " +
                "              ELSE 'NONE' END AS SHIFT_START, " +
                "         CASE WHEN MAX(SHIFT_1) = 0 THEN 'SHIFT_1' " +
                "              WHEN MAX(SHIFT_2) = 0 THEN 'SHIFT_2' " +
                "              WHEN MAX(SHIFT_3) = 0 THEN 'SHIFT_3' " +
                "              ELSE NULL END AS SHIFT_STOP " +
                "  FROM SRI_IMPP_T_MONTHLYPLAN1 " +
                "  WHERE MO_ID IN (:moIds) " +
                "  AND VERSION  = :version " +
                "  GROUP BY ITEM_CURING, DATE_VALID " +
                "), " +
                "OldWCT AS ( " +
                "  SELECT o.ITEM_CURING, o.NEXT_DATE, o.SHIFT_STOP, " +
                "         TRIM(REGEXP_SUBSTR(o.WCT_OLD_LIST, '[^,]+', 1, n.n)) AS WCT " +
                "  FROM changemould_old o " +
                "  JOIN numbers n ON n.n <= REGEXP_COUNT(o.WCT_OLD_LIST, ',') + 1 " +
                "), " +
                "NewWCT AS ( " +
                "  SELECT n.ITEM_CURING, n.DATE_VALID, n.SHIFT_STOP, n.SHIFT_START, " +
                "         TRIM(REGEXP_SUBSTR(n.WCT_NEW_LIST, '[^,]+', 1, n2.n)) AS WCT " +
                "  FROM changemould_new n " +
                "  JOIN numbers n2 ON n2.n <= REGEXP_COUNT(n.WCT_NEW_LIST, ',') + 1 " +
                "), " +
                "OldNotInNewSameDate AS ( " +
                "  SELECT o.ITEM_CURING, o.NEXT_DATE, o.SHIFT_STOP, o.WCT " +
                "  FROM OldWCT o " +
                "  LEFT JOIN NewWCT n ON o.ITEM_CURING = n.ITEM_CURING AND o.NEXT_DATE = n.DATE_VALID AND o.WCT = n.WCT " +
                "  WHERE n.WCT IS NULL " +
                "), " +
                "ReusedOnOtherDateRanked AS ( " +
                "  SELECT o.ITEM_CURING AS ORIGINAL_ITEM_CURING, o.NEXT_DATE AS ORIGINAL_DATE, " +
                "         o.SHIFT_STOP, o.WCT, n.DATE_VALID AS REUSED_DATE, " +
                "         n.ITEM_CURING AS REUSED_ITEM_CURING, n.SHIFT_START, " +
                "         ROW_NUMBER() OVER (PARTITION BY o.ITEM_CURING, o.NEXT_DATE, o.WCT ORDER BY n.DATE_VALID) AS rn " +
                "  FROM OldNotInNewSameDate o " +
                "  JOIN NewWCT n ON o.WCT = n.WCT " +
                "  WHERE n.DATE_VALID > o.NEXT_DATE " +
                ") " +
                "SELECT ORIGINAL_ITEM_CURING, ORIGINAL_DATE, SHIFT_STOP, WCT, REUSED_DATE, REUSED_ITEM_CURING, SHIFT_START " +
                "FROM ReusedOnOtherDateRanked " +
                "WHERE rn = 1 AND ORIGINAL_ITEM_CURING != REUSED_ITEM_CURING " +
                "ORDER BY WCT, ORIGINAL_DATE, REUSED_DATE",
        nativeQuery = true
    )
    List<Map<String, Object>> findMouldChangeData(@Param("moIds") List<String> moIds, @Param("version") BigDecimal version);

// Correct for Java 1.8
    @Query(value =
            "SELECT " +
            "    DATE_VALID, " +
            "    SUM( " +
            "        CASE " +
            "            WHEN TOTAL_USE > CAVITY THEN CAVITY " +
            "            ELSE TOTAL_USE " +
            "        END " +
            "    ) AS TOTAL_MOULD_USE_HARIAN " +
            "FROM ( " +
            "    SELECT " +
            "        DATE_VALID, " +
            "        WCT, " +
            "        SUM(MOULD_USE) AS TOTAL_USE, " +
            "        CAST(REGEXP_SUBSTR(WCT, ' (\\d+) ', 1, 1) AS NUMBER) AS CAVITY " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID IN (:moIds) " +
            "      AND VERSION = :version " +
            "    GROUP BY DATE_VALID, WCT " +
            ") subquery_alias " +
            "GROUP BY DATE_VALID " +
            "ORDER BY DATE_VALID", nativeQuery = true)
    List<Map<String, Object>> findDailyMouldUseSummaryAsMap(
            @Param("moIds") List<String> moIds,
            @Param("version") BigDecimal version);
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