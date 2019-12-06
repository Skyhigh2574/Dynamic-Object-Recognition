package ml.uncoded.yts.searchyoutube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FrameLayout frameLayout;
   public static FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefrenceUser sharedPreferences=SharedPrefrenceUser.getInstance(this);
        if(sharedPreferences.getKeyIsnewuser().equals("true"))
        {
            startActivity(new Intent(LoginActivity.this,ImgCaptureActivity.class));
        }else {
            setContentView(R.layout.activity_login2);
            mAuth = FirebaseAuth.getInstance();
            frameLayout = findViewById(R.id.frame_layout);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame_layout, new LoginFragment()).commit();
        }
    }
}
