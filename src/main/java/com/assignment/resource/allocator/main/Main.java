package com.assignment.resource.allocator.main;

import com.assignment.resource.allocator.entity.Configuration;
import com.assignment.resource.allocator.entity.DataCentre;
import com.assignment.resource.allocator.entity.Request;
import com.assignment.resource.allocator.utility.IOUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static String dataCentreIndexFilePath = "./DataCentreIndex.txt";
    private static String incomingRequestFilePath = "./IncomingRequest.txt";
    private static String configurationProposalFilePath = "./ConfigurationProposal.txt";

    public static void main(String[] args) {
        String dataCentreIndexString = IOUtility.readFile(dataCentreIndexFilePath);
        List<DataCentre> dataCentreList = IOUtility.parseDataCentreIndex(dataCentreIndexString);
        ResourceAllocator resourceAllocator = new ResourceAllocator(dataCentreList);

        String incomingRequestString = IOUtility.readFile(incomingRequestFilePath);
        List<Request> requestList = IOUtility.parseIncomingRequest(incomingRequestString);

        Map<Integer, List<Configuration>> processedRequests= new HashMap<Integer, List<Configuration>>();
        for(Request request : requestList) {
            processedRequests.put(request.getRequestId(), resourceAllocator.processRequest(request));
        }

        IOUtility.writeFile(configurationProposalFilePath, processedRequests.toString());
    }
}
