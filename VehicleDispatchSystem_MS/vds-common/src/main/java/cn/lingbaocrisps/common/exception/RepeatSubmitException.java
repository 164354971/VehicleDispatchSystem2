package cn.lingbaocrisps.common.exception;

public class RepeatSubmitException extends CommonException{

    public RepeatSubmitException(String message) {
        super(message, 409);
    }

    public RepeatSubmitException(String message, Throwable cause) {
        super(message, cause, 400);
    }

    public RepeatSubmitException(Throwable cause) {
        super(cause, 409);
    }
}
