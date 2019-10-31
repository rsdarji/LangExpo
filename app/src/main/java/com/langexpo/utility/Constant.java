package com.langexpo.utility;

public interface Constant {

    //String WEB_SERVICE_HOST = "192.168.43.73";
    String WEB_SERVICE_HOST = "10.0.2.2";
    String WEB_SERVICE_PORT= "8084";

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
    }
}
