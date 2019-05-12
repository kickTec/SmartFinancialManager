package com.kenick.log;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongodbTest {

    @Test
    public void TestMongodb(){
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        List<ServerAddress> serverList = new ArrayList<>();
        serverList.add(serverAddress);

        MongoCredential credential = MongoCredential.createCredential("kenick", "admin", "kenick.com".toCharArray());
        ArrayList<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(credential);

        MongoClient mongoClient = new MongoClient(serverList, mongoCredentialList);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("tutorial");
        String name = mongoDatabase.getName();
        System.out.println(name);
    }
}
