package com.jscxrz.zhddkk.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.jscxrz.zhddkk.enumx.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogAnnotation {

	//操作类型
	OperationEnum type();

    //模块
	ModuleEnum module() ;

    //子模块
    String subModule();

    //操作描述
    String describe() default "";
    
    //备注
    String remark() default "";
}