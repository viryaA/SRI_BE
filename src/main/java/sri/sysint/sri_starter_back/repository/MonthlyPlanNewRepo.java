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
    @Query(value = "BEGIN SP_HITUNGMOULD(:jsonInput); END;", nativeQuery = true)
    void hitungMould(@Param("jsonInput") String jsonInput);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SRI_IMPP_D_TOTALPLAN", nativeQuery = true)
    void deleteFromTotalPlan();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SRI_IMPP_T_MONTHLYPLAN1", nativeQuery = true)
    void deleteFromMonthlyPlan1();
}