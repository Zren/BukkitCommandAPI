/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import java.util.Set;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Admin
 */
public interface Command {

    /**
     * CommandManager calls this function. The
     * org.bukkit.command.CommandException is catched in CommandManager to
     * simplify error messages, so just add it to the thrown list.
     *
     * @param sender
     * @param args
     * @return true if command was used.
     */
    public boolean execute(CommandSender sender, String[] args);

    public String getName();

    public void setName(String name);

    public CommandStack getPrimaryAlias();

    public Set<CommandStack> getSecondaryAliases();

    public void setPrimaryAlias(CommandStack alias);

    public void addSecondaryAlias(CommandStack alias);

    /**
     * If primary alias is not set, this will set that. If it is already set,
     * this will add a secondary alias.
     *
     * @param alias
     */
    public void addAlias(CommandStack alias);
}
