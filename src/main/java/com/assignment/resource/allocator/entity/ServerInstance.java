package com.assignment.resource.allocator.entity;

/*
* Represents a single instance of Server
*/
public class ServerInstance implements Comparable {
    /*
    * Enum representing the different types of Servers
    * Enhanced it to be more suitable for requirement
    */
    public enum ServerType {
        LARGE("large", 1),
        XLARGE("xlarge", 2),
        X2LARGE("2xlarge", 4),
        X4LARGE("4xlarge", 8),
        X8LARGE("8xlarge", 16),
        X10LARGE("10xlarge", 32);
        private Integer cpuCount;
        private String name;

        ServerType(String name, Integer cpuCount) {
            this.name = name;
            this.cpuCount = cpuCount;
        }

        public Integer getCpuCount() {
            return cpuCount;
        }

        private String getName() {
            return name;
        }

        public static ServerType getServerType(String typeName) {
            for(ServerType serverType : values())
                if(serverType.getName().equalsIgnoreCase(typeName)) return serverType;
            throw new IllegalArgumentException();
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
    private ServerType serverType;
    private Double price;

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ServerInstance(ServerType serverType, Double price) {
        this.serverType = serverType;
        this.price = price;
    }

    public int compareTo(Object o) {
        ServerInstance serverInstance = (ServerInstance) o;
        return this.serverType.getCpuCount().compareTo(serverInstance.getServerType().getCpuCount());
    }

}
