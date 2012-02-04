package nz.co.simon;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

class WormActivity extends Activity {

    WormView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Gravity gravity = new Gravity((SensorManager) getSystemService(SENSOR_SERVICE));
        view = new WormView(this, null, gravity);
        new Thread(view).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
