package com.cl.testapp.dili.entity;

import java.io.Serializable;
import java.util.List;

/**
 * author : happyc
 * e-mail : bafs.jy@live.com
 * time   : 2019/05/28
 * desc   : 时间表
 * version: 1.0
 */
public class DWeek implements Serializable {

    private List<List<DBangumi>> weeks;

    public List<List<DBangumi>> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<List<DBangumi>> weeks) {
        this.weeks = weeks;
    }
}
