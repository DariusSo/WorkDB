package com.WorkDB;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static com.WorkDB.WorkDbApplication.completedPictures;
import static com.WorkDB.WorkDbApplication.completedTasks;

public class PictureThread implements Runnable{

    Repository repository = new Repository();

    @SneakyThrows
    @Override
    public void run() {
        while(true){
            List<Picture> workList = repository.getPictures();
            for(Picture p : workList){
                boolean flag = false;
                for(Picture pic : completedPictures){
                    if(pic.getId() == p.getId()){
                        flag = true;
                    }
                }
                if(flag){

                }else{
                    completedPictures.add(p);
                    byte[] bytes = p.getPicture();

                    Base64.Encoder encoder = Base64.getEncoder();
                    String encoded = encoder.encodeToString(bytes);
                    System.out.println(encoded);

                    String apiKey = URLEncoder.encode("b0a153b4ffa68be3b12a43d068976972", "UTF-8");
                    String image = URLEncoder.encode(encoded, "UTF-8");
                    String requestBody = "key=" + apiKey + "&image=" + image;
                    HttpRequest request = HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .uri(URI.create("https://api.imgbb.com/1/upload?"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    HttpResponse<String> response = null;
                    response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println(response.statusCode());
                    System.out.println(response.body());
                    Map<String, Object> mapping = new ObjectMapper().readValue(response.body(), HashMap.class);
                    Map<String, Object> mappingData = (Map<String, Object>) mapping.get("data");
                    repository.setPictureUrl((String) mappingData.get("url"), p.getId());
                }
            }
            Thread.sleep(10000);
        }
    }
}
