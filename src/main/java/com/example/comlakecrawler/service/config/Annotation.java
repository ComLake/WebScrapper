package com.example.comlakecrawler.service.config;

public class Annotation {
    public static String ULAKE_LOGOUT = "http://localhost:5000/api/auth/logout";
    public static String ULAKE_REGISTER = "http://localhost:5000/api/auth/signup";
    public static String ULAKE_UPLOAD_FILE = "http://localhost:5000/api/files";
    public static String ULAKE_REFRESH_TOKEN = "http://localhost:5000/api/auth/refresh-token";
    public static String ULAKE_SIGN_IN = "http://localhost:5000/api/auth/signin";
    public static String GITHUB_API_BASE_URL = "https://api.github.com/";
    public static String KAGGLE_API_BASE_URL = "https://www.kaggle.com/api/v1/datasets/";
    public static String KAGGLE_API_SEARCH = "list?group=public&sortBy=hottest&size=all&filetype=all&license=all&search=";
    public static String GITHUB_API_SEARCH_REPOSITORIES = "search/repositories?q=";
    public static String GITHUB_REPOS = "repos/";
    public static String KAGGLE_PAGE = "&page=1";
    public static String GITHUB_ZIP_DOWNLOAD = "zipball/master";
    public static String KAGGLE_ZIP_DOWNLOAD = "download/";
    public static String defaultKName = "thanhjeff";
    public static String defaultKPass = "2cd30ce68497dcab0c97efce84937a49";
}
