package uk.co.syski.client.android.data.thread.entity.linking.systemos;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemOS;
import uk.co.syski.client.android.data.thread.entity.linking.systemos.statement.InsertAll;

public class SystemOSThreads {
    private static final SystemOSThreads ourInstance = new SystemOSThreads();

    public static SystemOSThreads getInstance() {
        return ourInstance;
    }

    private SystemOSThreads() {}

    public Void InsertAll(SystemOS... systemOS) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemOS).get();
    }

}
