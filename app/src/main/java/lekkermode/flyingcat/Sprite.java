package lekkermode.flyingcat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 15.01.2017.
 */

public class Sprite {
    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;

    private double x;
    private double y;

    private double VelocityX;
    private double VelocityY;

    public Sprite(double x, double y, double VelocityX, double VelocityY, Rect initialFrame, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.VelocityX = VelocityX;
        this.VelocityY = VelocityY;
        this.bitmap = bitmap;
        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);
        this.bitmap = bitmap;
        this.timeForCurrentFrame = 0.5;
        this.frameTime = 25;
        this.currentFrame = 0;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
        this.padding = 20;


    }
    private int padding;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return VelocityX;
    }

    public void setVelocityX(double velocityX) {
        VelocityX = velocityX;
    }

    public double getVelocityY() {
        return VelocityY;
    }

    public void setVelocityY(double velocityY) {
        VelocityY = velocityY;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public double getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }

    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }

    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = timeForCurrentFrame;
    }
    public int getFramesCount() {
        return frames.size();
    }


    public void addFrame(Rect frame) {
        frames.add(frame);
    }

    public void update(int ms) { // обновляет текущий фрейм
        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame +1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }

        x = x + VelocityX * ms/1000.0;
        y = y + VelocityY * ms/1000.0;
    }

    public void draw (Canvas canvas) { // рисует фрейм
        Paint p = new Paint();
        Rect destination = new Rect( (int)x, (int)y, (int)(x+frameWidth), (int)(y+frameHeight));

        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, p);
    }

    public Rect getBoundingBoxRect() { // возвращает область, участвующую в определении столкновений
        return new Rect(
                (int)x+padding,
                (int)y+padding,
                (int)(x + frameWidth - 2*padding),
                (int)(y + frameHeight - 2*padding));
    }

    public boolean intersect (Sprite s) { // определяет пересечение спрайтов
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect());
    }

}
