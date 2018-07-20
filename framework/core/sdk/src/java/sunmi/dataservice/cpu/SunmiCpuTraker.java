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

package sunmi.dataservice.cpu;

import com.android.internal.os.ProcessCpuTracker;

/**
 * Keeps track of cpu information about all installed applications
 * @author wangjiaxi
 */
public class SunmiCpuTraker {

    final ProcessCpuTracker mProcessCpuTracker = new ProcessCpuTracker(false);

    /**
     * SunmiCpuTraker constructor function
     */
    public SunmiCpuTraker() {
        mProcessCpuTracker.init();
    }

    /**
     * update cpu information
     * @return float
     */
    public float updateCpuStatsNow() {
        mProcessCpuTracker.update();

        int user = mProcessCpuTracker.getLastUserTime();
        int system = mProcessCpuTracker.getLastSystemTime();
        int iowait = mProcessCpuTracker.getLastIoWaitTime();
        int irq = mProcessCpuTracker.getLastIrqTime();
        int softIrq = mProcessCpuTracker.getLastSoftIrqTime();
        int idle = mProcessCpuTracker.getLastIdleTime();

        int total = user + system + iowait + irq + softIrq + idle;
        if (total == 0) total = 1;

        return ((float)(user+system+iowait+irq+softIrq)*100) / total;
    }
}
