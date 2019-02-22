package uk.co.syski.client.android.data.thread.entity.cpu;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.DeleteAll;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.GetCPUs;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.InsertAll;

public class CPUThreads {
    private static final CPUThreads ourInstance = new CPUThreads();

    public static CPUThreads getInstance() {
        return ourInstance;
    }

    private CPUThreads() {}

    public List<CPUEntity> GetCPUs(UUID... Ids) throws ExecutionException, InterruptedException {
        return new GetCPUs().execute(Ids).get();
    }

    public Void InsertAll(CPUEntity... cpuEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(cpuEntities).get();
    }

    public Void DeleteAll(CPUEntity... cpuEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(cpuEntities).get();
    }
}
