package uk.co.syski.client.android.model.repository;

public enum Repository {
    INSTANCE;

    public static Repository getInstance() {
        return INSTANCE;
    }

    private UserRepository mUserRepository = new UserRepository();

    private MOBORepository mMOBORepository = new MOBORepository();

    private OSRepository mOSRepository = new OSRepository();

    public synchronized UserRepository getUserRepository()
    {
        return mUserRepository;
    }

    public synchronized SystemRepository getSystemRepository()
    {
        return SystemRepository.INSTANCE;
    }

    public synchronized CPURepository getCPURepository()
    {
        return CPURepository.INSTANCE;
    }

    public synchronized GPURepository getGPURepository()
    {
        return GPURepository.INSTANCE;
    }

    public synchronized RAMRepository getRAMRepository()
    {
        return RAMRepository.INSTANCE;
    }

    public synchronized StorageRepository getStorageRepository()
    {
        return StorageRepository.INSTANCE;
    }

    public synchronized MOBORepository getMOBORepository()
    {
        return mMOBORepository;
    }

    public synchronized OSRepository getOSRepository()
    {
        return mOSRepository;
    }

}
