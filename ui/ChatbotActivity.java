package com.example.smartfoods.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoods.R;
import com.example.smartfoods.chat.ChatbotEngine;

import java.util.ArrayList;

public class ChatbotActivity extends AppCompatActivity {

    private EditText input;
    private Button send;
    private ListView list;
    private ArrayList<String> messages = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ChatbotEngine engine;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        input = (EditText) findViewById(R.id.chatInput);
        send = (Button) findViewById(R.id.chatSend);
        list = (ListView) findViewById(R.id.chatList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        list.setAdapter(adapter);
        engine = new ChatbotEngine(getApplicationContext());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = input.getText().toString();
                if (text.trim().isEmpty()) return;
                messages.add("You: " + text);
                adapter.notifyDataSetChanged();
                input.setText("");
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final String reply = engine.respond(text);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.add("Bot: " + reply);
                                adapter.notifyDataSetChanged();
                                list.smoothScrollToPosition(messages.size() - 1);
                            }
                        });
                    }
                });
            }
        });
    }
}


