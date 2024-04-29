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

package com.edp2021c1.randomseatgenerator;

import com.edp2021c1.randomseatgenerator.core.SeatTable;
import com.edp2021c1.randomseatgenerator.ui.UIUtils;
import com.edp2021c1.randomseatgenerator.util.Logging;
import com.edp2021c1.randomseatgenerator.util.Metadata;
import com.edp2021c1.randomseatgenerator.util.PathWrapper;
import com.edp2021c1.randomseatgenerator.util.Strings;
import com.edp2021c1.randomseatgenerator.util.config.SeatConfigHolder;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

/**
 * Launches the application in console mode.
 *
 * @author Calboot
 * @since 1.2.9
 */
public class ConsoleLauncher {

    /**
     * Don't let anyone else instantiate this class.
     */
    private ConsoleLauncher() {
    }

    /**
     * Launches the application.
     *
     * @param args arguments used to launch the application.
     */
    public static void launch(final List<String> args) {
        if (!Files.isReadable(Metadata.DATA_DIR)) {
            throw new RuntimeException(new IOException("Does not have read permission of the data directory"));
        }

        int i;

        // 种子，默认为随机字符串
        var seed = Strings.randomString(30);
        // 获取种子
        if ((i = args.lastIndexOf("--seed")) != -1 && i < args.size() - 1) {
            seed = args.get(i + 1);
            Logging.info("Seed set to " + seed);
        }

        // 导出路径
        var outputPath = PathWrapper.wrap(SeatTable.DEFAULT_EXPORTING_DIR.resolve("%tF.xlsx".formatted(new Date())));
        // 获取导出路径
        if ((i = args.lastIndexOf("--output-path")) != -1 && i < args.size() - 1) {
            outputPath = PathWrapper.wrap(args.get(i + 1));
            if (!outputPath.endsWith(".xlsx")) {
                Logging.error("Invalid output path: " + outputPath);
                System.exit(1);
            }
            Logging.info("Output path set to " + outputPath);
            if (outputPath.exists()) {
                Logging.warning("Something's already on the output path, will try to overwrite");
                try {
                    outputPath.delete();
                } catch (final IOException e) {
                    Logging.warning("Failed to clear the output path");
                }
            }
        }

        // 处理座位表生成配置
        var config = SeatConfigHolder.global().get().checkAndReturn();
        // 座位表生成配置文件路径，默认为当前目录下的seat_config.json
        var configPath = SeatConfigHolder.global().getConfigPath();
        // 获取配置文件路径
        if ((i = args.lastIndexOf("--config-path")) != -1 && i < args.size() - 1) {
            configPath = PathWrapper.wrap(args.get(i + 1));
            Logging.info("Config path set to " + configPath);
            try {
                val holder = SeatConfigHolder.createHolder(configPath, false);
                config = holder.get().checkAndReturn();
                holder.close();
            } catch (final IOException e) {
                throw new RuntimeException("Failed to load config from specific file", e);
            }
        }
        Logging.debug("Config path: " + configPath);

        // 生成座位表
        val seatTable = SeatTable.generate(config, seed);

        Logging.info("\n" + seatTable);

        // 导出
        seatTable.exportToChart(outputPath, UIUtils.exportWritableProperty().get());
        Logging.info("Seat table successfully exported to " + outputPath);

        // 防止某表格抽风
        System.exit(0);
    }

}
