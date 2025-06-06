package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import sri.sysint.sri_starter_back.model.PMStop;

public interface PMStopRepo extends JpaRepository<PMStop, BigDecimal>{
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_STOP WHERE STOP_MACHINE_ID = :id", nativeQuery = true)
    Optional<PMStop> findById(@Param("id") BigDecimal id);
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_STOP ORDER BY STOP_MACHINE_ID ASC", nativeQuery = true)
    List<PMStop> getDataOrderId();
	
	@Query(value = "SELECT COUNT(*) FROM SRI_IMPP_M_MACHINE_STOP", nativeQuery = true)
    BigDecimal getNewId();
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_STOP WHERE STATUS = 1", nativeQuery = true)
	List<PMStop> findPMStopActive();
	
    @Query(value = "SELECT * FROM SRI_IMPP_M_MACHINE_STOP WHERE WORK_CENTER_TEXT = :name", nativeQuery = true)
    Optional<PMStop> findByWct(@Param("name") String name);
    
    @Procedure(procedureName = "INSERT_MACHINE_STOP")
    void insertMachineStop(
        @Param("p_WORK_CENTER_TEXT") String workCenterText,
        @Param("p_CREATED_BY") String createdBy,
        @Param("p_LAST_UPDATED_BY") String lastUpdatedBy,
        @Param("p_DATE_STOP") Date dateStop,
        @Param("p_START_TIME") Timestamp startTime,
        @Param("p_END_TIME") Timestamp endTime
    );
}
