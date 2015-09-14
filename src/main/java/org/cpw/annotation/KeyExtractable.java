package org.cpw.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * to identify instances of a type could use specified attributes or methods as a key to cache.
 * if more than one attributes specified, outcome will be an apendce. 
 * this a annotation has a higher priority comparing to 'common.cache.keySerializer'
 * ClassName: KeyExtractable
 * @description
 * @author yin_changbao
 * @Date   Sep 11, 2015
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface KeyExtractable {
	
	String[] attrs() default {"id"};
	String[] operations() default {"hashCode"};

}
