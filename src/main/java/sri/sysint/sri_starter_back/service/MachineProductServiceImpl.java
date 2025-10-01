package sri.sysint.sri_starter_back.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.MachineProduct;
import sri.sysint.sri_starter_back.repository.MachineProductRepo;

@Service
@Transactional
public class MachineProductServiceImpl {
	@Autowired
    private MachineProductRepo machineProductRepo;

	public MachineProductServiceImpl(MachineProductRepo machineProductRepo) {
        this.machineProductRepo = machineProductRepo;
    }
	
    public void saveMachineProducts(String jsonInput) {
    	machineProductRepo.saveMachineProducts(jsonInput);
    }
    
    public List<MachineProduct> findCheatingMacProdByMoIdAndVcheating(String moId1, String moId2, BigDecimal verCheating) {
        return machineProductRepo.findCheatingMacProdByMoIdAndVcheating(moId1, moId2, verCheating);
    }

    public List<MachineProduct> findCheatingMacProdByMoId(String moId1, String moId2) {
        return machineProductRepo.findCheatingMacProdByMoId(moId1, moId2);
    }    
    
    public List<MachineProduct> findCheatingMacProdByMoIdVcheatingAndItemCuring(String moId1, String moId2, BigDecimal verCheating, String itemCuring) {
        return machineProductRepo.findCheatingMacProdByMoIdVcheatingandItemCuring(moId1, moId2, verCheating, itemCuring);
    }

}
