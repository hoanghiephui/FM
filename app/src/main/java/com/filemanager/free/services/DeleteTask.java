/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file DeleteTask.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Toast;

import com.filemanager.free.R;
import com.filemanager.free.fragments.ZipViewer;
import com.filemanager.free.filesystem.BaseFile;
import com.filemanager.free.utils.Futils;

import java.util.ArrayList;

public class DeleteTask extends AsyncTask<ArrayList<BaseFile>, String, Boolean> {


    ArrayList<BaseFile> files;
    ContentResolver contentResolver;
    Context cd;
    Futils utils = new Futils();
    boolean rootMode;
    ZipViewer zipViewer;

    public DeleteTask(ContentResolver c, Context cd) {
        this.cd = cd;
        rootMode = PreferenceManager.getDefaultSharedPreferences(cd).getBoolean("rootmode", false);
    }

    public DeleteTask(ContentResolver c, Context cd, ZipViewer zipViewer) {
        this.cd = cd;
        rootMode = PreferenceManager.getDefaultSharedPreferences(cd).getBoolean("rootmode", false);
        this.zipViewer = zipViewer;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(cd, values[0], Toast.LENGTH_SHORT).show();
    }

    @SafeVarargs
    protected final Boolean doInBackground(ArrayList<BaseFile>... p1) {
        files = p1[0];
        boolean b = true;
        if (files.size() == 0) return true;
        for (BaseFile a : files)
            (a).delete(cd, rootMode);

        return b;
    }

    @Override
    public void onPostExecute(Boolean b) {
        Intent intent = new Intent("loadlist");
        cd.sendBroadcast(intent);

        if (!files.get(0).isSmb()) {
            try {
                for (BaseFile f : files) {
                    delete(cd, f.getPath());
                }
            } catch (Exception e) {
                for (BaseFile f : files) {
                    utils.scanFile(f.getPath(), cd);
                }
            }
        }
        if (!b) {
            Toast.makeText(cd, utils.getString(cd, R.string.error), Toast.LENGTH_SHORT).show();
        } else if (zipViewer == null) {
            Toast.makeText(cd, utils.getString(cd, R.string.done), Toast.LENGTH_SHORT).show();
        }
        if (zipViewer != null) {
            zipViewer.files.clear();
        }
    }

    void delete(final Context context, final String file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        // Delete the entry from the media database. This will actually delete media files.
        contentResolver.delete(filesUri, where, selectionArgs);

    }
}



