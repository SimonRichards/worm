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

    void draw(Surface surface) {
        Canvas c;
        try {
            c = surface.lockCanvas(null);
            c.drawCircle(x, y, RADIUS, paint);
            surface.unlockCanvas(c);
        } catch (IllegalArgumentException e) {
            System.exit(-1);
        } catch (Surface.OutOfResourcesException e) {
            System.exit(-2);
        }
    }

    void place(PointF point) {
        x = point.x;
        y = point.y;
    }
}