/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file FileBundle.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.services;

import com.filemanager.free.filesystem.HFile;

/**
 * Created by arpitkh996 on 25-01-2016.
 */
public class FileBundle {
    private HFile file, file2;
    private boolean move;

    public FileBundle(HFile file, HFile file2, boolean move) {
        this.file = file;
        this.file2 = file2;
        this.move = move;
    }

    public HFile getFile() {
        return file;
    }

    public HFile getFile2() {
        return file2;
    }

    public boolean isMove() {
        return move;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileBundle)) {
            return false;
        }
        if (this == obj || (this.file.equals(((FileBundle) obj).getFile()) && this.file2.equals(((FileBundle) obj).getFile2()))) {
            return true;
        }
        return false;
    }
}
