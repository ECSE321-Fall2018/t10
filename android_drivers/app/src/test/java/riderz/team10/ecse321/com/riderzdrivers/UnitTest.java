package riderz.team10.ecse321.com.riderzdrivers;

import org.junit.Test;

import riderz.team10.ecse321.com.riderzdrivers.assets.geolocation.Geocoding;
import riderz.team10.ecse321.com.riderzdrivers.assets.geolocation.LatLng;
import riderz.team10.ecse321.com.riderzdrivers.assets.konvertor.SQLCompliance;

import static org.junit.Assert.*;

public class UnitTest {
    @Test
    public void testSQLCompliance() {
        assertEquals(SQLCompliance.convertToSQLTimestamp("2017-06-06T23:00:06.000+050"),
                     "2017-06-06 23:00:06");
    }
}