/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import java.util.LinkedHashSet;
import java.util.Set;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

/**
 *
 * @author Admin
 */
public abstract class AbstractCommand implements Command, ConsoleCommand, BukkitPlayerCommand, NestableCommand {

    private String name = null;
    private CommandStack primaryAlias = null;
    private Set<CommandStack> secondaryAliases = new LinkedHashSet<CommandStack>();
    private Command parentCommand = null;

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {
        
        // Check if sender can use command.
        canUseCommand(sender);
        
        // Branch into type safe argument methods for simpicity.
        if (sender instanceof Player) {
            return executeFromPlayer((Player) sender, args);
        } else if (sender instanceof ConsoleCommandSender) {
            return executeFromConsole((ConsoleCommandSender) sender, args);
        } else {
            return false;
        }
    }
    
    public void canUseCommand(CommandSender sender) throws CommandException {
        AnnotatedCommand annotatedCommand = CommandManager.getCommandAnnotation(this);
        
        if (annotatedCommand == null)
            return;
        
        if (annotatedCommand.permision().isEmpty())
            return;
        
        if (!hasPermission(sender, annotatedCommand.permision())) {
            if (annotatedCommand.noPermissionMessage().isEmpty())
                throw new CommandException();
            else
                throw new CommandException(annotatedCommand.noPermissionMessage());
        }
    }
    
    public boolean hasPermission(Permissible permissible, String permission) {
        return permissible.hasPermission(permission);
    }

    @Override
    public boolean executeFromConsole(ConsoleCommandSender sender, String[] args) {
        return false;
    }

    @Override
    public boolean executeFromPlayer(Player player, String[] args) {
        return false;
    }

    @Override
    public Command getParentCommand() {
        return parentCommand;
    }

    @Override
    public void setParentCommand(Command command) {
        this.parentCommand = command;
    }

    @Override
    public boolean hasParentCommand() {
        return parentCommand != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CommandStack getPrimaryAlias() {
        return primaryAlias;
    }

    @Override
    public Set<CommandStack> getSecondaryAliases() {
        return secondaryAliases;
    }

    @Override
    public void setPrimaryAlias(CommandStack primaryAlias) {
        this.primaryAlias = primaryAlias;
    }

    @Override
    public void addAlias(CommandStack alias) {
        if (primaryAlias == null)
            setPrimaryAlias(alias);
        else
            addSecondaryAlias(alias);
    }
    
    @Override
    public void addSecondaryAlias(CommandStack alias) {
        secondaryAliases.add(alias);
    }
}
