package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "SRI_IMPP_M_MACHINE_STOP")
public class PMStop {

    @Id
    @Column(name = "STOP_MACHINE_ID")
    @JsonProperty("stop_MACHINE_ID")
    private BigDecimal STOP_MACHINE_ID;

    @Column(name = "WORK_CENTER_TEXT")
    @JsonProperty("work_CENTER_TEXT")
    private String WORK_CENTER_TEXT;

    @Column(name = "STATUS")
    @JsonProperty("status")
    private String STATUS;

    @Column(name = "CREATION_DATE")
    @JsonProperty("creation_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date CREATION_DATE;

    @Column(name = "CREATED_BY")
    @JsonProperty("created_BY")
    private String CREATED_BY;

    @Column(name = "LAST_UPDATE_DATE")
    @JsonProperty("last_UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date LAST_UPDATE_DATE;

    @Column(name = "LAST_UPDATED_BY")
    @JsonProperty("last_UPDATED_BY")
    private String LAST_UPDATED_BY;

    @Column(name = "DATE_STOP")
    @JsonProperty("date_STOP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date DATE_STOP;

    @Column(name = "START_TIME")
    @JsonProperty("start_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp START_TIME;

    @Column(name = "END_TIME")
    @JsonProperty("end_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp END_TIME;

    @Column(name = "TOTAL_TIME_STOP_SHIFT1")
    @JsonProperty("total_TIME_STOP_SHIFT1")
    private BigDecimal TOTAL_TIME_STOP_SHIFT1;

    @Column(name = "TOTAL_TIME_STOP_SHIFT2")
    @JsonProperty("total_TIME_STOP_SHIFT2")
    private BigDecimal TOTAL_TIME_STOP_SHIFT2;

    @Column(name = "TOTAL_TIME_STOP_SHIFT3")
    @JsonProperty("total_TIME_STOP_SHIFT3")
    private BigDecimal TOTAL_TIME_STOP_SHIFT3;

    @Column(name = "TOTAL_TIME_STOP")
    @JsonProperty("total_TIME_STOP")
    private BigDecimal TOTAL_TIME_STOP;

    // Getters and Setters
    public BigDecimal getSTOP_MACHINE_ID() {
        return STOP_MACHINE_ID;
    }

    public void setSTOP_MACHINE_ID(BigDecimal sTOP_MACHINE_ID) {
        STOP_MACHINE_ID = sTOP_MACHINE_ID;
    }

    public String getWORK_CENTER_TEXT() {
        return WORK_CENTER_TEXT;
    }

    public void setWORK_CENTER_TEXT(String wORK_CENTER_TEXT) {
        WORK_CENTER_TEXT = wORK_CENTER_TEXT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String sTATUS) {
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

    public Date getDATE_STOP() {
        return DATE_STOP;
    }

    public void setDATE_STOP(Date dATE_STOP) {
        DATE_STOP = dATE_STOP;
    }

    public Timestamp getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(Timestamp sTART_TIME) {
        START_TIME = sTART_TIME;
    }

	public String getStartTimeFormatted() {
		SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
		return hourMinuteFormat.format(START_TIME);
	}
	
	public String getEndTimeFormatted() {
		SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
		return hourMinuteFormat.format(END_TIME);
	}


    public Timestamp getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(Timestamp eND_TIME) {
        END_TIME = eND_TIME;
    }

    public BigDecimal getTOTAL_TIME_STOP_SHIFT1() {
        return TOTAL_TIME_STOP_SHIFT1;
    }

    public void setTOTAL_TIME_STOP_SHIFT1(BigDecimal tOTAL_TIME_STOP_SHIFT1) {
        TOTAL_TIME_STOP_SHIFT1 = tOTAL_TIME_STOP_SHIFT1;
    }

    public BigDecimal getTOTAL_TIME_STOP_SHIFT2() {
        return TOTAL_TIME_STOP_SHIFT2;
    }

    public void setTOTAL_TIME_STOP_SHIFT2(BigDecimal tOTAL_TIME_STOP_SHIFT2) {
        TOTAL_TIME_STOP_SHIFT2 = tOTAL_TIME_STOP_SHIFT2;
    }

    public BigDecimal getTOTAL_TIME_STOP_SHIFT3() {
        return TOTAL_TIME_STOP_SHIFT3;
    }

    public void setTOTAL_TIME_STOP_SHIFT3(BigDecimal tOTAL_TIME_STOP_SHIFT3) {
        TOTAL_TIME_STOP_SHIFT3 = tOTAL_TIME_STOP_SHIFT3;
    }

    public BigDecimal getTOTAL_TIME_STOP() {
        return TOTAL_TIME_STOP;
    }

    public void setTOTAL_TIME_STOP(BigDecimal tOTAL_TIME_STOP) {
        TOTAL_TIME_STOP = tOTAL_TIME_STOP;
    }

    // Constructors
    public PMStop() {}

    public PMStop(PMStop pmstop) {
        this.STOP_MACHINE_ID = pmstop.getSTOP_MACHINE_ID();
        this.WORK_CENTER_TEXT = pmstop.getWORK_CENTER_TEXT();
        this.STATUS = pmstop.getSTATUS();
        this.CREATION_DATE = pmstop.getCREATION_DATE();
        this.CREATED_BY = pmstop.getCREATED_BY();
        this.LAST_UPDATED_BY = pmstop.getLAST_UPDATED_BY();
        this.LAST_UPDATE_DATE = pmstop.getLAST_UPDATE_DATE();
        this.DATE_STOP = pmstop.getDATE_STOP();
        this.START_TIME = pmstop.getSTART_TIME();
        this.END_TIME = pmstop.getEND_TIME();
        this.TOTAL_TIME_STOP_SHIFT1 = pmstop.getTOTAL_TIME_STOP_SHIFT1();
        this.TOTAL_TIME_STOP_SHIFT2 = pmstop.getTOTAL_TIME_STOP_SHIFT2();
        this.TOTAL_TIME_STOP_SHIFT3 = pmstop.getTOTAL_TIME_STOP_SHIFT3();
        this.TOTAL_TIME_STOP = pmstop.getTOTAL_TIME_STOP();
    }

    public PMStop(BigDecimal sTOP_MACHINE_ID, String wORK_CENTER_TEXT, String sTATUS, Date cREATION_DATE,
                  String cREATED_BY, Date lAST_UPDATE_DATE, String lAST_UPDATED_BY, Date dATE_STOP, Timestamp sTART_TIME,
                  Timestamp eND_TIME, BigDecimal tOTAL_TIME_STOP_SHIFT1, BigDecimal tOTAL_TIME_STOP_SHIFT2,
                  BigDecimal tOTAL_TIME_STOP_SHIFT3, BigDecimal tOTAL_TIME_STOP) {
        STOP_MACHINE_ID = sTOP_MACHINE_ID;
        WORK_CENTER_TEXT = wORK_CENTER_TEXT;
        STATUS = sTATUS;
        CREATION_DATE = cREATION_DATE;
        CREATED_BY = cREATED_BY;
        LAST_UPDATE_DATE = lAST_UPDATE_DATE;
        LAST_UPDATED_BY = lAST_UPDATED_BY;
        DATE_STOP = dATE_STOP;
        START_TIME = sTART_TIME;
        END_TIME = eND_TIME;
        TOTAL_TIME_STOP_SHIFT1 = tOTAL_TIME_STOP_SHIFT1;
        TOTAL_TIME_STOP_SHIFT2 = tOTAL_TIME_STOP_SHIFT2;
        TOTAL_TIME_STOP_SHIFT3 = tOTAL_TIME_STOP_SHIFT3;
        TOTAL_TIME_STOP = tOTAL_TIME_STOP;
    }
}
