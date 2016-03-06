/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Preffrag.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.fragments.preference_fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.filemanager.free.R;
import com.filemanager.free.utils.PreferenceUtils;
import com.jenzz.materialpreference.SwitchPreference;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Preffrag extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    int theme;
    SharedPreferences sharedPref;
    String skin;
    private int COUNT = 0;
    private Toast toast;
    com.filemanager.free.activities.Preferences preferences;
    private final static int STORAGE_PERMISSION_RC = 69;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceUtils.reset();
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        preferences = (com.filemanager.free.activities.Preferences) getActivity();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final int th1 = Integer.parseInt(sharedPref.getString("theme", "0"));
        theme = th1 == 2 ? PreferenceUtils.hourOfDay() : th1;
        /*findPreference("donate").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((com.filemanager.free.activities.Preferences) getActivity()).donate();
                return false;
            }
        });*/
        findPreference("columns").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final String[] sort = getResources().getStringArray(R.array.columns);
                MaterialDialog.Builder a = new MaterialDialog.Builder(getActivity());
                if (theme == 1) a.theme(Theme.DARK);
                a.title(R.string.gridcolumnno);
                int current = Integer.parseInt(sharedPref.getString("columns", "-1"));
                current = current == -1 ? 0 : current;
                if (current != 0) current = current - 1;
                a.items(sort).itemsCallbackSingleChoice(current, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        sharedPref.edit().putString("columns", "" + (which != 0 ? sort[which] : "" + -1)).commit();
                        dialog.dismiss();
                        return true;
                    }
                });
                a.build().show();
                return true;
            }
        });

        findPreference("theme").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] sort = getResources().getStringArray(R.array.theme);
                int current = Integer.parseInt(sharedPref.getString("theme", "0"));
                MaterialDialog.Builder a = new MaterialDialog.Builder(getActivity());
                if (theme == 1) a.theme(Theme.DARK);
                a.items(sort).itemsCallbackSingleChoice(current, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        sharedPref.edit().putString("theme", "" + which).commit();
                        dialog.dismiss();
                        restartPC(getActivity());
                        return true;
                    }
                });
                a.title(R.string.theme);
                a.build().show();
                return true;
            }
        });

        final SwitchPreference rootmode = (SwitchPreference) findPreference("rootmode");
        rootmode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean b = sharedPref.getBoolean("rootmode", false);
                if (b) {
                    if (RootTools.isAccessGiven()) {
                        rootmode.setChecked(true);

                    } else {
                        rootmode.setChecked(false);

                        Toast.makeText(getActivity(), getResources().getString(R.string.rootfailure), Toast.LENGTH_LONG).show();
                    }
                } else {
                    rootmode.setChecked(false);

                }
                return false;
            }
        });

        //Directory sort mode
        findPreference("dirontop").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] sort = getResources().getStringArray(R.array.directorysortmode);
                MaterialDialog.Builder a = new MaterialDialog.Builder(getActivity());
                if (theme == 1) a.theme(Theme.DARK);
                a.title(R.string.directorysort);
                int current = Integer.parseInt(sharedPref.getString("dirontop", "0"));
                a.items(sort).itemsCallbackSingleChoice(current, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        sharedPref.edit().putString("dirontop", "" + which).commit();
                        dialog.dismiss();
                        return true;
                    }
                });
                a.build().show();
                return true;
            }
        });

        findPreference("sortby").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] sort = getResources().getStringArray(R.array.sortby);
                int current = Integer.parseInt(sharedPref.getString("sortby", "0"));
                MaterialDialog.Builder a = new MaterialDialog.Builder(getActivity());
                if (theme == 1) a.theme(Theme.DARK);
                a.items(sort).itemsCallbackSingleChoice(current, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        sharedPref.edit().putString("sortby", "" + which).commit();
                        dialog.dismiss();
                        return true;
                    }
                });
                a.title(R.string.sortby);
                a.build().show();
                return true;
            }
        });


        // Feedback
        Preference preference3 = (Preference) findPreference("feedback");
        preference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "hoanghiep8899@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback : File Manager");
                Toast.makeText(getActivity(), getActivity().getFilesDir().getPath(), Toast.LENGTH_SHORT).show();
                File f = new File(getActivity().getExternalFilesDir("internal"), "log.txt");
                if (f.exists()) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                }
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.feedback)));
                return false;
            }
        });

        // rate
        Preference preference5 = (Preference) findPreference("rate");
        preference5.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("market://details?id=com.filemanager.free"));
                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        // Colored random color
        final SwitchPreference randomColor = (SwitchPreference) findPreference("random_checkbox");
        randomColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preferences != null) preferences.changed = 1;
                Toast.makeText(getActivity(), R.string.setRandom, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        // Colored navigation bar
        final SwitchPreference colorNavigation = (SwitchPreference) findPreference("colorednavigation");
        colorNavigation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preferences != null) {
                    preferences.changed = 1;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.confirm));
                builder.setMessage(getResources().getString(R.string.themeRestart));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartPC(getActivity());
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            colorNavigation.setEnabled(true);
        } else {
            colorNavigation.setEnabled(false);
        }


        findPreference("skin").setOnPreferenceClickListener(this);
        findPreference("fab_skin").setOnPreferenceClickListener(this);
        findPreference("icon_skin").setOnPreferenceClickListener(this);
        findPreference("extractpath").setOnPreferenceClickListener(this);
        findPreference("zippath").setOnPreferenceClickListener(this);
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


    @Override
    public boolean onPreferenceClick(final Preference preference) {
        if (preferences != null) preferences.changed = 1;
        final MaterialDialog.Builder a = new MaterialDialog.Builder(getActivity());
        a.positiveText(R.string.cancel);
        a.title(R.string.choose_color);
        if (theme == 1)
            a.theme(Theme.DARK);

        a.autoDismiss(true);
        int fab_skin_pos = sharedPref.getInt("fab_skin_color_position", 1);
        int fab_skin = Color.parseColor(PreferenceUtils.getFabColor(fab_skin_pos));
        a.positiveColor(fab_skin);
        a.neutralColor(fab_skin);
        a.neutralText(R.string.defualt);
        a.onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                sharedPref.edit().remove(preference.getKey());
                sharedPref.edit().apply();
            }
        });
        ColorAdapter adapter = null;
        String[] colors = PreferenceUtils.colors;
        List<String> arrayList = Arrays.asList(colors);
        switch (preference.getKey()) {
            case "skin":
                adapter = new ColorAdapter(getActivity(), arrayList, "skin_color_position", sharedPref.getInt("skin_color_position", 9));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.confirm));
                builder.setMessage(getResources().getString(R.string.themeRestart));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartPC(getActivity());
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                builder.show();
                break;
            case "fab_skin":
                adapter = new ColorAdapter(getActivity(), arrayList, "fab_skin_color_position", fab_skin_pos);
                break;
            case "icon_skin":
                adapter = new ColorAdapter(getActivity(), arrayList, "icon_skin_color_position", sharedPref.getInt("icon_skin_color_position", 9));
                break;
            case "zippath":
                zipPath();
                break;
            case "extractpath":
                zipPath();
                break;
            default:
                break;
        }
        if (!preference.getKey().equals("zippath") && !preference.getKey().equals("extractpath")) {
            @SuppressLint("InflateParams")
            GridView v = (GridView) getActivity().getLayoutInflater().inflate(R.layout.dialog_grid, null);
            v.setAdapter(adapter);
            a.customView(v, false);
            MaterialDialog x = a.build();
            assert (adapter) != null;
            adapter.updateMatDialog(x);
            x.show();
        }

        return false;
    }


    class ColorAdapter extends ArrayAdapter<String> {

        String pref;
        int p;
        MaterialDialog b;

        public void updateMatDialog(MaterialDialog b) {
            this.b = b;
        }

        public ColorAdapter(Context context, List<String> arrayList, String pref, int pref1) {
            super(context, R.layout.rowlayout, arrayList);
            this.pref = pref;
            this.p = pref1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder")
            View rowView = inflater.inflate(R.layout.dialog_grid_item, parent, false);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            if (position == p) imageView.setImageResource((R.drawable.ic_check_white_24dp));
            GradientDrawable gradientDrawable = (GradientDrawable) imageView.getBackground();
            gradientDrawable.setColor(Color.parseColor(getItem(position)));
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p = position;
                    notifyDataSetChanged();
                    sharedPref.edit().putInt(pref, position).commit();
                    if (b != null) b.dismiss();
                }
            });
            return rowView;
        }
    }

    public void zipPath() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
                return;
            }
            preferences.show();
        } else {
            preferences.show();
        }
    }

    public void result(String input) {
        sharedPref.edit().putString("zippath", input).commit();
        sharedPref.edit().putString("extractpath", input).commit();
    }

}