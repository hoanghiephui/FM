/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Preferences.java is part of File Manager
 * Create at 3/6/16 9:31 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.filemanager.free.BuildConfig;
import com.filemanager.free.R;
import com.filemanager.free.fragments.preference_fragments.Preffrag;
import com.filemanager.free.utils.PreferenceUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.sufficientlysecure.donations.DonationsFragment;

import java.io.File;

public class Preferences extends BaseActivity implements
        FolderChooserDialog.FolderCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    int skinStatusBar;
    int select = 0;
    public int changed = 0;
    private final static int STORAGE_PERMISSION_RC = 69;
    private Handler mHandler;
    public static final int REQUEST_CODE_ENABLE = 11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences Sp = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefsfrag);
        mHandler = new Handler();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        skin = PreferenceUtils.getSkinColor(Sp.getInt("skin_color_position", 9));
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("File Manager", ((BitmapDrawable) ContextCompat.getDrawable(getBaseContext(), R.mipmap.ic_launcher)).getBitmap(), Color.parseColor(skin));
            setTaskDescription(taskDescription);
        }
        skinStatusBar = PreferenceUtils.getStatusColor(skin);
        setSupportActionBar(toolbar);
        assert (getSupportActionBar()) != null;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(skin)));
        int sdk = Build.VERSION.SDK_INT;

        if (sdk == 20 || sdk == 19) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor(skin));

            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(R.id.preferences).getLayoutParams();
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            p.setMargins(0, config.getStatusBarHeight(), 0, 0);
        } else if (Build.VERSION.SDK_INT >= 21) {
            boolean colourednavigation = Sp.getBoolean("colorednavigation", true);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor((PreferenceUtils.getStatusColor(skin)));
            if (colourednavigation)
                window.setNavigationBarColor((PreferenceUtils.getStatusColor(skin)));

        }
        selectItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    @Override
    public void onBackPressed() {
        if (select == 1 && changed == 1)
            restartPC(this);
        else if (select == 1 || select == 2) {
            selectItem(0);
        } else {
            Intent in = new Intent(Preferences.this, MainActivity.class);
            in.setAction(Intent.ACTION_MAIN);
            final int enter_anim = android.R.anim.fade_in;
            final int exit_anim = android.R.anim.fade_out;
            Activity activity = this;
            activity.overridePendingTransition(enter_anim, exit_anim);
            activity.finish();
            activity.overridePendingTransition(enter_anim, exit_anim);
            activity.startActivity(in);
        }
        MainActivity.showInterstitial();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                if (select == 1 && changed == 1)
                    restartPC(this);
                else if (select == 1) {
                    selectItem(0);
                } else {
                    Intent in = new Intent(Preferences.this, MainActivity.class);
                    in.setAction(Intent.ACTION_MAIN);
                    final int enter_anim = android.R.anim.fade_in;
                    final int exit_anim = android.R.anim.fade_out;
                    Activity activity = this;
                    activity.overridePendingTransition(enter_anim, exit_anim);
                    activity.finish();
                    activity.overridePendingTransition(enter_anim, exit_anim);
                    activity.startActivity(in);
                }
                MainActivity.showInterstitial();
                return true;

        }
        return true;
    }

    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo9hApxv/pAZAUQshPiQEX2L6ZPoifEUw9fuisAxZFOHpW83mcRbWDmcqdouc1JqHak0/J0tZEBMc4SqSngE+xK3NxS2Mf4uwXPhD40bC1InAKtGNOJllGXKS8RRmk2FDD33ZHrdFUcJuKL6EIXHl1bwFIrd9rvr5VRt1mvXGj+iSdZe1WQpLex/f/s+eEe1B046Z/U6YNoPvP7xFezbZr3F1kRsx4WD5fcrTdptn38BXcwabJ1T/c2fLuGjUCZbycrggqJS47zEJ+SJhJpQUJWabq0sEYAHlyVN0CR0AVTd4/+y4+hFuPaYkhT4u/H5Uvd78u0VQdljzDs4w8mS++QIDAQAB";
    private static final String[] GOOGLE_CATALOG = new String[]{"ntpsync.donation", "ntpsync.donation.2", "ntpsync.donation.13"};

    public void donate() {
        try {
            getFragmentManager().beginTransaction().remove(p).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] s = new String[]{"Minimal Donation", "Medium Donation", "High Donation"};
        DonationsFragment donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
                s, false, null, null,
                null, false, null, null, false, null);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.prefsfragment, donationsFragment);
        transaction.commit();
        assert (getSupportActionBar()) != null;
        getSupportActionBar().setTitle(R.string.donate);

    }

    public void restartPC(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }

    Preffrag p;

    public void selectItem(int i) {
        switch (i) {
            case 0:
                p = new Preffrag();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.prefsfragment, p);
                transaction.commit();
                select = 0;
                assert (getSupportActionBar()) != null;
                getSupportActionBar().setTitle(R.string.setting);
                break;
            default:
                break;
        }
    }


    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        Toast.makeText(this, folder.getAbsolutePath(), Toast.LENGTH_LONG).show();
        p.result(folder.getAbsolutePath());
    }

    public void show() {
        new FolderChooserDialog.Builder(Preferences.this)
                .chooseButton(R.string.md_choose_label)
                .tag("optional-identifier")
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_RC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show();
                    }
                }, 1000);
            } else {
                Toast.makeText(this, "The folder or file chooser will not work without permission to read external storage.", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                //p.getResultPin(REQUEST_CODE_ENABLE);
                break;
        }
    }

}
