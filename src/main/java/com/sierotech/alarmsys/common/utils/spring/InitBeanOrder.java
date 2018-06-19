package com.sierotech.alarmsys.common.utils.spring;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface InitBeanOrder
{
  public abstract Class<? extends IAutoRunBean>[] value();
}