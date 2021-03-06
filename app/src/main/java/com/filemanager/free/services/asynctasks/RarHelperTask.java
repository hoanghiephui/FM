/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file RarHelperTask.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.services.asynctasks;

/**
 * Created by Arpit on 25-01-2015.
 */

import android.os.AsyncTask;

import com.filemanager.free.fragments.ZipViewer;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RarHelperTask extends AsyncTask<File, Void, ArrayList<FileHeader>> {

    ZipViewer zipViewer;
    String dir;

    /**
     * AsyncTask to load RAR file items.
     *
     * @param zipViewer the zipViewer fragment instance
     * @param dir
     */

    public RarHelperTask(ZipViewer zipViewer, String dir) {
        this.zipViewer = zipViewer;
        this.dir = dir;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        zipViewer.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected ArrayList<FileHeader> doInBackground(File... params) {
        ArrayList<FileHeader> elements = new ArrayList<FileHeader>();
        try {
            Archive zipfile = new Archive(params[0]);
            zipViewer.archive = zipfile;
            if (zipViewer.wholelistRar.size() == 0) {

                FileHeader fh = zipfile.nextFileHeader();
                while (fh != null) {
                    zipViewer.wholelistRar.add(fh);
                    fh = zipfile.nextFileHeader();
                }
            }
            if (dir == null || dir.trim().length() == 0 || dir.equals("")) {

                for (FileHeader header : zipViewer.wholelistRar) {
                    String name = header.getFileNameString();

                    if (!name.contains("\\")) {
                        elements.add(header);

                    }
                }
            } else {
                for (FileHeader header : zipViewer.wholelistRar) {
                    String name = header.getFileNameString();
                    if (name.substring(0, name.lastIndexOf("\\")).equals(dir)) {
                        elements.add(header);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        Collections.sort(elements, new FileListSorter());
        return elements;
    }

    @Override
    protected void onPostExecute(ArrayList<FileHeader> zipEntries) {
        super.onPostExecute(zipEntries);
        zipViewer.swipeRefreshLayout.setRefreshing(false);
        zipViewer.createRarviews(zipEntries, dir);
    }

    class FileListSorter implements Comparator<FileHeader> {

        public FileListSorter() {

        }

        @Override
        public int compare(FileHeader file1, FileHeader file2) {

            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;


            } else if (file2.isDirectory() && !(file1).isDirectory()) {
                return 1;
            }
            return file1.getFileNameString().compareToIgnoreCase(file2.getFileNameString());
        }
    }
}

