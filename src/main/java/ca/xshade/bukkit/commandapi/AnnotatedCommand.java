/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AnnotatedCommand {

    String value() default ""; // command alias

    String permision() default "";
    
    String noPermissionMessage() default "";

    String help() default "";

    String args() default "";

    String desc() default "";

    String requirement() default ""; // Used at front of help

    boolean showInHelp() default true;
}
