package com.happy.wdwcity;

import java.util.Comparator;

/**
 * Created by 红玫瑰 on 2017/11/3.
 */

public class PinyinComparator implements Comparator<CityData> {

    public int compare(CityData o1, CityData o2) {
        if (o1.getFristA().equals("@")
                || o2.getFristA().equals("#")) {
            return -1;
        } else if (o1.getFristA().equals("#")
                || o2.getFristA().equals("@")) {
            return 1;
        } else {
            return o1.getFristA().compareTo(o2.getFristA());
        }
    }


}
