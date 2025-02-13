package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SRI_IMPP_M_FRONT_REAR")
public class FrontRear {
    
    @Id
    @Column(name = "FRONT_REAR_ID")
    private BigDecimal frontRearId;
    
    @Column(name = "FRONT_REAR_PARALLEL_ID")
    private BigDecimal frontRearParallelId;
    
    @Column(name = "MO_ID_1")
    private String moId1;
    
    @Column(name = "MO_ID_2")
    private String moId2;
    
    @Column(name = "VERSION_CHEATING")
    private BigDecimal versionCheating;
    
    @Column(name = "ITEM_CURING")
    private String itemCuring;
    
    public FrontRear() {
    }
    
    public FrontRear(BigDecimal frontRearId, BigDecimal frontRearParallelId, String moId1, String moId2, BigDecimal versionCheating, String itemCuring) {
        this.frontRearId = frontRearId;
        this.frontRearParallelId = frontRearParallelId;
        this.moId1 = moId1;
        this.moId2 = moId2;
        this.versionCheating = versionCheating;
        this.itemCuring = itemCuring;
    }
    
    public BigDecimal getFrontRearId() {
        return frontRearId;
    }
    
    public void setFrontRearId(BigDecimal frontRearId) {
        this.frontRearId = frontRearId;
    }
    
    public BigDecimal getFrontRearParallelId() {
        return frontRearParallelId;
    }
    
    public void setFrontRearParallelId(BigDecimal frontRearParallelId) {
        this.frontRearParallelId = frontRearParallelId;
    }
    
    public String getMoId1() {
        return moId1;
    }
    
    public void setMoId1(String moId1) {
        this.moId1 = moId1;
    }
    
    public String getMoId2() {
        return moId2;
    }
    
    public void setMoId2(String moId2) {
        this.moId2 = moId2;
    }
    
    public BigDecimal getVersionCheating() {
        return versionCheating;
    }
    
    public void setVersionCheating(BigDecimal versionCheating) {
        this.versionCheating = versionCheating;
    }
    
    public String getItemCuring() {
        return itemCuring;
    }
    
    public void setItemCuring(String itemCuring) {
        this.itemCuring = itemCuring;
    }
}
