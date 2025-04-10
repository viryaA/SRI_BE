package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.Size;
import sri.sysint.sri_starter_back.model.StatusMP;

public interface StatusMPRepo extends JpaRepository<StatusMP, BigDecimal>{
	@Query(value = "SELECT STATUS_MP FROM SRI_IMPP_B_STATUSMP ORDER BY STATUS_ID DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    String findLatestStatusMP();
}
