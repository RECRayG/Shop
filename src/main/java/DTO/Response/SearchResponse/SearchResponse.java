package DTO.Response.SearchResponse;

import DTO.Request.SearchRequest.Criteria;

import java.util.*;

public class SearchResponse {
    private String type;
    private List<SearchResults> results;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SearchResults> getResults() {
        return results;
    }

    public void setResults(List<SearchResults> results) {
        this.results = results;
    }
}
