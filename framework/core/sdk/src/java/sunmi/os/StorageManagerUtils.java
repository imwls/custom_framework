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
 
package sunmi.os;

import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.os.StatFs;
import android.content.Context;
import android.util.Log;


/**
 * Information of Storage size
 * @author liuqiong
*/
public class StorageManagerUtils {
    /**
     * get hotal rom size,and changing decimal(GB) to integer(GB)
     * @param context
     * @return String
    */
    public static String getROMSizeIntege(Context context) {
        long romSize = getROMSize();
        String totalSize = Formatter.formatFileSize(context, romSize).toUpperCase();
        if (totalSize.contains("GB")) {
            totalSize = totalSize.replace("GB", "");
            totalSize = totalSize.replace(",", ".");

            float tSize;
            float fSize = ((romSize/1024.0f)/1024.0f)/1024.0f;
            if (fSize > 0.99 && fSize < 1024){
                tSize = fSize;
            }else{
                tSize = Float.parseFloat(totalSize);
            }

            for (int i = 2; i < 5; i++) {
                if (Math.pow(2,i) > tSize) {
                    return (int) Math.pow(2,i) + " GB";
                }
            }
        }

        return totalSize;
    }

    /**
     * get hotal rom decimal(GB) size,consist of system, data, cache, dev, sys/fs/cgroup
     * @return long
    */
    public static long getROMSize() {
        long totalSize = 0;
        String[] pathes = new String[] {"/system", "/data", "/cache", "/dev", "/sys/fs/cgroup"};
        for (int i = 0; i < pathes.length; i++) {
            StatFs stat = new StatFs(pathes[i]);
            totalSize += stat.getBlockSizeLong() * stat.getBlockCountLong();
        }

        return totalSize;
    }

    /**
     * get free size, just the free of data
     * @param context
     * @return long
    */
    public static long getFreeSize(Context context) {
        StatFs stat = new StatFs("/data");
        return stat.getFreeBlocksLong() * stat.getBlockSizeLong();
    }
    
    /**
     * get system size, using size(getROMSizeIntege) - size(getFreeSize)
     * @param context
     * @return long
    */
    public static long getSystemSize(Context context) {
        String size = getROMSizeIntege(context).replace(" GB", "");
        int rom = Integer.parseInt(size);
        long romSize = rom * 1024l * 1024l *1024l;

        Log.d("JerryLiu", "romSize = " + romSize);

        return romSize - getFreeSize(context);
    }
}
