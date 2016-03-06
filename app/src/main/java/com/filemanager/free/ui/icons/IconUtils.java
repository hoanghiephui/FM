/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file IconUtils.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.ui.icons;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.filemanager.free.R;
import com.filemanager.free.utils.PreferenceUtils;

public class IconUtils {
    int LIGHT = 0, DARK = 1, CURRENT, rand;
    Context c;

    public IconUtils(SharedPreferences Sp, Context c) {

        rand = Integer.parseInt(Sp.getString("theme", "0"));
        CURRENT = rand == 2 ? PreferenceUtils.hourOfDay() : rand;
        this.c = c;
    }

    public Drawable getCopyDrawable() {
        return ContextCompat.getDrawable(c, R.drawable.ic_content_copy_white_36dp);
    }

    public Drawable getCutDrawable() {
        return ContextCompat.getDrawable(c, R.drawable.ic_content_cut_white_36dp);
    }

    public Drawable getRootDrawable() {
        if (CURRENT == LIGHT)
            return ContextCompat.getDrawable(c, R.drawable.root);
        else
            return ContextCompat.getDrawable(c, R.drawable.root);
    }

    public Drawable getSdDrawable() {
        if (CURRENT == LIGHT)
            return ContextCompat.getDrawable(c, R.drawable.ic_sd_storage_white_56dp);
        else
            return ContextCompat.getDrawable(c, R.drawable.ic_sd_storage_white_56dp);
    }

}
