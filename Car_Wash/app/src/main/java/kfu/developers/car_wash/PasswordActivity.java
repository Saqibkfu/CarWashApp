package kfu.developers.car_wash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    EditText resetmail;
    Button reset_pass_btn;
    FirebaseAuth mAuth;
    ImageView backimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        resetmail=(EditText)findViewById(R.id.resetmail);
        reset_pass_btn=(Button)findViewById(R.id.reset_pas_btn);
        backimg=(ImageView)findViewById(R.id.imageView2);
        mAuth=FirebaseAuth.getInstance();
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this,MainActivity.class));
            }
        });
        reset_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rmail=resetmail.getText().toString().trim();
                if(TextUtils.isEmpty(rmail)) {
                    resetmail.setError("Please Enter registered mail");
                    return;
                }
                else{
                    mAuth.sendPasswordResetEmail(rmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(getApplicationContext(),"Password reset email successfully sent...!",Toast.LENGTH_SHORT).show();
                               finish();
                               startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                           }
                           else{
                               resetmail.setError("Email account is not registered");
                           }

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
