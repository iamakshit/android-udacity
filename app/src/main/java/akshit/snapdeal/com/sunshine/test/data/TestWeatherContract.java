package akshit.snapdeal.com.sunshine.test.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import akshit.snapdeal.com.sunshine.data.WeatherContract;

/**
 * Created by akshit on 25/12/15.
 */
public class TestWeatherContract extends AndroidTestCase{

    private static final String TEST_WEATHER_LOCATION = "/North Pole";

    public void testBuildWeatherLocation() {
               Uri locationUri = WeatherContract.WeatherEntry.buildWeatherLocation(TEST_WEATHER_LOCATION);
               assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                               "WeatherContract.",
                       locationUri);
                assertEquals("Error: Weather location not properly appended to the end of the Uri",
                                TEST_WEATHER_LOCATION, locationUri.getLastPathSegment());
                assertEquals("Error: Weather location Uri doesn't match our expected result",
                               locationUri.toString(),
                                "content://com.example.android.sunshine.app/weather/%2FNorth%20Pole");
           }
}
