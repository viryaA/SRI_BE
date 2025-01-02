	package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.MachineTass;
import sri.sysint.sri_starter_back.model.Plant;


public interface MachineTassRepo extends JpaRepository<MachineTass, BigDecimal>{
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_TASS WHERE ID_MACHINE_TASS = :id", nativeQuery = true)
    Optional<MachineTass> findById(@Param("id") String id);
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_TASS ORDER BY ID_MACHINE_TASS ASC", nativeQuery = true)
    List<MachineTass> getDataOrderId();
	
	@Query(value = "SELECT COUNT(*) FROM SRI_IMPP_M_MACHINE_TASS", nativeQuery = true)
    BigDecimal getNewId();
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_TASS WHERE STATUS = 1", nativeQuery = true)
	List<MachineTass> findMachineTassActive();
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_TASS WHERE WORK_CENTER_TEXT = :wct", nativeQuery = true)
    Optional<MachineTass> findByWct(@Param("wct") String wct);
}
