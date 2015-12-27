package akshit.snapdeal.com.sunshine;

import android.app.Application;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import akshit.snapdeal.com.sunshine.data.WeatherContract;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * Created by akshit on 25/12/15.
     */
    public static class TestWeatherContract extends AndroidTestCase {

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
}