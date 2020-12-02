package kfu.developers.car_wash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView r_p_txtview,txtinfo,forgotpass;
    EditText main_usermail,main_password;
    Button main_login_btn, phonebtn;
    FirebaseAuth fdb;
    int counter = 5;
    RadioGroup radioGroup;
    RadioButton radiobtn, user, admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUiViews();
        fdb = FirebaseAuth.getInstance();
        r_p_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerationSelection(radioGroup);
            }
        });
        main_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String umail = main_usermail.getText().toString();
                String password = main_password.getText().toString();
                onRadioButtonClicked(radioGroup, umail, password);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_usermail.setHint("Enter your email");
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_usermail.setHint("Enter your CNIC");
            }
        });
        phonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhoneVarification.class));
            }
        });
    }
    public void setUiViews(){
        main_usermail = (EditText)findViewById(R.id.main_usermail);
        main_password = (EditText)findViewById(R.id.main_password);
        main_login_btn = (Button)findViewById(R.id.login_main_btn);
        r_p_txtview = (TextView)findViewById(R.id.r_p_txtview);
        txtinfo = (TextView)findViewById(R.id.txtinfo);
        forgotpass = (TextView)findViewById(R.id.forgotpass);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        user = (RadioButton)findViewById(R.id.radio_user);
        admin = (RadioButton)findViewById(R.id.radio_admin);
        phonebtn = (Button)findViewById(R.id.phonebtn);
    }

    public void onRadioButtonClicked(View view, String umail, String password) {
        int radioid = radioGroup.getCheckedRadioButtonId();
        radiobtn = view.findViewById(radioid);
        switch (radioid) {
            case ((int) R.id.radio_user):
                if (TextUtils.isEmpty(umail)) {
                    main_usermail.setError("Please Enter your email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    main_password.setError("Please Enter your password");
                    return;
                }
                fdb.signInWithEmailAndPassword(umail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkUserMail();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            counter--;
                            txtinfo.setText("No of remaining Attempts" + counter);
                            if (counter == 0) {
                                main_login_btn.setEnabled(false);
                                txtinfo.setText("Sorry yor are not login to my App");
                            }
                        }
                    }
                });
                break;
            case ((int) R.id.radio_admin):
                startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                break;
        }
    }

    public void registerationSelection(View view) {
        int radioid = radioGroup.getCheckedRadioButtonId();
        radiobtn =view.findViewById(radioid);
        switch (radioid) {
            case ((int) R.id.radio_user):
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case ((int) R.id.radio_admin):
                startActivity(new Intent(MainActivity.this, AdminRegistration.class));
                break;
        }

    }

    public void checkUserMail() {
        FirebaseUser user = fdb.getInstance().getCurrentUser();
        Boolean flag = user.isEmailVerified();
        if (flag) {
            startActivity(new Intent(MainActivity.this, UserDashBoard.class));
        } else {
            Toast.makeText(getApplicationContext(), "Please verify your email address first..!", Toast.LENGTH_SHORT).show();
            fdb.signOut();
        }
    }
}
