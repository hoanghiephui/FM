/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file SearchTask.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.services.asynctasks;

import android.os.AsyncTask;

import com.filemanager.free.fragments.Main;
import com.filemanager.free.filesystem.BaseFile;
import com.filemanager.free.filesystem.HFile;

import java.util.ArrayList;

/**
 * Created by chinmay on 6/9/2015.
 */
public class SearchTask extends AsyncTask<String, BaseFile, Void> {
    Main main;
    String key;

    public SearchTask(Main main, String key) {
        this.main = main;
        this.key = key;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        main.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected Void doInBackground(String... params) {

        String path = params[0];
        HFile file = new HFile(main.openMode, path);
        file.generateMode(main.getActivity());
        if (file.isSmb()) return null;
        search(file, key);
        return null;
    }

    @Override
    public void onPostExecute(Void c) {
        main.onSearchCompleted();
        main.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onProgressUpdate(BaseFile... val) {
        if (!isCancelled()) {
            main.addSearchResult(val[0]);
        }
    }

    public void search(HFile file, String text) {

        if (file.isDirectory()) {
            ArrayList<BaseFile> f = file.listFiles(main.ROOT_MODE);
            // do you have permission to read this directory?
            if (!isCancelled())
                for (BaseFile x : f) {
                    if (!isCancelled()) {
                        if (x.isDirectory()) {
                            if (x.getName().toLowerCase()
                                    .contains(text.toLowerCase())) {
                                publishProgress(x);
                            }
                            if (!isCancelled()) search(x, text);

                        } else {
                            if (x.getName().toLowerCase()
                                    .contains(text.toLowerCase())) {
                                publishProgress(x);
                            }
                        }
                    }
                }
        } else {
            System.out
                    .println(file.getPath() + "Permission Denied");
        }
    }
}
