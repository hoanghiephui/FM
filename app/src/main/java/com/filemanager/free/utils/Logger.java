/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Logger.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by arpitkh996 on 13-01-2016.
 */
public class Logger {

    public static void log(final Exception s, final String s1, Context context) {
        if (context == null) return;
        final File f = new File(context.getExternalFilesDir("internal"), "log.txt");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileWriter output = null;
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    if (s != null)
                        s.printStackTrace(pw);
                    output = new FileWriter(f.getPath());
                    BufferedWriter writer = new BufferedWriter(output);
                    writer.write(s1 + "\n");
                    writer.write(sw.toString());
                    writer.close();
                    output.close();
                } catch (IOException e) {
                }
            }
        }).start();
    }
}
