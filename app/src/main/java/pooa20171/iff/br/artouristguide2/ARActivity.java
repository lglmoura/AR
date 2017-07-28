package pooa20171.iff.br.artouristguide2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import pooa20171.iff.br.artouristguide2.util.KeyWikitude;

public class ARActivity extends AppCompatActivity {
    ArchitectView architectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey(KeyWikitude.WIKITUDE_SDK_KEY);
        this.architectView.onCreate( config );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.architectView.onPostCreate();

    }
}
