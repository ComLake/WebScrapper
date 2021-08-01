package com.example.comlakecrawler.service.uploader;

import okhttp3.*;
import org.apache.commons.io.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
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
    public void uploadTryOut(File fileSend, String token, String link){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit){
                    String crlf = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                    String mimeType = fileTypeMap.getContentType(fileSend.getName());
                    /*-------------------Set up the request--------------------*/
                    HttpURLConnection httpURLConnection = null;
                    try {
                        URL url = new URL(ULAKE_UPLOAD_FILE);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Connection","Keep-Alive");
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " +
                                token);
                        httpURLConnection.setRequestProperty("Cache-Control","no-cache");
                        httpURLConnection.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary="+boundary);
                        /*-------------------Start content wrapper--------------------*/
                        DataOutputStream request = new DataOutputStream(
                                httpURLConnection.getOutputStream()
                        );
                        request.writeBytes(twoHyphens+boundary+crlf);
                        request.writeBytes("Content-Disposition: form-data; name=\""+
                                        "file"+
                                "\";filename=\""+
                                fileSend.getAbsolutePath()+ "\""+crlf);
                        request.writeBytes("Content-Type: "+mimeType+crlf);
                        request.writeBytes(crlf);
                        /*-------------------Convert Bitmap to ByteBuffer--------------------*/
                        byte[] fileBytes = FileUtils.readFileToByteArray(fileSend);
                        request.write(fileBytes);
                        /*-------------------End content wrapper--------------------*/
                        request.writeBytes(crlf);
                        request.writeBytes(twoHyphens+boundary+twoHyphens+crlf);
                        /*-------------------Flush output buffer--------------------*/
                        request.flush();
                        request.close();
                        /*-------------------Get response--------------------*/
                        InputStream responseStream = new BufferedInputStream(httpURLConnection.getInputStream());
                        BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseStream));
                        String line="";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = responseReader.readLine())!=null){
                            stringBuilder.append(line).append("\n");
                        }
                        responseReader.close();
                        String response = stringBuilder.toString();
                        System.out.println(response);
                        /*-------------------Close response stream--------------------*/
                        responseStream.close();
                        httpURLConnection.disconnect();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        onStop();
                    }
                }
            }
        };
        runnable.run();
    }
    public String fileFormat(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
