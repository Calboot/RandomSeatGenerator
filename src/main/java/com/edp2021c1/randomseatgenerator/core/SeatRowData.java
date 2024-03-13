/*
 * RandomSeatGenerator
 * Copyright (C) 2023  EDP2021C1
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edp2021c1.randomseatgenerator.core;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.edp2021c1.randomseatgenerator.util.exception.IllegalConfigException;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.Field;
import java.util.List;

import static com.edp2021c1.randomseatgenerator.core.SeatTable.MAX_COLUMN_COUNT;
import static com.edp2021c1.randomseatgenerator.util.CollectionUtils.buildList;
import static com.edp2021c1.randomseatgenerator.util.CollectionUtils.range;

/**
 * An instance of this class saves a row of a seat table. This class also provides a method to
 * turn a seat table into an {@code ArrayList} of this class.
 *
 * @since 1.0.1
 */

@Getter
@ExcelIgnoreUnannotated
@ColumnWidth(12)
public class SeatRowData {

    private static final List<Field> fields;

    static {
        val clazz = SeatRowData.class;
        fields = buildList(range(1, MAX_COLUMN_COUNT + 1), i -> {
            try {
                val field = clazz.getDeclaredField("c" + i);
                field.setAccessible(true);
                return field;
            } catch (final NoSuchFieldException e) {
                // Impossible situation
                throw new RuntimeException(e);
            }
        });
    }

    @ExcelProperty("Column 1")
    private final String c1 = null;
    @ExcelProperty("Column 2")
    private final String c2 = null;
    @ExcelProperty("Column 3")
    private final String c3 = null;
    @ExcelProperty("Column 4")
    private final String c4 = null;
    @ExcelProperty("Column 5")
    private final String c5 = null;
    @ExcelProperty("Column 6")
    private final String c6 = null;
    @ExcelProperty("Column 7")
    private final String c7 = null;
    @ExcelProperty("Column 8")
    private final String c8 = null;
    @ExcelProperty("Column 9")
    private final String c9 = null;
    @ExcelProperty("Column 10")
    private final String c10 = null;
    @ExcelProperty("Column 11")
    private final String c11 = null;
    @ExcelProperty("Column 12")
    private final String c12 = null;
    @ExcelProperty("Column 13")
    private final String c13 = null;
    @ExcelProperty("Column 14")
    private final String c14 = null;
    @ExcelProperty("Column 15")
    private final String c15 = null;
    @ExcelProperty("Column 16")
    private final String c16 = null;
    @ExcelProperty("Column 17")
    private final String c17 = null;
    @ExcelProperty("Column 18")
    private final String c18 = null;
    @ExcelProperty("Column 19")
    private final String c19 = null;
    @ExcelProperty("Column 20")
    private final String c20 = null;

    private final String[] data = new String[MAX_COLUMN_COUNT];

    /**
     * Instantiates this class with the given names.
     *
     * @param names name data
     */
    SeatRowData(final String... names) {
        if (names == null) {
            return;
        }
        val len = names.length;
        if (len > MAX_COLUMN_COUNT) {
            throw new IllegalConfigException(
                    "Count of people in a row cannot be larger than " + MAX_COLUMN_COUNT
            );
        }
        try {
            for (var i = 0; i < len; i++) {
                fields.get(i).set(this, names[i]);
            }
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(names, 0, data, 0, len);
    }

    /**
     * Returns name on the given index
     *
     * @param columnIndex column index
     * @return name on the given index
     */
    public String getName(final int columnIndex) {
        return data[columnIndex];
    }

}
