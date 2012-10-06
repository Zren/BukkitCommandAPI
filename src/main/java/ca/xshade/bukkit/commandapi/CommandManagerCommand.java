/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import ca.xshade.util.JavaUtils;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Admin
 */
public class CommandManagerCommand extends AbstractCommand {

    private CommandManager subCommandManager = new CommandManager();

    public CommandManager getSubCommandManager() {
        return subCommandManager;
    }

    /**
     * Proxied CommandManager function that can also be chained.
     *
     * @param alias
     * @param command
     * @return
     */
    public CommandManagerCommand register(String alias, Command command) {
        subCommandManager.register(alias, command);

        return this;
    }

    /**
     * Proxied CommandManager function that can also be chained.
     *
     * @param clazz
     * @return
     */
    public CommandManagerCommand register(Class<? extends Command> clazz) {
        subCommandManager.register(clazz);
        return this;
    }

    /**
     * Proxied CommandManager function that can also be chained.
     *
     * @param alias
     * @param clazz
     * @return
     */
    public CommandManagerCommand register(String alias, Class<? extends Command> clazz) {
        subCommandManager.register(alias, clazz);
        return this;
    }

    /**
     * Proxied CommandManager function that can also be chained.
     *
     * @param clazz
     * @return
     */
    public CommandManagerCommand registerMethodCommands(Class<?> clazz) {
        subCommandManager.registerMethodCommands(clazz);
        return this;
    }
    
    /**
     * Proxied CommandManager function that can also be chained.
     *
     * @param clazz
     * @return
     */
    public CommandManagerCommand registerMethodCommands(Object obj) {
        subCommandManager.registerMethodCommands(obj);
        return this;
    }
    
    /**
     * Shortcut for .registerMethodCommands(this)
     * 
     * @return 
     */
    public CommandManagerCommand registerMethodCommands() {
        subCommandManager.registerMethodCommands(this);
        return this;
    }

    /**
     * Register command that will be called when no arguments is given.
     *
     * @param command
     * @return
     */
    public CommandManagerCommand registerNoArgs(Command command) {
        return register("", command);
    }

    /**
     * Register command that will be called when no arguments is given.
     *
     * @param clazz
     * @return
     */
    public CommandManagerCommand registerNoArgs(Class<? extends Command> clazz) {
        return register("", clazz);
    }

    /**
     *
     *
     * @param sender
     * @param args
     * @return
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return subCommandManager.call("", sender, args);
        } else { // args.length >= 1
            String subCommandAlias = args[0];
            String[] subCommandArgs = JavaUtils.subArray(args, 1); // slice array [1, -1]
            return subCommandManager.call(subCommandAlias, sender, subCommandArgs);
        }
    }
}
