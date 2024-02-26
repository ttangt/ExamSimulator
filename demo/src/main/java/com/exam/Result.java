package com.exam;

// import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

public class Result {
    MongoDatabase mongoDatabase;
    // String userName;
    String userEmail;
    // String userPassword;
    int questionID;
    String answer;
    boolean isCorrect;
    static Map<String, ArrayList<String>> questionHashMap;
    float score = 0;
    Date nowTime;

    public Result(MongoDatabase mongoDatabaseArg) {
        mongoDatabase = mongoDatabaseArg;
    }

    public void saveUser(String userEmailArg) {
        userEmail = userEmailArg;
    }

    int judgeResult(String answerArg,  String correctAnswerArg) {
        if (answerArg.equalsIgnoreCase(correctAnswerArg)) {
            isCorrect = true;
            return 1;
        } else {
            isCorrect = false;
            return 0;
        }
    }

    void saveAnswer(String userAnswerArg, String questionIdValArg) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("results");
        Document document = new Document("email", userEmail).
        append("createAt", nowTime).
        append("question_id", questionIdValArg).
        append("answer", userAnswerArg).
        append("is_correct", isCorrect);
        collection.insertOne(document);
    }

    public float checkSubmit(String userEmail, Map<String, ArrayList<String>> questionHashMap) {
        nowTime = new Date();
        return checkAnswers(questionHashMap);
    }

    float checkAnswers(Map<String, ArrayList<String>> questionHashMap) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("questions");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        Document doc;

        int correctCount = 0;
        String questionIdVal;

        while(mongoCursor.hasNext()) {
            doc = mongoCursor.next();
            questionIdVal = doc.get("question_id").toString();
            for (HashMap.Entry<String, ArrayList<String>> set: questionHashMap.entrySet()) {
                if (set.getValue().get(0).equalsIgnoreCase(questionIdVal)) {
                    String userAnswer = set.getValue().get(6);
                    String correctAnswer = doc.get("correct").toString();
                    correctCount += judgeResult(userAnswer,  correctAnswer);
                    saveAnswer(userAnswer, questionIdVal);
                }
            }
        }

        // System.out.println("(correctCount / questionHashMap.size()) * 100: " + ((float) correctCount / (float) questionHashMap.size()) * 100);
        return ((float) correctCount / (float) questionHashMap.size()) * 100;
    }

}
