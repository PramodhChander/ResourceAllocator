package com.assignment.resource.allocator.entity;

import java.util.List;

/*
* This class represents the Data Centre
* */
public class DataCentre {
    private String region;
    private List<Server> availableServers;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Server> getAvailableServers() {
        return availableServers;
    }

    public void setAvailableServers(List<Server> availableServers) {
        this.availableServers = availableServers;
    }

    public DataCentre(String region, List<Server> availableServers) {
        this.region = region;
        this.availableServers = availableServers;
    }

    @Override
    public String toString() {
        return this.region+" : "+this.availableServers;
    }
}
