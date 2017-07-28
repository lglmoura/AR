package pooa20171.iff.br.artouristguide2.activity;

import com.wikitude.common.camera.CameraSettings;

import pooa20171.iff.br.artouristguide2.util.CameraActivity;

/**
 * Created by lglmo on 28/07/2017.
 */

public class AutoHdCamera extends CameraActivity {

    @Override
    public CameraSettings.CameraResolution getCameraResolution() {
        return CameraSettings.CameraResolution.AUTO;
    }
}
