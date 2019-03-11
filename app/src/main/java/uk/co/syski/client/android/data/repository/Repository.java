package uk.co.syski.client.android.data.repository;

public enum Repository {
    INSTANCE;

    public static Repository getInstance() {
        return INSTANCE;
    }


    private UserRepository mUserRepository = new UserRepository();

    private SystemRepository mSystemRepository = new SystemRepository();

    private CPURepository mCPURepository = new CPURepository();

    public synchronized UserRepository getUserRepository() { return mUserRepository; }

    public synchronized SystemRepository getSystemRepository()
    {
        return mSystemRepository;
    }

    public synchronized CPURepository getCPURepository()
    {
        return mCPURepository;
    }

}
