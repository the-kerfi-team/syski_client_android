package uk.co.syski.client.android.data.thread.entity.cpu;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.CPU;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.DeleteAll;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.GetCPUs;
import uk.co.syski.client.android.data.thread.entity.cpu.statement.InsertAll;

public class CPUThreads {
    private static final CPUThreads ourInstance = new CPUThreads();

    public static CPUThreads getInstance() {
        return ourInstance;
    }

    private CPUThreads() {}

    public List<CPU> GetCPUs(UUID... Ids) throws ExecutionException, InterruptedException {
        return new GetCPUs().execute(Ids).get();
    }

    public Void InsertAll(CPU... CPUs) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(CPUs).get();
    }

    public Void DeleteAll(CPU... CPUs) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(CPUs).get();
    }
}
