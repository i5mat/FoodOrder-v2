package my.edu.utem.ftmk.foodorderv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utem.ftmk.foodorderv2.view.home.HomeActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner;
    private EditText username, pssword, names;
    private Button btn_reg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        btn_reg = findViewById(R.id.register_btn);
        btn_reg.setOnClickListener(this);

        username = findViewById(R.id.usrname);
        pssword = findViewById(R.id.pssword);
        names = findViewById(R.id.name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register_btn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = username.getText().toString().trim();
        String password = pssword.getText().toString().trim();
        String name = names.getText().toString().trim();

        if (email.isEmpty()) {
            username.setError("Email is required!");
            username.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pssword.setError("Password is required!");
            pssword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pssword.setError("Min password length should be 6 characters!");
            pssword.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            names.setError("Full name is required!");
            names.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            username.setError("Please provide valid email!");
            username.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}