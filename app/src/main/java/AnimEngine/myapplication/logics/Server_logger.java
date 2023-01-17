package AnimEngine.myapplication.logics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import AnimEngine.myapplication.client.Engine;
import AnimEngine.myapplication.client.SelectActivity;
import AnimEngine.myapplication.creator.CreateActivity;
import AnimEngine.myapplication.logics.DB;
import AnimEngine.myapplication.login.SignInActivity;
import AnimEngine.myapplication.utils.User;

public class Server_logger extends AsyncTask {

    FirebaseAuth myAuth = DB.getAU();

    public synchronized boolean log_in_with_server(String email, String pass, Context context) throws IOException, JSONException {
        //String endpoint = "http://172.20.10.2:8080/login";
        String endpoint = "http://10.12.12.199:8000/login/";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        //String email = "animenginese@gmail.com";
        String password = "1234567";

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
                String token=response.toString().split(":")[1];
                myAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> m = new HashMap<>();
                            m.put("password", pass);
                            DB.getDB().getReference("Users").child(DB.getAU().getUid()).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");
                                    Toast.makeText(context, "Welcome back", Toast.LENGTH_LONG).show();
                                    if (!isCreator) {
                                        DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Map<String, Object> m = new HashMap<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    m.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                                                }
                                                Intent intent = new Intent(context.getApplicationContext(), Engine.class);
                                                intent.putExtra("Likes", new JSONObject(m).toString());
                                                context.startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        context.startActivity(new Intent(context.getApplicationContext(), CreateActivity.class));
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(context, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                System.out.println(token.substring(0,token.length()-1));
            }
        } else {
            System.out.println("POST request failed: " + responseCode);
            return false;
        }
        return true;

    }


    public void log_in(String email, String pass, Context context) {
        myAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> m = new HashMap<>();
                            m.put("password", pass);
                            DB.getDB().getReference("Users").child(DB.getAU().getUid()).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    boolean isCreator = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("true");
                                    Toast.makeText(context, "Welcome back", Toast.LENGTH_LONG).show();
                                    if (!isCreator) {
                                        DB.getDB().getReference("Likes").child(DB.getAU().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Map<String, Object> m = new HashMap<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    m.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                                                }
                                                Intent intent = new Intent(context.getApplicationContext(), Engine.class);
                                                intent.putExtra("Likes", new JSONObject(m).toString());
                                                context.startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        context.startActivity(new Intent(context.getApplicationContext(), CreateActivity.class));
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(context, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    public static void send_reset_password_mail(String email, Context context) {
        FirebaseAuth myAuth = DB.getAU();
        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show();

        } else {
            myAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Reset link sent to your email.", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context.getApplicationContext(), SignInActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error, couldn't send a link.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    protected Object doInBackground(Object[] objects) {

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        log_in_with_server((String) objects[0], (String) objects[1], (Context) objects[2]);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).run();
        try {
            if(!log_in_with_server((String) objects[0], (String) objects[1], (Context) objects[2])){
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }



}

