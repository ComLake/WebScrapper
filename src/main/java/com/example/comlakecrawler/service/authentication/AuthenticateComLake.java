package com.example.comlakecrawler.service.authentication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static com.example.comlakecrawler.service.config.Annotation.*;

public class AuthenticateComLake {
    private String token;
    private String refreshToken;
    private boolean isExit;
    private String username;
    private String email;
    private String passwords;
    private UserInterface listener;
    private String roles;
    private int requestCode;

    public AuthenticateComLake() {
        isExit = false;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    public void setListener(UserInterface userInterface){
        this.listener = userInterface;
    }
    public void createAccount() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    try {
                        URL direct = new URL(ULAKE_REGISTER);
                        HttpURLConnection urlConnection = (HttpURLConnection) direct.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setDoOutput(true);
                        String jsonInputString = "{\n" +
                                "  \"username\": \""+username+"\",\n" +
                                "  \"email\": \""+email+"\",\n" +
                                "  \"firstname\": \"string\",\n" +
                                "  \"lastname\": \"string\",\n" +
                                "  \"department\": \"string\",\n" +
                                "  \"affiliation\": \"string\",\n" +
                                "  \"password\": \""+passwords+"\"\n" +
                                "}";
                        try (OutputStream outputStream = urlConnection.getOutputStream()) {
                            byte[] input = jsonInputString.getBytes("UTF-8");
                            outputStream.write(input, 0, input.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream(), "UTF-8"
                        ))) {
                            StringBuffer response = new StringBuffer();
                            String responseLine = null;
                            while ((responseLine = bufferedReader.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            System.out.println(response.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        isExit = true;
                    }
                }
            }
        };
        runnable.run();
        isExit = false;
    }

    public void login() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    try {
                        URL direct = new URL(ULAKE_SIGN_IN);
                        HttpURLConnection urlConnection = (HttpURLConnection) direct.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setDoOutput(true);
                        String jsonInputString = "{\n" +
                                "  \"username\": \"" + username + "\",\n" +
                                "  \"password\": \"" + passwords + "\"\n" +
                                "}";
                        StringBuffer response = null;
                        try (OutputStream outputStream = urlConnection.getOutputStream()) {
                            byte[] input = jsonInputString.getBytes("UTF-8");
                            outputStream.write(input, 0, input.length);
                        }
                        if (urlConnection.getResponseCode() != 200) {
                            System.out.println("Invalid access");
                        } else {
                            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream(), "UTF-8"
                            ))) {
                                response = new StringBuffer();
                                String responseLine = null;
                                while ((responseLine = bufferedReader.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                System.out.println(response.toString());
                            }
                            JSONObject jsonObject = new JSONObject(response.toString());
                            refreshToken = jsonObject.getString("refreshToken");
                            token = jsonObject.getString("accessToken");
                            email = jsonObject.getString("email");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        saveInfo();
                        isExit = true;
                    }
                }
            }
        };
        runnable.run();
        isExit = false;
    }
    private void saveInfo(){
        if (listener!=null){
            listener.infoUpdate(token,refreshToken,username,email,roles);
        }
    }
    public void logout(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit){
                    try {
                        URL direct = new URL(ULAKE_LOGOUT);
                        HttpURLConnection urlConnection = (HttpURLConnection)direct.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setRequestProperty("Authorization", "Bearer " +
                                token);
                        int responseCode = urlConnection.getResponseCode();
                        System.out.println("\nSending 'GET' Request to URL" + direct);
                        System.out.println("Response code :" + responseCode);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        isExit = true;
                    }
                }
            }
        };
        runnable.run();
        isExit = false;
    }
    public void refreshToken() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    try {
                        URL direct = new URL(ULAKE_REFRESH_TOKEN);
                        HttpURLConnection urlConnection = (HttpURLConnection) direct.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setRequestProperty("Authorization", "Bearer " +
                                token);
                        urlConnection.setDoOutput(true);
                        String jsonInputString = "{\n" +
                                "  \"refreshToken\": \"" + refreshToken + "\"\n" +
                                "}";
                        StringBuffer response = null;
                        try (OutputStream outputStream = urlConnection.getOutputStream()) {
                            byte[] input = jsonInputString.getBytes("UTF-8");
                            outputStream.write(input, 0, input.length);
                        }
                        System.out.println(urlConnection.getResponseCode());
                        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream(), "UTF-8"
                        ))) {
                            response = new StringBuffer();
                            String responseLine = null;
                            while ((responseLine = bufferedReader.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            System.out.println(response.toString());
                        }
                        JSONObject jsonObject = new JSONObject(response.toString());
                        token = jsonObject.getString("token");
                        refreshToken = jsonObject.getString("refreshToken");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (listener!=null){
                            listener.infoUpdate(token,refreshToken,username,email,roles);
                        }
                        isExit = true;
                    }
                }
            }
        };
        runnable.run();
        isExit = false;
    }
}
