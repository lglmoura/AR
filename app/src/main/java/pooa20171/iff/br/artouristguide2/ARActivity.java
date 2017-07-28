package pooa20171.iff.br.artouristguide2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

public class ARActivity extends AppCompatActivity {
    ArchitectView architectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey("fkH0oFR0thUhXuA+tnmFdZTOh/OfLbf1op3Z3XdGGbHCxEuBbssUNYuTbHA3trTLtKqrX70Rsy3jkCt7zCK0M3MGhr8/Cr8WKZlGaGn1vgOKKUaN+hXB0dtqO8F7oyaoDXZDWhpLtvtstuS2rpMemsM0la2L/+PFWJq2+Z6JjktTYWx0ZWRfXzJch8IfF7+u0O2+OW5Uc0hVnk5yp3DrTToLuE9idxzukTHZwzn8Qj0IMPmiPHVSd5h1jiHzMi0fD0Dwgjjhra1jN9DW3SCGwtYtAAVYEUyvBLw3Ic8niglsgwe84wLRv9pExsxr+9sUbCfMAe07hJRV7F8n6A7yFeAHPOnbhl1mOnHy/AkV/3BcYYAdqAOKPxNP3bbf8f16OuP75g7FNoFlIfE2OAhOHHIs+ZG9kaBBj+SY2dt/uq5xJLp32xAwjMi9OKllxD7Rlv7bWkkgX9rOmW0Qc6lmEoZ82ozosOHh/uid40DNg5MD/HcPEGNnmW1IOgC6ARgfKhhkF2k2uKam3+kEVhV61IvOupX02IU49dORJvtMbbXnHaot+U9sHoJT2VshwnMQAqNkvp2kZQQSfQFnmNFY3KfmrTd9dBXFZw7Krl6oznI0ME6VAJokeu5k+4uUBOt+8Ccgfv2dg/5da9wlHpmH89HPTcd+3QWlwmYRe1kp158=");
        this.architectView.onCreate( config );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        this.architectView.onPostCreate();

    }
}
