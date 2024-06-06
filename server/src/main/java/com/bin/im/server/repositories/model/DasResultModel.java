package com.bin.im.server.repositories.model;

public class DasResultModel<V> {
    private int result;
    private V msg;

    public DasResultModel(int result, V msg) {
        this.result = result;
        this.msg = msg;
    }

    private static final int OK = 1;

    private static final int FAIL = -1;

    public static <V> DasResultModel<V> ofOk(V msg) {
        return new DasResultModel(OK,msg);
    }

    public static <V> DasResultModel<V> ofFail(V msg) {
        return new DasResultModel(FAIL,msg);
    }

    public static <V> DasResultModel<V> of(int result, V msg) {
        return new DasResultModel(result,msg);
    }


    public boolean isOk() {
        return result == OK;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public V getMsg() {
        return msg;
    }

    public void setMsg(V msg) {
        this.msg = msg;
    }
}
