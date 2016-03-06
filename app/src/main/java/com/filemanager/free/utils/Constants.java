/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file Constants.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils;

public class Constants {
    public static final String SEARCH_BROADCAST_ACTION =
            "SEARCH_BROADCAST";
    public static final String SEARCH_BROADCAST_ACTION_COMPLETED =
            "SEARCH_BROADCAST_COMPLETED";
    public static final String SEARCH_BROADCAST_ARRAY =
            "SEARCH_BROADCAST_RESULTS";
    public static final String SEARCH_BROADCAST_PRESENT_CONDITION =
            "SEARCH_BROADCAST_CURRENTLY_SEARCHING";
    /**
     * A bundle key that can be used by external apps when triggering file picking from Amaze.
     * <p/>
     * <p>This can be use as follows from an external app:</p>
     * <code>
     * Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
     * intent.setPackage("com.amaze.filemanager");
     * intent.putExtra("com.amaze.filemanager.extra.TITLE", "Select the file...");
     * <p/>
     * ... and then startActivityForResult
     * </code>
     */
    public static final String FILE_PICKER_TITLE_BUNDLE_KEY = "com.filemanager.free.extra.TITLE";

}
