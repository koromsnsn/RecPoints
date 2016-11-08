package com.example.nao.recpoints;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class AddressTask extends AsyncTask<Double, Void, Address> {
    private RecPointsActivity mMapActivity = null;
    private Geocoder mGeocoder = null;
    private TextView mResultText = null;

    public AddressTask(RecPointsActivity mapactivity) {
        mMapActivity = mapactivity;
        mGeocoder = new Geocoder(mMapActivity, Locale.getDefault());
        mResultText = (TextView)mMapActivity.findViewById(R.id.textView6);

    }
    @Override
    protected Address doInBackground(Double... location) {

        Address result = null;
        try {
            List<Address> results  = mGeocoder.getFromLocation(location[0], location[1], 1);
            if (results != null && !results.isEmpty()) {
                result = results.get(0);
            }
        } catch (IOException e) {
            Log.e("AddressTask", e.getMessage());
        }
        return result;
    }
    @Override
    protected void onPostExecute(Address result)
    {
        if (result != null) {

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < result.getMaxAddressLineIndex() + 1; i++) {
                String item = result.getAddressLine(i);
                if (item == null) {
                    break;
                }

                sb.append(item);
            }
            mResultText.setText(sb.toString());
        }

    }
}
