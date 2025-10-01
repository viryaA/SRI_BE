package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SRI_IMPP_T_MONTHLYPLAN1")
public class MonthlyPlanningNew {
	@Id
    @Column(name = "MP_ID")
    private String mpId;
	
    @Column(name = "MO_ID")
    private String moId;

    @Column(name = "DATE_VALID")
    private Date dateMp;

    @Column(name = "ITEM_CURING")
    private String itemCuring;

    @Column(name = "WCT")
    private String wct;

    @Column(name = "MOULD_USE")
    private BigDecimal mouldUse;

    @Column(name = "TOTAL_HARIAN")
    private BigDecimal totalHarian;

    @Column(name = "SHIFT_1")
    private BigDecimal shift1;

    @Column(name = "SHIFT_2")
    private BigDecimal shift2;

    @Column(name = "SHIFT_3")
    private BigDecimal shift3;

    @Column(name = "KAPA_PERMOULD")
    private BigDecimal kapaPermould;
    
    @Column(name = "STATUS_MP")
    private String statusMP;
    
    @Column(name = "VERSION")
    private BigDecimal version;
    
//    @Column(name )

    // Default constructor
    public MonthlyPlanningNew() {}

    
    // Copy constructor
    public MonthlyPlanningNew(MonthlyPlanningNew monthlyPlanningNew) {
        this.moId = monthlyPlanningNew.getMoId();
        this.mpId = monthlyPlanningNew.getMpId();
        this.dateMp = monthlyPlanningNew.getDateMp();
        this.itemCuring = monthlyPlanningNew.getItemCuring();
        this.wct = monthlyPlanningNew.getWct();
        this.mouldUse = monthlyPlanningNew.getMouldUse();
        this.totalHarian = monthlyPlanningNew.getTotalHarian();
        this.shift1 = monthlyPlanningNew.getShift1();
        this.shift2 = monthlyPlanningNew.getShift2();
        this.shift3 = monthlyPlanningNew.getShift3();
        this.kapaPermould = monthlyPlanningNew.getKapaPermould();
        this.statusMP = monthlyPlanningNew.getStatusMP();
        this.version = monthlyPlanningNew.getVersion();
    }


	public MonthlyPlanningNew(String mpId, String moId, Date dateMp, String itemCuring, String wct, BigDecimal mouldUse,
			BigDecimal totalHarian, BigDecimal shift1, BigDecimal shift2, BigDecimal shift3, BigDecimal kapaPermould,
			String statusMP, BigDecimal version) {
		super();
		this.mpId = mpId;
		this.moId = moId;
		this.dateMp = dateMp;
		this.itemCuring = itemCuring;
		this.wct = wct;
		this.mouldUse = mouldUse;
		this.totalHarian = totalHarian;
		this.shift1 = shift1;
		this.shift2 = shift2;
		this.shift3 = shift3;
		this.kapaPermould = kapaPermould;
		this.statusMP = statusMP;
		this.version = version;
	}
	
	


	public String getStatusMP() {
		return statusMP;
	}


	public void setStatusMP(String statusMP) {
		this.statusMP = statusMP;
	}


	public BigDecimal getVersion() {
		return version;
	}


	public void setVersion(BigDecimal version) {
		this.version = version;
	}


	public String getMpId() {
		return mpId;
	}


	public void setMpId(String mpId) {
		this.mpId = mpId;
	}


	public String getMoId() {
		return moId;
	}



	public void setMoId(String moId) {
		this.moId = moId;
	}



	public Date getDateMp() {
		return dateMp;
	}



	public void setDateMp(Date dateMp) {
		this.dateMp = dateMp;
	}



	public String getItemCuring() {
		return itemCuring;
	}



	public void setItemCuring(String itemCuring) {
		this.itemCuring = itemCuring;
	}



	public String getWct() {
		return wct;
	}



	public void setWct(String wct) {
		this.wct = wct;
	}



	public BigDecimal getMouldUse() {
		return mouldUse;
	}



	public void setMouldUse(BigDecimal mouldUse) {
		this.mouldUse = mouldUse;
	}



	public BigDecimal getTotalHarian() {
		return totalHarian;
	}



	public void setTotalHarian(BigDecimal totalHarian) {
		this.totalHarian = totalHarian;
	}



	public BigDecimal getShift1() {
		return shift1;
	}



	public void setShift1(BigDecimal shift1) {
		this.shift1 = shift1;
	}



	public BigDecimal getShift2() {
		return shift2;
	}



	public void setShift2(BigDecimal shift2) {
		this.shift2 = shift2;
	}



	public BigDecimal getShift3() {
		return shift3;
	}



	public void setShift3(BigDecimal shift3) {
		this.shift3 = shift3;
	}



	public BigDecimal getKapaPermould() {
		return kapaPermould;
	}



	public void setKapaPermould(BigDecimal kapaPermould) {
		this.kapaPermould = kapaPermould;
	}

    
}


