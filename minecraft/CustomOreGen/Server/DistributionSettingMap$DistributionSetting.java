package CustomOreGen.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD})
public @interface DistributionSettingMap$DistributionSetting
{
    String name();

String info() default "";

boolean inherited() default true;
}
