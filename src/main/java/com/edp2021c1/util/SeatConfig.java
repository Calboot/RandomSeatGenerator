package com.edp2021c1.util;

import java.util.ArrayList;
import java.util.Arrays;

public class SeatConfig {
    public ArrayList<String> frontRows, middleRows, backRows, groupLeaders;
    public ArrayList<Separate> separated;

    public SeatConfig(String fr, String mr, String br, String gl, String sp) {
        this.frontRows = new ArrayList<>(Arrays.asList(fr.split(" ")));
        this.middleRows = new ArrayList<>(Arrays.asList(mr.split(" ")));
        this.backRows = new ArrayList<>(Arrays.asList(br.split(" ")));
        this.groupLeaders = new ArrayList<>(Arrays.asList(gl.split(" ")));

        this.separated = new ArrayList<>();
        String[] t = sp.split("\n");
        for (String i : t) {
            if (!i.isEmpty()) this.separated.add(new Separate(i));
        }
    }
}
