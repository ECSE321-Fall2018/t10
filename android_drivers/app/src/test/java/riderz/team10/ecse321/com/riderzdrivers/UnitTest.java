package riderz.team10.ecse321.com.riderzdrivers;

import org.junit.Test;

import riderz.team10.ecse321.com.riderzdrivers.assets.convertor.SQLCompliance;
import riderz.team10.ecse321.com.riderzdrivers.assets.verificator.RegexVerification;

import static org.junit.Assert.*;

public class UnitTest {
    @Test
    public void testSQLCompliance() {
        assertEquals(SQLCompliance.convertToSQLTimestamp("2017-06-06T23:00:06.000+050"),
                     "2017-06-06 23:00:06");
    }

    @Test
    public void testEmailCompliance() {
        assertEquals(RegexVerification.verifyEmail("abc@gmail.com"), true);
        assertEquals(RegexVerification.verifyEmail("53.af1.124_424@ga.gm.coqa"), true);
        assertEquals(RegexVerification.verifyEmail("513@a@.com"), false);
        assertEquals(RegexVerification.verifyEmail("52..2@gmail.com"), false);
    }

    @Test
    public void testNAPhoneCompliance() {
        assertEquals(RegexVerification.verifyPhone("513-535-2352"), true);
        assertEquals(RegexVerification.verifyPhone("1523288234"), true);
        assertEquals(RegexVerification.verifyPhone("514.535.2351"), true);
        assertEquals(RegexVerification.verifyPhone("599,166,2343"), true);
        assertEquals(RegexVerification.verifyPhone("123.5123535"), true);
        assertEquals(RegexVerification.verifyPhone("513a5359825"), true);
    }

    @Test
    public void testAlphabets() {
        assertEquals(RegexVerification.isAlphabetical("qwertyuioasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"), true);
        assertEquals(RegexVerification.isAlphabetical("1234567890"), false);
    }

    @Test
    public void textNumbers() {
        assertEquals(RegexVerification.isNumerical("1234567890"), true);
        assertEquals(RegexVerification.isNumerical("qwertyuiopasdfghjklzcxvbnm"), false);
    }

    @Test
    public void textAlphanumerical() {
        assertEquals(RegexVerification.isAlphanumerical("qwertyuioasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"), true);
        assertEquals(RegexVerification.isAlphanumerical(",./;'[]"), false);
    }
}