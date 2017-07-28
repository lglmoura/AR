package pooa20171.iff.br.artouristguide2.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.wikitude.architect.ArchitectJavaScriptInterfaceListener;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.camera.CameraSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import pooa20171.iff.br.artouristguide2.R;

public class CameraActivity extends AbstractArchitectCamActivity {

    private static final String TAG = "CameraActivity";
    public static final String ACTIVITY_TITLE_STRING = "Ar tour guide";
    public static final String ACTIVITY_ARCHITECT_WORLD_URL = "index.html";
    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    protected Bitmap screenCapture = null;

    private static final int WIKITUDE_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 3;

    @Override
    public String getARchitectWorldPath() {
        return ACTIVITY_ARCHITECT_WORLD_URL;
    }

    @Override
    public String getActivityTitle() {
        return ACTIVITY_TITLE_STRING;
    }

    @Override
    public int getContentViewId() {
        return R.layout.sample_cam;
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return KeyWikitude.WIKITUDE_SDK_KEY;
    }

    @Override
    public ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new ArchitectView.SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && CameraActivity.this != null && !CameraActivity.this.isFinishing() && System.currentTimeMillis() - CameraActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText( CameraActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
                    CameraActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectJavaScriptInterfaceListener getArchitectJavaScriptInterfaceListener() {
        return new ArchitectJavaScriptInterfaceListener() {
            @Override
            public void onJSONObjectReceived(JSONObject jsonObject) {
                try {
                    switch (jsonObject.getString("action")) {
                        case "present_poi_details":
                            final Intent poiDetailIntent = new Intent(CameraActivity.this, PoiDetailActivity.class);
                            poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_ID, jsonObject.getString("id"));
                            poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_TITILE, jsonObject.getString("title"));
                            poiDetailIntent.putExtra(PoiDetailActivity.EXTRAS_KEY_POI_DESCR, jsonObject.getString("description"));
                            CameraActivity.this.startActivity(poiDetailIntent);
                            break;

                        case "capture_screen":
                            CameraActivity.this.architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new ArchitectView.CaptureScreenCallback() {
                                @Override
                                public void onScreenCaptured(final Bitmap screenCapture) {
                                    if ( ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                                        CameraActivity.this.screenCapture = screenCapture;
                                        ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WIKITUDE_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                                    } else {
                                        CameraActivity.this.saveScreenCaptureToExternalStorage(screenCapture);
                                    }
                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onJSONObjectReceived: ", e);
                }
            }
        };
    }

    @Override
    public ArchitectView.ArchitectWorldLoadedListener getWorldLoadedListener() {
        return new ArchitectView.ArchitectWorldLoadedListener() {
            @Override
            public void worldWasLoaded(String url) {
                Log.i(TAG, "worldWasLoaded: url: " + url);
            }

            @Override
            public void worldLoadFailed(int errorCode, String description, String failingUrl) {
                Log.e(TAG, "worldLoadFailed: url: " + failingUrl + " " + description);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WIKITUDE_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    this.saveScreenCaptureToExternalStorage(CameraActivity.this.screenCapture);
                } else {
                    Toast.makeText(this, "Please allow access to external storage, otherwise the screen capture can not be saved.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        return new LocationProvider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    protected boolean hasGeo() {
        return true;
    }



   @Override
    protected boolean hasIR() {
        return false;
    }

    @Override
    protected boolean hasInstant() {
        return false;
    }

    @Override
    protected CameraSettings.CameraPosition getCameraPosition() {
        return CameraSettings.CameraPosition.DEFAULT;
    }

    protected void saveScreenCaptureToExternalStorage(Bitmap screenCapture) {
        if ( screenCapture != null ) {
            // store screenCapture into external cache directory
            final File screenCaptureFile = new File(Environment.getExternalStorageDirectory().toString(), "screenCapture_" + System.currentTimeMillis() + ".jpg");

            // 1. Save bitmap to file & compress to jpeg. You may use PNG too
            try {

                final FileOutputStream out = new FileOutputStream(screenCaptureFile);
                screenCapture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                // 2. create send intent
                final Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(screenCaptureFile));

                // 3. launch intent-chooser
                final String chooserTitle = "Share Snaphot";
                CameraActivity.this.startActivity(Intent.createChooser(share, chooserTitle));

            } catch (final Exception e) {
                // should not occur when all permissions are set
                CameraActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // show toast message in case something went wrong
                        Toast.makeText(CameraActivity.this, "Unexpected error, " + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}