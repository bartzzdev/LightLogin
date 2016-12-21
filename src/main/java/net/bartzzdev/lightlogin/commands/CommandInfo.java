package net.bartzzdev.lightlogin.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String name();

    String description() default "No description for this command.";

    String permission() default "lightlogin.command.*";

    String[] aliases() default {};

    String usage();

    boolean playerOnly() default false;
}
