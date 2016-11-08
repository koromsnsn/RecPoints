package com.example.nao.recpoints;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RecPointsActivity extends AppCompatActivity implements LocationListener, LocationSource, android.view.View.OnClickListener, LoaderCallbacks<Address> {

    private GoogleMap mMap;
    private OnLocationChangedListener mListener;
    private LocationManager lcMgr;
    private TextView tvLat;
    private TextView tvLon;
    private TextView tvAddress;
    private EditText edRemarks;
    private double mLat = 0;
    private double mLon = 0;
    private DatabaseHelper dbhelper;
    private SupportMapFragment mapFragment;

    //My add instance
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private Button mBtnGrantGPS;
    private TextView mTvLocationPermissionState, mTvLatitude, mTVLongitude;
    private LocationManager mLocationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_points);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        lcMgr = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();

            } else {

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {
            Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();

        }



        if(lcMgr != null)
        {
            boolean gpsIsEnabled = lcMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(gpsIsEnabled)
            {
                lcMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 2.0f, this);
            } else
            {
                Toast.makeText(this, R.string.gps_not_enable, Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(this, R.string.location_not_enable, Toast.LENGTH_SHORT).show();
        }

        setUpMapIfNeeded();

        tvLat = (TextView)findViewById(R.id.textView2);
        tvLon = (TextView)findViewById(R.id.textView4);
        tvAddress = (TextView)findViewById(R.id.textView6);
        edRemarks=(EditText)findViewById(R.id.editText1);

        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(this);


        dbhelper = new DatabaseHelper(this);
        Bundle args = new Bundle();
        getLoaderManager().initLoader(0, args,this);

//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map01);
//        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {

        super.onStart();
        setUpMapIfNeeded();

        if(lcMgr != null)
        {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                } else {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

            } else {

                Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
            }

            mMap.setMyLocationEnabled(true);
        }

    }
    private void setUpMapIfNeeded() {
        if (mMap == null)
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                } else {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

            } else {

                Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
            }

            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map01)).getMap();
            //SupportMapFragment mMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            //mMap.getMapAsync((OnMapReadyCallback) this);

            if (mMap != null)
            {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                    } else {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }

                } else {

                    Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
                }

                mMap.setMyLocationEnabled(true);
            }
            mMap.setLocationSource(this);
        }
    }

    @Override
    protected void onStop() {

        if(lcMgr != null)
        {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                } else {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

            } else {

                Toast.makeText(RecPointsActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
            }

            lcMgr .removeUpdates(this);
        }

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rec_points, menu);
        return true;
    }


    @Override
    public void activate(OnLocationChangedListener listener) {

        mListener = listener;

    }

    @Override
    public void deactivate() {

        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {

        if( mListener != null )
        {
            mListener.onLocationChanged(location);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mLat = location.getLatitude();
            mLon = location.getLongitude();
            tvLat.setText(Double.toString(mLat));
            tvLon.setText(Double.toString(mLon));

        }

    }

    @Override
    public void onProviderDisabled(String arg0) {


    }

    @Override
    public void onProviderEnabled(String arg0) {


    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:

                getAddressByLoader();

                return;
            case R.id.button2:

                createDialog();
                return;

            default:
                break;
        }

    }
    /*
    private void getAddress() {
        new AddressTask(this).execute(mLat, mLon);
    }
    */
    private void createDialog() {

        String remarks = edRemarks.getText().toString();
        if (remarks.equals("")) {
            Toast.makeText(this,R.string.remarks_required, Toast.LENGTH_SHORT).show();
            edRemarks.requestFocus();
            return;
        }

        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                R.string.alert_dialog_save_confirm_title, R.string.alert_dialog_save_confirm_message);

        newFragment.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick() {

        savePoint();

    }

    public void doNegativeClick() {

    }
    private void savePoint(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        double lat= mLat;
        double lon = mLon;
        String address = tvAddress.getText().toString();
        String remarks = edRemarks.getText().toString();

        values.put(DatabaseHelper.COLUMN_LAT, lat);
        values.put(DatabaseHelper.COLUMN_LON, lon);
        values.put(DatabaseHelper.COLUMN_ADDRESS, address);
        values.put(DatabaseHelper.COLUMN_REMARKS, remarks);
        try {
            db.insert(DatabaseHelper.TABLE_POINTS, null, values);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.save_failed, Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maker_on:

                markerOn();
                return true;
            case R.id.maker_off:

                markerOff();
                return true;
        }
        return false;
    }
    private void markerOn() {
        SQLiteDatabase db = null;

        try{
            db = dbhelper.getReadableDatabase();

            Cursor cur = null;
            try{
                cur = db.query(
                        DatabaseHelper.TABLE_POINTS,
                        new String[] { DatabaseHelper.COLUMN_ID,DatabaseHelper.COLUMN_LAT,DatabaseHelper.COLUMN_LON ,
                                DatabaseHelper.COLUMN_ADDRESS,DatabaseHelper.COLUMN_REMARKS },
                        null,
                        null,
                        null,
                        null,
                        DatabaseHelper.COLUMN_ID);
                while( cur.moveToNext() ){
                    MarkerOptions options = new MarkerOptions();

                    BitmapDescriptor bMapIcon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_red);
                    options.icon(bMapIcon);
                    options.position(new LatLng(cur.getDouble(1), cur.getDouble(2)));
                    options.title(cur.getString(4));
                    options.snippet(cur.getString(3));
                    mMap.addMarker(options);
                }
            }catch(Exception e){
                Log.e("markerOn()", e.getMessage());
            }finally{
                if( cur != null ){
                    cur.close();
                    cur = null;
                }
            }
        }
        catch(Exception e){
        }finally{
            if(db != null){
                db.close();
                db = null;
            }
        }
    }
    private void markerOff() {
        mMap.clear();

    }
    private void getAddressByLoader() {
        if (mLat != 0) {
            Bundle args = new Bundle();
            args.putDouble("lat", mLat);
            args.putDouble("lon", mLon);
            getLoaderManager().restartLoader(0, args, this);
        }
    }

    @Override
    public Loader<Address> onCreateLoader(int id, Bundle args) {

        double lat = args.getDouble("lat");
        double lon = args.getDouble("lon");
        Log.d("RecPoints","onCreateLoader called");
        return new AddressTaskLoader(this, lat,lon);

    }

    @Override
    public void onLoadFinished(Loader<Address> loader, Address result) {

        if (result != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < result.getMaxAddressLineIndex() + 1; i++) {
                String item = result.getAddressLine(i);
                if (item == null) {
                    break;
                }

                sb.append(item);
            }
            tvAddress.setText(sb.toString());
        }

    }

    @Override
    public void onLoaderReset(Loader<Address> arg0) {

    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//    }

}

