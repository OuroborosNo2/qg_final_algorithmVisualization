package com.qgstudio.vo;

import com.qgstudio.constant.ResultEnum;
import com.qgstudio.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: monitor server
 * @description: 返回值包装
 * @author: Jiao
 * @create: 2022-08-08 09:46
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result {

    /**编号*/
    private Integer code;

    /**信息*/
    private String msg;

    /**数据*/
    private Object data;


    /**
     * 没有返回值,
     * @param statusCode :状态
     */
    public Result(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    /**
     * 有返回值
     * @param statusCode :状态
     * @param data :数据
     */
    public Result(StatusCode statusCode, Object data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }


    /**
     * 成功直接传入data数据即可.
     * @param data :data数据
     */
    public Result(Object data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = data;
    }
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
