package DTO.Response.SearchResponse;

import DTO.Request.SearchRequest.Criteria;
import Entities.Customer;

import java.util.*;

public class SearchResults {
    private Criteria criteria;
    private List<Customer> results;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public List<Customer> getResults() {
        return results;
    }

    public void setResults(List<Customer> results) {
        this.results = results;
    }
}
