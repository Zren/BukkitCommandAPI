/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import org.bukkit.entity.Player;

/**
 *
 * @author Admin
 */
public interface BukkitPlayerCommand {

    public boolean executeFromPlayer(Player player, String[] args);
}
