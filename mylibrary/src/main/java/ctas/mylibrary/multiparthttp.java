package ctas.mylibrary;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by CTAS on 2017/10/15.
 */
public class multiparthttp {
    public static void main(String args[]){
        System.out.println("哈哈");
        OkHttpClient client = new OkHttpClient();
        RequestBody imagebody = RequestBody.create(MediaType.parse("image/jpeg"),new File("C:/Users/CTAS/Pictures/1_1920x1080.jpg"));
        RequestBody body = new MultipartBuilder()
                .addFormDataPart("filename","haha.jpg",imagebody)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.10:8080/PostWeb/UploadServlet").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                System.out.println(response.body().string());
            } else{
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
}
