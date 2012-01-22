package nz.co.simon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.view.Surface;
import java.util.EnumSet;
import java.util.LinkedList;

/**
 *
 * @author Simon
 */
abstract class Entity {

    private enum EdgeType {
        LINE,
        CURVE,
        HEAD
    }

    private static final float RADIUS = 10;

    static class Worm extends Entity {
        private int growTick = 0;
        private LinkedList<SegmentEdge> edges = new LinkedList<SegmentEdge>();
        public static final double LENGTH = 30;
        private static final int GROW_TIME = 10;
        private int screenWidth, screenHeight;
        private double currentDir = 0;
        Paint paint;

        Worm(int screenWidth, int screenHeight) {
            growTick = 0;
            edges = new LinkedList<SegmentEdge>();
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;
            paint = new Paint();
        }

        boolean place(PointF start, double dir) {
            PointF end = new PointF(
                    (float)(start.x + LENGTH * Math.sin(dir)),
                    (float)(start.y + LENGTH * Math.cos(dir)));
            if (
                    end.x < 0 || end.x > screenWidth ||
                    end.y < 0 || end.y > screenHeight) {
                return false;
            }
            edges.add(new SegmentEdge(end.x, end.y, EdgeType.LINE));
            edges.add(new SegmentEdge(start.x, start.y, EdgeType.HEAD));
            return true;
        }

        boolean intersects(Food currentFood) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        void draw(Surface surface) {

        }

        void grow() {
            growTick += GROW_TIME;
        }

        void update(double angle) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private class SegmentEdge {
            double x, y;
            EdgeType type;

            SegmentEdge(double x, double y, EdgeType type) {
                this.x = x;
                this.y = y;
                this.type = type;
            }
        }

    }

    static class Food extends Entity {
        float x, y;
        Paint paint;

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
}
