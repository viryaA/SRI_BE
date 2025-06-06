package sri.sysint.sri_starter_back.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.PMStop;
import sri.sysint.sri_starter_back.repository.PMStopRepo;

@Service
@Transactional
public class PMStopServiceImpl {

    @Autowired
    private PMStopRepo pmstopRepo;

    // --- CONSTRUCTOR ---
    public PMStopServiceImpl(PMStopRepo pmstopRepo) {
        this.pmstopRepo = pmstopRepo;
    }

    // --- PM STOP SERVICES ---
    public List<PMStop> getAllPMStops() {
        return pmstopRepo.getDataOrderId();
    }

    public List<PMStop> getActivePMStops() {
        return pmstopRepo.findPMStopActive();
    }

    public Optional<PMStop> getPMStopById(BigDecimal id) {
        return pmstopRepo.findById(id);
    }

    public Optional<PMStop> getPMStopByWorkCenter(String workCenterName) {
        return pmstopRepo.findByWct(workCenterName);
    }

    public BigDecimal generateNewPMStopId() {
        return pmstopRepo.getNewId().add(BigDecimal.ONE);
    }

//    public void insertPMStop(String workCenterText, String createdBy, String lastUpdatedBy,
//            Date dateStop, Timestamp startTime, Timestamp endTime) {
//	
//		ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
//		
//		ZonedDateTime zonedStart = startTime.toInstant().atZone(jakartaZone);
//		ZonedDateTime zonedEnd = endTime.toInstant().atZone(jakartaZone);
//		
//		Timestamp adjustedStartTime = Timestamp.from(zonedStart.toInstant());
//		Timestamp adjustedEndTime = Timestamp.from(zonedEnd.toInstant());
//		
//		pmstopRepo.insertMachineStop(workCenterText, createdBy, lastUpdatedBy, dateStop, adjustedStartTime, adjustedEndTime);
//	}
    
    public void insertPMStop(String workCenterText, String createdBy, String lastUpdatedBy,
            Date dateStop, Timestamp startTime, Timestamp endTime) {
    	
    	pmstopRepo.insertMachineStop(workCenterText, createdBy, lastUpdatedBy, dateStop, startTime, endTime);
	}

    public PMStop softDeletePMStop(BigDecimal id, String updatedBy) {
        Optional<PMStop> pmStopOpt = pmstopRepo.findById(id);
        if (pmStopOpt.isPresent()) {
            PMStop stop = pmStopOpt.get();
            stop.setSTATUS("0");
            stop.setLAST_UPDATED_BY(updatedBy);
            stop.setLAST_UPDATE_DATE(new Date());
            return pmstopRepo.save(stop);
        } else {
            throw new RuntimeException("PM Stop with ID " + id + " not found.");
        }
    }

    public PMStop restorePMStop(BigDecimal id, String updatedBy) {
        Optional<PMStop> pmStopOpt = pmstopRepo.findById(id);
        if (pmStopOpt.isPresent()) {
            PMStop stop = pmStopOpt.get();
            stop.setSTATUS("1");
            stop.setLAST_UPDATED_BY(updatedBy);
            stop.setLAST_UPDATE_DATE(new Date());
            return pmstopRepo.save(stop);
        } else {
            throw new RuntimeException("PM Stop with ID " + id + " not found.");
        }
    }
}
