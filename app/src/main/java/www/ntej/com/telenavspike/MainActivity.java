package www.ntej.com.telenavspike;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.telenav.mapkit.Annotation;
import com.telenav.mapkit.MapService;
import com.telenav.mapkit.MapView;
import com.telenav.mapkit.MapViewEventListener;
import com.telenav.mapkit.Popup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.map_view)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MapService.provideApiKeyAndSecret("4eed81e0-4fbe-46e6-8c62-978a6df436e2", "0e2ca092-4108-4963-b879-bf17e940963f", null);
        checkPermisionsAndloadTheMap();
    }

    private void checkPermisionsAndloadTheMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission(this);
        } else {
            loadMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1002: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


                    loadMap();
                    // permission was granted, yay! Do the
                    // location-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    public void requestLocationPermission(Activity activity) {
        if (activity != null) {
            Log.i(TAG, "callPhoneNumber: Permission is not granted.");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1002);
        }
    }

    public void loadMap() {

        mapView.setMapListener(new MapViewEventListener() {
            @Override
            public void mapLoadStatusChanged(MapLoadStatus mapLoadStatus) {

                switch (mapLoadStatus) {
                    case Created:

                        ArrayList<Annotation> locationsArrayList = new ArrayList<>();
                        ArrayList<Annotation.AnnotationLayer> annotationLayersList = new ArrayList<>();
                        annotationLayersList.add(Annotation.AnnotationLayer.PoiLayer);
                        annotationLayersList.add(Annotation.AnnotationLayer.Vehicle);

                        //Car current location
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        @SuppressLint("MissingPermission") Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        myLocation.setLatitude(myLocation.getLatitude());
                        myLocation.setLongitude(myLocation.getLongitude());
                        Annotation currentLocationAnnotation = new Annotation(MainActivity.this, R.drawable.car, myLocation);
                        currentLocationAnnotation.setType(Annotation.AnnotationType.Screen2D);
                        currentLocationAnnotation.setLayer(Annotation.AnnotationLayer.Vehicle);
                        locationsArrayList.add(currentLocationAnnotation);

                        //dealer0 current location
                        Location dealerLocation0 = new Location("");
                        dealerLocation0.setLatitude(42.345940);
                        dealerLocation0.setLongitude(-83.072724);
                        Annotation dealerAnnotation0 = new Annotation(MainActivity.this, R.drawable.pin, dealerLocation0);
                        dealerAnnotation0.setType(Annotation.AnnotationType.Screen2D);
                        dealerAnnotation0.setLayer(Annotation.AnnotationLayer.PoiLayer);
                        locationsArrayList.add(dealerAnnotation0);

                        //dealer1 current location
                        Location dealerLocation1 = new Location("");
                        dealerLocation1.setLatitude(42.348693);
                        dealerLocation1.setLongitude(-83.013506);
                        Annotation dealerAnnotation1 = new Annotation(MainActivity.this, R.drawable.pin2, dealerLocation1);
                        dealerAnnotation1.setType(Annotation.AnnotationType.Screen2D);
                        dealerAnnotation1.setLayer(Annotation.AnnotationLayer.PoiLayer);
                        locationsArrayList.add(dealerAnnotation1);


                        mapView.enableAnnotationLayers(annotationLayersList);
                        mapView.addAnnotation(currentLocationAnnotation);
                        mapView.addAnnotation(dealerAnnotation0);
                        mapView.addAnnotation(dealerAnnotation1);
                        mapView.setZoomLevel(mapView.calculateZoom(locationsArrayList, currentLocationAnnotation), false);
                        mapView.lookAt(locationsArrayList);

                        break;
                }
            }

            @Override
            public void mapIsPanning() {

            }

            @Override
            public void mapIsZooming() {

            }

            @Override
            public void mapDidZoom(float v) {

            }

            @Override
            public boolean mapWasTappedAtLocation(Location location, boolean b) {
                return false;
            }

            @Override
            public void connectivityStatusChanged(ConnectivityStatus connectivityStatus) {

            }

            @Override
            public Popup annotationWasTapped(Annotation annotation) {
                return null;
            }

            @Override
            public Popup trafficIncidentTappedAtCrossStreet(String s, String s1) {
                return null;
            }
        });
    }
}
