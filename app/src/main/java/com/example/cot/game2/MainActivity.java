package com.example.cot.game2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import static android.R.attr.id;


public class MainActivity extends AppCompatActivity {

    GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Loại bỏ tiêu đề.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);
        setContentView(gameView);


        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.planesound);
        mp.setLooping(true);
        mp.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

}

class GameView extends SurfaceView implements Runnable{

    Context context;
    // Initialize ourHolder and paint objects
    SurfaceHolder surfaceHolder;

    Bitmap plane, explosion, cloud, star;

    Canvas canvas;

    Paint paint = new Paint();

    Thread gameThread;

    boolean isMoving = false;

    int currentX = 100;
    int currentY = 100;

    SoundPool sp;

    int soundIds[] = new int[100];

    float speed = 0.2f;
    float letterSpeed = 0.3f;

    long lastDraw =-1;

    int x = 100;
    int y = 100;

    int xLetter = 0;
    int yLetter = 0;

    int objectToDraw = 0;

    int textSize = 300;

    Shape currentShape;

    Letter currentLetter;

    boolean loadNewLetter = false;

    public GameView(Context context) {
        super(context);

        this.context = context;

        surfaceHolder = getHolder();
        // Load Bob from his .png file
        //plane = BitmapFactory.decodeResource(this.getResources(), R.drawable.plane);
        explosion = BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion);
        cloud = BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud);
        //star = BitmapFactory.decodeResource(this.getResources(), R.drawable.star);

        sp = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        soundIds[0] = sp.load(context, R.raw.applause4, 1);

        ShapeLibrary.initializeLibrary(context, sp);

        currentShape = ShapeLibrary.nextShape();

        currentLetter = currentShape.nextLetter();
//
        for(Shape shape: ShapeLibrary.shapes){
            for(Letter letter:shape.letters){
                switch (letter.letter){
                    case "x":
                        letter.soundId = sp.load(context, R.raw.x, 1);
                        break;
                    case "e":
                        letter.soundId = sp.load(context, R.raw.e, 1);
                        break;
                    case "xe":
                        letter.soundId = sp.load(context, R.raw.xe, 1);
                        break;

                    case "b":
                        letter.soundId = sp.load(context, R.raw.b, 1);
                        break;
                    case "ay":
                        letter.soundId = sp.load(context, R.raw.ay, 1);
                        break;
                    case "bay":
                        letter.soundId = sp.load(context, R.raw.bay, 1);
                        break;
                }

            }
        }

//        for(Shape shape: ShapeLibrary.shapes){
//            for(Letter letter:shape.letters){
//                sp.play(letter.soundId, 1, 1, 1, 1, 1);
//            }
//        }

    }

    public Bitmap getBitmapById(int Id){
        return BitmapFactory.decodeResource(this.getResources(), id);
    }


    @Override
    public void run() {

        while(true){

            // Make sure our drawing surface is valid or we crash
            if (surfaceHolder.getSurface().isValid()) {

                canvas = surfaceHolder.lockCanvas();
                // Draw the background color
                canvas.drawColor(Color.WHITE);


                drawCloud(canvas);

                drawObject(canvas);

                drawLetter(canvas);


                lastDraw = System.currentTimeMillis();

                surfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    public void drawCloud(Canvas canvas){

        //now draw cloud
        x -= 5;
        if(x<-200){
            x = canvas.getWidth() + new Random().nextInt(200);
            y = new Random().nextInt(canvas.getHeight());
        }
        canvas.drawBitmap(cloud, x, y, paint);
    }

    public void drawObject(Canvas canvas){

        int length = 0;

        long now = System.currentTimeMillis();

        if(lastDraw>-1)
            length = (int)((now - lastDraw)*speed);

        currentY += length;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), currentShape.imageId);

        //show explosion and reposition the object
        if(currentY>canvas.getHeight()-100){
            canvas.drawBitmap(explosion, currentX, currentY, paint);

            if(currentY>canvas.getHeight()){
                currentY = 0;
            }

            //sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);

        }
        //on spot
        else if(currentY>=-200){
            canvas.drawBitmap(bitmap, currentX, currentY, paint);

        }
        //lift up the plane
        else{

            currentShape = ShapeLibrary.nextShape();
            xLetter = canvas.getWidth();
            currentLetter = currentShape.nextLetter();
            currentY = canvas.getHeight()/2;

            sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }

    public void drawLetter(Canvas cavas){

        Paint paint = new Paint(); //paint is the brush
//        Rect r = new Rect(0, canvas.getHeight(), 100, 50); // 200 - 100: The size of the rectangle to be drawn. It must be equal to the bitmap size
//        // fill
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.RED);
//        cavas.drawRect(r, paint);
//
//        // border
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLACK);
//        cavas.drawRect(r, paint);

        //text
        paint.setTextSize(textSize); // The size of the text will be written
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(true);

        yLetter = canvas.getHeight() - 100;

        int length = 0;

        long now = System.currentTimeMillis();

        if(lastDraw>-1)
            length = (int)((now - lastDraw)*letterSpeed);

        xLetter += length;

        if(xLetter>cavas.getWidth()){
            xLetter = 0;
            currentLetter = currentShape.nextLetter();
        }

        cavas.drawText(currentLetter.letter, xLetter, yLetter, paint); //30-40: The (top-left) relative position where the text starts from the rectangle

        textSize = 200;

    }

    // If SimpleGameEngine Activity is started then
    // start our thread.
    public void resume() {
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            sp.play(currentLetter.soundId, 1, 1, 1, 0, 1.0f);

            int x = (int)event.getX();
            int y = (int)event.getY();

            if(y>canvas.getHeight()-200){
                //sp.play(currentLetter.soundId, 1, 1, 1, 0, 1.0f);
                currentY-=50;
                textSize = 300;

                sp.play(currentLetter.soundId, 1, 1, 1, 0, 1.0f);
            }


        }

        return super.onTouchEvent(event);
    }


}