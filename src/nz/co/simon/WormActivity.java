package nz.co.simon;

import android.app.Activity;
import android.os.Bundle;

class WormActivity extends Activity {
    WormView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        view = new WormView(this, null);
        if (savedInstanceState == null) {
            new Thread(view).start();
        } else {
            new Thread(view).start();
        }
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
