package lekkermode.flyingcat;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    DrawView lvl1;
    private final int timerInterval = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lvl1 = new DrawView(this);
        super.onCreate(savedInstanceState);
        setContentView(lvl1);
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

