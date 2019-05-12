package com.kenick.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoWriteLogRunnable implements Runnable {

    @Override
    public void run() {
        // 写日志任务
        MongoDatabase databaseConn = getMongoDatabaseConn();
        writeLog(databaseConn);
    }

    private String username;
    private String pwd;
    private String msg;
    private String dbName;
    private static MongoDatabase mongoDatabase;

    public MongoWriteLogRunnable(String username, String pwd, String dbName, String msg){
        this.username = username;
        this.pwd = pwd;
        this.dbName = dbName;
        this.msg = msg;
    }

    // 获取mongodb数据库连接 注意多线程环境
    private MongoDatabase getMongoDatabaseConn(){
        if(mongoDatabase == null){
            synchronized (MongoWriteLogRunnable.class){
                if(mongoDatabase == null){
                    ServerAddress serverAddress = new ServerAddress("localhost", 27017);
                    List<ServerAddress> serverList = new ArrayList<>();
                    serverList.add(serverAddress);

                    MongoCredential credential = MongoCredential.createCredential(username, "admin", pwd.toCharArray());
                    ArrayList<MongoCredential> mongoCredentialList = new ArrayList<>();
                    mongoCredentialList.add(credential);

                    MongoClient mongoClient = new MongoClient(serverList, mongoCredentialList);
                    mongoDatabase = mongoClient.getDatabase(dbName);
                }
            }
        }
        return mongoDatabase;
    }

    // 往mongodb中写入日志
    private void writeLog(MongoDatabase mongoDatabase){
        try {
            MongoCollection<Document> testLogCollection = mongoDatabase.getCollection("testLog");
            testLogCollection.insertOne(new Document("msg",msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
