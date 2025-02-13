package sri.sysint.sri_starter_back.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.FrontRear;
import sri.sysint.sri_starter_back.repository.FrontRearRepo;

@Service
@Transactional
public class FrontRearServiceImpl {

    @Autowired
    private FrontRearRepo frontRearRepo;

    public FrontRearServiceImpl(FrontRearRepo frontRearRepo) {
        this.frontRearRepo = frontRearRepo;
    }

    public List<FrontRear> getAllFrontRear() {
        return frontRearRepo.findAll();
    }

    public Optional<FrontRear> getFrontRearById(BigDecimal id) {
        return frontRearRepo.findById(id);
    }

    public List<FrontRear> getFrontRearByParallelId(BigDecimal id) {
        return frontRearRepo.findListByIdParallel(id);
    }

    public List<FrontRear> getCheatingFrontRear(String moId1, String moId2, BigDecimal verCheating) {
        return frontRearRepo.findCheatingFrontRearByMoIdAndVcheating(moId1, moId2, verCheating);
    }

    public List<FrontRear> getCheatingFrontRearWithItemCuring(String moId1, String moId2, BigDecimal verCheating, String itemCuring) {
        return frontRearRepo.findCheatingFrontRearByMoIdVcheatingandItemCuring(moId1, moId2, verCheating, itemCuring);
    }

    public List<FrontRear> getCheatingFrontRearWithParallelId(String moId1, String moId2, BigDecimal verCheating, BigDecimal parallelId) {
        return frontRearRepo.findCheatingFrontRearByMoIdVcheatingandParallelId(moId1, moId2, verCheating, parallelId);
    }

    public void saveFrontRear(String jsonInput) {
        frontRearRepo.saveFrontRears(jsonInput);
    }
}
