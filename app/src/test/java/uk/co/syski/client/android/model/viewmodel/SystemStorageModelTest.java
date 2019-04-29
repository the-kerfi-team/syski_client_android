package uk.co.syski.client.android.model.viewmodel;

import org.junit.Test;

import static org.junit.Assert.*;

public class SystemStorageModelTest {
    @Test
    public void testDefault() {
        long testMemory = 1234567890;
        String testUnits = "An invalid input";

        //Create new SystemStorageModel with the test value as its memory size.
        SystemStorageModel testModel = new SystemStorageModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("1234567890 B", result);
    }

    @Test
    public void testValidUnits() {
        long testMemory = 10000000;
        String testUnits = "MB";

        //Create new SystemStorageModel with the test value as its memory size.
        SystemStorageModel testModel = new SystemStorageModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("10 MB", result);
    }

    @Test
    public void testNonIntegerResult() {
        long testMemory = 1234567890;
        String testUnits = "MB";

        //Create new SystemStorageModel with the test value as its memory size.
        SystemStorageModel testModel = new SystemStorageModel(null, null, null, testMemory);

        String result = testModel.getMemoryBytesAsString(testUnits);

        assertEquals("1234 MB", result);
    }

}