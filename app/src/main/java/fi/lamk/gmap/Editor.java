package fi.lamk.gmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Editor extends AppCompatActivity {
     Intent intent;
     EditText title;
     EditText lat;
     EditText lng;
     Button btnOk;
     Button btnOCancel;
    HashMap<String, MarkerInfo> mInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        intent = new Intent(this, MapsActivity.class);
        title = (EditText) findViewById(R.id.editTitle);
        lat = (EditText) findViewById(R.id.editLat);
        lng = (EditText) findViewById(R.id.editLong);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOCancel = (Button) findViewById(R.id.btnCancel);

        double latDouble = getIntent().getDoubleExtra("lat", 0);
        lat.setText(String.valueOf(latDouble));
        double lngDouble = getIntent().getDoubleExtra("lng", 0);
        lng.setText(String.valueOf(lngDouble));

        //getting a HashMap (a new title will be compare with keys of HashMap)
        mInfo = (HashMap<String, MarkerInfo>) getIntent().getSerializableExtra("markers");



         btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringTitle = title.getText().toString();
                if(!isDouble(lat.getText().toString()) || !isDouble(lng.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Wrong coordinates. Should be numbers! ", Toast.LENGTH_LONG).show();
                }
                else{

                    boolean isTitle = false;
                    for(String key : mInfo.keySet()){
                        if(stringTitle.equals(key)){
                            isTitle = true;
                            break;
                        }
                    }

                    if(isTitle){
                        Toast.makeText(getApplicationContext(), "The same title is already used!", Toast.LENGTH_LONG).show();
                    }

                    else if(stringTitle.equals(""))
                        Toast.makeText(getApplicationContext(), "The title field is empty!", Toast.LENGTH_LONG).show();

                    else {
                        intent.putExtra("title", stringTitle);
                        intent.putExtra("lat", Double.parseDouble(lat.getText().toString()));
                        intent.putExtra("lng", Double.parseDouble(lng.getText().toString()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void onClickCancel(View view) {
        startActivity(intent);
    }
}
