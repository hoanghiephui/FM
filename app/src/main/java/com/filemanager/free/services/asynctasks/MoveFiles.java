/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file MoveFiles.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.services.asynctasks;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.filemanager.free.fragments.Main;
import com.filemanager.free.services.CopyService;
import com.filemanager.free.filesystem.BaseFile;
import com.filemanager.free.utils.Futils;

import java.io.File;
import java.util.ArrayList;

public class MoveFiles extends AsyncTask<String, Void, Boolean> {
    ArrayList<BaseFile> files;
    Main ma;
    String path;
    Context context;
    int mode;

    public MoveFiles(ArrayList<BaseFile> files, Main ma, Context context, int mode) {
        this.ma = ma;
        this.context = context;
        this.files = files;
        this.mode = mode;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        path = strings[0];
        boolean b = true;
        int i = 0;
        if (files.size() == 0) return true;
        if (mode != 0) return false;
        if (files.get(0).isSmb()) {
            return false;
        }
        for (BaseFile f : files) {
            File file = new File(path + "/" + f.getName());
            File file1 = new File(f.getPath());
            if (!file1.renameTo(file)) {
                b = false;
            }
            i++;
        }
        return b;
    }

    @Override
    public void onPostExecute(Boolean b) {
        Futils futils = new Futils();
        if (b) {
            if (ma != null) if (ma.CURRENT_PATH.equals(path)) ma.updateList();
            for (BaseFile f : files) {
                futils.scanFile(f.getPath(), context);
                futils.scanFile(path + "/" + f.getName(), context);

            }
        } else if (!b) {
            Intent intent = new Intent(context, CopyService.class);
            intent.putExtra("FILE_PATHS", (files));
            intent.putExtra("COPY_DIRECTORY", path);
            intent.putExtra("move", true);
            intent.putExtra("MODE", mode);
            context.startService(intent);
        }
    }
}
