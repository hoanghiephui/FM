/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file DbViewer.java is part of File Manager
 * Create at 3/6/16 2:19 PM
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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.filemanager.free.R;
import com.filemanager.free.filesystem.BaseFile;
import com.filemanager.free.fragments.DbViewerFragment;
import com.filemanager.free.utils.PreferenceUtils;
import com.filemanager.free.filesystem.RootHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.util.ArrayList;

/**
 * @ Created by Vishal on 02-02-2015.
 */
public class DbViewer extends BaseActivity {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private Cursor c;
    private File pathFile;
    boolean delete = false;
    public Toolbar toolbar;
    public SQLiteDatabase sqLiteDatabase;
    public int skinStatusBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.checkStorage = false;
        super.onCreate(savedInstanceState);


        if (theme1 == 1) {
            setTheme(R.style.appCompatDark);
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.holo_dark_background));
        }
        setContentView(R.layout.activity_db_viewer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        skin = PreferenceUtils.getPrimaryColorString(Sp);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription
                    ("File Manager", ((BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap
                            .ic_launcher))
                            .getBitmap(),
                            Color.parseColor(skin));
            ((Activity) this).setTaskDescription(taskDescription);
        }
        skinStatusBar = PreferenceUtils.getStatusColor(skin);
        assert (getSupportActionBar()) != null;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(skin)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT == 20 || Build.VERSION.SDK_INT == 19) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor(skin));
            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(R.id.parentdb).getLayoutParams();
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

        String path = getIntent().getStringExtra("path");
        pathFile = new File(path);
        listView = (ListView) findViewById(R.id.listView);

        load(pathFile);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                DbViewerFragment fragment = new DbViewerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("table", arrayList.get(position));
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    private ArrayList<String> getDbTableNames(Cursor c) {
        ArrayList<String> result = new ArrayList<String>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
                result.add(c.getString(i));
            }
        }
        return result;
    }

    private void load(final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!file.canRead() && rootmode) {
                    File file1 = getExternalCacheDir();
                    if (file1 != null) file1 = getCacheDir();
                    RootTools.remount(file.getParent(), "RW");
                    assert (file1) != null;
                    RootTools.copyFile(pathFile.getPath(), new File(file1.getPath(), file.getName()).getPath(), true, false);
                    pathFile = new File(file1.getPath(), file.getName());
                    RootHelper.runAndWait("chmod 777 " + pathFile.getPath(), true);
                    delete = true;
                }
                try {
                    sqLiteDatabase = SQLiteDatabase.openDatabase(pathFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);

                    c = sqLiteDatabase.rawQuery(
                            "SELECT name FROM sqlite_master WHERE type='table'", null);
                    arrayList = getDbTableNames(c);
                    arrayAdapter = new ArrayAdapter(DbViewer.this, android.R.layout.simple_list_item_1, arrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (c != null) c.close();
        if (true) pathFile.delete();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        toolbar.setTitle(pathFile.getName());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            toolbar.setTitle(pathFile.getName());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toolbar.setTitle(pathFile.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
