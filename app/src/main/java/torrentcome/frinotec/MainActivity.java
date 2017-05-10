package torrentcome.frinotec;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import torrentcome.frinotec.helper.CameraHelper;
import torrentcome.frinotec.view.BaseBarView;
import torrentcome.frinotec.view.CameraPreview;
import torrentcome.frinotec.view.Chronometer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LINEAR_TAG = "drag tag";

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
        ImageButton clearChrono = (ImageButton) findViewById(R.id.clearchrono);
        if (clearChrono != null) {
            clearChrono.setOnClickListener(this);
        }

        BaseBarView barView = (BaseBarView) findViewById(R.id.bar_view);
        if (barView != null) {
            barView.animateStats(50000);
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
                final LinearLayout drag = (LinearLayout) findViewById(R.id.drag);

                if (preview != null && click_zone != null && drag != null) {
                    preview.addView(mPreview);
                    preview.setOnClickListener(this);
                    click_zone.setOnClickListener(this);

                    drag.setTag(LINEAR_TAG);
                    drag.setOnDragListener(new myDragEventListener());
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
            case R.id.clearchrono:
                chrono.reset();
                click = START;
                break;
            default:
                break;
        }
    }

    protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    int x_cord;
                    int y_cord;

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();

                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(v.getLayoutParams());
                    int left = x_cord - (v.getWidth() / 2);
                    int top = y_cord - (v.getHeight() / 2);
                    marginParams.setMargins(left, top, 0, 0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                    v.setLayoutParams(layoutParams);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    CharSequence dragData = item.getText();

                    // Displays a message containing the dragged data.
                    Toast.makeText(MainActivity.this, "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Toast.makeText(MainActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }


    protected static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth() / 2;

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight() / 2;

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

}
