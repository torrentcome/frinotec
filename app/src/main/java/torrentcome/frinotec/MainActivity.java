package torrentcome.frinotec;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import torrentcome.frinotec.dragdrop.MyDragEventListener;
import torrentcome.frinotec.dragdrop.MyDragShadowBuilder;
import torrentcome.frinotec.helper.CameraHelper;
import torrentcome.frinotec.view.BaseBarView;
import torrentcome.frinotec.view.CameraPreview;
import torrentcome.frinotec.view.Chronometer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int START = 0;
    public static final int STOP = 1;
    private static final String LINEAR_TAG = "drag tag";
    private int click;

    /* chrono */
    private Chronometer chrono;

    /* ui */
    private BaseBarView barView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* chrono */
        chrono = (Chronometer) findViewById(R.id.chronometer1);

        /* clear chrono */
        ImageButton clearChrono = (ImageButton) findViewById(R.id.clearchrono);
        if (clearChrono != null) {
            clearChrono.setOnClickListener(this);
        }

        ImageButton showTempo = (ImageButton) findViewById(R.id.show_tempo);
        if (showTempo != null) {
            showTempo.setOnClickListener(this);
        }

        barView = (BaseBarView) findViewById(R.id.bar_view);

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
                final LinearLayout drag = (LinearLayout) findViewById(R.id.drag);

                if (preview != null && click_zone != null && drag != null) {
                    preview.addView(mPreview);
                    preview.setOnClickListener(this);
                    click_zone.setOnClickListener(this);

                    drag.setTag(LINEAR_TAG);
                    drag.setOnDragListener(new MyDragEventListener(this));
                    drag.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ClipData.Item item = new ClipData.Item(v.getTag().toString());
                            // Create a new ClipData using the tag as a label, the plain text MIME type, and
                            // the already-created item. This will create a new ClipDescription object within the
                            // ClipData, and set its MIME type entry to "text/plain"
                            ClipData dragData = new ClipData(v.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                            // Instantiates the drag shadow builder.
                            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(drag);

                            // Starts the drag
                            v.startDrag(dragData,  // the data to be dragged
                                    myShadow,  // the drag shadow builder
                                    null,      // no need to use local data
                                    0          // flags (not currently used, set to 0)
                            );
                            return true;
                        }
                    });
                }
            }
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ConfigDialogFragment configDialogFragment = new ConfigDialogFragment();
        configDialogFragment.show(fm, "fragment_config");
    }

    public void showBarView(boolean show) {
        barView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void setBarView(int number) {
        barView.setAnimStats(number * 1000);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.click_zone:
                switch (click) {
                    case START:
                        chrono.start();
                        barView.animateStats();
                        click = STOP;
                        break;
                    case STOP:
                        chrono.stop();
                        barView.animateStop();
                        click = START;
                        break;
                }
                break;
            case R.id.clearchrono:
                chrono.reset();
                barView.animateStop();
                click = START;
                break;
            case R.id.show_tempo:
                showEditDialog();
                barView.animateStop();
                break;
            default:
                break;
        }
    }


}
