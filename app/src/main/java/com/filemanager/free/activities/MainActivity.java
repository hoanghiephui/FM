/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file MainActivity.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.filemanager.free.BuildConfig;
import com.filemanager.free.IMyAidlInterface;
import com.filemanager.free.Loadlistener;
import com.filemanager.free.R;
import com.filemanager.free.adapters.DrawerAdapter;
import com.filemanager.free.database.Tab;
import com.filemanager.free.database.TabHandler;
import com.filemanager.free.filesystem.BaseFile;
import com.filemanager.free.filesystem.FileUtil;
import com.filemanager.free.filesystem.HFile;
import com.filemanager.free.filesystem.RootHelper;
import com.filemanager.free.fragments.AppsList;
import com.filemanager.free.fragments.AsyncHelper;
import com.filemanager.free.fragments.Main;
import com.filemanager.free.fragments.ProcessViewer;
import com.filemanager.free.fragments.TabFragment;
import com.filemanager.free.fragments.ZipViewer;
import com.filemanager.free.services.CopyService;
import com.filemanager.free.services.DeleteTask;
import com.filemanager.free.services.asynctasks.CopyFileCheck;
import com.filemanager.free.services.asynctasks.MoveFiles;
import com.filemanager.free.ui.Layoutelements;
import com.filemanager.free.ui.dialogs.RenameBookmark;
import com.filemanager.free.ui.dialogs.RenameBookmark.BookmarkCallback;
import com.filemanager.free.ui.dialogs.SmbConnectDialog;
import com.filemanager.free.ui.dialogs.SmbConnectDialog.SmbConnectionListener;
import com.filemanager.free.ui.drawer.EntryItem;
import com.filemanager.free.ui.drawer.Item;
import com.filemanager.free.ui.drawer.SectionItem;
import com.filemanager.free.ui.icons.IconUtils;
import com.filemanager.free.ui.views.RoundedImageView;
import com.filemanager.free.ui.views.ScrimInsetsRelativeLayout;
import com.filemanager.free.utils.BookSorter;
import com.filemanager.free.utils.DataUtils;
import com.filemanager.free.utils.DataUtils.DataChangeListener;
import com.filemanager.free.utils.Futils;
import com.filemanager.free.utils.HistoryManager;
import com.filemanager.free.utils.MainActivityHelper;
import com.filemanager.free.utils.PreferenceUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class MainActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnRequestPermissionsResultCallback,
        SmbConnectionListener, DataChangeListener, BookmarkCallback,
        AsyncHelper.HelperCallbacks {

    final Pattern DIR_SEPARATOR = Pattern.compile("/");
    /* Request code used to invoke sign in user interactions. */
    static final int RC_SIGN_IN = 0;
    public Integer select;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ScrimInsetsRelativeLayout mDrawerLinear;
    public String path = "";
    public int theme;
    public ArrayList<BaseFile> COPY_PATH = null, MOVE_PATH = null;
    public FrameLayout frameLayout;
    public boolean mReturnIntent = false;

    public boolean aBoolean, openzip = false;
    public boolean mRingtonePickerIntent = false, colourednavigation = false;
    public Toolbar toolbar;
    public int skinStatusBar;
    public int storage_count = 0;
    public FloatingActionMenu floatingActionButton;
    public LinearLayout pathbar;
    public FrameLayout buttonBarFrame;
    public boolean isDrawerLocked = false;
    HistoryManager history, grid;
    Futils utils;
    MainActivity mainActivity = this;
    public DrawerAdapter adapter;
    IconUtils util;
    Context con = this;
    public MainActivityHelper mainActivityHelper;
    String zippath;
    FragmentTransaction pending_fragmentTransaction;
    String pending_path;
    boolean openprocesses = false;
    int hidemode;
    public int operation = -1;
    public ArrayList<BaseFile> oparrayList;
    public String oppathe, oppathe1;
    IMyAidlInterface aidlInterface;
    MaterialDialog materialDialog;
    String newPath = null;
    boolean backPressedToExitOnce = false;
    Toast toast = null;
    ActionBarDrawerToggle mDrawerToggle;
    Intent intent;
    GoogleApiClient mGoogleApiClient;
    View drawerHeaderLayout;
    View drawerHeaderView, indicator_layout;
    RoundedImageView drawerProfilePic;
    DisplayImageOptions displayImageOptions;
    int sdk, COUNTER = 0;
    TextView mGoogleName, mGoogleId;
    LinearLayout buttons;
    HorizontalScrollView scroll, scroll1;
    CountDownTimer timer;
    IconUtils icons;
    TabHandler tabHandler;
    RelativeLayout drawerHeaderParent;
    static final int image_selector_request_code = 31;
    // Check for user interaction for google+ api only once
    boolean mGoogleApiKey = false;
    /* A flag indicating that a PendingIntent is in progress and prevents
   * us from starting further intents.
   */
    boolean mIntentInProgress, showHidden = false;

    // string builder object variables for pathBar animations
    StringBuffer newPathBuilder, oldPathBuilder;
    AppBarLayout appBarLayout;
    // helper fragment for search task to hold activity instance even after config changes
    public AsyncHelper mAsyncHelperFragment;
    public static final String TAG_ASYNC_HELPER = "async_helper";
    public Main mainFragment;

    private static final int PATH_ANIM_START_DELAY = 0;
    private static final int PATH_ANIM_END_DELAY = 0;

    //search
    private SearchView mSearchView;
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;

    //Snackbar
    public CoordinatorLayout mCoordinatorLayout;
    //admob
    public AdView mAdView;
    public InterstitialAd mInterstitialAd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialisePreferences();
        DataUtils.registerOnDataChangedListener(this);
        setContentView(R.layout.main_toolbar);
        initialiseViews();

        tabHandler = new TabHandler(this, null, null, 1);
        utils = new Futils();

        mainActivityHelper = new MainActivityHelper(this);
        initialiseFab();

        if (mAsyncHelperFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            mAsyncHelperFragment = (AsyncHelper) fm.findFragmentByTag(TAG_ASYNC_HELPER);
        }

        history = new HistoryManager(this, "Table2");
        history.initializeTable(DataUtils.HISTORY, 0);
        history.initializeTable(DataUtils.HIDDEN, 0);
        grid = new HistoryManager(this, "listgridmodes");
        grid.initializeTable(DataUtils.LIST, 0);
        grid.initializeTable(DataUtils.GRID, 0);
        grid.initializeTable(DataUtils.BOOKS, 1);
        grid.initializeTable(DataUtils.DRIVE, 1);
        grid.initializeTable(DataUtils.SMB, 1);

        if (!Sp.getBoolean("booksadded", false)) {
            grid.make(DataUtils.BOOKS);
            Sp.edit().putBoolean("booksadded", true).commit();
        }
        DataUtils.setHiddenfiles(history.readTable(DataUtils.HIDDEN));
        DataUtils.setGridfiles(grid.readTable(DataUtils.GRID));
        DataUtils.setListfiles(grid.readTable(DataUtils.LIST));
        // initialize g+ api client as per preferences
        if (Sp.getBoolean("plus_pic", false)) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)

                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();
        }


        util = new IconUtils(Sp, this);
        icons = new IconUtils(Sp, this);

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                utils.crossfadeInverse(buttons, pathbar);
            }
        };
        path = getIntent().getStringExtra("path");
        openprocesses = getIntent().getBooleanExtra("openprocesses", false);
        try {
            intent = getIntent();
            if (intent.getStringArrayListExtra("failedOps") != null) {
                ArrayList<BaseFile> failedOps = intent.getParcelableArrayListExtra("failedOps");
                if (failedOps != null) {
                    mainActivityHelper.showFailedOperationDialog(failedOps, intent.getBooleanExtra("move", false), this);
                }
            }
            if (intent.getAction() != null)
                if (intent.getAction().equals(Intent.ACTION_GET_CONTENT)) {

                    // file picker intent
                    mReturnIntent = true;
                    Toast.makeText(this, utils.getString(con, R.string.pick_a_file), Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(RingtoneManager.ACTION_RINGTONE_PICKER)) {
                    // ringtone picker intent
                    mReturnIntent = true;
                    mRingtonePickerIntent = true;
                    Toast.makeText(this, utils.getString(con, R.string.pick_a_file), Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(Intent.ACTION_VIEW)) {

                    // zip viewer intent
                    Uri uri = intent.getData();
                    openzip = true;
                    zippath = uri.toString();
                }
        } catch (Exception ignored) {

        }
        updateDrawer();
        if (savedInstanceState == null) {

            if (openprocesses) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new ProcessViewer());
                //   transaction.addToBackStack(null);
                select = 102;
                openprocesses = false;
                //title.setText(utils.getString(con, R.string.process_viewer));
                //Commit the transaction
                transaction.commit();
                supportInvalidateOptionsMenu();
            } else {
                if (path != null && path.length() > 0) {
                    HFile file = new HFile(HFile.UNKNOWN, path);
                    file.generateMode(this);
                    if (file.isDirectory())
                        goToMain(path);
                    else {
                        goToMain("");
                        utils.openFile(new File(path), this);
                    }
                } else {
                    goToMain("");

                }
            }
        } else {
            COPY_PATH = savedInstanceState.getParcelableArrayList("COPY_PATH");
            MOVE_PATH = savedInstanceState.getParcelableArrayList("MOVE_PATH");
            oppathe = savedInstanceState.getString("oppathe");
            oppathe1 = savedInstanceState.getString("oppathe1");
            oparrayList = savedInstanceState.getParcelableArrayList("oparrayList");
            operation = savedInstanceState.getInt("operation");
            select = savedInstanceState.getInt("selectitem", 0);
            adapter.toggleChecked(select);
        }
        if (theme1 == 1) {
            mDrawerList.setBackgroundColor(ContextCompat.getColor(this, R.color.holo_dark_background));
        }
        mDrawerList.setDivider(null);
        if (!isDrawerLocked) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    mainActivity.onDrawerClosed();
                }

                public void onDrawerOpened(View drawerView) {
                    //title.setText("Amaze File Manager");
                    // creates call to onPrepareOptionsMenu()
                }
            };
            mDrawerLayout.addDrawerListener(mDrawerToggle);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_l);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            mDrawerToggle.syncState();
        }

        if (mDrawerToggle != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer_l);
        }
        //recents header color implementation
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("File Manager", ((BitmapDrawable) ContextCompat.getDrawable(con, R.mipmap.ic_launcher)).getBitmap(), Color.parseColor(skin));
            ((Activity) this).setTaskDescription(taskDescription);
        }


        //search
        searchView();
        //admob
        requestAdmob();

        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-2257698129050878/4313141545");
        requestNewInterstitial();
    }

    //search
    private void searchView() {
        //search
        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        // SearchView basic attributes  ------------------------------------------------------------
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        mSearchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        int mTheme = SearchCodes.THEME_LIGHT;
        mSearchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        mSearchView.setDivider(false);
        mSearchView.setHint("Search");
        mSearchView.setHint(R.string.search);
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setVoice(true);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(300);
        mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.hide(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                mainActivityHelper.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                floatingActionButton.hideMenuButton(true);
            }

            @Override
            public void onSearchViewClosed() {
                floatingActionButton.showMenuButton(true);
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, mTheme);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearchView.hide(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                mainActivityHelper.search(text.toString());
            }
        });

        mSearchView.setAdapter(mSearchAdapter);
    }

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        mSuggestionsList.add(new SearchItem("Google"));
        mSuggestionsList.add(new SearchItem("Android"));
        mSearchView.show(true);
    }

    /**
     * Returns all available SD-Cards in the system (include emulated)
     * <p/>
     * Warning: Hack! Based on Android source code of version 4.3 (API 18)
     * Because there is no standard way to get it.
     * TODO: Test on future Android versions 4.4+
     *
     * @return paths to all available SD-Cards in the system (include emulated)
     */


    public List<String> getStorageDirectories() {
        // Final set of paths
        final ArrayList<String> rv = new ArrayList<>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            } else {
                rv.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkStoragePermission())
            rv.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String strings[] = FileUtil.getExtSdCardPathsForActivity(this);
            for (String s : strings) {
                File f = new File(s);
                if (!rv.contains(s) && utils.canListFiles(f))
                    rv.add(s);
            }
        }
        rootmode = Sp.getBoolean("rootmode", false);
        if (rootmode)
            rv.add("/");
        File usb = getUsbDrive();
        if (usb != null && !rv.contains(usb.getPath())) rv.add(usb.getPath());
        return rv;
    }

    @Override
    public void onBackPressed() {
        if (!isDrawerLocked) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
                mDrawerLayout.closeDrawer(mDrawerLinear);
            } else {
                onbackpressed();
            }
        } else onbackpressed();
    }

    void onbackpressed() {
        try {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            String name = fragment.getClass().getName();
            if (name.contains("TabFragment")) {
                if (floatingActionButton.isOpened()) {
                    floatingActionButton.close(true);
                    utils.revealShow(findViewById(R.id.fab_bg), false);
                } else {
                    TabFragment tabFragment = ((TabFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame));
                    Fragment fragment1 = tabFragment.getTab();
                    Main main = (Main) fragment1;
                    main.goBack();
                }
            } else if (name.contains("ZipViewer")) {
                ZipViewer zipViewer = (ZipViewer) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (zipViewer.mActionMode == null) {
                    if (zipViewer.cangoBack()) {

                        zipViewer.goBack();
                    } else if (openzip) {
                        openzip = false;
                        finish();
                    } else {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_bottom);
                        fragmentTransaction.remove(zipViewer);
                        fragmentTransaction.commit();
                        supportInvalidateOptionsMenu();
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.showMenuButton(true);

                    }
                } else {
                    zipViewer.mActionMode.finish();
                }
            } else
                goToMain("");
        } catch (ClassCastException e) {
            goToMain("");
        }
    }

    public void invalidatePasteButton(MenuItem paste) {
        if (MOVE_PATH != null || COPY_PATH != null) {
            paste.setVisible(true);
        } else {
            paste.setVisible(false);
        }
    }

    public void exit() {
        if (backPressedToExitOnce) {
            finish();
            if (rootmode) {
                try {
                    RootTools.closeAllShells();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.backPressedToExitOnce = true;
            showToast(utils.getString(this, R.string.pressagain));
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }

    public void updateDrawer() {
        ArrayList<Item> list = new ArrayList<>();
        List<String> val = getStorageDirectories();
        ArrayList<String[]> books = new ArrayList<>();
        ArrayList<String[]> Servers = new ArrayList<>();
        ArrayList<String[]> accounts = new ArrayList<>();
        storage_count = 0;
        for (String file : val) {
            File f = new File(file);
            String name;
            Drawable icon1 = ContextCompat.getDrawable(this, R.drawable.ic_sd_storage_white_24dp);
            if ("/storage/emulated/legacy".equals(file) || "/storage/emulated/0".equals(file)) {
                name = getResources().getString(R.string.storage);
                icon1 = ContextCompat.getDrawable(this, R.drawable.ic_memory_white_24dp);
            } else if ("/storage/sdcard1".equals(file)) {
                name = getResources().getString(R.string.extstorage);
            } else if ("/".equals(file)) {
                name = getResources().getString(R.string.rootdirectory);
                icon1 = ContextCompat.getDrawable(this, R.drawable.ic_security_white_24dp);
            } else name = f.getName();
            if (!f.isDirectory() || f.canExecute()) {
                storage_count++;
                list.add(new EntryItem(name, file, icon1));
            }
        }
        DataUtils.setStorages(val);
        list.add(new SectionItem());
        try {
            for (String[] file : grid.readTableSecondary(DataUtils.SMB))
                Servers.add(file);
            DataUtils.setServers(Servers);
            if (Servers.size() > 0) {
                Collections.sort(Servers, new BookSorter());
                for (String[] file : Servers)
                    list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable
                            .ic_settings_remote_white_48dp)));
                list.add(new SectionItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (String[] file : grid.readTableSecondary(DataUtils.DRIVE)) {
                accounts.add(file);
            }
            DataUtils.setAccounts(accounts);
            if (accounts.size() > 0) {
                Collections.sort(accounts, new BookSorter());
                for (String[] file : accounts)
                    list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable
                            .drive)));
                list.add(new SectionItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (String[] file : grid.readTableSecondary(DataUtils.BOOKS)) {
                books.add(file);
            }
            DataUtils.setBooks(books);
            if (books.size() > 0) {
                Collections.sort(books, new BookSorter());
                for (String[] file : books)
                    list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable
                            .folder_fab)));
                list.add(new SectionItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.add(new EntryItem(getResources().getString(R.string.quick), "5", ContextCompat.getDrawable(this, R.drawable.ic_star_white_18dp)));
        list.add(new EntryItem(getResources().getString(R.string.recent), "6", ContextCompat.getDrawable(this, R.drawable.ic_history_white_48dp)));
        list.add(new EntryItem(getResources().getString(R.string.images), "0", ContextCompat.getDrawable(this, R.drawable.ic_doc_image)));
        list.add(new EntryItem(getResources().getString(R.string.videos), "1", ContextCompat.getDrawable(this, R.drawable.ic_doc_video_am)));
        list.add(new EntryItem(getResources().getString(R.string.audio), "2", ContextCompat.getDrawable(this, R.drawable.ic_doc_audio_am)));
        list.add(new EntryItem(getResources().getString(R.string.documents), "3", ContextCompat.getDrawable(this, R.drawable.ic_doc_doc_am)));
        list.add(new EntryItem(getResources().getString(R.string.apks), "4", ContextCompat.getDrawable(this, R.drawable.ic_doc_apk_grid)));
        DataUtils.setList(list);
        adapter = new DrawerAdapter(con, list, MainActivity.this, Sp);
        mDrawerList.setAdapter(adapter);
    }

    public void updateDrawer(String path) {
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... strings) {
                String path = strings[0];
                int k = 0, i = 0;
                for (Item item : DataUtils.getList()) {
                    if (!item.isSection()) {
                        if (((EntryItem) item).getPath().equals(path))
                            k = i;
                    }
                    i++;
                }
                return k;
            }

            @Override
            public void onPostExecute(Integer integers) {
                if (adapter != null)
                    adapter.toggleChecked(integers);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);

    }

    public void goToMain(String path) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //title.setText(R.string.app_name);
        TabFragment tabFragment = new TabFragment();
        if (path != null && path.length() > 0) {
            Bundle b = new Bundle();
            b.putString("path", path);
            tabFragment.setArguments(b);
        }
        transaction.replace(R.id.content_frame, tabFragment);
        // Commit the transaction
        select = 0;
        transaction.addToBackStack("tabt" + 1);
        transaction.commitAllowingStateLoss();
        setActionBarTitle(null);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.showMenuButton(true);
        if (openzip && zippath != null) {
            if (zippath.endsWith(".zip") || zippath.endsWith(".apk")) openZip(zippath);
            else {
                openRar(zippath);
            }
            zippath = null;
        }
    }

    public void selectItem(final int i) {
        ArrayList<Item> list = DataUtils.getList();
        if (!list.get(i).isSection())
            if ((select == null || select >= list.size())) {

                TabFragment tabFragment = new TabFragment();
                Bundle a = new Bundle();
                a.putString("path", ((EntryItem) list.get(i)).getPath());
                tabFragment.setArguments(a);

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, tabFragment);

                transaction.addToBackStack("tabt1" + 1);
                pending_fragmentTransaction = transaction;
                select = i;
                adapter.toggleChecked(select);
                if (!isDrawerLocked) mDrawerLayout.closeDrawer(mDrawerLinear);
                else onDrawerClosed();
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.showMenuButton(true);


            } else {
                pending_path = ((EntryItem) list.get(i)).getPath();
                if (pending_path.equals("drive")) {
                    pending_path = ((EntryItem) list.get(i)).getTitle();
                }
                select = i;
                adapter.toggleChecked(select);
                if (!isDrawerLocked) mDrawerLayout.closeDrawer(mDrawerLinear);
                else onDrawerClosed();

            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_extra, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setActionBarTitle(String title) {
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem s = menu.findItem(R.id.view);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem paste = menu.findItem(R.id.paste);
        String f = null;
        Fragment fragment;
        try {
            fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            f = fragment.getClass().getName();
        } catch (Exception e1) {
            return true;
        }
        if (f.contains("TabFragment")) {
            setActionBarTitle("File Manager");
            if (aBoolean) {
                s.setTitle(getResources().getString(R.string.gridview));
            } else {
                s.setTitle(getResources().getString(R.string.listview));
            }
            try {
                TabFragment tabFragment = (TabFragment) fragment;
                Main ma = ((Main) tabFragment.getTab());
                if (ma.IS_LIST) s.setTitle(R.string.gridview);
                else s.setTitle(R.string.listview);
                updatePath(ma.CURRENT_PATH, ma.results, ma.openMode, ma.folder_count, ma.file_count);
            } catch (Exception e) {
            }

            initiatebbar();
            if (Build.VERSION.SDK_INT >= 21) toolbar.setElevation(0);
            invalidatePasteButton(paste);
            search.setVisible(true);
            if (indicator_layout != null) indicator_layout.setVisibility(View.VISIBLE);
            menu.findItem(R.id.search).setVisible(true);
            menu.findItem(R.id.home).setVisible(true);
            menu.findItem(R.id.history).setVisible(true);
            menu.findItem(R.id.sethome).setVisible(true);

            menu.findItem(R.id.item10).setVisible(true);
            if (showHidden) menu.findItem(R.id.hiddenitems).setVisible(true);
            menu.findItem(R.id.view).setVisible(true);
            menu.findItem(R.id.extract).setVisible(false);
            invalidatePasteButton(menu.findItem(R.id.paste));
            findViewById(R.id.buttonbarframe).setVisibility(View.VISIBLE);
        } else if (f.contains("AppsList") || f.contains("ProcessViewer")) {
            appBarLayout.setExpanded(true);
            menu.findItem(R.id.sethome).setVisible(false);
            if (indicator_layout != null) indicator_layout.setVisibility(View.GONE);
            findViewById(R.id.buttonbarframe).setVisibility(View.GONE);
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.history).setVisible(false);
            menu.findItem(R.id.extract).setVisible(false);
            if (f.contains("ProcessViewer")) menu.findItem(R.id.item10).setVisible(false);
            else {
                menu.findItem(R.id.dsort).setVisible(false);
                menu.findItem(R.id.sortby).setVisible(false);
            }
            menu.findItem(R.id.hiddenitems).setVisible(false);
            menu.findItem(R.id.view).setVisible(false);
            menu.findItem(R.id.paste).setVisible(false);
        } else if (f.contains("ZipViewer")) {
            menu.findItem(R.id.sethome).setVisible(false);
            if (indicator_layout != null) indicator_layout.setVisibility(View.GONE);
            TextView textView = (TextView) mainActivity.pathbar.findViewById(R.id.fullpath);
            pathbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.history).setVisible(false);
            menu.findItem(R.id.item10).setVisible(false);
            menu.findItem(R.id.hiddenitems).setVisible(false);
            menu.findItem(R.id.view).setVisible(false);
            menu.findItem(R.id.paste).setVisible(false);
            menu.findItem(R.id.extract).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    void showToast(String message) {
        if (this.toast == null) {
            // Create toast if found null, it would he the case of first call only
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else if (this.toast.getView() == null) {
            // Toast not showing, so create new one
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else {
            // Updating toast message is showing
            this.toast.setText(message);
        }

        // Showing toast finally
        this.toast.show();
    }

    void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }

    public void back() {
        super.onBackPressed();
    }

    //// called when the user exits the action mode
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        Main ma = null;
        try {
            TabFragment tabFragment = getFragment();
            if (tabFragment != null)
                ma = (Main) tabFragment.getTab();
        } catch (Exception e) {
        }
        switch (item.getItemId()) {
            case R.id.home:
                if (ma != null)
                    ma.home();
                break;
            case R.id.history:
                if (ma != null)
                    utils.showHistoryDialog(ma);
                break;
            case R.id.sethome:
                if (ma == null) return super.onOptionsItemSelected(item);
                final Main main = ma;
                if (main.openMode != 0 && main.openMode != 3) {
                    Toast.makeText(mainActivity, R.string.not_allowed, Toast.LENGTH_SHORT).show();
                    break;
                }
                final MaterialDialog b = utils.showBasicDialog(mainActivity, fabskin, theme1, new String[]{getResources().getString(R.string.questionset), getResources().getString(R.string.setashome), getResources().getString(R.string.yes), getResources().getString(R.string.no), null});
                b.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        main.home = main.CURRENT_PATH;
                        updatepaths(main.no);
                        b.dismiss();
                    }
                });
                b.show();
                break;
            case R.id.item3:
                finish();
                break;
            case R.id.item10:
                Fragment fragment = getDFragment();
                if (fragment.getClass().getName().contains("AppsList"))
                    utils.showSortDialog((AppsList) fragment);

                break;
            case R.id.sortby:
                if (ma != null)
                    utils.showSortDialog(ma);
                break;
            case R.id.dsort:
                if (ma == null) return super.onOptionsItemSelected(item);
                String[] sort = getResources().getStringArray(R.array.directorysortmode);
                MaterialDialog.Builder a = new MaterialDialog.Builder(mainActivity);
                if (theme == 1) a.theme(Theme.DARK);
                a.title(R.string.directorysort);
                int current = Integer.parseInt(Sp.getString("dirontop", "0"));
                a.items(sort).itemsCallbackSingleChoice(current, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Sp.edit().putString("dirontop", "" + which).commit();
                        dialog.dismiss();
                        return true;
                    }
                });
                a.build().show();
                break;
            case R.id.hiddenitems:
                utils.showHiddenDialog(ma);
                break;
            case R.id.view:
                if (ma.IS_LIST) {
                    if (DataUtils.listfiles.contains(ma.CURRENT_PATH)) {
                        DataUtils.listfiles.remove(ma.CURRENT_PATH);
                        grid.removePath(ma.CURRENT_PATH, DataUtils.LIST);
                    }
                    grid.addPath(null, ma.CURRENT_PATH, DataUtils.GRID, 0);
                    DataUtils.gridfiles.add(ma.CURRENT_PATH);
                } else {
                    if (DataUtils.gridfiles.contains(ma.CURRENT_PATH)) {
                        DataUtils.gridfiles.remove(ma.CURRENT_PATH);
                        grid.removePath(ma.CURRENT_PATH, DataUtils.GRID);
                    }
                    grid.addPath(null, ma.CURRENT_PATH, DataUtils.LIST, 0);
                    DataUtils.listfiles.add(ma.CURRENT_PATH);

                }
                ma.switchView();
                break;
            case R.id.search:
                //mainActivityHelper.search();
                showSearchView();
                break;
            case R.id.paste:
                String path = ma.CURRENT_PATH;
                ArrayList<BaseFile> arrayList = new ArrayList<>();
                if (COPY_PATH != null) {
                    arrayList = COPY_PATH;
                    new CopyFileCheck(ma, path, false, mainActivity, rootmode).executeOnExecutor(AsyncTask
                            .THREAD_POOL_EXECUTOR, arrayList);
                } else if (MOVE_PATH != null) {
                    arrayList = MOVE_PATH;
                    new CopyFileCheck(ma, path, true, mainActivity, rootmode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            arrayList);
                }
                COPY_PATH = null;
                MOVE_PATH = null;

                invalidatePasteButton(item);
                break;
            case R.id.extract:
                Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (fragment1.getClass().getName().contains("ZipViewer"))
                    mainActivityHelper.extractFile(((ZipViewer) fragment1).f);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        COPY_PATH=savedInstanceState.getStringArrayList("COPY_PATH");
        MOVE_PATH=savedInstanceState.getStringArrayList("MOVE_PATH");
        oppathe = savedInstanceState.getString("oppathe");
        oppathe1 = savedInstanceState.getString("oppathe1");
        oparrayList = savedInstanceState.getStringArrayList("oparrayList");
        opnameList=savedInstanceState.getStringArrayList("opnameList");
        operation = savedInstanceState.getInt("operation");
        select = savedInstanceState.getInt("selectitem", 0);
    }*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    boolean mbound = false;

    public void bindDrive() {
        Intent i = new Intent();
        i.setClassName("com.amaze.filemanager.driveplugin", "com.amaze.filemanager.driveplugin.MainService");
        try {
            bindService((i), mConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void unbindDrive() {
        if (mbound != false)
            unbindService(mConnection);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        if (select != null)
            outState.putInt("selectitem", select);
        if (COPY_PATH != null)
            outState.putParcelableArrayList("COPY_PATH", COPY_PATH);
        if (MOVE_PATH != null)
            outState.putParcelableArrayList("MOVE_PATH", MOVE_PATH);
        if (oppathe != null) {
            outState.putString("oppathe", oppathe);
            outState.putString("oppathe1", oppathe1);
            outState.putParcelableArrayList("oparraylist", (oparrayList));
            outState.putInt("operation", operation);
        }
        mainActivity.getFragmentManager().beginTransaction().commitAllowingStateLoss();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        unregisterReceiver(mainActivityHelper.mNotificationReceiver);
        unregisterReceiver(receiver2);
        killToast();
    }

    @Override
    public void onResume() {
        super.onResume();
        //admob
        if (mAdView != null) {
            mAdView.resume();
        }

        if (materialDialog != null && !materialDialog.isShowing()) {
            materialDialog.show();
            materialDialog = null;
        }
        IntentFilter newFilter = new IntentFilter();
        newFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        newFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        newFilter.addDataScheme(ContentResolver.SCHEME_FILE);
        registerReceiver(mainActivityHelper.mNotificationReceiver, newFilter);
        registerReceiver(receiver2, new IntentFilter("general_communications"));
        if (getSupportFragmentManager().findFragmentById(R.id.content_frame)
                .getClass().getName().contains("TabFragment")) {

            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.showMenuButton(false);
        } else {

            floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.hideMenuButton(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            /*ImageView ib = (ImageView) findViewById(R.id.action_overflow);
            if (ib.getVisibility() == View.VISIBLE) {
                ib.performClick();
            }*/
            // perform your desired action here

            // return 'true' to prevent further propagation of the key event
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //admob
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        if (rootmode) {
            try {
                RootTools.closeAllShells();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DataUtils.clear();
        unbindDrive();
        if (grid != null)
            grid.end();
        if (history != null)
            history.end();
    }

    public void updatepaths(int pos) {
        TabFragment tabFragment = getFragment();
        if (tabFragment != null)
            tabFragment.updatepaths(pos);
    }

    public void openZip(String path) {
        findViewById(R.id.lin).animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_in_bottom);
        Fragment zipFragment = new ZipViewer();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        zipFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.content_frame, zipFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void openRar(String path) {
        openZip(path);
    }

    public TabFragment getFragment() {
        Fragment fragment = getDFragment();
        if (fragment == null) return null;
        if (fragment instanceof TabFragment) {
            TabFragment tabFragment = (TabFragment) fragment;
            return tabFragment;
        }
        return null;
    }

    public Fragment getDFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    public void setPagingEnabled(boolean b) {
        getFragment().mViewPager.setPagingEnabled(b);
    }

    public File getUsbDrive() {
        File parent;
        parent = new File("/storage");

        try {
            for (File f : parent.listFiles()) {
                if (f.exists() && f.getName().toLowerCase().contains("usb") && f.canExecute()) {
                    return f;
                }
            }
        } catch (Exception e) {
        }
        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute())
            return (parent);
        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute())
            return parent;

        return null;
    }

    public void refreshDrawer() {
        List<String> val = DataUtils.getStorages();
        if (val == null)
            val = getStorageDirectories();
        ArrayList<Item> list = new ArrayList<>();
        storage_count = 0;
        for (String file : val) {
            File f = new File(file);
            String name;
            Drawable icon1 = ContextCompat.getDrawable(this, R.drawable.ic_memory_white_24dp);
            if ("/storage/emulated/legacy".equals(file) || "/storage/emulated/0".equals(file)) {
                name = getResources().getString(R.string.storage);
                icon1 = ContextCompat.getDrawable(this, R.drawable.ic_sd_storage_white_24dp);
            } else if ("/storage/sdcard1".equals(file)) {
                name = getResources().getString(R.string.extstorage);
            } else if ("/".equals(file)) {
                name = getResources().getString(R.string.rootdirectory);
                icon1 = ContextCompat.getDrawable(this, R.drawable.ic_security_white_24dp);
            } else name = f.getName();
            if (!f.isDirectory() || f.canExecute()) {
                storage_count++;
                list.add(new EntryItem(name, file, icon1));
            }
        }
        list.add(new SectionItem());
        ArrayList<String[]> Servers = DataUtils.getServers();
        if (Servers != null && Servers.size() > 0) {
            for (String[] file : Servers) {
                list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable.ic_settings_remote_white_48dp)));
            }

            list.add(new SectionItem());
        }
        ArrayList<String[]> accounts = DataUtils.getAccounts();
        if (accounts != null && accounts.size() > 0) {
            Collections.sort(accounts, new BookSorter());
            for (String[] file : accounts) {
                list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable.drive)));
            }

            list.add(new SectionItem());
        }
        ArrayList<String[]> books = DataUtils.getBooks();
        if (books != null && books.size() > 0) {
            Collections.sort(books, new BookSorter());
            for (String[] file : books) {
                list.add(new EntryItem(file[0], file[1], ContextCompat.getDrawable(this, R.drawable
                        .folder_fab)));
            }
            list.add(new SectionItem());
        }
        list.add(new EntryItem(getResources().getString(R.string.quick), "5", ContextCompat.getDrawable(this, R.drawable.ic_star_white_18dp)));
        list.add(new EntryItem(getResources().getString(R.string.recent), "6", ContextCompat.getDrawable(this, R.drawable.ic_history_white_48dp)));
        list.add(new EntryItem(getResources().getString(R.string.images), "0", ContextCompat.getDrawable(this, R.drawable.ic_doc_image)));
        list.add(new EntryItem(getResources().getString(R.string.videos), "1", ContextCompat.getDrawable(this, R.drawable.ic_doc_video_am)));
        list.add(new EntryItem(getResources().getString(R.string.audio), "2", ContextCompat.getDrawable(this, R.drawable.ic_doc_audio_am)));
        list.add(new EntryItem(getResources().getString(R.string.documents), "3", ContextCompat.getDrawable(this, R.drawable.ic_doc_doc_am)));
        list.add(new EntryItem(getResources().getString(R.string.apks), "4", ContextCompat.getDrawable(this, R.drawable.ic_doc_apk_grid)));
        DataUtils.setList(list);
        adapter = new DrawerAdapter(con, list, MainActivity.this, Sp);
        mDrawerList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if user enabled g+ api from preferences
        if (mGoogleApiClient != null) {

            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Person.Image personImage;
            Person.Cover.CoverPhoto personCover;

            try {

                personImage = currentPerson.getImage();
                personCover = currentPerson.getCover().getCoverPhoto();
            } catch (Exception e) {

                personCover = null;
                personImage = null;
            }

            if (personCover != null && personImage != null) {

                String imgUrl = personImage.getUrl();

                // getting full size image
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(imgUrl);
                stringBuilder.delete(imgUrl.length() - 6, imgUrl.length());
                Log.d("G+", stringBuilder.toString());
                mGoogleName.setText(currentPerson.getDisplayName());
                mGoogleId.setText(accountName);
                // setting cover pic
                ImageLoader.getInstance().loadImage(personCover.getUrl(), displayImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        drawerHeaderParent.setBackgroundColor(Color.parseColor("#ffffff"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            drawerHeaderView.setBackground(new BitmapDrawable(con.getResources(), loadedImage));
                        } else {
                            drawerHeaderView.setBackgroundDrawable(new BitmapDrawable(con.getResources(), loadedImage));
                        }
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        drawerHeaderView.setBackgroundResource(R.drawable.amaze_header);
                        drawerHeaderParent.setBackgroundColor(Color.parseColor(skin));
                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        drawerHeaderView.setBackgroundResource(R.drawable.amaze_header);
                        drawerHeaderParent.setBackgroundColor(Color.parseColor(skin));
                    }
                });

                // setting profile pic
                ImageLoader.getInstance().loadImage(stringBuilder.toString(), displayImageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        super.onLoadingComplete(imageUri, view, loadedImage);

                        drawerProfilePic.setImageBitmap(loadedImage);
                        drawerProfilePic.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                    }
                });
            } else {
                Toast.makeText(this, getResources().getText(R.string.no_cover_photo), Toast.LENGTH_SHORT).show();
                drawerHeaderView.setBackgroundResource(R.drawable.amaze_header);
                drawerHeaderParent.setBackgroundColor(Color.parseColor(skin));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("G+", "Connection suspended");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient != null) {

                    mGoogleApiClient.connect();
                }
            }
        }).run();
    }

    public void onConnectionFailed(@NonNull final ConnectionResult result) {
        Log.d("G+", "Connection failed");
        if (!mIntentInProgress && result.hasResolution()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mIntentInProgress = true;
                        startIntentSenderForResult(result.getResolution().getIntentSender(),
                                RC_SIGN_IN, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        // The intent was canceled before it was sent.  Return to the default
                        // state and attempt to connect to get an updated ConnectionResult.
                        mIntentInProgress = false;
                        if (mGoogleApiClient != null) {

                            mGoogleApiClient.connect();
                        }
                    }
                }
            }).run();
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN && !mGoogleApiKey && mGoogleApiClient != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mIntentInProgress = false;
                    mGoogleApiKey = true;
                    // !mGoogleApiClient.isConnecting
                    if (mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    } else
                        mGoogleApiClient.disconnect();

                }
            }).run();
        } else if (requestCode == image_selector_request_code) {
            if (Sp != null && intent != null && intent.getData() != null) {
                if (Build.VERSION.SDK_INT >= 19) {
                    getContentResolver().takePersistableUriPermission(intent.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    con.getApplicationContext().grantUriPermission(BuildConfig.APPLICATION_ID, intent.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                Sp.edit().putString("drawer_header_path", intent.getData().toString()).commit();
                setDrawerHeaderBackground();
            }
        } else if (requestCode == 3) {
            String p = Sp.getString("URI", null);
            Uri oldUri = null;
            if (p != null) oldUri = Uri.parse(p);
            Uri treeUri = null;
            if (responseCode == Activity.RESULT_OK) {
                // Get Uri from Storage Access Framework.
                treeUri = intent.getData();
                // Persist URI - this is required for verification of writability.
                if (treeUri != null) {
                    Sp.edit().putString("URI", treeUri.toString()).commit();
                }
            }
            // If not confirmed SAF, or if still not writable, then revert settings.
            if (responseCode != Activity.RESULT_OK) {
               /* DialogUtil.displayError(getActivity(), R.string.message_dialog_cannot_write_to_folder_saf, false,
                        currentFolder);||!FileUtil.isWritableNormalOrSaf(currentFolder)
*/
                if (treeUri != null) {
                    Sp.edit().putString("URI", oldUri.toString()).commit();
                }
                return;
            }

            // After confirmation, update stored value of folder.
            // Persist access permissions.
            int takeFlags = intent.getFlags();
            takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (treeUri != null) {
                    getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
                }
            } else {
                if (treeUri != null) {
                    con.getApplicationContext().grantUriPermission(BuildConfig.APPLICATION_ID, treeUri, takeFlags);
                }
            }
            switch (operation) {
                case DataUtils.DELETE://deletion
                    new DeleteTask(null, mainActivity).execute((oparrayList));
                    break;
                case DataUtils.COPY://copying
                    Intent intent1 = new Intent(con, CopyService.class);
                    intent1.putExtra("FILE_PATHS", (oparrayList));
                    intent1.putExtra("COPY_DIRECTORY", oppathe);
                    startService(intent1);
                    break;
                case DataUtils.MOVE://moving
                    new MoveFiles((oparrayList), ((Main) getFragment().getTab()), ((Main) getFragment().getTab()).getActivity(), 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
                    break;
                case DataUtils.NEW_FOLDER://mkdir
                    Main ma1 = ((Main) getFragment().getTab());
                    mainActivityHelper.mkDir(RootHelper.generateBaseFile(new File(oppathe), true), ma1);
                    break;
                case DataUtils.RENAME:
                    mainActivityHelper.rename(HFile.LOCAL_MODE, (oppathe), (oppathe1), mainActivity, rootmode);
                    Main ma2 = ((Main) getFragment().getTab());
                    ma2.updateList();
                    break;
                case DataUtils.NEW_FILE:
                    Main ma3 = ((Main) getFragment().getTab());
                    mainActivityHelper.mkFile(new HFile(HFile.LOCAL_MODE, oppathe), ma3);

                    break;
                case DataUtils.EXTRACT:
                    mainActivityHelper.extractFile(new File(oppathe));
                    break;
                case DataUtils.COMPRESS:
                    mainActivityHelper.compressFiles(new File(oppathe), oparrayList);
            }
            operation = -1;
        }
    }


    public void bbar(final Main main) {
        final String text = main.CURRENT_PATH;
        try {
            buttons.removeAllViews();
            buttons.setMinimumHeight(pathbar.getHeight());
            Drawable arrow = ContextCompat.getDrawable(con, R.drawable.abc_ic_ab_back_holo_dark);
            Bundle b = utils.getPaths(text, this);
            ArrayList<String> names = b.getStringArrayList("names");
            ArrayList<String> rnames = new ArrayList<String>();

            for (int i = names.size() - 1; i >= 0; i--) {
                rnames.add(names.get(i));
            }

            ArrayList<String> paths = b.getStringArrayList("paths");
            final ArrayList<String> rpaths = new ArrayList<String>();

            for (int i = paths.size() - 1; i >= 0; i--) {
                rpaths.add(paths.get(i));
            }
            View view = new View(this);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    toolbar.getContentInsetLeft(), LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params1);
            buttons.addView(view);
            for (int i = 0; i < names.size(); i++) {
                final int k = i;
                ImageView v = new ImageView(this);
                v.setImageDrawable(arrow);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                v.setLayoutParams(params);
                final int index = i;
                if (rpaths.get(i).equals("/")) {
                    ImageButton ib = new ImageButton(this);
                    ib.setImageDrawable(icons.getRootDrawable());
                    ib.setBackgroundColor(Color.parseColor("#00ffffff"));
                    ib.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View p1) {
                            main.loadlist(("/"), false, main.openMode);
                            timer.cancel();
                            timer.start();
                        }
                    });
                    ib.setLayoutParams(params);
                    buttons.addView(ib);
                    if (names.size() - i != 1)
                        buttons.addView(v);
                } else if (isStorage(rpaths.get(i))) {
                    ImageButton ib = new ImageButton(this);
                    ib.setImageDrawable(icons.getSdDrawable());
                    ib.setBackgroundColor(Color.parseColor("#00ffffff"));
                    ib.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View p1) {
                            main.loadlist((rpaths.get(k)), false, main.openMode);
                            timer.cancel();
                            timer.start();
                        }
                    });
                    ib.setLayoutParams(params);
                    buttons.addView(ib);
                    if (names.size() - i != 1)
                        buttons.addView(v);
                } else {
                    Button button = new Button(this);
                    button.setText(rnames.get(index));
                    button.setTextColor(ContextCompat.getColor(con, android.R.color.white));
                    button.setTextSize(13);
                    button.setLayoutParams(params);
                    button.setBackgroundResource(0);
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View p1) {
                            main.loadlist((rpaths.get(k)), false, main.openMode);
                            main.loadlist((rpaths.get(k)), false, main.openMode);
                            timer.cancel();
                            timer.start();
                        }
                    });
                    button.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            File file1 = new File(rpaths.get(index));
                            copyToClipboard(MainActivity.this, file1.getPath());
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.pathcopied), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    buttons.addView(button);
                    if (names.size() - i != 1)
                        buttons.addView(v);
                }
            }

            scroll.post(new Runnable() {
                @Override
                public void run() {
                    sendScroll(scroll);
                    sendScroll(scroll1);
                }
            });

            if (buttons.getVisibility() == View.VISIBLE) {
                timer.cancel();
                timer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("button view not available");
        }
    }

    boolean isStorage(String path) {
        List<String> val = DataUtils.getStorages();
        for (String s : val)
            if (s.equals(path)) return true;
        return false;
    }

    void sendScroll(final HorizontalScrollView scrollView) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_RIGHT);
                    }
                });
            }
        }).start();
    }

    void initialisePreferences() {
        theme = Integer.parseInt(Sp.getString("theme", "0"));
        hidemode = Sp.getInt("hidemode", 0);
        showHidden = Sp.getBoolean("showHidden", false);
        skinStatusBar = (PreferenceUtils.getStatusColor(skin));
        aBoolean = Sp.getBoolean("view", true);
    }

    //set id view
    @SuppressLint("InflateParams")
    private void initialiseViews() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_frame);
        appBarLayout = (AppBarLayout) findViewById(R.id.lin);
        if (!ImageLoader.getInstance().isInited()) {

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        }
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.amaze_header)
                .showImageForEmptyUri(R.drawable.amaze_header)
                .showImageOnFail(R.drawable.amaze_header)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        buttonBarFrame = (FrameLayout) findViewById(R.id.buttonbarframe);
        buttonBarFrame.setBackgroundColor(Color.parseColor(skin));
        drawerHeaderLayout = getLayoutInflater().inflate(R.layout.drawerheader, null);
        drawerHeaderParent = (RelativeLayout) drawerHeaderLayout.findViewById(R.id.drawer_header_parent);
        drawerHeaderView = (View) drawerHeaderLayout.findViewById(R.id.drawer_header);
        drawerHeaderView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, image_selector_request_code);
                return false;
            }
        });
        drawerProfilePic = (RoundedImageView) drawerHeaderLayout.findViewById(R.id.profile_pic);
        mGoogleName = (TextView) drawerHeaderLayout.findViewById(R.id.account_header_drawer_name);
        mGoogleId = (TextView) drawerHeaderLayout.findViewById(R.id.account_header_drawer_email);
        toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        indicator_layout = findViewById(R.id.indicator_layout);
        mDrawerLinear = (ScrimInsetsRelativeLayout) findViewById(R.id.left_drawer);
        if (theme1 == 1) mDrawerLinear.setBackgroundColor(Color.parseColor("#303030"));
        else mDrawerLinear.setBackgroundColor(Color.WHITE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(Color.parseColor(skin));
        mDrawerList = (ListView) findViewById(R.id.menu_drawer);
        drawerHeaderView.setBackgroundResource(R.drawable.amaze_header);
        drawerHeaderParent.setBackgroundColor(Color.parseColor(skin));
        if (findViewById(R.id.tab_frame) != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, mDrawerLinear);
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            isDrawerLocked = true;
        }
        mDrawerList.addHeaderView(drawerHeaderLayout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        View v = findViewById(R.id.fab_bg);
        if (theme1 == 1)
            v.setBackgroundColor(Color.parseColor("#a6ffffff"));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButton.close(true);
                utils.revealShow(view, false);
            }
        });

        pathbar = (LinearLayout) findViewById(R.id.pathbar);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        scroll = (HorizontalScrollView) findViewById(R.id.scroll);
        scroll1 = (HorizontalScrollView) findViewById(R.id.scroll1);
        scroll.setSmoothScrollingEnabled(true);
        scroll1.setSmoothScrollingEnabled(true);
        ImageView divider = (ImageView) findViewById(R.id.divider1);
        if (theme1 == 0)
            divider.setImageResource(R.color.divider);
        else
            divider.setImageResource(R.color.divider_dark);

        setDrawerHeaderBackground();
        View settingsbutton = findViewById(R.id.settingsbutton);
        if (theme1 == 1) {
            settingsbutton.setBackgroundResource(R.drawable.safr_ripple_black);
            ((ImageView) settingsbutton.findViewById(R.id.settingicon)).setImageResource(R.drawable.ic_settings_white_48dp);
            ((TextView) settingsbutton.findViewById(R.id.settingtext)).setTextColor(ContextCompat.getColor(con, android.R.color.white));
        }
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Preferences.class);
                finish();
                final int enter_anim = android.R.anim.fade_in;
                final int exit_anim = android.R.anim.fade_out;
                Activity s = MainActivity.this;
                s.overridePendingTransition(exit_anim, enter_anim);
                s.finish();
                s.overridePendingTransition(enter_anim, exit_anim);
                s.startActivity(in);
            }

        });
        View appbutton = findViewById(R.id.appbutton);
        if (theme1 == 1) {
            appbutton.setBackgroundResource(R.drawable.safr_ripple_black);
            ((ImageView) appbutton.findViewById(R.id.appicon)).setImageResource(R.drawable.ic_doc_apk_white);
            ((TextView) appbutton.findViewById(R.id.apptext)).setTextColor(ContextCompat.getColor(con, android.R.color.white));
        }
        appbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.content_frame, new AppsList());
                findViewById(R.id.lin).animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                pending_fragmentTransaction = transaction2;
                if (!isDrawerLocked) mDrawerLayout.closeDrawer(mDrawerLinear);
                else onDrawerClosed();
                select = -2;
                adapter.toggleChecked(false);
            }
        });
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(skin)));


        // status bar0
        sdk = Build.VERSION.SDK_INT;

        if (sdk == 20 || sdk == 19) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor(skin));
            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(R.id.drawer_layout).getLayoutParams();
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            if (!isDrawerLocked) p.setMargins(0, config.getStatusBarHeight(), 0, 0);
        } else if (Build.VERSION.SDK_INT >= 21) {
            colourednavigation = Sp.getBoolean("colorednavigation", true);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isDrawerLocked) {
                window.setStatusBarColor((skinStatusBar));
            } else window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (colourednavigation)
                window.setNavigationBarColor(skinStatusBar);

        }
        //admob
        mAdView = (AdView) findViewById(R.id.adView);
    }

    //thiết lập admob
    public void requestAdmob() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
            }
        });
        if (mAdView.isLoading()) {
            mAdView.setVisibility(View.VISIBLE);
        }
    }

    public void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            requestNewInterstitial();
        }
    }

    //khởi tạo các nut floating
    Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

    void initialiseFab() {
        String folder_skin = PreferenceUtils.getFolderColorString(Sp);
        int fabSkinPressed = PreferenceUtils.getStatusColor(fabskin);
        int folderskin = Color.parseColor(folder_skin);
        int fabskinpressed = (PreferenceUtils.getStatusColor(folder_skin));
        floatingActionButton = (FloatingActionMenu) findViewById(R.id.menu);
        floatingActionButton.setMenuButtonColorNormal(Color.parseColor(fabskin));
        floatingActionButton.setMenuButtonColorPressed(fabSkinPressed);

        //if (theme1 == 1) floatingActionButton.setMen
        floatingActionButton.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean b) {
                View v = findViewById(R.id.fab_bg);
                if (b) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        v.setVisibility(View.GONE);
                    } else {
                        utils.revealShow(v, true);
                    }
                } else {
                    utils.revealShow(v, false);
                }
            }
        });
        //menu thêm thư mục
        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.menu_item);
        floatingActionButton1.setColorNormal(folderskin);
        floatingActionButton1.setColorPressed(fabskinpressed);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0, true);
                ;
            }
        });
        //menu thêm file
        FloatingActionButton floatingActionButton2 = (FloatingActionButton) findViewById(R.id.menu_item1);
        floatingActionButton2.setColorNormal(folderskin);
        floatingActionButton2.setColorPressed(fabskinpressed);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1, true);
            }
        });
        //add smb
        FloatingActionButton floatingActionButton3 = (FloatingActionButton) findViewById(R.id.menu_item2);
        floatingActionButton3.setColorNormal(folderskin);
        floatingActionButton3.setColorPressed(fabskinpressed);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(2, false);
            }
        });
        final FloatingActionButton floatingActionButton4 = (FloatingActionButton) findViewById(R.id.menu_item3);
        floatingActionButton4.setColorNormal(folderskin);
        floatingActionButton4.setColorPressed(fabskinpressed);
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityHelper.add(3);
                utils.revealShow(findViewById(R.id.fab_bg), false);
                floatingActionButton.close(true);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = getPackageManager();
                boolean app_installed;
                try {
                    pm.getPackageInfo("com.amaze.filemanager.driveplugin", PackageManager.GET_ACTIVITIES);
                    app_installed = true;
                } catch (PackageManager.NameNotFoundException e) {
                    app_installed = false;
                }
                if (!app_installed) floatingActionButton4.setVisibility(View.GONE);
            }
        }).run();
    }

    //thiết lập hiển thị quảng cảo và các chức năng của menu
    public void setResult(final int num, final boolean check) {
        showInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("Interstitial", "close");
                if (check) {
                    if (isSDPresent) {
                        mainActivityHelper.add(num);
                    } else {
                        Snackbar.make(mCoordinatorLayout, getString(R.string.nullSD), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    mainActivityHelper.add(num);
                }
                requestNewInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.d("Interstitial", "fail");
                if (check) {
                    if (isSDPresent) {
                        mainActivityHelper.add(num);
                    } else {
                        Snackbar.make(mCoordinatorLayout, getString(R.string.nullSD), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    mainActivityHelper.add(num);
                }
                requestNewInterstitial();
            }
        });
        if (mInterstitialAd != null && !mInterstitialAd.isLoaded()) {
            if (check) {
                if (isSDPresent) {
                    mainActivityHelper.add(num);
                } else {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.nullSD), Snackbar.LENGTH_LONG).show();
                }
            } else {
                mainActivityHelper.add(num);
            }
            requestNewInterstitial();
        }
        utils.revealShow(findViewById(R.id.fab_bg), false);
        floatingActionButton.close(true);
    }

    public void requestNewInterstitial() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public void updatePath(@NonNull final String news, boolean results, int
            openmode, int folder_count, int file_count) {

        if (news.length() == 0) return;
        if (news == null) return;
        if (openmode == 1 && news.startsWith("smb:/"))
            newPath = mainActivityHelper.parseSmbPath(news);
        else if (openmode == 2)
            newPath = mainActivityHelper.getIntegralNames(news);
        else newPath = news;
        final TextView bapath = (TextView) pathbar.findViewById(R.id.fullpath);
        final TextView animPath = (TextView) pathbar.findViewById(R.id.fullpath_anim);
        TextView textView = (TextView) pathbar.findViewById(R.id.pathname);
        if (!results) {
            textView.setText(folder_count + " " + getResources().getString(R.string.folders) + "" +
                    " " + file_count + " " + getResources().getString(R.string.files));
        } else {
            bapath.setText(R.string.searchresults);
            textView.setText(R.string.empty);
            return;
        }
        final String oldPath = bapath.getText().toString();
        if (null != oldPath && oldPath.equals(newPath)) return;

        // implement animation while setting text
        newPathBuilder = new StringBuffer().append(newPath);
        oldPathBuilder = new StringBuffer().append(oldPath);

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        if (newPath.length() > oldPath.length() &&
                newPathBuilder.delete(oldPath.length(), newPath.length()).toString().equals(oldPath) &&
                oldPath.length() != 0) {

            // navigate forward
            newPathBuilder.delete(0, newPathBuilder.length());
            newPathBuilder.append(newPath);
            newPathBuilder.delete(0, oldPath.length());
            animPath.setAnimation(slideIn);
            animPath.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            animPath.setVisibility(View.GONE);
                            bapath.setText(newPath);
                        }
                    }, PATH_ANIM_END_DELAY);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    animPath.setVisibility(View.VISIBLE);
                    animPath.setText(newPathBuilder.toString());
                    //bapath.setText(oldPath);

                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll1.fullScroll(View.FOCUS_RIGHT);
                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    //onAnimationEnd(animation);
                }
            }).setStartDelay(PATH_ANIM_START_DELAY).start();
        } else if (newPath.length() < oldPath.length() &&
                oldPathBuilder.delete(newPath.length(), oldPath.length()).toString().equals(newPath)) {

            // navigate backwards
            oldPathBuilder.delete(0, oldPathBuilder.length());
            oldPathBuilder.append(oldPath);
            oldPathBuilder.delete(0, newPath.length());
            animPath.setAnimation(slideOut);
            animPath.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animPath.setVisibility(View.GONE);
                    bapath.setText(newPath);

                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll1.fullScroll(View.FOCUS_RIGHT);
                        }
                    });
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    animPath.setVisibility(View.VISIBLE);
                    animPath.setText(oldPathBuilder.toString());
                    bapath.setText(newPath);

                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll1.fullScroll(View.FOCUS_LEFT);
                        }
                    });
                }
            }).setStartDelay(PATH_ANIM_START_DELAY).start();
        } else if (oldPath.isEmpty()) {

            // case when app starts
            // FIXME: COUNTER is incremented twice on app startup
            COUNTER++;
            if (COUNTER == 2) {

                animPath.setAnimation(slideIn);
                animPath.setText(newPath);
                animPath.animate().setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        animPath.setVisibility(View.VISIBLE);
                        bapath.setText("");
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll1.fullScroll(View.FOCUS_RIGHT);
                            }
                        });
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animPath.setVisibility(View.GONE);
                                bapath.setText(newPath);
                            }
                        }, PATH_ANIM_END_DELAY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        //onAnimationEnd(animation);
                    }
                }).setStartDelay(PATH_ANIM_START_DELAY).start();
            }

        } else {

            // completely different path
            // first slide out of old path followed by slide in of new path
            animPath.setAnimation(slideOut);
            animPath.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    super.onAnimationStart(animator);
                    animPath.setVisibility(View.VISIBLE);
                    animPath.setText(oldPath);
                    bapath.setText("");

                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll1.fullScroll(View.FOCUS_LEFT);
                        }
                    });
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);

                    //animPath.setVisibility(View.GONE);
                    animPath.setText(newPath);
                    bapath.setText("");
                    animPath.setAnimation(slideIn);

                    animPath.animate().setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animPath.setVisibility(View.GONE);
                                    bapath.setText(newPath);
                                }
                            }, PATH_ANIM_END_DELAY);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            // we should not be having anything here in path bar
                            animPath.setVisibility(View.VISIBLE);
                            bapath.setText("");
                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll1.fullScroll(View.FOCUS_RIGHT);
                                }
                            });
                        }
                    }).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    //onAnimationEnd(animation);
                }
            }).setStartDelay(PATH_ANIM_START_DELAY).start();
        }
    }

    public int dpToPx(double dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)));
        return px;
    }

    public void initiatebbar() {
        final View pathbar = findViewById(R.id.pathbar);
        TextView textView = (TextView) findViewById(R.id.fullpath);

        pathbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main m = ((Main) getFragment().getTab());
                if (m.openMode == 0) {
                    bbar(m);
                    utils.crossfade(buttons, pathbar);
                    timer.cancel();
                    timer.start();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main m = ((Main) getFragment().getTab());
                if (m.openMode == 0) {
                    bbar(m);
                    utils.crossfade(buttons, pathbar);
                    timer.cancel();
                    timer.start();
                }
            }
        });

    }

    public boolean copyToClipboard(Context context, String text) {
        try {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                    .getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("Path copied to clipboard", text);
            clipboard.setPrimaryClip(clip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void invalidateFab(int openmode) {
        if (openmode == 2) {
            floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.hideMenuButton(true);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.showMenuButton(true);
        }
    }

    public void renameBookmark(final String title, final String path) {
        if (DataUtils.containsBooks(new String[]{title, path}) != -1 || DataUtils.containsAccounts(new String[]{title, path}) != -1) {
            RenameBookmark renameBookmark = RenameBookmark.getInstance(title, path, fabskin, theme1);
            if (renameBookmark != null) {
                renameBookmark.show(getFragmentManager(), "renamedialog");
            }
        }
    }

    void onDrawerClosed() {
        if (pending_fragmentTransaction != null) {
            pending_fragmentTransaction.commit();
            pending_fragmentTransaction = null;
        }
        if (pending_path != null) {
            try {

                HFile hFile = new HFile(HFile.UNKNOWN, pending_path);
                hFile.generateMode(this);
                if (hFile.isSimpleFile()) {
                    utils.openFile(new File(pending_path), mainActivity);
                    pending_path = null;
                    return;
                }
                TabFragment m = getFragment();
                if (m == null) {
                    goToMain(pending_path);
                    return;
                }
                Main main = ((Main) m.getTab());
                if (main != null)
                    main.loadlist(pending_path, false, -1);
            } catch (ClassCastException e) {
                select = null;
                goToMain("");
            }
            pending_path = null;
        }
        supportInvalidateOptionsMenu();
    }


    @Override
    public void onNewIntent(Intent i) {
        intent = i;
        path = i.getStringExtra("path");
        if (path != null) {
            if (new File(path).isDirectory()) {
                Fragment f = getDFragment();
                if ((f.getClass().getName().contains("TabFragment"))) {
                    Main m = ((Main) getFragment().getTab());
                    m.loadlist(path, false, 0);
                } else goToMain(path);
            } else utils.openFile(new File(path), mainActivity);
        } else if (i.getStringArrayListExtra("failedOps") != null) {
            ArrayList<BaseFile> failedOps = i.getParcelableArrayListExtra("failedOps");
            if (failedOps != null) {
                mainActivityHelper.showFailedOperationDialog(failedOps, i.getBooleanExtra("move", false), this);
            }
        } else if ((openprocesses = i.getBooleanExtra("openprocesses", false))) {

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, new ProcessViewer());
            //   transaction.addToBackStack(null);
            select = 102;
            openprocesses = false;
            //title.setText(utils.getString(con, R.string.process_viewer));
            //Commit the transaction
            transaction.commitAllowingStateLoss();
            supportInvalidateOptionsMenu();
        } else if (intent.getAction() != null)
            if (intent.getAction().equals(Intent.ACTION_GET_CONTENT)) {

                // file picker intent
                mReturnIntent = true;
                Toast.makeText(this, utils.getString(con, R.string.pick_a_file), Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(RingtoneManager.ACTION_RINGTONE_PICKER)) {
                // ringtone picker intent
                mReturnIntent = true;
                mRingtonePickerIntent = true;
                Toast.makeText(this, utils.getString(con, R.string.pick_a_file), Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Intent.ACTION_VIEW)) {

                // zip viewer intent
                Uri uri = intent.getData();
                zippath = uri.toString();
                openZip(zippath);
            }
    }

    void setDrawerHeaderBackground() {
        new Thread(new Runnable() {
            public void run() {
                if (Sp.getBoolean("plus_pic", false)) return;
                String path = Sp.getString("drawer_header_path", null);
                if (path == null) return;
                try {
                    ImageLoader.getInstance().loadImage(path, displayImageOptions, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap b) {
                            if (b == null) return;
                            Drawable d = new BitmapDrawable(getResources(), b);
                            if (d == null) return;
                            drawerHeaderParent.setBackgroundDrawable(d);
                            drawerHeaderView.setBackgroundResource(R.drawable.amaze_header_2);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }

    private BroadcastReceiver receiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent i) {
            if (i.getStringArrayListExtra("failedOps") != null) {
                ArrayList<BaseFile> failedOps = i.getParcelableArrayListExtra("failedOps");
                if (failedOps != null) {
                    mainActivityHelper.showFailedOperationDialog(failedOps, i.getBooleanExtra("move", false), mainActivity);
                }
            }
        }
    };


    public void translateDrawerList(boolean down) {
        if (down)
            mDrawerList.animate().translationY(toolbar.getHeight());
        else mDrawerList.setTranslationY(0);
    }

    Loadlistener loadlistener = new Loadlistener.Stub() {
        @Override
        public void load(final List<Layoutelements> layoutelements, String driveId) throws RemoteException {
            if (layoutelements == null && DataUtils.containsAccounts(driveId) == -1) {
                DataUtils.addAcc(new String[]{driveId, driveId});
                grid.addPath(driveId, driveId, DataUtils.DRIVE, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDrawer();
                    }
                });
                unbindDrive();

            }
        }

        @Override
        public void error(final String message, final int mode) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, "Error " + message + mode, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            aidlInterface = (IMyAidlInterface.Stub.asInterface(service));
            mbound = true;
            try {
                aidlInterface.registerCallback(loadlistener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                aidlInterface.create();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mbound = false;
            Log.d("DriveConnection", "DisConnected");
            aidlInterface = null;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 77) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateDrawer();
                TabFragment tabFragment = getFragment();
                boolean b = Sp.getBoolean("needtosethome", true);
                //reset home and current paths according to new storages
                if (b) {
                    tabHandler.clear();
                    if (storage_count > 1)
                        tabHandler.addTab(new Tab(1, "", ((EntryItem) DataUtils.list.get(1)).getPath(), "/"));
                    else
                        tabHandler.addTab(new Tab(1, "", "/", "/"));
                    if (!DataUtils.list.get(0).isSection()) {
                        String pa = ((EntryItem) DataUtils.list.get(0)).getPath();
                        tabHandler.addTab(new Tab(2, "", pa, pa));
                    } else
                        tabHandler.addTab(new Tab(2, "", ((EntryItem) DataUtils.list.get(1)).getPath(), "/"));
                    if (tabFragment != null) {
                        Fragment main = tabFragment.getTab(0);
                        if (main != null)
                            ((Main) main).updateTabWithDb(tabHandler.findTab(1));
                        Fragment main1 = tabFragment.getTab(1);
                        if (main1 != null)
                            ((Main) main1).updateTabWithDb(tabHandler.findTab(2));
                    }
                    Sp.edit().putBoolean("needtosethome", false).commit();
                } else {
                    //just refresh list
                    if (tabFragment != null) {
                        Fragment main = tabFragment.getTab(0);
                        if (main != null)
                            ((Main) main).updateList();
                        Fragment main1 = tabFragment.getTab(1);
                        if (main1 != null)
                            ((Main) main1).updateList();
                    }
                }
            } else {
                Toast.makeText(this, R.string.grantfailed, Toast.LENGTH_SHORT).show();
                requestStoragePermission();
            }

        }
    }

    public void showSMBDialog(String name, String path, boolean edit) {
        if (path.length() > 0 && name.length() == 0) {
            int i = -1;
            if ((i = DataUtils.containsServer(new String[]{name, path})) != -1) {
                name = DataUtils.servers.get(i)[0];
            }
        }
        SmbConnectDialog smbConnectDialog = new SmbConnectDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("path", path);
        bundle.putBoolean("edit", edit);
        smbConnectDialog.setArguments(bundle);
        smbConnectDialog.show(getFragmentManager(), "smbdailog");

    }

    @Override
    public void addConnection(boolean edit, String name, String path, String oldname, String oldPath) {

        try {
            String[] s = new String[]{name, path};
            if (!edit) {
                TabFragment fragment = getFragment();
                if (fragment != null) {
                    Fragment fragment1 = fragment.getTab();
                    if (fragment1 != null) {
                        final Main ma = (Main) fragment1;
                        ma.loadlist(path, false, -1);
                    }
                }
                DataUtils.addServer(new String[]{name, path});
                refreshDrawer();
                grid.addPath(name, path, DataUtils.SMB, 1);
            } else {
                int i = -1;
                if ((i = DataUtils.containsServer(new String[]{oldname, oldPath})) != -1) {
                    DataUtils.removeServer(i);
                    mainActivity.grid.removePath(oldPath, DataUtils.SMB);
                }
                DataUtils.addServer(s);
                Collections.sort(DataUtils.servers, new BookSorter());
                mainActivity.refreshDrawer();
                mainActivity.grid.addPath(s[0], s[1], DataUtils.SMB, 1);
            }
        } catch (Exception e) {
            Toast.makeText(mainActivity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteConnection(String name, String path) {
        int i = -1;
        if ((i = DataUtils.containsServer(new String[]{name, path})) != -1) {
            DataUtils.removeServer(i);
            grid.removePath(path, DataUtils.SMB);
            refreshDrawer();
        }

    }

    @Override
    public void onHiddenFileAdded(String path) {
        history.addPath(null, path, DataUtils.HIDDEN, 0);
    }

    @Override
    public void onHiddenFileRemoved(String path) {
        history.removePath(path, DataUtils.HIDDEN);
    }

    @Override
    public void onHistoryAdded(String path) {
        history.addPath(null, path, DataUtils.HISTORY, 0);
    }

    @Override
    public void onBookAdded(String[] path, boolean refreshdrawer) {
        grid.addPath(path[0], path[1], DataUtils.BOOKS, 1);
        if (refreshdrawer)
            refreshDrawer();
    }

    @Override
    public void onHistoryCleared() {
        history.clear(DataUtils.HISTORY);
    }

    @Override
    public void delete(String title, String path) {
        grid.removePath(title, path, DataUtils.BOOKS);
        refreshDrawer();

    }

    @Override
    public void modify(String oldpath, String oldname, String newPath, String newname) {
        grid.rename(oldname, oldpath, newPath, newname, DataUtils.BOOKS);
        refreshDrawer();
    }

    @Override
    public void onPreExecute() {
        mainFragment.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostExecute() {

        mainFragment.onSearchCompleted();
        mainFragment.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onProgressUpdate(BaseFile val) {

        mainFragment.addSearchResult(val);
    }

    @Override
    public void onCancelled() {

    }
}