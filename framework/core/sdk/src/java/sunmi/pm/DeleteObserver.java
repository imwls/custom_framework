package sunmi.pm;

import android.content.pm.IPackageDeleteObserver;

public abstract class DeleteObserver {

	private final IPackageDeleteObserver.Stub mBinder = new IPackageDeleteObserver.Stub() {
		@Override
		public void packageDeleted(java.lang.String packageName, int returnCode) {
			DeleteObserver.this.packageDeleted(packageName, returnCode);
		}
	};

	/** {@hide} */
    public IPackageDeleteObserver getBinder() {
        return mBinder;
    }

    /**
     * 
     * @param packageName
     * @param returnCode
     */
	public abstract void packageDeleted(java.lang.String packageName, int returnCode);

}
