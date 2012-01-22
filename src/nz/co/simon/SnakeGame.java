package nz.co.simon;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.Surface;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Random;
import nz.co.simon.Entity.Food;

/**
 *
 * @author Simon
 */
class WormGame {
    Entity.Worm snake;
    Entity.Food currentFood;
    Random rng;
    private int width, height;
    private Rect screen;

    WormGame(int width, int height) {
        rng = new Random();
        snake = new Entity.Worm(width, height);
        currentFood = new Entity.Food();
        while (snake.place(randomPoint(), rng.nextDouble() * Math.PI * 2));
        this.width = width; this.height = height;
        screen = new Rect(0, 0, width, height);
    }


    void update(double gravity) {
        snake.update(gravity);
        if (snake.intersects(currentFood)) {
            currentFood = new Entity.Food();
            do {
                currentFood.place(randomPoint());
            } while (!snake.intersects(currentFood));
            snake.grow();
        }
    }

    void draw(Surface surface) {
        snake.draw(surface);
        currentFood.draw(surface);
        try {
            Canvas c = surface.lockCanvas(screen);
            surface.unlockCanvasAndPost(c);
        } catch (Exception e) {}
    }

    private PointF randomPoint() {
        return new PointF(width * (float)rng.nextDouble(), height * (float)rng.nextDouble());
    }

}
