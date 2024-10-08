package com.WorkDB;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;

import com.mailjet.client.resource.Emailv31;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.WorkDB.WorkDbApplication.*;

public class EmailThread implements Runnable{

    Repository repository = new Repository();

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            List<Email> workList = repository.getEmails();

            for(Email e : workList){

                //lockEmailThread.lock();

                boolean flag = false;
                for(Email em : completedTasks){
                    if(em.getId() == e.getId()){
                        flag = true;
                    }
                }
                if(flag){
                    //lockEmailThread.unlock();
                }else{
                    completedTasks.add(e);
                    //lockEmailThread.unlock();

                    MailjetClient client;
                    MailjetRequest request;
                    MailjetResponse response;
                    client = new MailjetClient(System.getenv("API_KEY"), System.getenv("SECRET_API_KEY"), new ClientOptions("v3.1"));
                    request = new MailjetRequest(Emailv31.resource)
                            .property(Emailv31.MESSAGES, new JSONArray()
                                    .put(new JSONObject()
                                            .put(Emailv31.Message.FROM, new JSONObject()
                                                    .put("Email", "dariussspam@gmail.com")
                                                    .put("Name", "Darius So"))
                                            .put(Emailv31.Message.TO, new JSONArray()
                                                    .put(new JSONObject()
                                                            .put("Email", e.getSendTo())
                                                            .put("Name", "Name")))
                                            .put(Emailv31.Message.SUBJECT, "Test")
                                            .put(Emailv31.Message.TEXTPART, e.getContent())
                                            .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));

                    response = client.post(request);
                    System.out.println(Thread.currentThread().getName() + " " +response.getStatus());
                    System.out.println(response.getData());
                    if(response.getStatus() == 200){
                        repository.setDate(e.getId());
                    }
                }
            }
            Thread.sleep(10000);
        }
    }
}
