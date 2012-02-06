package nz.co.simon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.Surface;

class Food {

    float x, y;
    Paint paint;
    private static final float RADIUS = 10;

    Food() {
        paint = new Paint();
    }

    void draw(Canvas canvas) throws IllegalArgumentException, Surface.OutOfResourcesException {
        canvas.drawCircle(x, y, RADIUS, paint);
    }

    void place(PointF point) {
        x = point.x;
        y = point.y;
    }
}