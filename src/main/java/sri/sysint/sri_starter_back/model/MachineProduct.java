package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SRI_IMPP_M_MACHINE_PRODUCT")
public class MachineProduct {
    
    @Id
    @Column(name = "CHEATING_MACHINE_PRODUCT_ID")
    private BigDecimal cheatingMachineProductId;
    
    @Column(name = "MO_ID_1")
    private String moId1;
    
    @Column(name = "MO_ID_2")
    private String moId2;
    
    @Column(name = "ITEM_CURING")
    private String itemCuring;
    
    @Column(name = "WORK_CENTER_TEXT")
    private String workCenterText;
    
    @Column(name = "VERSION_CHEATING")
    private BigDecimal versionCheating;
    
    public MachineProduct() {
    }
    
    public MachineProduct(BigDecimal cheatingMachineProductId, String moId1, String moId2, String itemCuring, String workCenterText, BigDecimal versionCheating) {
        this.cheatingMachineProductId = cheatingMachineProductId;
        this.moId1 = moId1;
        this.moId2 = moId2;
        this.itemCuring = itemCuring;
        this.workCenterText = workCenterText;
        this.versionCheating = versionCheating;
    }
    
    public BigDecimal getCheatingMachineProductId() {
        return cheatingMachineProductId;
    }
    
    public void setCheatingMachineProductId(BigDecimal cheatingMachineProductId) {
        this.cheatingMachineProductId = cheatingMachineProductId;
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

	public String getItemCuring() {
        return itemCuring;
    }
    
    public void setItemCuring(String itemCuring) {
        this.itemCuring = itemCuring;
    }
    
    public String getWorkCenterText() {
        return workCenterText;
    }
    
    public void setWorkCenterText(String workCenterText) {
        this.workCenterText = workCenterText;
    }
    
    public BigDecimal getVersionCheating() {
        return versionCheating;
    }
    
    public void setVersionCheating(BigDecimal versionCheating) {
        this.versionCheating = versionCheating;
    }
}
