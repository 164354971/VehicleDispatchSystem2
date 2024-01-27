package love.lingbao.common.exception;

import lombok.Data;

@Data
public class CommonException extends RuntimeException {
    private int code;

    public CommonException(String message, int code){
        super(message);
        this.code = code;
    }

    public CommonException(Throwable cause, int code){
        super(cause);
        this.code = code;
    }

    public CommonException(String message, Throwable cause, int code){
        super(message, cause);
        this.code = code;
    }
}
