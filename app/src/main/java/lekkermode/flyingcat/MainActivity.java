package lekkermode.flyingcat;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
     //   MainActivity.Timer t = new Timer();
      //  t.start();
    }

  /*  class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            if (lvl1.points >= 30) {
                startActivity(new Intent(lvl1.getContext(), Level2Activity.class));
            }
        }
        @Override
        public void onFinish(){}
    }
**/
}

