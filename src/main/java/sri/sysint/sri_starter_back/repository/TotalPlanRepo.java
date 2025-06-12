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

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.TotalPlan;

public interface TotalPlanRepo extends JpaRepository<TotalPlan, BigDecimal>{

    boolean existsTotalPlanByMOID(String idMo);
    
    List<TotalPlan> findAllByMOIDIn(List<String> moids);

    
    void deleteByMOID(String idMo);

    @Modifying
    @Query(value = "CALL SP_SaveTotalPlan(:jsonInput)", nativeQuery = true)
    void GenerateTotalPlan(@Param("jsonInput") String jsonInput);

    @Query(value = "WITH prev_day AS ( " +
            "    SELECT " +
            "        ITEM_CURING, " +
            "        LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_OLD_LIST, " +
            "        MAX(MOULD_USE) AS MOULD_USE_OLD, " +
            "        DATE_VALID + 1 AS NEXT_DATE " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID IN (:moid1, :moid2) " +
            "      AND VERSION = :versionParam " +
            "    GROUP BY ITEM_CURING, DATE_VALID " +
            "), " +
            "curr_day AS ( " +
            "    SELECT " +
            "        ITEM_CURING, " +
            "        DATE_VALID, " +
            "        LISTAGG(WCT, ',') WITHIN GROUP (ORDER BY WCT) AS WCT_NEW_LIST, " +
            "        MAX(MOULD_USE) AS MOULD_USE_NEW " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID IN (:moid1, :moid2) " +
            "      AND VERSION = :versionParam " +
            "    GROUP BY ITEM_CURING, DATE_VALID " +
            "), " +
            "mould_changes AS ( " +
            "    SELECT " +
            "        c.ITEM_CURING, " +
            "        c.DATE_VALID, " +
            "        CASE " +
            "            WHEN p.WCT_OLD_LIST IS NOT NULL " +
            "                AND EXISTS ( " +
            "                    SELECT 1 FROM ( " +
            "                        SELECT REGEXP_SUBSTR(p.WCT_OLD_LIST, '[^,]+', 1, LEVEL) AS OLD_WCT FROM DUAL " +
            "                        CONNECT BY LEVEL <= REGEXP_COUNT(p.WCT_OLD_LIST, ',') + 1 " +
            "                    ) po " +
            "                    FULL OUTER JOIN ( " +
            "                        SELECT REGEXP_SUBSTR(c.WCT_NEW_LIST, '[^,]+', 1, LEVEL) AS NEW_WCT FROM DUAL " +
            "                        CONNECT BY LEVEL <= REGEXP_COUNT(c.WCT_NEW_LIST, ',') + 1 " +
            "                    ) pn " +
            "                    ON po.OLD_WCT = pn.NEW_WCT " +
            "                    WHERE po.OLD_WCT IS NULL OR pn.NEW_WCT IS NULL " +
            "                ) " +
            "            THEN 1 ELSE 0 " +
            "        END AS MOULD_CHANGE " +
            "    FROM curr_day c " +
            "    LEFT JOIN prev_day p " +
            "        ON c.ITEM_CURING = p.ITEM_CURING " +
            "        AND c.DATE_VALID = p.NEXT_DATE " +
            "), " +
            "change_mould_summary AS ( " +
            "    SELECT " +
            "        DATE_VALID, " +
            "        SUM(MOULD_CHANGE) AS JUMLAH_CHANGE_MOULD " +
            "    FROM mould_changes " +
            "    GROUP BY DATE_VALID " +
            ") " +
            "SELECT " +
            "    b.DATE_VALID, " +
            "    SUM(d.MOULD_USE) AS TOTAL_MOULD_USE_HARIAN, " +
            "    b.TOTAL_HARIAN_PER_TANGGAL, " +
            "    COALESCE(a.TOTAL_HARIAN_CM, 0) AS TOTAL_HARIAN_TT, " +
            "    COALESCE(c.TOTAL_HARIAN_CN, 0) AS TOTAL_HARIAN_TL, " +
            "    ROUND((COALESCE(a.TOTAL_HARIAN_CM, 0) / NULLIF(b.TOTAL_HARIAN_PER_TANGGAL, 0)) * 100) AS PERSENTASE_TT, " +
            "    ROUND((COALESCE(c.TOTAL_HARIAN_CN, 0) / NULLIF(b.TOTAL_HARIAN_PER_TANGGAL, 0)) * 100) AS PERSENTASE_TL, " +
            "    COALESCE(m.JUMLAH_CHANGE_MOULD, 0) AS JUMLAH_CHANGE_MOULD " +
            "FROM " +
            "    (SELECT DATE_VALID, SUM(TOTAL_HARIAN) AS TOTAL_HARIAN_CM " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID BETWEEN :moid1 AND :moid2 " +
            "      AND VERSION = :versionParam " +
            "      AND ITEM_CURING LIKE '%CM%' " +
            "    GROUP BY DATE_VALID) a " +
            "FULL JOIN " +
            "    (SELECT DATE_VALID, SUM(TOTAL_HARIAN) AS TOTAL_HARIAN_CN " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID BETWEEN :moid1 AND :moid2 " +
            "      AND VERSION = :versionParam " +
            "      AND ITEM_CURING LIKE '%CN%' " +
            "    GROUP BY DATE_VALID) c " +
            "ON a.DATE_VALID = c.DATE_VALID " +
            "FULL JOIN " +
            "    (SELECT DATE_VALID, SUM(TOTAL_HARIAN) AS TOTAL_HARIAN_PER_TANGGAL " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID BETWEEN :moid1 AND :moid2 " +
            "      AND VERSION = :versionParam " +
            "    GROUP BY DATE_VALID) b " +
            "ON a.DATE_VALID = b.DATE_VALID OR c.DATE_VALID = b.DATE_VALID " +
            "FULL JOIN " +
            "    (SELECT DATE_VALID, SUM(MOULD_USE) AS MOULD_USE " +
            "    FROM SRI_IMPP_T_MONTHLYPLAN1 " +
            "    WHERE MO_ID BETWEEN :moid1 AND :moid2 " +
            "      AND VERSION = :versionParam " +
            "    GROUP BY DATE_VALID) d " +
            "ON b.DATE_VALID = d.DATE_VALID " +
            "FULL JOIN change_mould_summary m " +
            "ON b.DATE_VALID = m.DATE_VALID " +
            "GROUP BY b.DATE_VALID, " +
            "b.TOTAL_HARIAN_PER_TANGGAL, " +
            "a.TOTAL_HARIAN_CM, " +
            "c.TOTAL_HARIAN_CN, " +
            "m.JUMLAH_CHANGE_MOULD " +
            "ORDER BY b.DATE_VALID",
        nativeQuery = true)
    List<Map<String, Object>> getDetailTotalPlan(
        @Param("moid1") String moid1,
        @Param("moid2") String moid2,
        @Param("versionParam") BigDecimal versionParam);

    @Query(value = "SELECT MAX(VERSION) AS NEWEST_VERSION " +
                "FROM SRI_IMPP_T_MONTHLYPLAN1 " +
                "WHERE MO_ID IN (:moid1, :moid2)", nativeQuery = true)
    BigDecimal getNewestVersion(@Param("moid1") String moid1, @Param("moid2") String moid2);


}
