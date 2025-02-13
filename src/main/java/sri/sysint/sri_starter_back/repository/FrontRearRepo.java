package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sri.sysint.sri_starter_back.model.FrontRear;
@Repository
public interface FrontRearRepo extends JpaRepository<FrontRear, BigDecimal>{
	@Query(value = "SELECT * FROM SRI_IMPP_M_FRONT_REAR  WHERE FRONT_REAR_ID = :id", nativeQuery = true)
    Optional<FrontRear> findById(@Param("id") BigDecimal id);
	
	@Query(value = "SELECT * FROM SRI_IMPP_M_FRONT_REAR  WHERE FRONT_REAR_PARALLEL_ID = :id", nativeQuery = true)
	List<FrontRear> findListByIdParallel(@Param("id") BigDecimal id);

    @Query("SELECT f FROM FrontRear  f WHERE f.moId1 IN (:moId1, :moId2) " +
            "AND f.moId2 IN (:moId1, :moId2) " +
            "AND f.versionCheating = :verCheating")
     List<FrontRear> findCheatingFrontRearByMoIdAndVcheating(
             @Param("moId1") String moId1,
             @Param("moId2") String moId2,
             @Param("verCheating") BigDecimal verCheating);

     @Query("SELECT f FROM FrontRear  f WHERE f.moId1 IN (:moId1, :moId2) " +
            "AND f.moId2 IN (:moId1, :moId2) " +
            "AND f.versionCheating = :verCheating " +
            "AND f.itemCuring = :itemCuring")
     List<FrontRear> findCheatingFrontRearByMoIdVcheatingandItemCuring(
             @Param("moId1") String moId1,
             @Param("moId2") String moId2,
             @Param("verCheating") BigDecimal verCheating,
             @Param("itemCuring") String itemCuring);

     @Query("SELECT f FROM FrontRear  f WHERE f.moId1 IN (:moId1, :moId2) " +
            "AND f.moId2 IN (:moId1, :moId2) " +
            "AND f.versionCheating = :verCheating " +
            "AND f.frontRearParallelId = :parallelId")
     List<FrontRear> findCheatingFrontRearByMoIdVcheatingandParallelId(
             @Param("moId1") String moId1,
             @Param("moId2") String moId2,
             @Param("verCheating") BigDecimal verCheating,
             @Param("parallelId") BigDecimal parallelId);
     
     @Transactional
     @Procedure(name = "SaveFrontRears")
     void saveFrontRears(@Param("p_json_input") String jsonInput);
}
