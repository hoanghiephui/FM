// RegisterCallback.aidl
package com.filemanager.free;

// Declare any non-default types here with import statements
import com.filemanager.free.ProgressListener;
import com.filemanager.free.utils.DataPackage;
interface RegisterCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void registerCallBack(in ProgressListener p);
    List<DataPackage> getCurrent();
}
