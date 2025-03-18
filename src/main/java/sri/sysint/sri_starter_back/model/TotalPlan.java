package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "SRI_IMPP_D_TOTALPLAN")
public class TotalPlan {
	@Id
    @Column(name = "ID_PLAN")
    private BigDecimal ID_PLAN;
	
	@Column(name = "ID_MO")
    private String ID_MO;
	
	@Column(name = "ITEM_CURING")
	private String ITEM_CURING;
	
	@Column(name = "TOTAL_PLAN")
	private BigDecimal TOTAL_PLAN;
	
	@Column(name = "PPD")
	private BigDecimal PPD;
	
	@Column(name = "MOULD_NEEDED")
	private BigDecimal MOULD_NEEDED;

	public TotalPlan() {
		
	}

	public TotalPlan(BigDecimal iD_PLAN, String iD_MO, String iTEM_CURING, BigDecimal tOTAL_PLAN, BigDecimal pPD,
			BigDecimal mOULD_NEEDED) {
		ID_PLAN = iD_PLAN;
		ID_MO = iD_MO;
		ITEM_CURING = iTEM_CURING;
		TOTAL_PLAN = tOTAL_PLAN;
		PPD = pPD;
		MOULD_NEEDED = mOULD_NEEDED;
	}
	
	public TotalPlan(TotalPlan TotalPlan) {
		ID_PLAN = TotalPlan.ID_PLAN;
		ID_MO = TotalPlan.ID_MO;
		ITEM_CURING = TotalPlan.ITEM_CURING;
		TOTAL_PLAN = TotalPlan.TOTAL_PLAN;
		PPD = TotalPlan.PPD;
		MOULD_NEEDED = TotalPlan.MOULD_NEEDED;
	}

	public BigDecimal getID_PLAN() {
		return ID_PLAN;
	}

	public void setID_PLAN(BigDecimal iD_PLAN) {
		ID_PLAN = iD_PLAN;
	}

	public String getID_MO() {
		return ID_MO;
	}

	public void setID_MO(String iD_MO) {
		ID_MO = iD_MO;
	}

	public String getITEM_CURING() {
		return ITEM_CURING;
	}

	public void setITEM_CURING(String iTEM_CURING) {
		ITEM_CURING = iTEM_CURING;
	}

	public BigDecimal getTOTAL_PLAN() {
		return TOTAL_PLAN;
	}

	public void setTOTAL_PLAN(BigDecimal tOTAL_PLAN) {
		TOTAL_PLAN = tOTAL_PLAN;
	}

	public BigDecimal getPPD() {
		return PPD;
	}

	public void setPPD(BigDecimal pPD) {
		PPD = pPD;
	}

	public BigDecimal getMOULD_NEEDED() {
		return MOULD_NEEDED;
	}

	public void setMOULD_NEEDED(BigDecimal mOULD_NEEDED) {
		MOULD_NEEDED = mOULD_NEEDED;
	}
	
}
