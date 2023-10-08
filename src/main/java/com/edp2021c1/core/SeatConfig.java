package com.edp2021c1.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseUnsignedInt;

public final class SeatConfig {
    @Deprecated
    public String ot, tf, fs;
    private String rows;
    private String columns;
    private String random_between_rows;
    private String last_row_pos_can_be_choosed;
    private String person_sort_by_height;
    @Getter
    private String zz;
    @Getter
    private String separate;

    public int getRowCount() {
        return parseUnsignedInt(rows);
    }

    public int getColumnCount() {
        return parseUnsignedInt(columns);
    }

    public int getRandomBetweenRows() {
        return parseUnsignedInt(random_between_rows);
    }

    public ArrayList<Integer> getLastRowPosCanBeChoosed() {
        String[] t = last_row_pos_can_be_choosed.split(" ");
        ArrayList<Integer> i = new ArrayList<>(t.length);
        for (String s : t) {
            i.add(parseUnsignedInt(s));
        }
        return i;
    }

    public List<String> getNameList() {
        return Arrays.asList(person_sort_by_height.split(" "));
    }

    public List<String> getGroupLeaderList() {
        return Arrays.asList(zz.split(" "));
    }

    public List<Separate> getSeparatedList() {
        String[] t = separate.split("\n");
        ArrayList<Separate> s = new ArrayList<>(t.length);

        for (String m : t) {
            if (!m.isBlank()) {
                s.add(new Separate(m));
            }
        }

        return s;
    }
}
