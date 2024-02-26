package com.exam;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
// import com.mongodb.client.MongoCollection;
// import org.bson.Document;

public class DatabaseGeneral {
    String hostDomain;
    int hostPort;
    MongoClient mongoClient;
    static MongoDatabase mongoDatabase;

    public DatabaseGeneral(String hostDomainArg, int hostPortArg) {
        hostDomain = hostDomainArg;
        hostPort = hostPortArg;
    }

    void checkServiceConnection() {
        // connected to mongodb service
        mongoClient = new MongoClient(hostDomain, hostPort);
        System.out.println("Connect to mongoDB service successfully");
    }

    void checkDatabaseConnection() {
        // connected to mongo database
        mongoDatabase = mongoClient.getDatabase("exam_simulator");
        System.out.println("Connect to mongoDB database successfully");
    }

    void createConnection(String name) {
        mongoDatabase.createCollection(name);
        System.out.println(name + " collection is created");
    }
}
