package com.example.hades.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Hades on 2017/4/4.
 */
public class CircleTransfomation implements Transformation {

    private static final int STROKE_WIDTH=6;

    @Override
    public Bitmap transform(Bitmap source) {
        int size=Math.min(source.getWidth(),source.getHeight());
        int x=(source.getWidth()-size)/2;
        int y=(source.getHeight()-size)/2;
        Bitmap squaredBitmap=Bitmap.createBitmap(source,x,y,size,size);
        if(squaredBitmap!=source){
            source.recycle();
        }

        Bitmap bitmap=Bitmap.createBitmap(source,x,y,size,size);
        Canvas canvas=new Canvas(bitmap);
        Paint avatarPaint=new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        avatarPaint.setShader(shader);

        Paint outlinePaint=new Paint();
        outlinePaint.setColor(Color.WHITE);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(STROKE_WIDTH);
        outlinePaint.setAntiAlias(true);

        float r=size/2f;
        canvas.drawCircle(r,r,r,avatarPaint);
        canvas.drawCircle(r,r,r-STROKE_WIDTH/2,outlinePaint);
        squaredBitmap.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "circleTransformation()";
    }
}
