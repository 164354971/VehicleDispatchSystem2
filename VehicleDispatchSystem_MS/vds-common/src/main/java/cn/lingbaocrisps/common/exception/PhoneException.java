package cn.lingbaocrisps.common.exception;

public class PhoneException extends CommonException{

    public PhoneException(String message) {
        super(message, 410);
    }

    public PhoneException(String message, Throwable cause) {
        super(message, cause, 410);
    }

    public PhoneException(Throwable cause) {
        super(cause, 410);
    }
}
