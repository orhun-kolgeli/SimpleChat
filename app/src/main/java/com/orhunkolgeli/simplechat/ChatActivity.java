package com.orhunkolgeli.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final String USER_ID_KEY = "userId";
    private static final String BODY_KEY = "body";
    EditText etMessage;
    ImageButton ibSend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Check if the user is logged in. If they are, start with an existing user, otherwise, login as an anonymous user.
        if (ParseUser.getCurrentUser() != null) { // Start with existing user
            startWithCurrentUser(); //TODO: We will build out this method in the next step
        } else { // If not logged in, login as a new anonymous user
            login();
        }
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    private void startWithCurrentUser() {
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        // Get a reference to the EditText and ImageButton components.
        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message String
                String data = etMessage.getText().toString();
                // Create a ParseObject for the message
                ParseObject message = ParseObject.create("Message");
                // Save the USER_ID_KEY and BODY_KEY values in it.
                message.put(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
                message.put(BODY_KEY, data);
                // Persist the data to the database and pass it a call to SaveCallback()
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                // Clear the EditText component
                etMessage.setText(null);
            }
        });
    }



}