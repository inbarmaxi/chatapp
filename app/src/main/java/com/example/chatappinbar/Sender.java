package com.example.chatappinbar;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sender {

    String userFcmToken;
    ChatMessage cm;
    Context mContext;
    Activity mActivity;


    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAVwA2F9g:APA91bGsppdn7Vz_x8PN869ZEUE2pbx9dXPE9BYjJQBcfvTh2qymGQr-oWIYlVw1buDnKwrY106912LHiJQlU06dGud2hJ_a0CskOXqHr-MpqCsqRqYV868z3mMwqrVVv9XU_jgYcVmv";

    public Sender(String userFcmToken, ChatMessage cm, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.cm = cm;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }

    public void sendNotification() {

        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", "/topics/" + userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", cm.getSenderName());
            notiObject.put("body", cm.getContent());
            notiObject.put("icon", R.id.logoChat); // enter icon that exists in drawable only

            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, (Response.ErrorListener) error -> {
            }) {
                @Override
                public Map<String, String> getHeaders() {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}