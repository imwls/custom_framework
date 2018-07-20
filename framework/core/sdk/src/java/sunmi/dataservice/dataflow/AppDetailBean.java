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

package sunmi.dataservice.dataflow;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class AppDetailBean implements Parcelable {
    public String appName;
    public String packageName;
    public String appVersion;
    public long appSize;
    public long appCacheSize;
    public String appUsedPower;
    public long appUsedFlow;
    public long appUsedTime;
    public int appUsedCameraCount;
    public boolean isActive;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.packageName);
        dest.writeString(this.appVersion);
        dest.writeLong(this.appSize);
        dest.writeLong(this.appCacheSize);
        dest.writeString(this.appUsedPower);
        dest.writeLong(this.appUsedFlow);
        dest.writeLong(this.appUsedTime);
        dest.writeInt(this.appUsedCameraCount);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    public AppDetailBean() {
    }

    public AppDetailBean(Parcel in) {
        this.appName = in.readString();
        this.packageName = in.readString();
        this.appVersion = in.readString();
        this.appSize = in.readLong();
        this.appCacheSize = in.readLong();
        this.appUsedPower = in.readString();
        this.appUsedFlow = in.readLong();
        this.appUsedTime = in.readLong();
        this.appUsedCameraCount = in.readInt();
        this.isActive = in.readByte() != 0;
    }

    public static final Creator<AppDetailBean> CREATOR = new Creator<AppDetailBean>() {
        @Override
        public AppDetailBean createFromParcel(Parcel source) {
            return new AppDetailBean(source);
        }

        @Override
        public AppDetailBean[] newArray(int size) {
            return new AppDetailBean[size];
        }
    };
}
