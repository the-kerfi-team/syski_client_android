package uk.co.syski.client.android.data.repository;

public class Repository {

    private static SystemRepository mSystemRepository = new SystemRepository();

    public static SystemRepository getSystemRepository()
    {
        return mSystemRepository;
    }

    private static CPURepository mCPURepository = new CPURepository();

    public static CPURepository getCPURepository()
    {
        return mCPURepository;
    }

}
