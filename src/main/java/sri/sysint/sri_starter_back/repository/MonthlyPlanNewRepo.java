package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sri.sysint.sri_starter_back.model.DetailDailyMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.DetailMonthlyPlanCuring;
import sri.sysint.sri_starter_back.model.MonthlyPlanningNew;
import sri.sysint.sri_starter_back.model.MonthlyPlanningCuring;

public interface MonthlyPlanNewRepo extends JpaRepository<MonthlyPlanningNew, String>{
	@Query(value = "SELECT COUNT(*) FROM SRI_IMPP_T_MONTHLYPLAN", nativeQuery = true)
    BigDecimal getNewId();
	
	@Query(value = "SELECT * FROM SRI_IMPP_T_MONTHLYPLAN WHERE MO_ID = :id", nativeQuery = true)
    List<MonthlyPlanningNew> findByMpCuringId(@Param("id") String id);
}