package CustomOreGen.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD})
public @interface ConsoleCommand$CommandDelegate
{

String[] names() default {};

boolean isCheat() default true;

boolean isDebugging() default true;

String desc() default "";
}
