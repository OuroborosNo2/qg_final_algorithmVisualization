package com.qgstudio.constant;

import lombok.*;

/**
 * @description 结果集状态码
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Getter
@AllArgsConstructor
public enum ResultEnum implements StatusCode{
    /**成功通用*/
    SUCCESS(200,"成功"),
    ARGS_ERR(400,"参数错误"),
    SYSTEM_ERR(500,"服务器出错")
    ;
    /**状态码*/
    private final int code;
    /**信息*/
    private final String msg;
}
