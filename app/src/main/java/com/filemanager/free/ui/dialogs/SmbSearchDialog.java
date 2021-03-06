/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file SmbSearchDialog.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.filemanager.free.R;
import com.filemanager.free.activities.MainActivity;
import com.filemanager.free.utils.Computer;
import com.filemanager.free.utils.PreferenceUtils;
import com.filemanager.free.utils.SubnetScanner;

import java.util.ArrayList;
import java.util.List;

public class SmbSearchDialog extends DialogFragment {
    Listviewadapter listviewadapter;
    ArrayList<Computer> computers = new ArrayList<>();
    SharedPreferences Sp;
    int theme, fabskin;
    SubnetScanner subnetScanner;
    Context context;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getActivity();
        Sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        theme = PreferenceUtils.getTheme(Sp);
        fabskin = Color.parseColor(PreferenceUtils.getAccentString(Sp));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (subnetScanner != null)
            subnetScanner.interrupt();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.searchingdevices);
        builder.negativeColor(fabskin);
        builder.negativeText(R.string.cancel);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (subnetScanner != null)
                    subnetScanner.interrupt();
                dismiss();
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (subnetScanner != null)
                    subnetScanner.interrupt();
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    dismiss();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.showSMBDialog("", "", false);
                }
            }
        });
        builder.positiveText(getResources().getString(R.string.useIpCustom));
        builder.positiveColor(fabskin);
        computers.add(new Computer("-1", "-1"));
        listviewadapter = new Listviewadapter(getActivity(), R.layout.smb_computers_row, computers);
        subnetScanner = new SubnetScanner(getActivity());
        subnetScanner.setObserver(new SubnetScanner.ScanObserver() {
            @Override
            public void computerFound(final Computer computer) {
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!computers.contains(computer))
                                computers.add(computers.size() - 1, computer);
                            listviewadapter.notifyDataSetChanged();
                        }
                    });
            }

            @Override
            public void searchFinished() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (computers.size() == 1) {
                                dismiss();
                                Snackbar.make(MainActivity.mCoordinatorLayout, R.string.nodevicefound, Snackbar.LENGTH_LONG).show();
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.showSMBDialog("", "", false);
                                return;
                            }
                            computers.remove(computers.size() - 1);
                            listviewadapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        subnetScanner.start();

        builder.adapter(listviewadapter, new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

            }
        });
        return builder.build();
    }

    private class Listviewadapter extends ArrayAdapter<Computer> {
        LayoutInflater mInflater;

        public Listviewadapter(Context context, int resource, List<Computer> objects) {
            super(context, resource, objects);
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }


        private class ViewHolder {
            ImageView image;
            TextView txtTitle;
            TextView txtDesc;
            // RelativeLayout row;
        }


        @SuppressLint({"ViewHolder", "InflateParams"})
        public View getView(int position, View convertView, ViewGroup parent) {
            Computer f = getItem(position);
            if (f.addr.equals("-1")) {
                ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
                progressBar.setIndeterminate(true);
                progressBar.setBackgroundDrawable(null);
                return progressBar;
            }
            View view;
            final int p = position;
            view = mInflater.inflate(R.layout.smb_computers_row, null);
            final ViewHolder holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.firstline);
            holder.image = (ImageView) view.findViewById(R.id.icon);
            holder.txtDesc = (TextView) view.findViewById(R.id.secondLine);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subnetScanner != null)
                        subnetScanner.interrupt();
                    if (getActivity() != null && getActivity() instanceof MainActivity) {
                        dismiss();
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.showSMBDialog(listviewadapter.getItem(p).name, listviewadapter.getItem(p).addr, false);
                    }
                }
            });
            if (holder != null && holder.txtTitle != null) {
                holder.txtTitle.setText(f.name);
                holder.image.setImageResource(R.drawable.ic_settings_remote_white_48dp);
                if (theme == 0)
                    holder.image.setColorFilter(Color.parseColor("#666666"));
                holder.txtDesc.setText(f.addr);
            }

            return view;
        }
    }
}
