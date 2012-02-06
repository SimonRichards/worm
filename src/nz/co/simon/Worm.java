package nz.co.simon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.Surface;
import java.util.Iterator;
import java.util.LinkedList;

class Worm {

    private int growTick = 0;
    private WormBody body;
    public static final double LENGTH = 30;
    private static final int GROW_TIME = 10;
    private int screenWidth, screenHeight;
    float currentAngle = 0;
    Paint paint = new Paint();
    private static final float RADIUS = 10;
    private static final float FOUR_R_SQUARED = 4*RADIUS*RADIUS;
    private static final float TURN_THRESHOLD = 10;

    Worm(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
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
        body = new WormBody(start.x, start.y, end.x, end.y);
        return true;
    }

    boolean intersects(Food food) {
        float dx = body.getHead().x - food.x;
        float dy = body.getHead().y - food.y;
        return dx*dx + dy*dy < FOUR_R_SQUARED;
    }

    void draw(Canvas canvas) throws IllegalArgumentException, Surface.OutOfResourcesException {
        WormBody.SegmentEdge current = body.getTail();
        for (WormBody.SegmentEdge next : body) {
            current.drawTo(next, canvas);
            current = next;
        }
    }

    void grow() {
        growTick += GROW_TIME;
    }

    void update(double angle) {
        double turnAngle = angle - currentAngle;
        if (turnAngle > 180) {
        }
        if (turnAngle < -180) {
            turnAngle += 360;
        }
        if (Math.abs(turnAngle) < TURN_THRESHOLD) {
            body.moveStraight();
        } else {
            if (turnAngle < 0) {
                body.turnWiddershins();
            } else {
                body.turnCounterWiddershins();
            }
        }

        if (growTick > 0) {
            growTick -= 1;
        } else {
            body.moveTail();
        }
    }

    static enum TurnState {STRAIGHT, WIDDERSHINS, COUNTER_WIDDERSHINS}

    private class WormBody implements Iterable<WormBody.SegmentEdge> {

        private final LinkedList<SegmentEdge> body = new LinkedList<SegmentEdge>();
        private final PointF head;
        static final float STEP_SIZE = 5;

        TurnState state = TurnState.STRAIGHT;


        WormBody(float x1, float y1, float x2, float y2) {
            body.add(new StraightEdge(x1, y1));
            head = new PointF(x2, y2);
        }

        PointF getHead() {
            return head;
        }

        SegmentEdge getTail() {
            return body.getLast();
        }

        public Iterator<SegmentEdge> iterator() {
            return body.iterator();
        }

        private void moveTail() {
            float toRemove = STEP_SIZE;
            Iterator<SegmentEdge> it = body.descendingIterator();
            SegmentEdge tail = it.next();
            SegmentEdge nextToLast = it.next();
            float lastSegmentLength = tail.distanceTo(nextToLast);
            while (lastSegmentLength > STEP_SIZE) {
                tail = nextToLast;
                nextToLast = it.next(); // TODO test this works
                body.removeLast();
                toRemove -= lastSegmentLength;
                lastSegmentLength = tail.distanceTo(nextToLast);
            }
            tail.MoveBy(toRemove);
        }

        private void moveStraight() {
            if (state != TurnState.STRAIGHT) {
                body.addFirst(new StraightEdge(head.x, head.y));
            }
            head.x += Math.cos(currentAngle) * STEP_SIZE;
            head.y += Math.sin(currentAngle) * STEP_SIZE;
        }

        private void turnWiddershins() {
            if (state == TurnState.WIDDERSHINS) {

            } else {
                body.addFirst(new CurveEdge(head.x, head.y, true));
            }
            state = TurnState.WIDDERSHINS;
        }

        private void turnCounterWiddershins() {
            if (state == TurnState.COUNTER_WIDDERSHINS) {

            } else {
                body.addFirst(new CurveEdge(head.x, head.y, false));
            }
            state = TurnState.WIDDERSHINS;
        }

        abstract class SegmentEdge extends PointF{

            SegmentEdge(float x, float y) {
                super(x, y);
            }

            float distanceTo(float otherX, float otherY) {
                float xDiff = x - otherX;
                float yDiff = y - otherY;
                return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            }

            abstract void drawTo(PointF next, Canvas canvas);

            abstract float distanceTo(PointF next);

            abstract void MoveBy(float distance);
        }

        private class CurveEdge extends SegmentEdge {

            RectF turnBox;
            private final float TURN_RADIUS = 50;
            private float startAngle;
            float centreX, centreY;
            boolean widdershins;

            CurveEdge(float x, float y, boolean widdershins) {
                super(x, y);
                this.widdershins = widdershins;
                centreX = widdershins ? x + (float)Math.cos(currentAngle) :
                                        x - (float)Math.cos(currentAngle);
                centreY = widdershins ? y + (float)Math.sin(currentAngle) :
                                        y - (float)Math.sin(currentAngle);
                turnBox = new RectF(
                        centreX - TURN_RADIUS, centreY - TURN_RADIUS,
                        centreX + TURN_RADIUS, centreY + TURN_RADIUS);
                startAngle = currentAngle + (float)Math.PI; // I think? (TODO)
            }

            void drawTo(PointF next, Canvas canvas) {
                float offsetX = next.x - centreX;
                float offsetY = centreY - next.y;
                canvas.drawArc(turnBox, startAngle, s, true, paint); //TODO
            }

            void MoveBy(float distance) {
            }

            float distanceTo(PointF next) {
                float xDiff = x - next.x;
                float yDiff = y - next.y;
                return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff); //TODO
            }
        }

        private class StraightEdge extends SegmentEdge {

            StraightEdge(float x, float y) {
                super(x, y);
            }

            void drawTo(PointF next, Canvas canvas) {
                canvas.drawLine(x, y, next.x, next.y, paint);
            }

            void MoveBy(float distance) {
                x += Math.sin(currentAngle) * STEP_SIZE;
                y += Math.cos(currentAngle) * STEP_SIZE;
            }

            float distanceTo(PointF next) {
                float xDiff = x - next.x;
                float yDiff = y - next.y;
                return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            }
        }
    }
}