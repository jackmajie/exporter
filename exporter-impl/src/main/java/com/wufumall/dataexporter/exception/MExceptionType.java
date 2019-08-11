package com.wufumall.dataexporter.exception;


/**
 * 异常类型常量
 */
public enum MExceptionType {

    /**
     * 处理成功
     */
    EXECUTE_SUCCESS("0", "处理成功"),

    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION("10", "未知异常"),

    /**
     * 数据库异常
     */
    DATA_ACCESS_EXCEPTION("11", "数据库异常"),

    /**
     * 入参错误
     */
    IN_BOUND_PARM_NULL_EXCEPTION("12", "入参错误"),

    /**
     * *****************************************************   Mysql BEGIN    ***************************************************
     */


    /**
     * *****************************************************   Mysql END    ***************************************************
     */

    /**
     * *****************************************************   Redis BEGIN    ***************************************************
     */
    REDIS_DUPLICATE_PRIMARY_KEY_ERROR("REDIS_DUPLICATE_PRIMARY_KEY_ERROR", "REDIS数据主键重复");

    /**
     * *****************************************************   Redis END    ***************************************************
     */


    private MExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码中文描述
     */
    private String message;

    
    
    /**
     * 通过枚举<code>code</code>获得枚举
     * @param code
     * @return 枚举
     */
    public static MExceptionType getByCode(String code) {
        for (MExceptionType lrcResult : values()) {
            if (lrcResult.getCode().equals(code)) {
                return lrcResult;
            }
        }
        return null;
    }

    public String toString() {
        return "[" + code + ":" + message + "]";
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}