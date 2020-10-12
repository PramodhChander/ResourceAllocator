package com.assignment.resource.allocator.entity;

import java.util.List;

/*
* This class represents the single possible configuration for a given request
*/
public class Configuration implements Comparable {

    private String region;
    private Double totalCost;
    private List<Server> serverList;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public Configuration(String region) {
        this.region = region;
    }

    public Configuration(String region, Integer hours, List<Server> serverList) {
        this.region = region;
        this.serverList = serverList;
        Double totalCostForHours = 0.0;
        for(Server server : serverList) {
            totalCostForHours += server.getServerCostPerHour() * hours;
        }
        this.totalCost = totalCostForHours;
    }

    @Override
    public String toString() {
        return "\n{\n" +
                "    \"region\":\""+this.region+"\",\n" +
                "    \"total_cost\":\"$"+this.totalCost+"\",\n" +
                "    \"servers\":"+this.serverList+
                "\n}";
    }

    public int compareTo(Object o) {
        Configuration configuration = (Configuration) o;
        return this.totalCost.compareTo(configuration.getTotalCost());
    }

}
