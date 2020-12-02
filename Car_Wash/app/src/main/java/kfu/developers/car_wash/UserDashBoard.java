package kfu.developers.car_wash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashBoard extends AppCompatActivity {
    ImageView logout_img;
    CardView logout;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        logout_img=(ImageView)findViewById(R.id.logout_img);
        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //auth.signOut();
               // finish();
                startActivity(new Intent(UserDashBoard.this,MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
