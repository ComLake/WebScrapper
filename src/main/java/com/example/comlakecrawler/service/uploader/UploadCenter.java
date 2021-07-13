package com.example.comlakecrawler.service.uploader;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.comlakecrawler.service.config.Annotation.ULAKE_UPLOAD_FILE;

public class UploadCenter {
    private boolean isExit = false;

    public UploadCenter() {
    }

    private void onStop() {
        this.isExit = true;
    }

    public void upload(File fileSend, String token, String link) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    try {
                        URL direct = new URL(ULAKE_UPLOAD_FILE);
                        OkHttpClient client = new OkHttpClient.Builder().build();
                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("file", fileSend.getAbsolutePath(),
                                        RequestBody.create(MediaType.parse("application/form-data"),
                                                fileSend))
                                .addFormDataPart("topics", "topic_sample")
                                .addFormDataPart("source", link)
                                .build();
                        Request request = new Request.Builder().
                                url(direct).
                                method("POST", body).
                                addHeader("Authorization", "Bearer " + token)
                                .build();
                        Response response = client.newCall(request).execute();
                        System.out.println(response.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        onStop();
                    }
                }
            }
        };
        runnable.run();
    }
}
