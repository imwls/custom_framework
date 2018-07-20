/*
 * Copyright (C) 2017 The Sunmi Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sunmi.dataservice.appusage;

import android.app.Application;
import android.os.Environment;
import sunmi.dataservice.appusage.ApplicationsState;
import sunmi.dataservice.appusage.ApplicationsState.AppEntry;
import sunmi.dataservice.appusage.ApplicationsState.AppFilter;
import sunmi.dataservice.appusage.ApplicationsState.CompoundFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * get information about the specified applications
 */
public class AppUsageInfo implements ApplicationsState.Callbacks {

    public static final int SIZE_TOTAL = 0;
    public static final int SIZE_INTERNAL = 1;
    public static final int SIZE_EXTERNAL = 2;

    public static final int sort_order_size = 1;
    // sort order
    private int mSortOrder = sort_order_size;

    private int mWhichSize = SIZE_TOTAL;

    // whether showing system apps.
    private boolean mShowSystem;

    private ApplicationsState mApplicationsState;

    private ApplicationsState.Session mSession;

    private ArrayList<ApplicationsState.AppEntry> mBaseEntries;
    private ArrayList<ApplicationsState.AppEntry> mEntries;
    private boolean mHasReceivedLoadEntries;
    private int mLastSortMode=-1;
    final Callbacks mCallbacks;

    /**
     * AppUsageInfo constructed function
     * <p><pre>{@code
     * Using:
     * new AppUsageInfo(Application, boolean, Callbacks);
     * }</pre>
     * @param app                   the Application
     * @param showsystem    if true, show system app
     * @param callbacks         when get app list {@link Callbacks}
     */
    public AppUsageInfo(Application app, boolean showsystem, Callbacks callbacks) {
        mApplicationsState = ApplicationsState.getInstance(app);
        mSession = mApplicationsState.newSession(this);
        mShowSystem = showsystem;
        mCallbacks = callbacks;
    }

    /**
     * resume mSession and rebuild AppEntry
     * <p><pre>{@code
     * Using:
     * new AppUsageInfo(Application, boolean, Callbacks).resume(int);
     * }</pre>
     * @param sort
     */
    public void resume(int sort) {
        mSession.resume();
        mLastSortMode = sort;
        rebuild(true);
    }

    /**
     * rebuild AppEntry by the sort mode
     * @param eraseold
     */
    public void rebuild(boolean eraseold) {
        if (!mHasReceivedLoadEntries) {
            // Don't rebuild the list until all the app entries are loaded.
            return;
        }

        ApplicationsState.AppFilter filterObj;
        Comparator<ApplicationsState.AppEntry> comparatorObj;
        boolean emulated = Environment.isExternalStorageEmulated();
        if (emulated) {
            mWhichSize = SIZE_TOTAL;
        } else {
            mWhichSize = SIZE_INTERNAL;
        }
        filterObj = ApplicationsState.FILTER_EVERYTHING;

        if (!mShowSystem) {
            filterObj = new ApplicationsState.CompoundFilter(filterObj,
                    ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER);
        }
        switch (mLastSortMode) {
            case sort_order_size:
                switch (mWhichSize) {
                    case SIZE_INTERNAL:
                        comparatorObj = ApplicationsState.INTERNAL_SIZE_COMPARATOR;
                        break;
                    case SIZE_EXTERNAL:
                        comparatorObj = ApplicationsState.EXTERNAL_SIZE_COMPARATOR;
                        break;
                    default:
                        comparatorObj = ApplicationsState.SIZE_COMPARATOR;
                        break;
                }
                break;
            default:
                comparatorObj = ApplicationsState.ALPHA_COMPARATOR;
                break;
        }
        ArrayList<ApplicationsState.AppEntry> entries
                = mSession.rebuild(filterObj, comparatorObj);
        if (entries == null && !eraseold) {
            // Don't have new list yet, but can continue using the old one.
            return;
        }
        mBaseEntries = entries;
        if (mBaseEntries != null) {
            mEntries = mBaseEntries;
        } else {
            mEntries = null;
        }
    }

    /**
     * start get app usage info about size
     */
    public void startAppUsageSizeInfo() {
        resume(mSortOrder);
    }

    @Override
    public void onRunningStateChanged(boolean running) {
    }

    /**
     * callback the method once package has changed
     */
    @Override
    public void onPackageListChanged() {
        rebuild(false);
    }

    /**
     * callback the method once app list has rebuild completed
     * @param  apps
     */
    @Override
    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> apps) {
        mBaseEntries = apps;
        mEntries = mBaseEntries;
    }

    /**
     * callback the method once app icon has changed
     */
    @Override
    public void onPackageIconChanged() {
        // We ensure icons are loaded when their item is displayed, so
        // don't care about icons loaded in the background.
    }

    /**
     * callback the method once app size has changed
     * @param packageName
     */
    @Override
    public void onPackageSizeChanged(String packageName) {
    }

    @Override
    public void onLoadEntriesCompleted() {
        mHasReceivedLoadEntries = true;
    }

    /**
     * callback the method once all app has got the app size
     */
    @Override
    public void onAllSizesComputed() {
        if (mLastSortMode == 1) {
            rebuild(false);
        }
        mCallbacks.onGetAppInfoComplete(mEntries);
    }

    /**
     * callback the method once launcher info has changed
     */
    @Override
    public void onLauncherInfoChanged() {
        if (!mShowSystem) {
            rebuild(false);
        }
    }

    /**
     * callback the method to get app list
     */
    public interface Callbacks {
        void onGetAppInfoComplete(ArrayList<AppEntry> apps);
    }

}
