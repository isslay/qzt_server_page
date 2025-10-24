package com.qzt.common.web.model;

import lombok.NoArgsConstructor;

/**
 * 返回结果类
 *
 * @author cgw
 * @date 2017/11/9 23:45
 */
@NoArgsConstructor
public class ResultModel<T> {

    public ResultModel(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 显式声明分页构造器，避免在部分环境下Lombok未生效导致的构造器缺失
    public ResultModel(int code, String message, T data, int count, int pageSize, int current) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.count = count;
        this.pageSize = pageSize;
        this.current = current;
    }

    /**
     * 状态码
     */
    public int code;

    /**
     * 描述
     */
    public String message;

    /**
     * 数据结果集
     */
    public T data;

    /**
     * 分页结果记录数
     */
    public int count;

    /**
     * 每页显示数
     */
    public int pageSize;

    /**
     * 当前第几页
     */
    public int current;
}
