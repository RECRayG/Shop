package DTO.Request.SearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastNameCriteria implements Criteria {
    @JsonProperty("lastName")
    private String lastName;

    public LastNameCriteria() {
        lastName = "";
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
