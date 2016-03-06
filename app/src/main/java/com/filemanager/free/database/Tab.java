/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Tab.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.database;

/**
 * Created by Vishal on 9/17/2014.
 */
public class Tab {

    private int _id;
    private int _tab_no;
    private String _label;
    private String _path;
    private String _home;

    public Tab() {
        // Empty constructor
    }

    public Tab(int tab_no, String label, String path, String home) {
        this._tab_no = tab_no;
        this._label = label;
        this._path = path;
        this._home = home;

    }

    public void setTab(int tab_no) {
        this._tab_no = tab_no;
    }

    public int getTab() {
        return this._tab_no;
    }

    public void setHome(String tab_no) {
        this._home = tab_no;
    }

    public String getHome() {
        return this._home;
    }

    public void setLabel(String label) {
        this._label = label;
    }

    public String getLabel() {
        return this._label;
    }

    public void setPath(String path) {
        this._path = path;
    }

    public String getPath() {
        return this._path;
    }

    public String getOriginalPath(boolean savePaths) {
        if (savePaths) return getPath();
        else return getHome();
    }
}
