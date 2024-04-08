package au.com.telstra.simcardactivator;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class SimCard {
    @Id
    private String iccid;
    @JsonProperty(access = Access.READ_ONLY)
    private String customerEmail;   

    public SimCard(String iccid, String customerEmail) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
    }

    // Getter and setter methods for 'iccid'
    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    // Getter and setter methods for 'customerEmail'
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
