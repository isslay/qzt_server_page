package com.qzt.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomId {

    public static Long getRandomId() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        return Long.parseLong(dateFormat.format(date) + (int) (Math.random() * 900));
    }

    public static Date getCreateDate() {
        return new Date();
    }

}
