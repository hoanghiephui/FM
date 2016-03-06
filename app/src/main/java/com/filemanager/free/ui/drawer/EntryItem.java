/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file EntryItem.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.ui.drawer;

import android.graphics.drawable.Drawable;

public class EntryItem implements Item {

    final String title;
    final String subtitle;
    Drawable icon1;

    public EntryItem(String title, String path, Drawable icon1) {
        this.title = title;
        this.subtitle = path;
        this.icon1 = icon1;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public Drawable getIcon() {
        return icon1;
    }

    public String getPath() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }
}
