package pooa20171.iff.br.artouristguide2.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;

import java.util.Arrays;

import pooa20171.iff.br.artouristguide2.R;

public class MainActivity extends Activity {

    private Button btAr;
    public static final String EXTRAS_KEY_ACTIVITY_GEO = "activityGeo";
    private PermissionManager mPermissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAr = (Button) findViewById(R.id.bt_ar);

        mPermissionManager = ArchitectView.getPermissionManager();

        btAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
                mPermissionManager.checkPermissions((Activity) getContext(), permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
                    @Override
                    public void permissionsGranted(int requestCode) {
                        loadExample();
                    }

                    @Override
                    public void permissionsDenied(String[] deniedPermissions) {

                        Toast.makeText(MainActivity.this, "ARTourristGuide necessita de Permissao: " + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void showPermissionRationale(final int requestCode, final String[] permissions) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("ARTourristGuide");
                        alertBuilder.setMessage("ARTourristGuide necessita de Permissao: " + Arrays.toString(permissions));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPermissionManager.positiveRationaleResult(requestCode, permissions);
                            }
                        });

                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                });



            }
        });
    }
    private Context getContext(){
        return this;
    }

    private void loadExample() {
        try {

            Intent ar = new Intent(getContext(),ARActivity.class);
            startActivity(ar);

        } catch (Exception e) {
			/*
			 * may never occur, as long as all SampleActivities exist and are
			 * listed in manifest
			 */

            Toast.makeText(this,  "\nnot defined/accessible",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
