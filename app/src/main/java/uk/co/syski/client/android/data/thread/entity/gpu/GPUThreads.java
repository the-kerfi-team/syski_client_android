package uk.co.syski.client.android.data.thread.entity.gpu;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.thread.entity.gpu.statement.*;

public class GPUThreads {
    private static final GPUThreads ourInstance = new GPUThreads();

    public static GPUThreads getInstance() {
        return ourInstance;
    }

    private GPUThreads() {}

    public List<GPUEntity> GetGPUs(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetGPUs().execute(uuids).get();
    }

    public Void InsertAll(GPUEntity... gpuEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(gpuEntities).get();
    }

    public Void DeleteAll(GPUEntity... gpuEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(gpuEntities).get();
    }

}
