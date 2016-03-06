/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Util.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Hoang Hiep on 12/10/2015.
 */
public class Util {
    private static final String CONFERENCE_YEAR_PREF_POSTFIX = "_2015";
    public static final String PREF_TOS_ACCEPTED = "pref_tos_accepted" +
            CONFERENCE_YEAR_PREF_POSTFIX;

    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }

    public static void markTosAccepted(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, newValue).apply();
    }

    public static final String CONTENT_AUTHORITY = "com.filemanager.free";
    public static final String PREF_USER_REFUSED_SIGN_IN = "pref_user_refused_sign_in" +
            CONFERENCE_YEAR_PREF_POSTFIX;

    public static boolean hasUserRefusedSignIn(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_USER_REFUSED_SIGN_IN, false);
    }

    public static void markUserRefusedSignIn(final Context context, final boolean refused) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_USER_REFUSED_SIGN_IN, refused).apply();
    }

    public static void restartPC(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }

    public static final String MARKET_APP_URL = "market://details?id=%s";
}
