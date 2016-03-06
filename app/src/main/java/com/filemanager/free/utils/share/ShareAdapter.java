/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file ShareAdapter.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.filemanager.free.R;
import com.filemanager.free.ui.icons.IconHolder;

import java.util.ArrayList;

/**
 * Created by Arpit on 01-07-2015.
 */

public class ShareAdapter extends ArrayAdapter<Intent> {

    MaterialDialog b;
    ArrayList<String> labels;
    IconHolder iconHolder;
    ArrayList<Drawable> arrayList;
    int theme;

    public void updateMatDialog(MaterialDialog b) {
        this.b = b;
    }

    public ShareAdapter(Context context, ArrayList<Intent> arrayList, ArrayList<String> labels,
                        ArrayList<Drawable> arrayList1, int theme) {
        super(context, R.layout.rowlayout, arrayList);
        this.labels = labels;
        iconHolder = new IconHolder(context, true, true);
        this.arrayList = arrayList1;
        this.theme = theme;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = (View) inflater.inflate(R.layout.simplerow, parent, false);
        TextView a = ((TextView) rowView.findViewById(R.id.firstline));
        ImageView v = (ImageView) rowView.findViewById(R.id.icon);
        if (arrayList.get(position) != null)
            v.setImageDrawable(arrayList.get(position));
        a.setVisibility(View.VISIBLE);
        a.setText(labels.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (b != null && b.isShowing()) b.dismiss();
                getContext().startActivity(getItem(position));

            }
        });
        return rowView;
    }
}
