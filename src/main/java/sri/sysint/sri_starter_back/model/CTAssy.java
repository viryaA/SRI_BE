package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SRI_IMPP_M_CT_ASSY")
public class CTAssy {

    @Id
    @Column(name = "CT_ASSY_ID")
    private BigDecimal CT_ASSY_ID;

    @Column(name = "WIP")
    private String WIP;

    @Column(name = "DESCRIPTION")
    private String DESCRIPTION;

    @Column(name = "GROUP_COUNTER")
    private String GROUP_COUNTER;

    @Column(name = "VAR_GROUP_COUNTER")
    private String VAR_GROUP_COUNTER;

    @Column(name = "SEQUENCE")
    private BigDecimal SEQUENCE;

    @Column(name = "WCT")
    private String WCT;

    @Column(name = "OPERATION_SHORT_TEXT")
    private String OPERATION_SHORT_TEXT;

    @Column(name = "OPERATION_UNIT")
    private String OPERATION_UNIT;

    @Column(name = "BASE_QUANTITY")
    private BigDecimal BASE_QUANTITY;

    @Column(name = "STANDARD_VALUE_UNIT")
    private String STANDARD_VALUE_UNIT;

    @Column(name = "CT_SEC_1")
    private BigDecimal CT_SEC_1;

    @Column(name = "CT_HR_1000")
    private BigDecimal CT_HR_1000;

    @Column(name = "WH_NORMAL_SHIFT_0")
    private BigDecimal WH_NORMAL_SHIFT_0;

    @Column(name = "WH_NORMAL_SHIFT_1")
    private BigDecimal WH_NORMAL_SHIFT_1;

    @Column(name = "WH_NORMAL_SHIFT_2")
    private BigDecimal WH_NORMAL_SHIFT_2;

    @Column(name = "WH_SHIFT_FRIDAY")
    private BigDecimal WH_SHIFT_FRIDAY;

    @Column(name = "WH_TOTAL_NORMAL_SHIFT")
    private BigDecimal WH_TOTAL_NORMAL_SHIFT;

    @Column(name = "WH_TOTAL_SHIFT_FRIDAY")
    private BigDecimal WH_TOTAL_SHIFT_FRIDAY;

    @Column(name = "ALLOW_NORMAL_SHIFT_0")
    private BigDecimal ALLOW_NORMAL_SHIFT_0;

    @Column(name = "ALLOW_NORMAL_SHIFT_1")
    private BigDecimal ALLOW_NORMAL_SHIFT_1;

    @Column(name = "ALLOW_NORMAL_SHIFT_2")
    private BigDecimal ALLOW_NORMAL_SHIFT_2;

    @Column(name = "ALLOW_TOTAL")
    private BigDecimal ALLOW_TOTAL;

    @Column(name = "OP_TIME_NORMAL_SHIFT_0")
    private BigDecimal OP_TIME_NORMAL_SHIFT_0;

    @Column(name = "OP_TIME_NORMAL_SHIFT_1")
    private BigDecimal OP_TIME_NORMAL_SHIFT_1;

    @Column(name = "OP_TIME_NORMAL_SHIFT_2")
    private BigDecimal OP_TIME_NORMAL_SHIFT_2;

    @Column(name = "OP_TIME_SHIFT_FRIDAY")
    private BigDecimal OP_TIME_SHIFT_FRIDAY;

    @Column(name = "OP_TIME_TOTAL_NORMAL_SHIFT")
    private BigDecimal OP_TIME_TOTAL_NORMAL_SHIFT;

    @Column(name = "OP_TIME_TOTAL_SHIFT_FRIDAY")
    private BigDecimal OP_TIME_TOTAL_SHIFT_FRIDAY;

    @Column(name = "KAPS_NORMAL_SHIFT_0")
    private BigDecimal KAPS_NORMAL_SHIFT_0;

    @Column(name = "KAPS_NORMAL_SHIFT_1")
    private BigDecimal KAPS_NORMAL_SHIFT_1;

    @Column(name = "KAPS_NORMAL_SHIFT_2")
    private BigDecimal KAPS_NORMAL_SHIFT_2;

    @Column(name = "KAPS_SHIFT_FRIDAY")
    private BigDecimal KAPS_SHIFT_FRIDAY;

    @Column(name = "KAPS_TOTAL_NORMAL_SHIFT")
    private BigDecimal KAPS_TOTAL_NORMAL_SHIFT;

    @Column(name = "KAPS_TOTAL_SHIFT_FRIDAY")
    private BigDecimal KAPS_TOTAL_SHIFT_FRIDAY;

    @Column(name = "WAKTU_TOTAL_CT_NORMAL")
    private BigDecimal WAKTU_TOTAL_CT_NORMAL;

    @Column(name ="WAKTU_TOTAL_CT_FRIDAY")
    private BigDecimal WAKTU_TOTAL_CT_FRIDAY;
    
	@Column(name = "STATUS")
	private BigDecimal STATUS;

    @Column(name = "CREATION_DATE")
    private Date CREATION_DATE;

    @Column(name = "CREATED_BY")
    private String CREATED_BY;

    @Column(name = "LAST_UPDATE_DATE")
    private Date LAST_UPDATE_DATE;

    @Column(name = "LAST_UPDATED_BY")
    private String LAST_UPDATED_BY;

    
    public CTAssy() {}
    
    public CTAssy(CTAssy ctAssy) {
        this.CT_ASSY_ID = ctAssy.getCT_ASSY_ID();
        this.WIP = ctAssy.getWIP();
        this.DESCRIPTION = ctAssy.getDESCRIPTION();
        this.GROUP_COUNTER = ctAssy.getGROUP_COUNTER();
        this.VAR_GROUP_COUNTER = ctAssy.getVAR_GROUP_COUNTER();
        this.SEQUENCE = ctAssy.getSEQUENCE();
        this.WCT = ctAssy.getWCT();
        this.OPERATION_SHORT_TEXT = ctAssy.getOPERATION_SHORT_TEXT();
        this.OPERATION_UNIT = ctAssy.getOPERATION_UNIT();
        this.BASE_QUANTITY = ctAssy.getBASE_QUANTITY();
        this.STANDARD_VALUE_UNIT = ctAssy.getSTANDARD_VALUE_UNIT();
        this.CT_SEC_1 = ctAssy.getCT_SEC_1();
        this.CT_HR_1000 = ctAssy.getCT_HR_1000();
        this.WH_NORMAL_SHIFT_0 = ctAssy.getWH_NORMAL_SHIFT_0();
        this.WH_NORMAL_SHIFT_1 = ctAssy.getWH_NORMAL_SHIFT_1();
        this.WH_NORMAL_SHIFT_2 = ctAssy.getWH_NORMAL_SHIFT_2();
        this.WH_SHIFT_FRIDAY = ctAssy.getWH_SHIFT_FRIDAY();
        this.WH_TOTAL_NORMAL_SHIFT = ctAssy.getWH_TOTAL_NORMAL_SHIFT();
        this.WH_TOTAL_SHIFT_FRIDAY = ctAssy.getWH_TOTAL_SHIFT_FRIDAY();
        this.ALLOW_NORMAL_SHIFT_0 = ctAssy.getALLOW_NORMAL_SHIFT_0();
        this.ALLOW_NORMAL_SHIFT_1 = ctAssy.getALLOW_NORMAL_SHIFT_1();
        this.ALLOW_NORMAL_SHIFT_2 = ctAssy.getALLOW_NORMAL_SHIFT_2();
        this.ALLOW_TOTAL = ctAssy.getALLOW_TOTAL();
        this.OP_TIME_NORMAL_SHIFT_0 = ctAssy.getOP_TIME_NORMAL_SHIFT_0();
        this.OP_TIME_NORMAL_SHIFT_1 = ctAssy.getOP_TIME_NORMAL_SHIFT_1();
        this.OP_TIME_NORMAL_SHIFT_2 = ctAssy.getOP_TIME_NORMAL_SHIFT_2();
        this.OP_TIME_SHIFT_FRIDAY = ctAssy.getOP_TIME_SHIFT_FRIDAY();
        this.OP_TIME_TOTAL_NORMAL_SHIFT = ctAssy.getOP_TIME_TOTAL_NORMAL_SHIFT();
        this.OP_TIME_TOTAL_SHIFT_FRIDAY = ctAssy.getOP_TIME_TOTAL_SHIFT_FRIDAY();
        this.KAPS_NORMAL_SHIFT_0 = ctAssy.getKAPS_NORMAL_SHIFT_0();
        this.KAPS_NORMAL_SHIFT_1 = ctAssy.getKAPS_NORMAL_SHIFT_1();
        this.KAPS_NORMAL_SHIFT_2 = ctAssy.getKAPS_NORMAL_SHIFT_2();
        this.KAPS_SHIFT_FRIDAY = ctAssy.getKAPS_SHIFT_FRIDAY();
        this.KAPS_TOTAL_NORMAL_SHIFT = ctAssy.getKAPS_TOTAL_NORMAL_SHIFT();
        this.KAPS_TOTAL_SHIFT_FRIDAY = ctAssy.getKAPS_TOTAL_SHIFT_FRIDAY();
        this.WAKTU_TOTAL_CT_NORMAL = ctAssy.getWAKTU_TOTAL_CT_NORMAL();
        this.WAKTU_TOTAL_CT_FRIDAY = ctAssy.getWAKTU_TOTAL_CT_FRIDAY();
		this.STATUS = ctAssy.getSTATUS();
        this.CREATION_DATE = ctAssy.getCREATION_DATE();
        this.CREATED_BY = ctAssy.getCREATED_BY();
        this.LAST_UPDATE_DATE = ctAssy.getLAST_UPDATE_DATE();
        this.LAST_UPDATED_BY = ctAssy.getLAST_UPDATED_BY();
    }

	public CTAssy(BigDecimal cT_ASSY_ID, String wIP, String dESCRIPTION, String gROUP_COUNTER, String vAR_GROUP_COUNTER,
			BigDecimal sEQUENCE, String wCT, String oPERATION_SHORT_TEXT, String oPERATION_UNIT,
			BigDecimal bASE_QUANTITY, String sTANDARD_VALUE_UNIT, BigDecimal cT_SEC_1, BigDecimal cT_HR_1000,
			BigDecimal wH_NORMAL_SHIFT_0, BigDecimal wH_NORMAL_SHIFT_1, BigDecimal wH_NORMAL_SHIFT_2,
			BigDecimal wH_SHIFT_FRIDAY, BigDecimal wH_TOTAL_NORMAL_SHIFT, BigDecimal wH_TOTAL_SHIFT_FRIDAY,
			BigDecimal aLLOW_NORMAL_SHIFT_0, BigDecimal aLLOW_NORMAL_SHIFT_1, BigDecimal aLLOW_NORMAL_SHIFT_2,
			BigDecimal aLLOW_TOTAL, BigDecimal oP_TIME_NORMAL_SHIFT_0, BigDecimal oP_TIME_NORMAL_SHIFT_1,
			BigDecimal oP_TIME_NORMAL_SHIFT_2, BigDecimal oP_TIME_SHIFT_FRIDAY, BigDecimal oP_TIME_TOTAL_NORMAL_SHIFT,
			BigDecimal oP_TIME_TOTAL_SHIFT_FRIDAY, BigDecimal kAPS_NORMAL_SHIFT_0, BigDecimal kAPS_NORMAL_SHIFT_1,
			BigDecimal kAPS_NORMAL_SHIFT_2, BigDecimal kAPS_SHIFT_FRIDAY, BigDecimal kAPS_TOTAL_NORMAL_SHIFT,
			BigDecimal kAPS_TOTAL_SHIFT_FRIDAY, BigDecimal wAKTU_TOTAL_CT_NORMAL, BigDecimal wAKTU_TOTAL_CT_FRIDAY,
			BigDecimal sTATUS, Date cREATION_DATE, String cREATED_BY, Date lAST_UPDATE_DATE, String lAST_UPDATED_BY) {
		super();
		CT_ASSY_ID = cT_ASSY_ID;
		WIP = wIP;
		DESCRIPTION = dESCRIPTION;
		GROUP_COUNTER = gROUP_COUNTER;
		VAR_GROUP_COUNTER = vAR_GROUP_COUNTER;
		SEQUENCE = sEQUENCE;
		WCT = wCT;
		OPERATION_SHORT_TEXT = oPERATION_SHORT_TEXT;
		OPERATION_UNIT = oPERATION_UNIT;
		BASE_QUANTITY = bASE_QUANTITY;
		STANDARD_VALUE_UNIT = sTANDARD_VALUE_UNIT;
		CT_SEC_1 = cT_SEC_1;
		CT_HR_1000 = cT_HR_1000;
		WH_NORMAL_SHIFT_0 = wH_NORMAL_SHIFT_0;
		WH_NORMAL_SHIFT_1 = wH_NORMAL_SHIFT_1;
		WH_NORMAL_SHIFT_2 = wH_NORMAL_SHIFT_2;
		WH_SHIFT_FRIDAY = wH_SHIFT_FRIDAY;
		WH_TOTAL_NORMAL_SHIFT = wH_TOTAL_NORMAL_SHIFT;
		WH_TOTAL_SHIFT_FRIDAY = wH_TOTAL_SHIFT_FRIDAY;
		ALLOW_NORMAL_SHIFT_0 = aLLOW_NORMAL_SHIFT_0;
		ALLOW_NORMAL_SHIFT_1 = aLLOW_NORMAL_SHIFT_1;
		ALLOW_NORMAL_SHIFT_2 = aLLOW_NORMAL_SHIFT_2;
		ALLOW_TOTAL = aLLOW_TOTAL;
		OP_TIME_NORMAL_SHIFT_0 = oP_TIME_NORMAL_SHIFT_0;
		OP_TIME_NORMAL_SHIFT_1 = oP_TIME_NORMAL_SHIFT_1;
		OP_TIME_NORMAL_SHIFT_2 = oP_TIME_NORMAL_SHIFT_2;
		OP_TIME_SHIFT_FRIDAY = oP_TIME_SHIFT_FRIDAY;
		OP_TIME_TOTAL_NORMAL_SHIFT = oP_TIME_TOTAL_NORMAL_SHIFT;
		OP_TIME_TOTAL_SHIFT_FRIDAY = oP_TIME_TOTAL_SHIFT_FRIDAY;
		KAPS_NORMAL_SHIFT_0 = kAPS_NORMAL_SHIFT_0;
		KAPS_NORMAL_SHIFT_1 = kAPS_NORMAL_SHIFT_1;
		KAPS_NORMAL_SHIFT_2 = kAPS_NORMAL_SHIFT_2;
		KAPS_SHIFT_FRIDAY = kAPS_SHIFT_FRIDAY;
		KAPS_TOTAL_NORMAL_SHIFT = kAPS_TOTAL_NORMAL_SHIFT;
		KAPS_TOTAL_SHIFT_FRIDAY = kAPS_TOTAL_SHIFT_FRIDAY;
		WAKTU_TOTAL_CT_NORMAL = wAKTU_TOTAL_CT_NORMAL;
		WAKTU_TOTAL_CT_FRIDAY = wAKTU_TOTAL_CT_FRIDAY;
		STATUS = sTATUS;
		CREATION_DATE = cREATION_DATE;
		CREATED_BY = cREATED_BY;
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}

	public BigDecimal getCT_ASSY_ID() {
		return CT_ASSY_ID;
	}

	public void setCT_ASSY_ID(BigDecimal cT_ASSY_ID) {
		CT_ASSY_ID = cT_ASSY_ID;
	}

	public String getWIP() {
		return WIP;
	}

	public void setWIP(String wIP) {
		WIP = wIP;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getGROUP_COUNTER() {
		return GROUP_COUNTER;
	}

	public void setGROUP_COUNTER(String gROUP_COUNTER) {
		GROUP_COUNTER = gROUP_COUNTER;
	}

	public String getVAR_GROUP_COUNTER() {
		return VAR_GROUP_COUNTER;
	}

	public void setVAR_GROUP_COUNTER(String vAR_GROUP_COUNTER) {
		VAR_GROUP_COUNTER = vAR_GROUP_COUNTER;
	}

	public BigDecimal getSEQUENCE() {
		return SEQUENCE;
	}

	public void setSEQUENCE(BigDecimal sEQUENCE) {
		SEQUENCE = sEQUENCE;
	}

	public String getWCT() {
		return WCT;
	}

	public void setWCT(String wCT) {
		WCT = wCT;
	}

	public String getOPERATION_SHORT_TEXT() {
		return OPERATION_SHORT_TEXT;
	}

	public void setOPERATION_SHORT_TEXT(String oPERATION_SHORT_TEXT) {
		OPERATION_SHORT_TEXT = oPERATION_SHORT_TEXT;
	}

	public String getOPERATION_UNIT() {
		return OPERATION_UNIT;
	}

	public void setOPERATION_UNIT(String oPERATION_UNIT) {
		OPERATION_UNIT = oPERATION_UNIT;
	}

	public BigDecimal getBASE_QUANTITY() {
		return BASE_QUANTITY;
	}

	public void setBASE_QUANTITY(BigDecimal bASE_QUANTITY) {
		BASE_QUANTITY = bASE_QUANTITY;
	}

	public String getSTANDARD_VALUE_UNIT() {
		return STANDARD_VALUE_UNIT;
	}

	public void setSTANDARD_VALUE_UNIT(String sTANDARD_VALUE_UNIT) {
		STANDARD_VALUE_UNIT = sTANDARD_VALUE_UNIT;
	}

	public BigDecimal getCT_SEC_1() {
		return CT_SEC_1;
	}

	public void setCT_SEC_1(BigDecimal cT_SEC_1) {
		CT_SEC_1 = cT_SEC_1;
	}

	public BigDecimal getCT_HR_1000() {
		return CT_HR_1000;
	}

	public void setCT_HR_1000(BigDecimal cT_HR_1000) {
		CT_HR_1000 = cT_HR_1000;
	}

	public BigDecimal getWH_NORMAL_SHIFT_0() {
		return WH_NORMAL_SHIFT_0;
	}

	public void setWH_NORMAL_SHIFT_0(BigDecimal wH_NORMAL_SHIFT_0) {
		WH_NORMAL_SHIFT_0 = wH_NORMAL_SHIFT_0;
	}

	public BigDecimal getWH_NORMAL_SHIFT_1() {
		return WH_NORMAL_SHIFT_1;
	}

	public void setWH_NORMAL_SHIFT_1(BigDecimal wH_NORMAL_SHIFT_1) {
		WH_NORMAL_SHIFT_1 = wH_NORMAL_SHIFT_1;
	}

	public BigDecimal getWH_NORMAL_SHIFT_2() {
		return WH_NORMAL_SHIFT_2;
	}

	public void setWH_NORMAL_SHIFT_2(BigDecimal wH_NORMAL_SHIFT_2) {
		WH_NORMAL_SHIFT_2 = wH_NORMAL_SHIFT_2;
	}

	public BigDecimal getWH_SHIFT_FRIDAY() {
		return WH_SHIFT_FRIDAY;
	}

	public void setWH_SHIFT_FRIDAY(BigDecimal wH_SHIFT_FRIDAY) {
		WH_SHIFT_FRIDAY = wH_SHIFT_FRIDAY;
	}

	public BigDecimal getWH_TOTAL_NORMAL_SHIFT() {
		return WH_TOTAL_NORMAL_SHIFT;
	}

	public void setWH_TOTAL_NORMAL_SHIFT(BigDecimal wH_TOTAL_NORMAL_SHIFT) {
		WH_TOTAL_NORMAL_SHIFT = wH_TOTAL_NORMAL_SHIFT;
	}

	public BigDecimal getWH_TOTAL_SHIFT_FRIDAY() {
		return WH_TOTAL_SHIFT_FRIDAY;
	}

	public void setWH_TOTAL_SHIFT_FRIDAY(BigDecimal wH_TOTAL_SHIFT_FRIDAY) {
		WH_TOTAL_SHIFT_FRIDAY = wH_TOTAL_SHIFT_FRIDAY;
	}

	public BigDecimal getALLOW_NORMAL_SHIFT_0() {
		return ALLOW_NORMAL_SHIFT_0;
	}

	public void setALLOW_NORMAL_SHIFT_0(BigDecimal aLLOW_NORMAL_SHIFT_0) {
		ALLOW_NORMAL_SHIFT_0 = aLLOW_NORMAL_SHIFT_0;
	}

	public BigDecimal getALLOW_NORMAL_SHIFT_1() {
		return ALLOW_NORMAL_SHIFT_1;
	}

	public void setALLOW_NORMAL_SHIFT_1(BigDecimal aLLOW_NORMAL_SHIFT_1) {
		ALLOW_NORMAL_SHIFT_1 = aLLOW_NORMAL_SHIFT_1;
	}

	public BigDecimal getALLOW_NORMAL_SHIFT_2() {
		return ALLOW_NORMAL_SHIFT_2;
	}

	public void setALLOW_NORMAL_SHIFT_2(BigDecimal aLLOW_NORMAL_SHIFT_2) {
		ALLOW_NORMAL_SHIFT_2 = aLLOW_NORMAL_SHIFT_2;
	}

	public BigDecimal getALLOW_TOTAL() {
		return ALLOW_TOTAL;
	}

	public void setALLOW_TOTAL(BigDecimal aLLOW_TOTAL) {
		ALLOW_TOTAL = aLLOW_TOTAL;
	}

	public BigDecimal getOP_TIME_NORMAL_SHIFT_0() {
		return OP_TIME_NORMAL_SHIFT_0;
	}

	public void setOP_TIME_NORMAL_SHIFT_0(BigDecimal oP_TIME_NORMAL_SHIFT_0) {
		OP_TIME_NORMAL_SHIFT_0 = oP_TIME_NORMAL_SHIFT_0;
	}

	public BigDecimal getOP_TIME_NORMAL_SHIFT_1() {
		return OP_TIME_NORMAL_SHIFT_1;
	}

	public void setOP_TIME_NORMAL_SHIFT_1(BigDecimal oP_TIME_NORMAL_SHIFT_1) {
		OP_TIME_NORMAL_SHIFT_1 = oP_TIME_NORMAL_SHIFT_1;
	}

	public BigDecimal getOP_TIME_NORMAL_SHIFT_2() {
		return OP_TIME_NORMAL_SHIFT_2;
	}

	public void setOP_TIME_NORMAL_SHIFT_2(BigDecimal oP_TIME_NORMAL_SHIFT_2) {
		OP_TIME_NORMAL_SHIFT_2 = oP_TIME_NORMAL_SHIFT_2;
	}

	public BigDecimal getOP_TIME_SHIFT_FRIDAY() {
		return OP_TIME_SHIFT_FRIDAY;
	}

	public void setOP_TIME_SHIFT_FRIDAY(BigDecimal oP_TIME_SHIFT_FRIDAY) {
		OP_TIME_SHIFT_FRIDAY = oP_TIME_SHIFT_FRIDAY;
	}

	public BigDecimal getOP_TIME_TOTAL_NORMAL_SHIFT() {
		return OP_TIME_TOTAL_NORMAL_SHIFT;
	}

	public void setOP_TIME_TOTAL_NORMAL_SHIFT(BigDecimal oP_TIME_TOTAL_NORMAL_SHIFT) {
		OP_TIME_TOTAL_NORMAL_SHIFT = oP_TIME_TOTAL_NORMAL_SHIFT;
	}

	public BigDecimal getOP_TIME_TOTAL_SHIFT_FRIDAY() {
		return OP_TIME_TOTAL_SHIFT_FRIDAY;
	}

	public void setOP_TIME_TOTAL_SHIFT_FRIDAY(BigDecimal oP_TIME_TOTAL_SHIFT_FRIDAY) {
		OP_TIME_TOTAL_SHIFT_FRIDAY = oP_TIME_TOTAL_SHIFT_FRIDAY;
	}

	public BigDecimal getKAPS_NORMAL_SHIFT_0() {
		return KAPS_NORMAL_SHIFT_0;
	}

	public void setKAPS_NORMAL_SHIFT_0(BigDecimal kAPS_NORMAL_SHIFT_0) {
		KAPS_NORMAL_SHIFT_0 = kAPS_NORMAL_SHIFT_0;
	}

	public BigDecimal getKAPS_NORMAL_SHIFT_1() {
		return KAPS_NORMAL_SHIFT_1;
	}

	public void setKAPS_NORMAL_SHIFT_1(BigDecimal kAPS_NORMAL_SHIFT_1) {
		KAPS_NORMAL_SHIFT_1 = kAPS_NORMAL_SHIFT_1;
	}

	public BigDecimal getKAPS_NORMAL_SHIFT_2() {
		return KAPS_NORMAL_SHIFT_2;
	}

	public void setKAPS_NORMAL_SHIFT_2(BigDecimal kAPS_NORMAL_SHIFT_2) {
		KAPS_NORMAL_SHIFT_2 = kAPS_NORMAL_SHIFT_2;
	}

	public BigDecimal getKAPS_SHIFT_FRIDAY() {
		return KAPS_SHIFT_FRIDAY;
	}

	public void setKAPS_SHIFT_FRIDAY(BigDecimal kAPS_SHIFT_FRIDAY) {
		KAPS_SHIFT_FRIDAY = kAPS_SHIFT_FRIDAY;
	}

	public BigDecimal getKAPS_TOTAL_NORMAL_SHIFT() {
		return KAPS_TOTAL_NORMAL_SHIFT;
	}

	public void setKAPS_TOTAL_NORMAL_SHIFT(BigDecimal kAPS_TOTAL_NORMAL_SHIFT) {
		KAPS_TOTAL_NORMAL_SHIFT = kAPS_TOTAL_NORMAL_SHIFT;
	}

	public BigDecimal getKAPS_TOTAL_SHIFT_FRIDAY() {
		return KAPS_TOTAL_SHIFT_FRIDAY;
	}

	public void setKAPS_TOTAL_SHIFT_FRIDAY(BigDecimal kAPS_TOTAL_SHIFT_FRIDAY) {
		KAPS_TOTAL_SHIFT_FRIDAY = kAPS_TOTAL_SHIFT_FRIDAY;
	}

	public BigDecimal getWAKTU_TOTAL_CT_NORMAL() {
		return WAKTU_TOTAL_CT_NORMAL;
	}

	public void setWAKTU_TOTAL_CT_NORMAL(BigDecimal wAKTU_TOTAL_CT_NORMAL) {
		WAKTU_TOTAL_CT_NORMAL = wAKTU_TOTAL_CT_NORMAL;
	}

	public BigDecimal getWAKTU_TOTAL_CT_FRIDAY() {
		return WAKTU_TOTAL_CT_FRIDAY;
	}

	public void setWAKTU_TOTAL_CT_FRIDAY(BigDecimal wAKTU_TOTAL_CT_FRIDAY) {
		WAKTU_TOTAL_CT_FRIDAY = wAKTU_TOTAL_CT_FRIDAY;
	}

	public BigDecimal getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(BigDecimal sTATUS) {
		STATUS = sTATUS;
	}

	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

	public Date getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}

	public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}

	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}

	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
    
    
}
