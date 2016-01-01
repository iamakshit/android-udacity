package akshit.snapdeal.com.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import akshit.snapdeal.com.sunshine.data.WeatherContract;
import akshit.snapdeal.com.sunshine.utils.Utility;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    String TAG = "akshit.snapdeal.com.sunshine.ForecastFragment";
    ForecastAdapter weatherFroecastAdapter;
    public static final String OPEN_WEATHER_MAP_API_KEY = "f4a6312a0459542973769c10abf61c7c";


    public ForecastFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        updateWeatherTask();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        inflater.inflate(R.menu.menu_settings, menu);

    }

    public void updateWeatherTask() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String pinCode = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String units = prefs.getString(getString(R.string.pref_temperature_key), getString(R.string.pref_temp_default));


        FetchWeatherTask task = new FetchWeatherTask(getActivity());

        int corePoolSize = 60;
        int maximumPoolSize = 80;
        int keepAliveTime = 10;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
        Log.i(TAG, "Refresh Action being called");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pinCode,units);
        else
            task.execute(pinCode,units);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Log.i(TAG, "Refresh Action executed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);

        //Note that list_item_forecast.xml is used to specify the behaviour of every item in the list
        //fragment_main.xml is used to define the behaviour of the list
      //  weatherFroecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList<String>());

        weatherFroecastAdapter = new ForecastAdapter(getActivity(), cur, 0);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview.setAdapter(weatherFroecastAdapter);

       /* listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity().getApplicationContext();
                String text = (String) listview.getAdapter().getItem(position);

                Toast.makeText(context, "Following info: " + text, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);


            }
        }); */
        return rootView;

    }
}
