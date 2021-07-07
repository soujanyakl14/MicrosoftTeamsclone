package com.example.microsoftclone.utilities;

import java.util.HashMap;

public class constants {
    public static final String KEY_COLLECTION_USERS="users";
    public static final String KEY_FIRST_NAME="first_name";
    public static final String KEY_LAST_NAME="last_name";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_IS_SIGNED_IN="signed in";
    public static final String KEY_USER_ID="user_id";
    public static final String KEY_USER_ICON="usericon";
    public static final String KEY_FCM_TOKEN="fcm_token";
    public static final String KEY_USER_NAME="username";
    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_TYPE="user_id";
    public static final String KEY_ROOM="room";
    public static final String REMOTE_MSG_CALL="call";
    public static final String REMOTE_MSG_CALL_TYPE="calltype";
    public static final String REMOTE_MSG_INVITER_TOKEN="invitertoken";
    public static final String REMOTE_MSG_CALL_RESPONSE="callresponse";
    public static final String REMOTE_MSG_CALL_ACCEPTED="accepted";
    public static final String REMOTE_MSG_CALL_REJECTED="rejected";
    public static final String REMOTE_MSG_CALL_CANCELLED="cancelled";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";






    public static HashMap<String,String> getHeaders(){
        HashMap<String,String> headers=new HashMap<>();
        headers.put(constants.REMOTE_MSG_AUTHORIZATION,
               "key=AAAAgc3sivM:APA91bFu2sppMy8NdrJBnyLF9Id8QkNNF2E0nrfvUOeqvBj7L2eED9dvJifKnk3GYU5XtaLG4nOVe1CYAAZxL5u99C_k-zwiAGW3JgvCz-O5p7jNfsNS22nleYaJPyBHGbNwXtx7pHUL");
        headers.put(constants.REMOTE_MSG_CONTENT_TYPE,"application/json");
        return headers;
    }

}
