/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

/**
 *
 * @author Admin
 */
public interface NestableCommand extends Command {

    public void setParentCommand(Command command);

    public Command getParentCommand();

    public boolean hasParentCommand();
}
