package riderz.team10.ecse321.com.riderzdrivers;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import riderz.team10.ecse321.com.riderzdrivers.assets.geolocation.Geocoding;
import riderz.team10.ecse321.com.riderzdrivers.assets.geolocation.LatLng;
import riderz.team10.ecse321.com.riderzdrivers.assets.geolocation.ReverseGeocoding;
import riderz.team10.ecse321.com.riderzdrivers.assets.convertor.Display;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Test
    public void testGeocoding() {
        Context context = InstrumentationRegistry.getTargetContext();
        LatLng latLng = Geocoding.getLatLngFromAddress(context, "Antartica", "InstrumentationTest");
        assertEquals(latLng.getLatitude(), -82.862752, 1);
        assertEquals(latLng.getLongitude(), 135.0, 1);
    }

    @Test
    public void testReverseGeocoding() {
        Context context = InstrumentationRegistry.getTargetContext();
        try {
            Address address = ReverseGeocoding.reverseLookup(context, -82.862752, 135.0);
            assertEquals(address.getCountryName(), "Antarctica");

            assertEquals(ReverseGeocoding.safeAddressToString(address), "Antarctica");
        } catch (IOException e) {
            assert(false);
        }
    }

    /**
     * WARNING: THIS TEST IS POTENTIALLY DANGEROUS TO RUN
     * Resource will change from phone to phone. Therefore, scaling will not be similar.
     */
    @Test
    @Ignore
    public void testDisplayConversion() {
        Resources resource = InstrumentationRegistry.getTargetContext().getResources();
        assertEquals(Display.dpToPX(resource, 25), 3 * 25);
        assertEquals(Display.pxToDP(resource, 50), Math.floor(50 / 3), 1);
    }
}