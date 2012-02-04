package nz.co.simon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.Surface;
import java.util.LinkedList;

class Worm {

    private int growTick = 0;
    private LinkedList<SegmentEdge> body = new LinkedList<SegmentEdge>();
    public static final double LENGTH = 30;
    private static final int GROW_TIME = 10;
    private int screenWidth, screenHeight;
    private double currentDir = 0;
    Paint paint;
    private static final float RADIUS = 10;

    Worm(int screenWidth, int screenHeight) {
        growTick = 0;
        body = new LinkedList<SegmentEdge>();
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(RADIUS);
    }

    boolean place(PointF start, double dir) {
        PointF end = new PointF(
                (float) (start.x + LENGTH * Math.sin(dir)),
                (float) (start.y + LENGTH * Math.cos(dir)));
        if (end.x < 0 || end.x > screenWidth
                || end.y < 0 || end.y > screenHeight) {
            return false;
        }
        body.add(new StraightEdge(end.x, end.y));
        body.add(new StraightEdge(start.x, start.y));
        return true;
    }

    boolean intersects(Food currentFood) {
        return false;// TODO
    }

    void draw(Surface surface) {
        Canvas canvas;
        try {
            canvas = surface.lockCanvas(null);
            SegmentEdge current = body.getFirst();
            for (SegmentEdge next : body) {
                current.DrawTo(next, canvas);
                current = next;
            }
            surface.unlockCanvas(canvas);
        } catch (IllegalArgumentException e) {
            System.exit(-1);
        } catch (Surface.OutOfResourcesException e) {
            System.exit(-2);
        }
    }

    void grow() {
        growTick += GROW_TIME;
    }

    void update(double angle) {
        // TODO
    }

    private abstract class SegmentEdge {

        float x, y;

        SegmentEdge(float x, float y) {
            this.x = x;
            this.y = y;
        }

        abstract void DrawTo(SegmentEdge next, Canvas canvas);

        void MoveTo(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private class CurveEdge extends SegmentEdge {

        RectF turnBox;

        CurveEdge(float x, float y) {
            super(x, y);
            turnBox = new RectF();
        }

        void DrawTo(SegmentEdge next, Canvas canvas) {
            canvas.drawArc(turnBox, 0, 0, true, paint); //TODO
        }

        void MoveTo(float x, float y) {
            super.MoveTo(x, y);
            // TODO
        }
    }

    private class StraightEdge extends SegmentEdge {

        StraightEdge(float x, float y) {
            super(x, y);
        }

        void DrawTo(SegmentEdge next, Canvas canvas) {
            canvas.drawLine(x, y, next.x, next.y, paint);
        }
    }
}