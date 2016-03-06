/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file BookSorter.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils;

import java.util.Comparator;

/**
 * Created by Arpit on 20-11-2015.
 */
public class BookSorter implements Comparator<String[]> {
    public BookSorter() {
    }

    @Override
    public int compare(String[] lhs, String[] rhs) {
        return 1 * lhs[0].compareToIgnoreCase(rhs[0]);
    }
}
