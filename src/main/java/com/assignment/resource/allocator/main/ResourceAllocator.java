package com.assignment.resource.allocator.main;

import com.assignment.resource.allocator.entity.Configuration;
import com.assignment.resource.allocator.entity.DataCentre;
import com.assignment.resource.allocator.entity.Request;
import com.assignment.resource.allocator.entity.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* All business logic is added to this class. Entity of this class is configured for a given
* input configuration to propose configurations from each data centre
*/
public class ResourceAllocator {

    public static java.util.logging.Logger LOGGER = Logger.getLogger(ResourceAllocator.class.getSimpleName());

    public List<DataCentre> dataCentreList;

    public List<DataCentre> getDataCentreList() {
        return dataCentreList;
    }

    public void setDataCentreList(List<DataCentre> dataCentreList) {
        this.dataCentreList = dataCentreList;
    }

    public ResourceAllocator(List<DataCentre> dataCentreList) {
        this.dataCentreList = dataCentreList;
    }

    /*
    * For a given request this method returns best suited configurations from each data centre
    * This method delegates the call accordingly based on request
    *
    * @param request Request object
    * @return configurationList List of Configurations
    *
    */
    public List<Configuration> processRequest(Request request) {
        List<Configuration> possibleConfigurations = new ArrayList<Configuration>();
        if(request.getHours() == null || request.getRequestId() == null) {
            LOGGER.log(Level.WARNING, "Invalid request : "+request.getRequestId());
            return possibleConfigurations;
        }
        if(request.getPrice() == null) {
            for(DataCentre dataCentre : this.dataCentreList) {
                Configuration config = getConfigurationProposal(dataCentre, request.getHours(), request.getCpus());
                if(config != null) {
                    possibleConfigurations.add(config);
                } else {
                    LOGGER.log(Level.INFO, "No feasible configuration available in "+dataCentre.getRegion()+" for request : "+request.getRequestId());
                }
            }
        } else if(request.getCpus() == null) {
            for(DataCentre dataCentre : this.dataCentreList) {
                Configuration config = getConfigurationProposal(dataCentre, request.getHours(), request.getPrice());
                if(config != null) {
                    possibleConfigurations.add(config);
                } else {
                    LOGGER.log(Level.INFO, "No feasible configuration available in "+dataCentre.getRegion()+" for request : "+request.getRequestId());
                }
            }
        } else {
            for(DataCentre dataCentre : this.dataCentreList) {
                Configuration config = getConfigurationProposal(dataCentre, request.getHours(), request.getCpus(), request.getPrice());
                if(config != null) {
                    possibleConfigurations.add(config);
                } else {
                    LOGGER.log(Level.INFO, "No feasible configuration available in "+dataCentre.getRegion()+" for request : "+request.getRequestId());
                }
            }
        }
        Collections.sort(possibleConfigurations);
        return possibleConfigurations;
    }

    private Configuration getConfigurationProposal(DataCentre dataCentre, Integer hours, Integer cpuCount, Double maxPrice) {
        Configuration config = this.getConfigurationProposal(dataCentre, cpuCount, hours);
        if(config.getTotalCost() > maxPrice) {
            return null;
        } else {
            return config;
        }
    }

    private Configuration getConfigurationProposal(DataCentre dataCentre, Integer hours, Integer cpuCount) {
        Configuration config = null;
        List<Server> configServerList = new ArrayList<Server>();
        Server prevServer = null;
        for(Server server : dataCentre.getAvailableServers()) {
            Server configServer = new Server(server);
            Integer maxPossibleInstance = cpuCount / configServer.getCoreCount();
            if(maxPossibleInstance > 1) {
                configServer.setInstanceCount(maxPossibleInstance);
                cpuCount -= configServer.getCoreCount();
                configServerList.add(configServer);
            }
            prevServer = configServer;
        }
        if(cpuCount > 0 && prevServer != null) {
            prevServer.incrementInstance();
            if(!prevServer.equals(configServerList.get(configServerList.size() - 1))) {
                configServerList.add(prevServer);
            }
            cpuCount -= prevServer.getInstance().getServerType().getCpuCount();
        }
        if(configServerList.size() > 0 && cpuCount <= 0) {
            config = new Configuration(dataCentre.getRegion(), hours, configServerList);
        }
        return config;
    }

    private Configuration getConfigurationProposal(DataCentre dataCentre, Integer hours, Double maxPrice) {
        Configuration config = null;
        List<Server> configServerList = new ArrayList<Server>();
        Integer cpuCount = 0;
        for(Server server : dataCentre.getAvailableServers()) {
            Server configServer = new Server(server);
            Double cost = configServer.getServerCostPerHour() * hours;
            Integer maxPossibleInstance = (int)(maxPrice / cost);
            if(maxPossibleInstance > 0) {
                configServer.setInstanceCount(maxPossibleInstance);
                maxPrice -= cost * maxPossibleInstance;
                cpuCount += configServer.getCoreCount();
                configServerList.add(configServer);
            }
        }
        if(configServerList.size() > 0 && cpuCount > 0) {
            config = new Configuration(dataCentre.getRegion(), hours, configServerList);
        }
        return config;
    }
}
