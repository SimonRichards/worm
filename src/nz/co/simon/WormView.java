package nz.co.simon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.atomic.AtomicBoolean;

class WormView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private WormGame game;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private AtomicBoolean running;
    private Gravity gravity;

    WormView(Context context, AttributeSet attrs, Gravity gravity) {
        super(context, attrs);
        game = new WormGame(getWidth(), getHeight());
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.gravity = gravity;
    }

    public void run() {
        while (running.get()) {
            double angle = gravity.get();
            game.update(angle);
            game.draw(surfaceHolder.getSurface());
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO
    }
}
