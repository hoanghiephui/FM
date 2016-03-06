/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file ZipObj.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.ui;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.zip.ZipEntry;

/**
 * Created by Arpit on 11-12-2014.
 */
public class ZipObj implements Parcelable {
    boolean directory;
    ZipEntry entry;
    String name;
    long date, size;

    public ZipObj(ZipEntry entry, long date, long size, boolean directory) {
        this.directory = directory;
        this.entry = entry;
        if (entry != null) {
            name = entry.getName();
            this.date = date;
            this.size = size;

        }
    }

    public ZipEntry getEntry() {
        return entry;
    }

    public boolean isDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getTime() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p1, int p2) {
        p1.writeString(name);
        p1.writeLong(size);
        p1.writeLong(date);
        p1.writeInt(isDirectory() ? 1 : 0);
    }

    public static final Parcelable.Creator<ZipObj> CREATOR =
            new Parcelable.Creator<ZipObj>() {
                public ZipObj createFromParcel(Parcel in) {
                    return new ZipObj(in);
                }

                public ZipObj[] newArray(int size) {
                    return new ZipObj[size];
                }
            };

    public ZipObj(Parcel im) {
        name = im.readString();
        size = im.readLong();
        date = im.readLong();
        int i = im.readInt();
        if (i == 0) {
            directory = false;
        } else {
            directory = true;
        }
        entry = new ZipEntry(name);
    }

}
