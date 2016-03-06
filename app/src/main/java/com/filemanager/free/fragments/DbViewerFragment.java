/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file DbViewerFragment.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.filemanager.free.R;
import com.filemanager.free.activities.DbViewer;
import com.filemanager.free.services.asynctasks.DbViewerTask;

/**
 * Created by Vishal on 06-02-2015.
 */
public class DbViewerFragment extends Fragment {
    public DbViewer dbViewer;
    private String tableName;
    private View rootView;
    private Cursor schemaCursor, contentCursor;
    private RelativeLayout relativeLayout;
    public TextView loadingText;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dbViewer = (DbViewer) getActivity();

        rootView = inflater.inflate(R.layout.fragment_db_viewer, null);
        webView = (WebView) rootView.findViewById(R.id.webView1);
        loadingText = (TextView) rootView.findViewById(R.id.loadingText);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.tableLayout);
        tableName = getArguments().getString("table");
        dbViewer.setTitle(tableName);

        schemaCursor = dbViewer.sqLiteDatabase.rawQuery("PRAGMA table_info(" + tableName + ");", null);
        contentCursor = dbViewer.sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);

        new DbViewerTask(schemaCursor, contentCursor, webView, this).execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (dbViewer.theme1 == 1) {

            relativeLayout.setBackgroundColor(getResources().getColor(R.color.holo_dark_background));
            webView.setBackgroundColor(getResources().getColor(R.color.holo_dark_background));
        } else {

            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            webView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        schemaCursor.close();
        contentCursor.close();
    }
}
