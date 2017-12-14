package com.project.com.project3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private int count = 0;
    private Spinner spin, spin2;
    private String arr2[] = {"Polyline", "Polygon", "Circle"};
    private String arr[] = {
            "MAP_TYPE_HYBRID",
            "MAP_TYPE_NONE",
            "MAP_TYPE_NORMAL",
            "MAP_TYPE_SATELLITE",
            "MAP_TYPE_TERRAIN"
    };
    private int[] style = {
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_NONE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_TERRAIN
    };
    private GoogleMap mMap;
    private int paintStyle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        anhXa();
        init();
    }

    private void moveCamera(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void paintStyle(int style, LatLng latLng) {
        switch (style) {
            case 0:
                functionPolyline(latLng);
                break;
            case 1:
                functionPolygon(latLng);
                break;
            case 2:
                functionCircle(latLng);
                break;
            default:
                break;
        }
    }

    private void anhXa() {
        spin = findViewById(R.id.planets_spinner);
        spin2 = findViewById(R.id.spinner2);
    }

    private void setStyle(GoogleMap googleMap, int i) {
        googleMap.setMapType(style[i]);
    }

    private void init() {

        polylineOptions = new PolylineOptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin2.setAdapter(adapter2);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setStyle(mMap, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paintStyle = i;
                count = 0;
                optionCircle = new CircleOptions();
                polygonOptions = new PolygonOptions();
                polylineOptions = new PolylineOptions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private PolylineOptions polylineOptions;

    private void functionPolyline(LatLng latLng) {
        MarkerOptions option = new MarkerOptions();
        option.title("Polyline " + (++count));
        option.position(latLng);
        Marker currentMarker = mMap.addMarker(option);
        currentMarker.showInfoWindow();
        polylineOptions.add(latLng);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        polyline.setColor(Color.RED);
    }

    CircleOptions optionCircle = new CircleOptions();

    private void functionCircle(LatLng latLng) {
        MarkerOptions option = new MarkerOptions();
        option.title("Circle " + (++count));
        option.position(latLng);
        Marker currentMarker = mMap.addMarker(option);
        currentMarker.showInfoWindow();
        optionCircle.center(latLng).radius(50);
        Circle cir = mMap.addCircle(optionCircle);
        cir.setFillColor(Color.RED);
        cir.setStrokeColor(Color.BLUE);
    }

    PolygonOptions polygonOptions = new PolygonOptions();

    private void functionPolygon(LatLng latLng) {
        MarkerOptions option = new MarkerOptions();
        option.title("Polygon " + (++count));
        option.position(latLng);
        Marker currentMarker = mMap.addMarker(option);
        currentMarker.showInfoWindow();

        polygonOptions.add(latLng);
        Polygon polygon = mMap.addPolygon(polygonOptions);
        polygon.setStrokeColor(Color.BLUE);
        polygon.setFillColor(Color.YELLOW);
        polygon.setStrokeWidth(5);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add some markers to the map, and add a data object to each marker.
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        moveCamera(new LatLng(10.8252945, 106.6804997));

    }

    /**
     * Called when the user clicks a marker.
     */

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();
//        polylineOptions.add(marker.getPosition());

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        paintStyle(paintStyle, latLng);
    }
}
