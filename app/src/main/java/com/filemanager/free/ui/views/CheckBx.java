/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file CheckBx.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.ui.views;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

/**
 * Created by Arpit on 10/18/2015.
 */
public class CheckBx extends SwitchPreference {
    public CheckBx(Context context) {
        super(context);
    }

    public CheckBx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        // Clean listener before invoke SwitchPreference.onBindView
        ViewGroup viewGroup = (ViewGroup) view;
        clearListenerInViewGroup(viewGroup);
        super.onBindView(view);
    }

    /**
     * Clear listener in Switch for specify ViewGroup.
     *
     * @param viewGroup The ViewGroup that will need to clear the listener.
     */
    private void clearListenerInViewGroup(ViewGroup viewGroup) {
        if (null == viewGroup) {
            return;
        }

        int count = viewGroup.getChildCount();
        for (int n = 0; n < count; ++n) {
            View childView = viewGroup.getChildAt(n);
            if (childView instanceof Switch) {
                final Switch switchView = (Switch) childView;
                switchView.setOnCheckedChangeListener(null);
                return;
            } else if (childView instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) childView;
                clearListenerInViewGroup(childGroup);
            }
        }
    }

}


