package kfu.developers.car_wash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneVarification extends AppCompatActivity {
    Button signinbtn,getcodebtn;
    EditText phone,code;
    ImageView backimg;
    FirebaseAuth mauth;
    String veificatinCode,recivedcode,mobile;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_varification);
        signinbtn=(Button)findViewById(R.id.button);
        getcodebtn=(Button)findViewById(R.id.button2);
        phone=(EditText)findViewById(R.id.editText);
        code=(EditText)findViewById(R.id.editText2);
        backimg=(ImageView)findViewById(R.id.imageView2);
        mauth= FirebaseAuth.getInstance();
        getcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile=phone.getText().toString().trim();
                if (mobile.isEmpty()) {
                    phone.setError("Please enter phone number");
                }
                if(PhoneValidation(mobile)){
                    phone.setError("Please enter phone number");
                }
                else{
                    getVerificationCode(mobile);
                }
            }
        });
    }
    public void getVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mcallback
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            veificatinCode=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
               recivedcode=phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), "Verification failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code1){
           PhoneAuthCredential credential=PhoneAuthProvider.getCredential(veificatinCode,code1);
           signInwithCrendential(credential);
    }

    public void signInwithCrendential(PhoneAuthCredential credential){


    }



    public boolean PhoneValidation(String pnum){
        Pattern pattern = Pattern.compile("\\d{4}-\\d{7}");
        Matcher matcher = pattern.matcher(pnum);

        if (matcher.matches()) {
            return true;
        }
        else
        {
            return false;
        }
    }


}
