package com.example.cot.game2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;

/**
 * Created by CoT on 8/19/17.
 */

public class ShapeLibrary {

    static List<Shape> shapes = new ArrayList<>();

    static int currentShapeIndex = 0;

    public static void initializeLibrary(Context context, SoundPool soundPool){

        int soundId[] = new int[100];
        int i=0;

        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);

       //xe
        soundId[i++] = soundPool.load(context, R.raw.x, 1);
        Letter x = new Letter("x", soundId[i]);

        soundId[i++] = soundPool.load(context, R.raw.e, 1);
        Letter e = new Letter("e", soundId[i]);

        soundId[i++] = soundPool.load(context, R.raw.xe, 1);
        Letter xe = new Letter("xe", soundId[i]);

        Shape shape_xe = new Shape("xe", R.drawable.xe, new Letter[]{x, e, xe});


        //bay
        soundId[i++] = soundPool.load(context, R.raw.b, 1);
        Letter b = new Letter("b", soundId[i]);

        soundId[i++] = soundPool.load(context, R.raw.ay, 1);
        Letter ay = new Letter("ay", soundId[i]);

        soundId[i++] = soundPool.load(context, R.raw.bay, 1);
        Letter bay = new Letter("bay", soundId[i]);

        Shape shape_bay = new Shape("bay", R.drawable.plane, new Letter[]{b, ay, bay});

        shapes.add(shape_xe);
        shapes.add(shape_bay);

        System.out.println(shapes);

    }

    public static Shape nextShape(){

        currentShapeIndex++;

        if(currentShapeIndex>shapes.size()-1) currentShapeIndex=0;

        return shapes.get(currentShapeIndex);
    }

}
