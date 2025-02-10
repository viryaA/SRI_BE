package sri.sysint.sri_starter_back.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "SRI_IMPP_HD_FRONT_REAR")
public class FrontRear implements Serializable {

    @EmbeddedId
    private FrontRearId id;

    @Column(name = "COUPLE")
    private Integer COUPLE;    
    
    @Column(name = "STATUS")
    private Integer STATUS;

    @Column(name = "CREATION_DATE")
    private Date CREATION_DATE;
    
    @Column(name = "CREATED_BY")
    private String CREATED_BY;
    
    @Column(name = "LAST_UPDATE_DATE")
    private Date LAST_UPDATE_DATE;
    
    @Column(name = "LAST_UPDATED_BY")
    private String LAST_UPDATED_BY;

    // Default constructor
    public FrontRear() {}

    public FrontRear(FrontRear frontRear) {
        this.id = frontRear.getId();
		this.COUPLE = frontRear.getCOUPLE();
		this.STATUS = frontRear.getSTATUS();
		this.CREATION_DATE = frontRear.getCREATION_DATE();
		this.CREATED_BY = frontRear.getCREATED_BY();
		this.LAST_UPDATE_DATE = frontRear.getLAST_UPDATE_DATE();
		this.LAST_UPDATED_BY = frontRear.getLAST_UPDATED_BY();

    }

    // Constructor with all fields
    public FrontRear(FrontRearId id,Integer COUPLE, Integer STATUS, Date CREATION_DATE, 
                     String CREATED_BY, Date LAST_UPDATE_DATE, String LAST_UPDATED_BY) {
        super();
        this.id = id;
        this.STATUS = STATUS;
        this.COUPLE = COUPLE;
        this.CREATION_DATE = CREATION_DATE;
        this.CREATED_BY = CREATED_BY;
        this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
        this.LAST_UPDATED_BY = LAST_UPDATED_BY;
    }

    // Getters and Setters
    public FrontRearId getId() {
        return id;
    }

    public void setId(FrontRearId id) {
        this.id = id;
    }

    public Integer getCOUPLE() {
        return COUPLE;
    }

    public void setCOUPLE(Integer COUPLE) {
        this.COUPLE = COUPLE;
    }

    public Integer getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Integer STATUS) {
        this.STATUS = STATUS;
    }

    public Date getCREATION_DATE() {
        return CREATION_DATE;
    }

    public void setCREATION_DATE(Date CREATION_DATE) {
        this.CREATION_DATE = CREATION_DATE;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public Date getLAST_UPDATE_DATE() {
        return LAST_UPDATE_DATE;
    }

    public void setLAST_UPDATE_DATE(Date LAST_UPDATE_DATE) {
        this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
    }

    public String getLAST_UPDATED_BY() {
        return LAST_UPDATED_BY;
    }

    public void setLAST_UPDATED_BY(String LAST_UPDATED_BY) {
        this.LAST_UPDATED_BY = LAST_UPDATED_BY;
    }
}
