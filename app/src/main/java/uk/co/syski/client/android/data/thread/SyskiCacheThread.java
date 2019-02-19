package uk.co.syski.client.android.data.thread;

import uk.co.syski.client.android.data.thread.entity.cpu.CPUThreads;
import uk.co.syski.client.android.data.thread.entity.system.SystemThreads;
import uk.co.syski.client.android.data.thread.entity.systemtype.SystemTypeThreads;
import uk.co.syski.client.android.data.thread.entity.type.TypeThreads;

public class SyskiCacheThread {
    private static final SyskiCacheThread ourInstance = new SyskiCacheThread();

    public static SyskiCacheThread getInstance() {
        return ourInstance;
    }

    private SyskiCacheThread() {
    }

    public final SystemThreads SystemThreads = uk.co.syski.client.android.data.thread.entity.system.SystemThreads.getInstance();

    public final TypeThreads TypeThreads = uk.co.syski.client.android.data.thread.entity.type.TypeThreads.getInstance();

    public final SystemTypeThreads SystemTypeThreads = uk.co.syski.client.android.data.thread.entity.systemtype.SystemTypeThreads.getInstance();

    public final CPUThreads CPUThreads = uk.co.syski.client.android.data.thread.entity.cpu.CPUThreads.getInstance();

}
