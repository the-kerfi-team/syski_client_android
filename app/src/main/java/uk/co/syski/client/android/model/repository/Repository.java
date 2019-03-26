package uk.co.syski.client.android.model.repository;

public enum Repository {
    INSTANCE;

    public static Repository getInstance() {
        return INSTANCE;
    }


    private UserRepository mUserRepository = new UserRepository();

    private SystemRepository mSystemRepository = new SystemRepository();

    private CPURepository mCPURepository = new CPURepository();

    private GPURepository mGPURepository = new GPURepository();

    private RAMRepository mRAMRepository = new RAMRepository();

    private StorageRepository mStorageRepository = new StorageRepository();

    private MOBORepository mMOBORepository = new MOBORepository();

    private OSRepository mOSRepository = new OSRepository();

    public synchronized UserRepository getUserRepository() { return mUserRepository; }

    public synchronized SystemRepository getSystemRepository()
    {
        return mSystemRepository;
    }

    public synchronized CPURepository getCPURepository()
    {
        return mCPURepository;
    }

    public synchronized GPURepository getGPURepository()
    {
        return mGPURepository;
    }

    public synchronized RAMRepository getRAMRepository()
    {
        return mRAMRepository;
    }

    public synchronized StorageRepository getStorageRepository()
    {
        return mStorageRepository;
    }

    public synchronized MOBORepository getMOBORepository() { return mMOBORepository;}

    public synchronized OSRepository getOSRepository() { return mOSRepository;}

}
