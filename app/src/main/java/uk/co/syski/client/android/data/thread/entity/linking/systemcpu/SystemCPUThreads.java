package uk.co.syski.client.android.data.thread.entity.linking.systemcpu;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.data.thread.entity.linking.systemcpu.statement.InsertAll;

public class SystemCPUThreads {
    private static final SystemCPUThreads ourInstance = new SystemCPUThreads();

    public static SystemCPUThreads getInstance() {
        return ourInstance;
    }

    private SystemCPUThreads() {}

    public Void InsertAll(SystemCPUEntity... systemCPUEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemCPUEntities).get();
    }
}
