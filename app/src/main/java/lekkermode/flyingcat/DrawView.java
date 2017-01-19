package lekkermode.flyingcat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * Created by Alex on 18.01.2017.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int timerActivityInterval = 50;
    private DrawThread drawThread;

    private int viewWidth;
    private int viewHeight;

    protected int points = 0;

    private Bitmap planet = BitmapFactory.decodeResource(getResources(), R.drawable.planet2);
    private Sprite Cat;
    private Sprite enemyMonster;
    private Sprite banana;

    private final int timerInterval = 30;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);

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

        Bitmap bonus = BitmapFactory.decodeResource(getResources(), R.drawable.banana);

        w = bonus.getWidth();
        h = bonus.getHeight();

        firstFrame = new Rect(0, 0, w, h);

        banana = new Sprite(2000, 250, -200, 0, firstFrame, bonus);

        DrawView.DrawTimer t = new DrawView.DrawTimer();
        t.start();

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(), getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            }
            catch (InterruptedException e) {}
        }
    }


    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    protected void update() {
        Cat.update(timerInterval);
        enemyMonster.update(timerInterval);
        banana.update(timerInterval);

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
        if (banana.getX() < -banana.getFrameWidth()) {
            ReturnBanana();
        }
        if (banana.intersect(Cat)) {
            ReturnBanana();
            points+=15;
        }
        invalidate();
    }

    private void ReturnEnemy() {
        enemyMonster.setX(viewWidth + Math.random() * 500);
        enemyMonster.setY(Math.random() * (viewHeight - enemyMonster.getFrameHeight()));
    }

    private void ReturnBanana() {
        banana.setX(viewWidth + Math.random() * 700);
        banana.setY(Math.random() * (viewHeight - banana.getFrameHeight()));
    }

    class DrawTimer extends CountDownTimer {
        public DrawTimer() {
            super(Integer.MAX_VALUE, timerInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }
        @Override
        public void onFinish(){}
    }

    class ActivityTimer extends CountDownTimer {
        public ActivityTimer() {
            super(Integer.MAX_VALUE, timerActivityInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (points >= 30) {

            }
        }
            @Override
            public void onFinish(){}
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN) {

            if (event.getY() < Cat.getBoundingBoxRect().top) {
                Cat.setVelocityY(-200);
            }
            else {
                if (event.getY() > Cat.getBoundingBoxRect().bottom) {
                    Cat.setVelocityY(200);
                }
            }
        }
        return true;
    }

    public class DrawThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private volatile boolean running = true;
        private volatile boolean running2 = false;

        public DrawThread(Context context, SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void requestStop(){
            running = false;
        }

        @Override
        public void run(){
            while (running) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try { // risovat suda. Ya ne znayu pochemu kogda nadpis na russkom, sletayet kodirovka :c

                        Rect dstRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()); // создаём прямоугольник по размерам экрана
                        canvas.drawBitmap(planet, null, dstRect, null); // устанавливаем на фон картинку, вписывая в прямоугольник
                        Cat.draw(canvas); // отрисовываем кота
                        enemyMonster.draw(canvas); // отрисовываем врага
                        banana.draw(canvas); // отрисовываем ещё какую-то ерунду
                        Paint point = new Paint();
                        point.setAntiAlias(true);
                        point.setColor(Color.BLUE);
                        point.setTextSize(80);
                        canvas.drawText(points+"/300", viewWidth - 280, 90, point); // пишем количество очков
                        canvas.drawText("points", viewWidth - 280, 180, point);
                        Paint lvl = new Paint();
                        lvl.setTextSize(80);
                        lvl.setColor(Color.WHITE);
                        canvas.drawText("Level 1", 0, 90, lvl); // пишем номер уровня
                    }
                    finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
            while (running2) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        Rect dstRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()); // создаём прямоугольник по размерам экрана
                        canvas.drawBitmap(planet, null, dstRect, null); // устанавливаем на фон картинку, вписывая в прямоугольник
                        Cat.draw(canvas); // отрисовываем кота
                        enemyMonster.draw(canvas); // отрисовываем врага
                        banana.draw(canvas); // отрисовываем ещё какую-то ерунду
                        tapmonster.draw(canvas);
                        Paint point = new Paint();
                        point.setAntiAlias(true);
                        point.setColor(Color.BLUE);
                        point.setTextSize(80);
                        canvas.drawText(points+"/1000", viewWidth/2, 90, point); // пишем количество очков
                        canvas.drawText("points", viewWidth - 280, 180, point);
                        Paint lvl = new Paint();
                        lvl.setTextSize(80);
                        lvl.setColor(Color.CYAN);
                        canvas.drawText("Level 2", viewWidth, 90, lvl); // пишем номер уровня
                    }
                }
            }
        }
    }
}
