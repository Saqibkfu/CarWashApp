package kfu.developers.car_wash;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class RegisterActivity extends AppCompatActivity {
    EditText register_usermail;
    EditText register_password;
    EditText confirm_pass;
    Button register_btn;
    TextView register_textview;
    ProgressBar progressbar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_usermail=(EditText)findViewById(R.id.register_usermail);
        register_password=(EditText)findViewById(R.id.register_password);
        confirm_pass=(EditText)findViewById(R.id.confirm_pass);
        register_btn=(Button)findViewById(R.id.register_btn);
        register_textview=(TextView)findViewById(R.id.register_textview);
        progressbar=(ProgressBar)findViewById(R.id.progressbar);
        auth=FirebaseAuth.getInstance();
        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, pass2;  email = register_usermail.getText().toString().trim();
                pass = register_password.getText().toString().trim();
                pass2 = confirm_pass.getText().toString().trim();
                if(TextUtils.isEmpty(email)) {
                    register_usermail.setError("Please Enter your email");
                    return;
                }
                if(TextUtils.isEmpty(pass)) {
                    register_password.setError("Please Enter your password");
                    return;
                }
                if(TextUtils.isEmpty(pass2)){
                    confirm_pass.setError("Please reenter your password");
                    return;
                }

                if(isNetworkAvailable()){
                    if (isValidEmail(email)) {
                            if (pass.equals(pass2)) {
                                progressbar.setVisibility(View.VISIBLE);
                                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressbar.setVisibility(View.GONE);
                                            sendEmailVerifcation();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Email address already exist", Toast.LENGTH_SHORT).show();
                                            progressbar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            else {
                                confirm_pass.setError("Password does not match");
                            }
                    }
                    else {
                        register_usermail.setError("Please Enter valid Email address");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Network not availbel", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void sendEmailVerifcation(){
        FirebaseUser user =auth.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(getApplicationContext(), "Successfully Registered verification Email has been sent...!", Toast.LENGTH_SHORT).show();
                       auth.signOut();
                       finish();
                       startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                   }
                   else {
                       Toast.makeText(getApplicationContext(), "Verification Email has'nt sent", Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
