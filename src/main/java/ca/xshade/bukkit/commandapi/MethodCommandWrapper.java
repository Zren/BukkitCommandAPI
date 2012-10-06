/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Admin
 */
public class MethodCommandWrapper extends AbstractCommand {

    Object classInstance;
    Method method;

    public MethodCommandWrapper(Object classInstance, Method method) {
        this.classInstance = classInstance;
        this.method = method;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        canUseCommand(sender);
        
        try {
            return (Boolean) method
                    .invoke(
                    classInstance,
                    new Object[]{sender, args});
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MethodCommandWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MethodCommandWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MethodCommandWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Method getMethod() {
        return method;
    }
}
