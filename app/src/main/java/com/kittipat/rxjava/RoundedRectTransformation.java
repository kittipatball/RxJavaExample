package com.kittipat.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by gissoft95_hp on 05-Sep-16.
 */
public class RoundedRectTransformation implements Transformation {
    private final int radius;
    private final int margin;
    private final int stroke;

    public RoundedRectTransformation(int radius, int margin, int stroke) {
        this.radius = radius;
        this.margin = margin;
        this.stroke = stroke;
    }


    @Override
    public Bitmap transform(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF rF = new RectF(margin,margin,source.getWidth() - margin, source.getHeight()- margin);
        canvas.drawRoundRect(rF,radius,radius,paint);

        //border
        Paint paintST = new Paint();
        paintST.setStyle(Paint.Style.STROKE);
        paintST.setStrokeWidth(stroke);
        paintST.setColor(0xff3d8ec6);
        canvas.drawRoundRect(rF,radius,radius,paintST);
        if(source != output){
            source.recycle();
        }

        return output;
    }

    @Override
    public String key() {
        return null;
    }
}
