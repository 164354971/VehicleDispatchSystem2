package cn.lingbaocrisps.common.annotate;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    long lockTime() default 5;
}
