package sri.sysint.sri_starter_back.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "SRI_IMPP_HD_FRONT_REAR")
public class FrontRear implements Serializable {

    @EmbeddedId
    private FrontRearId id;

    @Column(name = "STATUS")
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @Column(name = "LAST_UPDATED_BY", length = 50)
    private String lastUpdatedBy;

    // Default constructor
    public FrontRear() {}

    // Constructor with all fields
    public FrontRear(FrontRearId id, Integer status, Date creationDate, 
                     String createdBy, Date lastUpdateDate, String lastUpdatedBy) {
        this.id = id;
        this.status = status;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Getters and Setters
    public FrontRearId getId() {
        return id;
    }

    public void setId(FrontRearId id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
