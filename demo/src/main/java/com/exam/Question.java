package com.exam;

import java.util.*;

// import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class Question {
    MongoDatabase mongoDatabase;
    Map<String, ArrayList<String>> questionHashMap;
    static int totalQuestionIndex;

    public Question(MongoDatabase mongoDatabaseArg, Map<String, ArrayList<String>> questionHashMapArg) {
        mongoDatabase = mongoDatabaseArg;
        questionHashMap = questionHashMapArg;
        loopQuestions(questionHashMap);
    }
    
    void loopQuestions(Map<String, ArrayList<String>> questionHashMap) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("questions");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        Document doc;

        int count = 0;
        String questionIdVal;
        String questionVal;
        String aVal;
        String bVal;
        String cVal;
        String dVal;

        // iterating 'questions' collection
        while(mongoCursor.hasNext()) {
                doc = mongoCursor.next();
                questionIdVal = doc.get("question_id").toString();
                questionVal = doc.get("question").toString();
                aVal = doc.get("A").toString();
                bVal = doc.get("B").toString();
                cVal = doc.get("C").toString();
                dVal = doc.get("D").toString();
                ArrayList<String> questionArrayList = new ArrayList<>();
                questionArrayList.add(questionIdVal);
                questionArrayList.add(questionVal);
                questionArrayList.add(aVal);
                questionArrayList.add(bVal);
                questionArrayList.add(cVal);
                questionArrayList.add(dVal);
                questionArrayList.add("none");
                questionHashMap.put(Integer.toString(count),questionArrayList);
                count++;
        }

        totalQuestionIndex = count;
    }
}
