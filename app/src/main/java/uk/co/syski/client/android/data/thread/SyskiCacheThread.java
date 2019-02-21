package uk.co.syski.client.android.data.thread;

import uk.co.syski.client.android.data.thread.entity.cpu.CPUThreads;
import uk.co.syski.client.android.data.thread.entity.linking.systemos.SystemOSThreads;
import uk.co.syski.client.android.data.thread.entity.linking.systemram.SystemRAMThreads;
import uk.co.syski.client.android.data.thread.entity.operatingsystem.OperatingSystemThreads;
import uk.co.syski.client.android.data.thread.entity.ram.RAMThreads;
import uk.co.syski.client.android.data.thread.entity.system.SystemThreads;
import uk.co.syski.client.android.data.thread.entity.linking.systemcpu.SystemCPUThreads;
import uk.co.syski.client.android.data.thread.entity.linking.systemtype.SystemTypeThreads;
import uk.co.syski.client.android.data.thread.entity.type.TypeThreads;
import uk.co.syski.client.android.data.thread.entity.user.UserThreads;

public class SyskiCacheThread {
    private static final SyskiCacheThread ourInstance = new SyskiCacheThread();

    public static SyskiCacheThread getInstance() {
        return ourInstance;
    }

    private SyskiCacheThread() {}

    public final UserThreads UserThreads = uk.co.syski.client.android.data.thread.entity.user.UserThreads.getInstance();

    public final SystemThreads SystemThreads = uk.co.syski.client.android.data.thread.entity.system.SystemThreads.getInstance();

    public final TypeThreads TypeThreads = uk.co.syski.client.android.data.thread.entity.type.TypeThreads.getInstance();

    public final SystemTypeThreads SystemTypeThreads = uk.co.syski.client.android.data.thread.entity.linking.systemtype.SystemTypeThreads.getInstance();

    public final CPUThreads CPUThreads = uk.co.syski.client.android.data.thread.entity.cpu.CPUThreads.getInstance();

    public final SystemCPUThreads SystemCPUThreads = uk.co.syski.client.android.data.thread.entity.linking.systemcpu.SystemCPUThreads.getInstance();

    public final OperatingSystemThreads OperatingSystemThreads = uk.co.syski.client.android.data.thread.entity.operatingsystem.OperatingSystemThreads.getInstance();

    public final SystemOSThreads SystemOSThreads = uk.co.syski.client.android.data.thread.entity.linking.systemos.SystemOSThreads.getInstance();

    public final RAMThreads RAMThreads = uk.co.syski.client.android.data.thread.entity.ram.RAMThreads.getInstance();

    public final SystemRAMThreads SystemRAMThreads = uk.co.syski.client.android.data.thread.entity.linking.systemram.SystemRAMThreads.getInstance();

}
