package dm.sime.com.kharetati.view.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import dm.sime.com.kharetati.utility.Global;


public class SlantView extends View {
    private final LinearGradient gradient;
    private Context mContext;
    Paint paint ;
    Path path;
    private boolean isExecuted;

    public SlantView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //
        gradient = new LinearGradient(0, 0, 0, getHeight(), 0xFFa31533, 0xFFa31533, Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth(), h = getHeight();
        paint.setStrokeWidth(2);
        paint.setShader(gradient);
        path = new Path();



        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(false);

        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
//        path.moveTo(0,800);




            if(Global.getScreenWidth((Activity) mContext)>=411 && Global.getScreenHeight((Activity) mContext)>=800 ){
                //path.moveTo(0,0);
                Path path = new Path();
                path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
                path.moveTo(0,0);
                path.lineTo(0,h-h/10);
                path.moveTo(0,h-h/10);
                path.lineTo(0,h);
                path.lineTo(w,h);
                path.close();
                canvas.drawPath(path, paint);


            }
            else{
                path.moveTo(0,0);
                path.lineTo(0,h-h/10);
            //path.lineTo(w,h);

                path.moveTo(0,h-h/10);
                path.lineTo(0,h);
                path.lineTo(w,h);
                path.close();
                canvas.drawPath(path, paint);
            }
    }
}

