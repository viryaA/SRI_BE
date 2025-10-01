package sri.sysint.sri_starter_back.repository;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sri.sysint.sri_starter_back.model.MachineProduct;

public interface MachineProductRepo extends JpaRepository<MachineProduct, BigDecimal>{
    @Query("SELECT m FROM MachineProduct m WHERE m.moId1 IN (:moId1, :moId2) AND m.moId2 IN (:moId1, :moId2) AND m.versionCheating = :verCheating")
    List<MachineProduct> findCheatingMacProdByMoIdAndVcheating(@Param("moId1") String moId1,
                                                                @Param("moId2") String moId2,
                                                                @Param("verCheating") BigDecimal verCheating);
                                                                

    @Query("SELECT m FROM MachineProduct m WHERE m.moId1 IN (:moId1, :moId2) AND m.moId2 IN (:moId1, :moId2) AND m.versionCheating = :verCheating AND m.itemCuring = :itemCuring")
    List<MachineProduct> findCheatingMacProdByMoIdVcheatingandItemCuring(@Param("moId1") String moId1,
                                                                          @Param("moId2") String moId2,
                                                                          @Param("verCheating") BigDecimal verCheating,
                                                                          @Param("itemCuring") String itemCuring);

    @Query("SELECT m FROM MachineProduct m WHERE m.moId1 IN (:moId1, :moId2) AND m.moId2 IN (:moId1, :moId2)")
        List<MachineProduct> findCheatingMacProdByMoId(@Param("moId1") String moId1,
                                                                    @Param("moId2") String moId2);   

    @Transactional
    @Procedure(name = "SaveMachineProducts")
    void saveMachineProducts(@Param("p_json_input") String jsonInput);

}
