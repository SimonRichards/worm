package nz.co.simon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Simon
 */
class WormView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private WormGame game;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private AtomicBoolean running;
    private Gravity gravity;


    WormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        game = new WormGame(getWidth(), getHeight());
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gravity = new Gravity();
    }

    public void run() {
        while(running.get()) {
            double angle = gravity.get();
            game.update(angle);
            game.draw(surfaceHolder.getSurface());
        }
    }



    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
