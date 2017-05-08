package torrentcome.frinotec;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import torrentcome.frinotec.helper.CameraHelper;
import torrentcome.frinotec.helper.CameraPreview;
import torrentcome.frinotec.view.Chronometer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* chrono */
    private Chronometer chrono;

    public static final int START = 0;
    public static final int STOP = 1;
    private int click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* chrono */
        chrono = (Chronometer) findViewById(R.id.chronometer1);

        /* clear chrono */
        ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        if (button1 != null) {
            button1.setOnClickListener(this);
        }

        /* camera */
        if (CameraHelper.checkCameraHardware(this)) {
            // Create an instance of Camera
            Camera mCamera = CameraHelper.getCameraInstance();
            if (mCamera != null) {
                // make any resize, rotate or reformatting changes here
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    mCamera.setDisplayOrientation(90);
                } else {
                    mCamera.setDisplayOrientation(0);
                }
                // Create our Preview view and set it as the content of our activity.
                CameraPreview mPreview = new CameraPreview(this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                FrameLayout click_zone = (FrameLayout) findViewById(R.id.click_zone);
                if (preview != null && click_zone != null) {
                    preview.addView(mPreview);
                    preview.setOnClickListener(this);
                    click_zone.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.click_zone:
                switch (click) {
                    case START:
                        chrono.start();
                        click = STOP;
                        break;
                    case STOP:
                        chrono.stop();
                        click = START;
                        break;
                }
                break;
            case R.id.button1:
                chrono.reset();
                click = START;
                break;
            default:
                break;
        }
    }
}
