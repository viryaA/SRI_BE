package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sri.sysint.sri_starter_back.model.MachineProduct;

public interface MachineProductRepo extends JpaRepository<MachineProduct, BigDecimal>{
	 	@Modifying
	    @Transactional
	    @Query(value = "INSERT INTO SRI_IMPP_M_MACHINE_PRODUCT (PART_NUMBER, WORK_CENTER_TEXT) " +
	                   "VALUES (:partnum, :wct)", nativeQuery = true)
	    int insertNew(
	        @Param("partnum") BigDecimal partnum,
	        @Param("wct") String wct
	    );
	    
		@Modifying
	    @Transactional 
	    @Query(value = "DELETE FROM SRI_IMPP_M_MACHINE_PRODUCT", nativeQuery = true)
	    void deleteAll();
	

		@Query(value = "SELECT D.PART_NUMBER, NULL AS WORK_CENTER_TEXT " +
	               "FROM SRI_IMPP_D_MARKETINGORDER D " +
	               "JOIN SRI_IMPP_M_PRODUCT P ON D.PART_NUMBER = P.PART_NUMBER " +
	               "WHERE (D.MO_ID = :moId1 OR D.MO_ID = :moId2) " +
	               "AND P.ITEM_CURING = :itemCuring", nativeQuery = true)
		List<MachineProduct> findPartNumByCuring(@Param("moId1") String moId1, 
		                                          @Param("moId2") String moId2, 
		                                          @Param("itemCuring") String itemCuring);





}
