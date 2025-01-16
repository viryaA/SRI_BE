package sri.sysint.sri_starter_back.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SRI_IMPP_M_MACHINE_PRODUCT")
public class MachineProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_CURING", nullable = false)
    private String ITEM_CURING;

	@Column(name = "WORK_CENTER_TEXT", nullable = false)
	private String WORK_CENTER_TEXT;
	

	public MachineProduct() {
		super();
	}

	public MachineProduct(String iTEM_CURING, String wORK_CENTER_TEXT) {
		super();
		ITEM_CURING = iTEM_CURING;
		WORK_CENTER_TEXT = wORK_CENTER_TEXT;
	}

	public String getITEM_CURING() {
		return ITEM_CURING;
	}

	public void setITEM_CURING(String iTEM_CURING) {
		ITEM_CURING = iTEM_CURING;
	}


	public String getWORK_CENTER_TEXT() {
		return WORK_CENTER_TEXT;
	}


	public void setWORK_CENTER_TEXT(String wORK_CENTER_TEXT) {
		WORK_CENTER_TEXT = wORK_CENTER_TEXT;
	}
	
	
}
