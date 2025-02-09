package sri.sysint.sri_starter_back.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FrontRearId implements Serializable {

    @Column(name = "HEADER_PLAN_ID", nullable = false)
    private Long headerPlanId;

    @Column(name = "ITEM_CURING", nullable = false, length = 20)
    private String itemCuring;

    // Default constructor
    public FrontRearId() {}

    public FrontRearId(Long headerPlanId, String itemCuring) {
        this.headerPlanId = headerPlanId;
        this.itemCuring = itemCuring;
    }

    public Long getHeaderPlanId() {
        return headerPlanId;
    }

    public void setHeaderPlanId(Long headerPlanId) {
        this.headerPlanId = headerPlanId;
    }

    public String getItemCuring() {
        return itemCuring;
    }

    public void setItemCuring(String itemCuring) {
        this.itemCuring = itemCuring;
    }

    // Override equals and hashCode for correct behavior in JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrontRearId that = (FrontRearId) o;
        return Objects.equals(headerPlanId, that.headerPlanId) && 
               Objects.equals(itemCuring, that.itemCuring);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerPlanId, itemCuring);
    }
}
