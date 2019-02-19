package uk.co.syski.client.android.data.thread.entity.systemcpu;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.SystemCPU;
import uk.co.syski.client.android.data.thread.entity.systemcpu.statement.*;

public class SystemCPUThreads {
    private static final SystemCPUThreads ourInstance = new SystemCPUThreads();

    public static SystemCPUThreads getInstance() {
        return ourInstance;
    }

    private SystemCPUThreads() {}

    public Void InsertAll(SystemCPU... SystemCPUs) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(SystemCPUs).get();
    }

    public Void DeleteAll(SystemCPU... SystemCPUs) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute().get();
    }
}
