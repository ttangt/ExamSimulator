package com.exam;

// import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

public class User {
    MongoDatabase mongoDatabase;
    String userName;
    static String userEmail;
    int isNew = 0;
    
    public User(MongoDatabase mongoDatabaseArg, String userNameArg, String userEmailArg) {
        mongoDatabase = mongoDatabaseArg;
        userName = userNameArg;
        userEmail = userEmailArg;
    }

    int createUser() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("users");
        Document document = new Document("name", userName).
        append("email", userEmail);
        verifyUniqueEmail(collection);
        if (isNew > 0) {
            collection.insertOne(document);
            // System.out.println("user is created!");
            return 1;
        } else {
            // System.out.println("use other email");
            return 0;
        }
    }

    void verifyUniqueEmail(MongoCollection<Document> collection) {
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        Document doc;

        String dbUserEmail;

        isNew = 1;

        while(mongoCursor.hasNext()) {
            doc = mongoCursor.next();
            dbUserEmail = doc.get("email").toString();
            if (userEmail.equalsIgnoreCase(dbUserEmail)){
                isNew = 0;
                break;
            }
        }
    }
}
