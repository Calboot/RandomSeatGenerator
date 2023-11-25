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

package com.edp2021c1.randomseatgenerator.util;

import com.alibaba.excel.EasyExcel;
import com.edp2021c1.randomseatgenerator.core.SeatRowData;
import com.edp2021c1.randomseatgenerator.core.SeatTable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * Contains methods related to {@link SeatTable}.
 *
 * @author Calboot
 * @since 1.2.9
 */
public class SeatUtils {
    /**
     * Exports this instance to an Excel form file (.xlsx).
     *
     * @param seatTable to export to Excel document.
     * @param file      to export seat table to.
     * @throws IOException if failed to save seat table to Excel document.
     */
    public static void exportToExcelDocument(final SeatTable seatTable, final File file) throws IOException {
        Objects.requireNonNull(file);
        final Date date = new Date();
        if (!file.createNewFile()) {
            if (!(file.delete() & file.createNewFile())) {
                throw new IOException("Failed to save seat table to Excel document.");
            }
        }
        EasyExcel.write(file, SeatRowData.class).sheet(String.format("座位表-%tF", date)).doWrite(SeatRowData.fromSeat(seatTable));
        if (!file.setReadOnly()) {
            throw new IOException("Failed to save seat table to Excel document.");
        }
    }
}
