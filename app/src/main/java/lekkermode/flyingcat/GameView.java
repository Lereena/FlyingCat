package lekkermode.flyingcat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 15.01.2017.
 */

public class GameView extends View {
    private int viewWidth;
    private int viewHeight;

    private int points = 0;

    private Bitmap planet = BitmapFactory.decodeResource(getResources(), R.drawable.planet2);
    private Sprite Cat;
    private Sprite enemyMonster;

    private final int timerInterval = 30;


    public GameView(Context context) {
        super(context);

        Bitmap player = BitmapFactory.decodeResource(getResources(), R.drawable.catsprites2);

        int w = player.getWidth()/4;
        int h = player.getHeight()/2;

        Rect firstFrame = new Rect(0, 0, w, h);
        Cat = new Sprite(10, 0, 0, 100, firstFrame, player);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i ==1 && j == 3) {
                    continue;
                }
                Cat.addFrame(new Rect(j*w, i*h, j*w + w, i*h + h));
            }
        }

        Bitmap enemy = BitmapFactory.decodeResource(getResources(), R.drawable.monstersprites2);

        w = enemy.getWidth()/4; // ширина-высота врага
        h = enemy.getHeight()/2;

        firstFrame = new Rect(4*w, 0, 5*w, h);

        enemyMonster = new Sprite(2000, 250, -300, 0, firstFrame, enemy);

        for (int i = 0; i < 2; i++) {
            for (int j = 3; j >= 0; j--) {
                if ((i == 0) && (j == 3)) {
                    continue;
                }
                if ((i == 1) && (j == 0)) {
                    continue;
                }
                enemyMonster.addFrame(new Rect(j*w, i*h, j*w+w, i*h+h));
            }
        }

        Timer t = new Timer();
        t.start();

    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect dstRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()); // создаём прямоугольник по размерам экрана
        canvas.drawBitmap(planet, null, dstRect, null); // устанавливаем на фон картинку, вписывая в прямоугольник
        Cat.draw(canvas); // отрисовываем кота
        enemyMonster.draw(canvas); // отрисовываем врага
        Paint point = new Paint();
        point.setAntiAlias(true);
        point.setColor(Color.BLUE);
        point.setTextSize(80);
        canvas.drawText(points+"", viewWidth - 280, 90, point); // пишем количество очков
        canvas.drawText("points", viewWidth - 280, 180, point);
    }

    protected void update() {
        Cat.update(timerInterval);
        enemyMonster.update(timerInterval);

        if (Cat.getY() + Cat.getFrameHeight() > viewHeight) {
            Cat.setY(viewHeight - Cat.getFrameHeight());
            Cat.setVelocityY(-Cat.getVelocityY());
        }
        else if (Cat.getY() < 0) {
            Cat.setY(0);
            Cat.setVelocityY(-Cat.getVelocityY());
        }

        if (enemyMonster.getX() < -enemyMonster.getFrameWidth()) {
            ReturnEnemy();
            points+=10;
        }
        if (enemyMonster.intersect(Cat)) {
            ReturnEnemy();
            points-=40;
        }
        invalidate();
    }

    private void ReturnEnemy() {
        enemyMonster.setX(viewWidth + Math.random() * 500);
        enemyMonster.setY(Math.random() * (viewHeight - enemyMonster.getFrameHeight()));
    }

    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }
        @Override
        public void onFinish(){}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN) {

            if (event.getY() < Cat.getBoundingBoxRect().top) {
                Cat.setVelocityY(-100);
            }
            else if (event.getY() > Cat.getBoundingBoxRect().bottom) {
                Cat.setVelocityY(100);
            }
        }
        return true;
    }
}

