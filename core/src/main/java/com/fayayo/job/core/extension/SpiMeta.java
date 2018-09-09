
package com.fayayo.job.core.extension;
import java.lang.annotation.*;

/**
 * 微博Motan框架代码
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SpiMeta {
    String name() default "";
}
