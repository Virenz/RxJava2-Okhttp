package com.wr.demo.rxbus;

/**
 * Created by weir on 2020/12/22.
 * rxbus 封装对象
 */

public class Action {
    public int code;
    public Object data;

    public Action(int code, Object data) {
        this.code = code;
        this.data = data;
    }
}
