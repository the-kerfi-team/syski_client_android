package uk.co.syski.client.android.model.viewmodel;

import org.junit.Test;

import static org.junit.Assert.*;

public class SystemRAMModelTest {

    @Test
    public void testDefault() {
        long testMemory = 1234567890;
        String testUnits = "An invalid input";

        //Create new SystemRAMModel with the test value as its memory size.
        SystemRAMModel testModel = new SystemRAMModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("1234567890 B", result);
    }

    @Test
    public void testValidUnit() {
        long testMemory = 10485760; //1085760 bytes = 10 MB
        String testUnits = "MB";

        //Create new SystemRAMModel with the test value as its memory size.
        SystemRAMModel testModel = new SystemRAMModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("10 MB", result);
    }

    @Test
    public void testNonIntegerResult() //All values should be rounded to a whole number.
    {
        long testMemory = 1234567890; ///1085760 bytes ≈ 1177 MB
        String testUnits = "MB";

        //Create new SystemRAMModel with the test value as its memory size.
        SystemRAMModel testModel = new SystemRAMModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("1177 MB", result);
    }
}