package net.bartzz.lightlogin.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo
{
    String name();

    String permission();

    String usage() default "/help";

    String description() default "No description for this command.";

    String[] aliases() default {};

    boolean playerOnly() default false;
}
