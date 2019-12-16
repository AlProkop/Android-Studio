//Prakapenka Aliaksei 07TVT17
//student number 1701212


package fi.lamk.gmap;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Random;

import static android.content.Context.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    DBHelper myDb;
    private GoogleMap mMap;
    Intent intent;
    HashMap<String, MarkerInfo> markerStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myDb = new DBHelper(this);
        intent = new Intent(this, Editor.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        markerStorage = new HashMap<>();
        String title;
        Double lat, lng;
        mMap = googleMap;
        LatLng lastCoord = null;
        Cursor res = myDb.getData();
        if(res.getCount()>0) {
            while (res.moveToNext()) {
                Random random = new Random();
                float hue = random.nextFloat() * 359;
                title = res.getString(0);
                lat = res.getDouble(1);
                lng = res.getDouble(2);
                MarkerInfo markerInfo = new MarkerInfo(title, lat, lng);

                markerStorage.put(title, markerInfo);
                LatLng coord = new LatLng(lat, lng);
                lastCoord = coord;
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(coord)
                        .title(title)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(hue)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCoord));
        }
        else mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(61.005729, 25.664363)));


        // deleting Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Delete marker")
                        .setMessage("Do you really want to delete the marker '"+ marker.getTitle() +"' ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int delRow = myDb.deleteMarker(marker.getTitle());
                                if (delRow == 1) {
                                    marker.remove();
                                    markerStorage.remove(marker.getTitle());

                                    Toast.makeText(MapsActivity.this, "The marker was deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MapsActivity.this,"Aborted",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog  = builder.create();
                dialog.show();
                return false;
            }
        });


        // sending coordinates to the Editor activity
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double lat, lng;
                lat = latLng.latitude;
                lng = latLng.longitude;

                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("count", markerStorage.size());
                intent.putExtra("markers", markerStorage);
                startActivityForResult(intent, 1);

            }
        });
    }
    //getting data from Editor activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String title;
        Double lat, lng;
        Random random = new Random();
        float hue = random.nextFloat()*359; //random color
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            title = data.getStringExtra ("title");
            lat = data.getDoubleExtra("lat",0);
            lng = data.getDoubleExtra("lng",0);

            long result = myDb.insertData(title, lat, lng);
            if (result == -1)
                Toast.makeText(getApplicationContext(), "can't save this marker!", Toast.LENGTH_LONG).show();

            else {
                LatLng coord = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(coord)
                            .title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(hue)))
                            .showInfoWindow();
                    Toast.makeText(getApplicationContext(), "the marker is saved!", Toast.LENGTH_LONG).show();
            }
        }

    }
}