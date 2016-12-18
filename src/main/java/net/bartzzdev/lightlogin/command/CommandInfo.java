package net.bartzzdev.lightlogin.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandInfo {

    String value();

    String permission() default "lightlogin.command.*";

    String description() default "No description for that command.";

    String[] aliases();

    String usage();

    boolean onlyPlayer() default false;
}
