/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Admin
 */
public class RootCommandExecutor extends org.bukkit.command.Command {
    private Plugin plugin;
    private CommandManager commandManager;
    private Command command;
    
    public RootCommandExecutor(Plugin plugin, CommandManager commandManager, Command command) {
        super(command.getName());
        this.plugin = plugin;
        this.commandManager = commandManager;
        this.command = command;
        //TODO setAliases(CommandManager.getRootSecondaryAliases(command));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!plugin.isEnabled())
            return false;
        
        return commandManager.call(commandLabel, sender, args);
    }
    
}
