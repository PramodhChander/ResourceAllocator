package com.assignment.resource.allocator.entity;

/*
* Represents Server where ServerInstance has Server type specific details and keep track of count
* This class can be used to represent the Server present in DataCentre and also Server present in Configuration
* Keeping in mind that a real life Data Centre will definitely have upper bound on available resources
* In which case instanceCount field can be used to store it and keep track of available instances at any given moment.
*/
public class Server implements Comparable{

    private ServerInstance instance;
    private Integer instanceCount;

    /*
    * Used to mark when there are no upper bounds in available instances of given Server instance
    * Typically used to as a value for instanceCount when Server object is part of DataCentre
    */
    private static final Integer NO_LIMIT = -1;

    public ServerInstance getInstance() {
        return instance;
    }

    public void setInstance(ServerInstance instance) {
        this.instance = instance;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public Server(ServerInstance.ServerType serverType, Double price) {
        this(serverType, price, NO_LIMIT);
    }

    public Server(ServerInstance.ServerType serverType, Double price, Integer instanceCount) {
        this.instance = new ServerInstance(serverType, price);
        this.instanceCount = instanceCount;
    }

    public Server(Server originalServer) {
        this.instance = originalServer.getInstance();
        this.instanceCount = new Integer(originalServer.getInstanceCount());
    }

    public Double getServerCostPerHour() {
        Double cost = 0.0;
        cost = this.instance.getPrice();
        if(!NO_LIMIT.equals(this.instanceCount)) {
            cost = cost * this.instanceCount;
        }
        return cost;
    }

    public Integer getCoreCount() {
        Integer cpuCoreCount = this.instance.getServerType().getCpuCount();
        if(!NO_LIMIT.equals(this.instanceCount)) {
            cpuCoreCount *= this.instanceCount;
        }
        return cpuCoreCount;
    }

    public void incrementInstance() {
        if(!NO_LIMIT.equals(this.instanceCount)) {
            this.instanceCount = 1;
        } else {
            this.instanceCount++;
        }
    }

    /*
    * toString methods are overridden to get the output written in required format
    */
    @Override
    public String toString() {
        return "(\""+this.instance.getServerType()+"\", "+this.instanceCount+")";
    }

    /*
    * compareTo methods are implemented to help in sorting in desired fashion
    */
    public int compareTo(Object o) {
        Server server = (Server) o;
        return this.instance.compareTo(server.getInstance());
    }
}
