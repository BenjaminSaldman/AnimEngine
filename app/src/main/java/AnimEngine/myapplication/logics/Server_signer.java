package AnimEngine.myapplication.logics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.client.SelectActivity;
import AnimEngine.myapplication.logics.DB;
import AnimEngine.myapplication.login.SignInActivity;
import AnimEngine.myapplication.utils.User;

public class Server_signer extends AsyncTask {
    String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
            "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
    public synchronized boolean sign_up_with_server(String uname, String email, String pass, String nick, boolean isCreator, Context context) throws IOException, JSONException {
        //String endpoint = "http://172.20.10.2:8080/signup/";
        String endpoint = "http://172.20.10.2:8000/signup/";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

//        String email2 = "animenginese@gmail.com";
//        String password = "1234567";

// Create the request body as a JSON object
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", pass);

// Write the request body to the output stream
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 307) {
            url = new URL(conn.getHeaderField("location"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            //String email = "animenginese@gmail.com";


// Create the request body as a JSON object
            requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", pass);

// Write the request body to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            responseCode = conn.getResponseCode();
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response from the server
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

            }
            DB.getAU().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();
                        String uid=user_.getUid();
                        if (!isCreator) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("false").build();
                            user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    User user = new User(uname, email, pass, nick, false, uid);
                                    DB.setUser(user);
                                    Map<String, Integer> likes = new HashMap<>();
                                    for (int i = 0; i < Gen.length; i++) {
                                        likes.put(Gen[i], 0);
                                    }
                                    DatabaseReference myRef = DB.getDB().getReference("Likes").child(uid);
                                    myRef.setValue(likes);
                                    Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context.getApplicationContext(), SelectActivity.class);
                                    intent.putExtra("uid", uid);
                                    context.startActivity(intent);
                                }
                            });
                        } else {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("true").build();
                            user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    User user = new User(uname, email, pass, nick, true, uid);
                                    DB.setUser(user);
                                    Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context.getApplicationContext(), SignInActivity.class));
                                }
                            });
                        }

                    }else{

                    }
                }
            });

        } else {
            //Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
            System.out.println("POST request failed: " + responseCode);
            return false;
        }


        return true;
    }
    public void sign_up(String uname, String email, String pass, String nick, boolean isCreator, Context context) {
        String[] Gen = {"Action", "Comedy", "Shonen", "Adventure", "Slice-of-Life", "Drama", "Fantasy", "Horror", "Magic", "Mystery",
                "Sci-Fi", "Psychological", "Supernatural", "Romance", "Crime", "Superhero", "Martial-arts"};
        FirebaseAuth myAuth = DB.getAU();
        myAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = task.getResult().getUser().getUid();
                    if (!isCreator) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("false").build();
                        user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                User user = new User(uname, email, pass, nick, false, uid);
                                DB.setUser(user);
                                Map<String, Integer> likes = new HashMap<>();
                                for (int i = 0; i < Gen.length; i++) {
                                    likes.put(Gen[i], 0);
                                }
                                DatabaseReference myRef = DB.getDB().getReference("Likes").child(uid);
                                myRef.setValue(likes);
                                Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context.getApplicationContext(), SelectActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });
                    } else {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("true").build();
                        user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                User user = new User(uname, email, pass, nick, true, uid);
                                DB.setUser(user);
                                Toast.makeText(context, "Welcome to AnimEngine, " + uname, Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context.getApplicationContext(), SignInActivity.class));
                            }
                        });
                    }


                } else {
                    Toast.makeText(context, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            if(!sign_up_with_server((String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3], (Boolean) objects[4],(Context) objects[5])){
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
