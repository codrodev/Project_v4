package dm.sime.com.kharetati.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


public class SlantView extends View {
    private Context mContext;
    Paint paint ;
    Path path;

    public SlantView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth(), h = getHeight();
        paint.setStrokeWidth(2);
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), 0xFFb71e3e, 0xFF940e2a, Shader.TileMode.MIRROR));


        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
//        path.moveTo(0,800);
        path.moveTo(0,h-h/10);
        path.lineTo(0,h);
        path.lineTo(w,h);
        path.close();
        canvas.drawPath(path, paint);
    }
}

