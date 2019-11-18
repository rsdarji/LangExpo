package com.langexpo.utility;

public interface Constant {

    //String WEB_SERVICE_HOST = "192.168.43.73";
    String WEB_SERVICE_HOST = "35.184.35.177"; //GOOGLE CLOUD INSTANCE
    //String WEB_SERVICE_HOST = "10.0.2.2";
    //String WEB_SERVICE_PORT= "8084";
    String WEB_SERVICE_PORT= "8080"; // GOOGLE CLOUD TOMCAT PORT

    String CONTEXT_PATH = "WebApplication5";
    String APPLICATION_PATH = "webresources";
    String CLASS_PATH = "webservices";
    String PROTOCOL = "http";
    String COLON = ":";
    String FORWARD_SLASH = "/";

    String SPINNER_DEFAULT_VALUE_LEVEL = "---Select level---";
    String SPINNER_DEFAULT_VALUE_LANGUAGE = "---Select Language---";
    String SPINNER_DEFAULT_VALUE_USER_LEVEL = "---Select User Level---";
    /**
     * Google cloud storage
     */

    String IMAGE_FOLDER = "images";
    String AUDIO_FOLDER = "audios";


    /**
     * Media content type
     */

    String PNG = "png";
    String JPG = "jpg";
    String MP3 = "mp3";
    String MP4 = "mp4";

    /**
     * User Role
     */

    int ADMIN = 1;
    int USER = 2;

    /**
     * Session constants
     */

    String UPLOADED_ITEM_URL = "uploaded_item_url";

    /**
     * Database
     */
    public interface User{
        String USER_ID = "user_id";
        String USER_NAME = "user_name";
        String EMAIL = "email";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PHONE = "phone";
        String ACTIVE = "active";
        String ROLE = "role_";
        String LANGUAGE = "language_";
        String USER_LEVEL = "user_level";
        String AVTAR = "avtar";

    }

    public final long userId = 1;


    public interface Session{
        String USER_SELECTED_LANGUAGE="userSelectedLanguage"; //SelectLanuguageAdapter.java
        String USER_SELECTED_GOALS = "userSelectedGoals"; //DailyGoal.java -> goalContinue()
        String USER_SELECTED_LEVEL = "userSelectedlevel";
        String USER_SELECTED_LEVEL_NAME = "userSelectedlevelName";
    }

    String NO_INTERNET_ERROR_MESSAGE = "Please connect to internet and try again.";

}
