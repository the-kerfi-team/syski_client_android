package uk.co.syski.client.android.data.thread.entity.linking.systemgpu;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.thread.entity.linking.systemgpu.statement.InsertAll;

public class SystemGPUThreads {
    private static final SystemGPUThreads ourInstance = new SystemGPUThreads();

    public static SystemGPUThreads getInstance() {
        return ourInstance;
    }

    private SystemGPUThreads() {}

    public Void InsertAll(SystemGPUEntity... systemGPUEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemGPUEntities).get();
    }

}
