package com.fayayo.job.core.extension;

import java.lang.annotation.*;

/**
 * 微博Motan框架代码
 * 自定义的SPI机制 对于调用者来说都是透明的 替代原有的工厂模式
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {

    Scope scope() default Scope.PROTOTYPE;

}
