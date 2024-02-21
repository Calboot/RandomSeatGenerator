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

import com.edp2021c1.randomseatgenerator.util.Strings;
import lombok.Cleanup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static com.edp2021c1.randomseatgenerator.core.SeatTable.EMPTY_SEAT_PLACEHOLDER;
import static com.edp2021c1.randomseatgenerator.core.SeatTable.groupLeaderFormat;
import static com.edp2021c1.randomseatgenerator.util.CollectionUtils.*;
import static java.util.Collections.*;

/**
 * Manages the generation of seat tables.
 *
 * @author Calboot
 * @since 1.2.0
 */
public class SeatTableFactory {

    /**
     * Don't let anyone else instantiate this class.
     */
    private SeatTableFactory() {
    }

    /**
     * Generates a seat table.
     *
     * @param config used to generate the seat table.
     * @param seed   used to generate the seat table.
     * @return an instance of {@code SeatTable}.
     * @throws NullPointerException   if the config is null.
     * @throws IllegalConfigException if the config has an illegal format.
     */
    private static SeatTable generate0(final SeatConfig config, String seed)
            throws NullPointerException, IllegalConfigException {
        if (config == null) {
            throw new IllegalConfigException("Config cannot be null");
        }

        long longSeed;
        try {
            longSeed = Long.parseLong(seed);
            seed += " (integer)";
        } catch (RuntimeException e) {
            if (seed == null || seed.isEmpty()) {
                longSeed = 0;
                seed = "empty_string";
            } else {
                longSeed = Strings.longHashOf(seed);
                seed += " (string)";
            }
        }

        final Random rd = new Random(longSeed);

        // 获取配置
        final int rowCount;
        final int columnCount = config.getColumnCount();
        final int randomBetweenRows;
        final List<String> nameList = unmodifiableList(config.getNames());
        final List<String> groupLeaderList = unmodifiableList(config.getGroupLeaders());
        final boolean lucky = config.isLucky();

        // 防止lucky为true时数组越界
        final int minus = lucky ? 1 : 0;

        // 临时变量，提前声明以减少内存和计算操作
        final int peopleNum = nameList.size();

        // 防止行数过多引发无限递归
        rowCount = (int) Math.min(config.getRowCount(), Math.ceil((double) (peopleNum - minus) / columnCount));
        randomBetweenRows = Math.min(config.getRandomBetweenRows(), rowCount);

        // 临时变量，提前声明以减少内存和计算操作
        final int seatNum = rowCount * columnCount;
        final int randomPeopleCount = columnCount * randomBetweenRows;
        final int tPeopleNum = peopleNum - minus;
        final int peopleLeft = tPeopleNum > seatNum ? 0 : tPeopleNum % columnCount;

        final int forTimesMinusOne = (
                peopleNum % randomPeopleCount > columnCount
                        ? seatNum / randomPeopleCount + 1
                        : seatNum / randomPeopleCount
        ) - 1;

        final List<String> emptyRow = Arrays.asList(new String[columnCount]);
        fill(emptyRow, EMPTY_SEAT_PLACEHOLDER);

        final List<Integer> availableLastRowPos;
        {
            final List<Integer> tmp = range(1, columnCount);
            tmp.removeAll(config.getDisabledLastRowPos());
            if (tmp.size() < peopleLeft) {
                throw new IllegalConfigException("Available last row seat not enough");
            }
            availableLastRowPos = unmodifiableList(tmp);
        }

        final List<String> tNameList = new ArrayList<>(tPeopleNum);
        final List<Integer> tAvailableLastRowPos = new ArrayList<>(availableLastRowPos.size());

        String tGroupLeader;

        // 座位表数据
        final List<String> seatTable = new ArrayList<>(seatNum);
        String luckyPerson = null;

        do {
            seatTable.clear();
            tNameList.clear();
            tNameList.addAll(nameList);
            tAvailableLastRowPos.clear();
            tAvailableLastRowPos.addAll(availableLastRowPos);

            if (lucky) {
                luckyPerson = pickRandomlyAndRemove(tNameList.subList(
                        peopleNum - randomPeopleCount - peopleLeft, peopleNum
                ), rd);
            }

            for (int i = 0; i < forTimesMinusOne; i++) {
                shuffle(tNameList.subList(i * randomPeopleCount, (i + 1) * randomPeopleCount), rd);
            }
            shuffle(tNameList.subList(forTimesMinusOne * randomPeopleCount, tPeopleNum), rd);

            if (peopleLeft == 0) {
                seatTable.addAll(tNameList.subList(0, seatNum));
            } else {
                seatTable.addAll(tNameList.subList(0, seatNum - columnCount));
                seatTable.addAll(emptyRow);
                for (int i = seatNum - columnCount; i < tPeopleNum; i++) {
                    Integer t = pickRandomly(tAvailableLastRowPos, rd);
                    seatTable.set(t + seatNum - columnCount - 1, tNameList.get(i));
                    tAvailableLastRowPos.remove(t);
                }
            }
        } while (!checkSeatTableFormat(seatTable, config));

        for (int i = 0; i < columnCount; i++) {
            int t;
            do {
                t = rd.nextInt(rowCount) * columnCount + i;
            } while (!groupLeaderList.contains((tGroupLeader = seatTable.get(t))));
            seatTable.set(t, groupLeaderFormat.formatted(tGroupLeader));
        }

        return new SeatTable(seatTable, config, seed, luckyPerson);
    }

    /**
     * Generate a seat table using the specified config and the seed.
     *
     * @param config used to generate the seat table.
     * @param seed   used to generate the seat table.
     * @return an instance of {@code SeatTable}.
     * @throws NullPointerException   if the config is null.
     * @throws IllegalConfigException if the config has an illegal format, or if it
     *                                costs too much time to generate the seat table.
     */
    public static SeatTable generate(final SeatConfig config, final String seed) {
        @Cleanup final ExecutorService exe = Executors.newSingleThreadExecutor(r -> new Thread(r, "Seat Table Factory Thread"));
        try {
            return exe.submit(() -> generate0(config, seed)).get(3, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            final Throwable ex = e.getCause();
            if (ex instanceof final IllegalConfigException exx) {
                throw exx;
            }
            if (ex instanceof final RuntimeException exx) {
                throw exx;
            }
            throw new RuntimeException(ex);
        } catch (final TimeoutException e) {
            throw new IllegalConfigException(
                    "Seat table generating timeout, please check your config or use another seed"
            );
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates an empty seat table.
     *
     * @param config used to generate the empty seat table
     * @return an empty seat table
     */
    public static SeatTable generateEmpty(final SeatConfig config) {
        final List<String> seat = Arrays.asList(new String[config.getRowCount() * config.getColumnCount()]);
        fill(seat, EMPTY_SEAT_PLACEHOLDER);
        return new SeatTable(seat, config, "-", "-");
    }

    private static boolean checkSeatTableFormat(final List<String> seatTable, final SeatConfig config) throws IllegalConfigException {
        final List<String> gl = unmodifiableList(config.getGroupLeaders());
        final List<SeparatedPair> sp = unmodifiableList(config.getSeparatedPairs());
        boolean hasLeader = false;
        final int spNum = sp.size();
        final int rowCount;
        final int columnCount = config.getColumnCount();
        final int minus = config.isLucky() ? 1 : 0;
        rowCount = (int) Math.min(
                config.getRowCount(),
                Math.ceil((double) (config.getNames().size() - minus) / columnCount)
        );

        // 检查每列是否都有组长
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                hasLeader = gl.contains(seatTable.get(j * columnCount + i));
                if (hasLeader) {
                    break;
                }
            }
            if (!hasLeader) {
                return false;
            }
            hasLeader = false;
        }
        // 检查是否分开
        for (int i = 0; i < spNum; i++) {
            if (!sp.get(i).check(seatTable, columnCount)) {
                return false;
            }
        }

        return true;
    }

}
