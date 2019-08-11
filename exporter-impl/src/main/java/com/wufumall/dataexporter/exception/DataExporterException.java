package com.wufumall.dataexporter.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础异常
 */
public class DataExporterException extends Exception {

    private static final long serialVersionUID = -4713418939331442777L;

    private static final String EP_MSG = "";

    /**
     * 错误代码默认0
     */
    private
    @Getter
    @Setter
    String errorCode;

    /**
     * 错误信息
     */
    private
    @Getter
    @Setter
    String errorMsg;

    /**
     * Constructs a new exception with erpExceptionType.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DataExporterException(DataExporterExceptionType type) {
        this(type.getCode(), type.getMessage());
    }

    /**
     * Constructs a new exception with detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DataExporterException(String errorCode, String errorMsg) {
        super("ErrorCode[" + errorCode + "],errorMsg[" + errorMsg + "]");
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DataExporterException() {
        super();
        this.errorCode = "0";
        this.errorMsg = EP_MSG + getMessage();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DataExporterException(String message) {
        this("0", message);

    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public DataExporterException(String message, Throwable cause) {
        super(cause);
        this.errorCode = "0";
        this.errorMsg = message;
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public DataExporterException(Throwable cause) {
        super(cause);
        this.errorCode = "0";
        this.errorMsg = EP_MSG + getMessage();
    }

    /**
     * Constructs a new exception with the specified exception.  The
     * cause is initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     * @param e
     */
    public DataExporterException(Exception e) {
        this(e.getCause());
        if (e instanceof DataExporterException) {
            this.errorCode = ((DataExporterException) e).getErrorCode();
            this.errorMsg = ((DataExporterException) e).getErrorMsg();
        }
    }
}
