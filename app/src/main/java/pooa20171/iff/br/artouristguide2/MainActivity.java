package pooa20171.iff.br.artouristguide2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btAr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAr = (Button) findViewById(R.id.bt_ar);

        btAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ar = new Intent(getContext(),ARActivity.class);
                startActivity(ar);

            }
        });
    }
    private Context getContext(){
        return this;
    }
}
