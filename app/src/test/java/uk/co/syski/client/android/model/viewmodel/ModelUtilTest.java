package uk.co.syski.client.android.model.viewmodel;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelUtilTest {
    @Test
    public void testNonNull() {
        String expectedResult = "Not null";
        String actualResult = ModelUtil.nullToUnknown("Not null");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void TestNull() {
        String result = ModelUtil.nullToUnknown(null);

        assertEquals("Unknown", result);
    }

    @Test
    public void testNullAsString() {
        String result = ModelUtil.nullToUnknown("null");

        assertEquals("Unknown", result);
    }

}