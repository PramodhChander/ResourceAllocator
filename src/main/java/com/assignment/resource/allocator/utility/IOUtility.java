package com.assignment.resource.allocator.utility;

import com.assignment.resource.allocator.entity.DataCentre;
import com.assignment.resource.allocator.entity.Server;
import com.assignment.resource.allocator.entity.ServerInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.assignment.resource.allocator.entity.Request;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOUtility {

    public static Logger LOGGER = Logger.getLogger(IOUtility.class.getSimpleName());
    private static Gson gson = new Gson();

    public static String readFile(String path) {
        String readInput = null;
        try {
            File inputFile = new File(path);
            return FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
        } catch(IOException ie) {
            LOGGER.log(Level.SEVERE, "Exception occurred while reading file "+path);
        }
        return readInput;
    }

    public static void writeFile(String path, String content) {
        try{
            File outputFile = new File(path);
            FileUtils.write(outputFile, content, true);
        } catch(IOException ie) {
            LOGGER.log(Level.SEVERE, "Exception occurred while writing file "+path);
        }
    }

    public static List<DataCentre> parseDataCentreIndex(String inputString) {
        List<DataCentre> dataCentreList = new ArrayList<DataCentre>();
        try {
            JSONObject inputJson = new JSONObject(inputString);
            for(String regionKey : inputJson.keySet()) {
                JSONObject serverListJson = inputJson.getJSONObject(regionKey);
                if(serverListJson != null) {
                    List<Server> serverList = new ArrayList<Server>();
                    for(String serverTypeKey : serverListJson.keySet()) {
                        ServerInstance.ServerType serverType = ServerInstance.ServerType.getServerType(serverTypeKey);
                        Server server = new Server(serverType, serverListJson.getDouble(serverTypeKey));
                        serverList.add(server);
                    }
                    Collections.reverse(serverList);
                    DataCentre dataCentre = new DataCentre(regionKey, serverList);
                    dataCentreList.add(dataCentre);
                }
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid input");
        }
        return dataCentreList;
    }

    public static List<Request> parseIncomingRequest(String inputString) {
        Type requestListType = new TypeToken<ArrayList<Request>>(){}.getType();
        ArrayList<Request> requestList = gson.fromJson(inputString, requestListType);
        return requestList;
    }

}
