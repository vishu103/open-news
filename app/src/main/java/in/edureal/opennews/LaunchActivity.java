package in.edureal.opennews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        Glide.with(LaunchActivity.this).load(R.drawable.logo).into((ImageView) findViewById(R.id.logo));

        VollySingleton v = VollySingleton.getInstance(this.getApplicationContext());

        if(!SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().contains("first")){
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("country","in");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("category","");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("sources","");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("apiKey","3e3ede875fb54bb5839065a9972f861f");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putInt("first",1);
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();
        }

        new CountDownTimer(3000,3000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }
}
