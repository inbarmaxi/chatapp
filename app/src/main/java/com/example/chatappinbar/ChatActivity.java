package com.example.chatappinbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ChatRv;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerViewMessages;
    EditText sendContentEt;
    Button sendContentBtn;


    private RequestQueue requestQueue;

    interface MessagesListener {
        void onReceiveMessages(List<ChatMessage> messageList);

        void onError(Exception e);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendContentBtn = findViewById(R.id.sendBtn);
        sendContentEt = findViewById(R.id.sendEditText);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));


        fetchMessages(new MessagesListener() {
            @Override
            public void onReceiveMessages(List<ChatMessage> messageList) {
                recyclerViewMessages.setAdapter(new ChatRv(messageList,
                        FirebaseAuth.getInstance().getCurrentUser()));
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ChatActivity.this, "Error loading messages, please check your network", Toast.LENGTH_SHORT).show();
            }
        });
        sendContentBtn.setOnClickListener((v) -> sendMessage());
    }

    public void sendMessage() {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (!sendContentEt.getText().toString().isEmpty() && u != null) {
            ChatMessage cm = new ChatMessage(getUserName(u), u.getUid(), getUserImage(u), sendContentEt.getText().toString());
            FirebaseFirestore.getInstance().collection("chat")
                    .add(cm);
            sendContentEt.getText().clear();
            sendFCMMessage(cm);
        }
    }


    public void sendFCMMessage(ChatMessage cm) {
        Sender sender = new Sender("all", cm, this, this);
        sender.sendNotification();
    }

    private String getUserName(FirebaseUser u) {
        return u.getDisplayName() != null ? u.getDisplayName() : u.getEmail().split("@")[0];
    }

    private String getUserImage(FirebaseUser u) {
        return u.getPhotoUrl() != null ? u.getPhotoUrl().toString() : "https://www.seekpng.com/png/full/966-9665493_my-profile-icon-blank-profile-image-circle.png";
    }


    public void fetchMessages(MessagesListener listener) {
        FirebaseFirestore.getInstance()
                .collection("chat")
                .orderBy("date")
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        listener.onError(error);
                    else if (value != null) {
                        List<ChatMessage> messageList = new ArrayList<>();
                        for (DocumentSnapshot messageDoc : value.getDocuments())
                            messageList.add(messageDoc.toObject(ChatMessage.class));
                        listener.onReceiveMessages(messageList);
                    }

                });
    }
}
