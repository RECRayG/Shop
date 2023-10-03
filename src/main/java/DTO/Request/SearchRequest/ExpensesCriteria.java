package DTO.Request.SearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ExpensesCriteria {
    @JsonProperty("minExpenses")
    private BigDecimal minExpenses;
    @JsonProperty("maxExpenses")
    private BigDecimal maxExpenses;

    public ExpensesCriteria() {
        minExpenses = BigDecimal.ZERO;
        maxExpenses = BigDecimal.ZERO;
    }

    public BigDecimal getMinExpenses() {
        return minExpenses;
    }

    public void setMinExpenses(BigDecimal minExpenses) {
        this.minExpenses = minExpenses;
    }

    public BigDecimal getMaxExpenses() {
        return maxExpenses;
    }

    public void setMaxExpenses(BigDecimal maxExpenses) {
        this.maxExpenses = maxExpenses;
    }
}
