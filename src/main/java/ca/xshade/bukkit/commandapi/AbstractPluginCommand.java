/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Admin
 */
public abstract class AbstractPluginCommand<T extends JavaPlugin> extends AbstractCommand {
    public final T plugin;

    public AbstractPluginCommand(T plugin) {
        this.plugin = plugin;
    }

    public T getPlugin() {
        return plugin;
    }
    
}
