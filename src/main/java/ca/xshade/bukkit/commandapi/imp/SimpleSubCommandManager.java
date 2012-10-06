/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi.imp;

import ca.xshade.bukkit.commandapi.CommandManagerCommand;

/**
 *
 * @author Admin
 */
public class SimpleSubCommandManager extends CommandManagerCommand {

    public SimpleSubCommandManager() {
        SubCommandManagerHelpCommand commandManagerHelpCommand = new SubCommandManagerHelpCommand(this);
        register("help", commandManagerHelpCommand);
        registerNoArgs(commandManagerHelpCommand);
    }
}
