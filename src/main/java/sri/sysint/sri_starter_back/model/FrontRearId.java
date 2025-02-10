package sri.sysint.sri_starter_back.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FrontRearId implements Serializable {

    @Column(name = "HEADER_PLAN_ID", nullable = false)
    private Integer HEADER_PLAN_ID;

    @Column(name = "ITEM_CURING", nullable = false, length = 20)
    private String ITEM_CURING;

    // Default constructor
    public FrontRearId() {}

    public FrontRearId(FrontRearId frontRearId) {
        this.HEADER_PLAN_ID = frontRearId.getHEADER_PLAN_ID();
        this.ITEM_CURING = frontRearId.getITEM_CURING();
    }

    public FrontRearId(Integer HEADER_PLAN_ID, String ITEM_CURING) {
        super();
        this.HEADER_PLAN_ID = HEADER_PLAN_ID;
        this.ITEM_CURING = ITEM_CURING;
    }

    public Integer getHEADER_PLAN_ID() {
        return HEADER_PLAN_ID;
    }

    public void setHEADER_PLAN_ID(Integer HEADER_PLAN_ID) {
        this.HEADER_PLAN_ID = HEADER_PLAN_ID;
    }

    public String getITEM_CURING() {
        return ITEM_CURING;
    }

    public void setITEM_CURING(String ITEM_CURING) {
        this.ITEM_CURING = ITEM_CURING;
    }

    // Override equals and hashCode for correct behavior in JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrontRearId that = (FrontRearId) o;
        return Objects.equals(HEADER_PLAN_ID, that.HEADER_PLAN_ID) && 
               Objects.equals(ITEM_CURING, that.ITEM_CURING);
    }

    @Override
    public int hashCode() {
        return Objects.hash(HEADER_PLAN_ID, ITEM_CURING);
    }
}
