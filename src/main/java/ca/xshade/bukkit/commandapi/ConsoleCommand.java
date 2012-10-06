/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author Admin
 */
public interface ConsoleCommand {

    public boolean executeFromConsole(ConsoleCommandSender sender, String[] args);
}
