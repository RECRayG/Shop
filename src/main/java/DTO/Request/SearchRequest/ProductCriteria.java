package DTO.Request.SearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductCriteria {
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("minTimes")
    private Integer minTimes;

    public ProductCriteria() {
        productName = "";
        minTimes = 0;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getMinTimes() {
        return minTimes;
    }

    public void setMinTimes(Integer minTimes) {
        this.minTimes = minTimes;
    }
}
