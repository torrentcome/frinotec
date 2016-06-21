package torrentcome.frinotec;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import torrentcome.frinotec.helper.CameraHelper;
import torrentcome.frinotec.helper.CameraPreview;
import torrentcome.frinotec.view.Chronometre;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* chrono */
    private Chronometre chrono;
    private ImageButton start, stop, restart;

    /* camera */
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* chrono */
        start = (ImageButton) findViewById(R.id.button1);
        stop = (ImageButton) findViewById(R.id.button2);
        restart = (ImageButton) findViewById(R.id.button3);
        chrono = (Chronometre) findViewById(R.id.chronometer1);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        restart.setOnClickListener(this);

        /* camera */
        if (CameraHelper.checkCameraHardware(this)) {
            // Create an instance of Camera
            mCamera = CameraHelper.getCameraInstance();
            // make any resize, rotate or reformatting changes here
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                mCamera.setDisplayOrientation(90);
            } else {
                mCamera.setDisplayOrientation(0);
            }
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            if (preview != null) {
                preview.addView(mPreview);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chrono != null) chrono.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button1:
                chrono.start();
                break;
            case R.id.button2:
                chrono.stop();
                break;
            case R.id.button3:
                chrono.setBase(SystemClock.elapsedRealtime());
                break;
        }
    }
}
