/*
 * Copyright (c) 2016. by Hoang Hiep (hoanghiep8899@gmail.com)
 * This file MapEntry.java is part of File Manager
 * Create at 3/6/16 2:19 PM
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.filemanager.free.utils;

import java.util.LinkedHashMap;

/**
 * Created by Vishal on 21/12/15.
 */
public class MapEntry implements LinkedHashMap.Entry {

    private KeyMapEntry key;
    private Integer value;

    public MapEntry(KeyMapEntry key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object getKey() {
        return this.key;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Object setValue(Object object) {
        // use constructor
        return null;
    }

    public static class KeyMapEntry implements LinkedHashMap.Entry {

        private Integer key, value;

        public KeyMapEntry(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Object getKey() {
            return this.key;
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public Object setValue(Object object) {
            // use constructor
            return null;
        }
    }
}
