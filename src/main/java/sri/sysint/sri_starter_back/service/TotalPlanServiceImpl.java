package sri.sysint.sri_starter_back.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.TotalPlan;
import sri.sysint.sri_starter_back.repository.TotalPlanRepo;

@Service
@Transactional
public class TotalPlanServiceImpl {
    @Autowired
    private TotalPlanRepo totalPlanRepo;

    public TotalPlanServiceImpl(TotalPlanRepo totalPlanRepo) {
        this.totalPlanRepo = totalPlanRepo;
    }
    
    public List<TotalPlan> getAllKapa() {
        Iterable<TotalPlan> totalPlans = totalPlanRepo.findAll();
        List<TotalPlan> totalPlanList = new ArrayList<>();
        for (TotalPlan item : totalPlans) {
            TotalPlan totalPlanTemp = new TotalPlan(item);
            totalPlanList.add(totalPlanTemp);
        }
        return totalPlanList;
    }
  
}
