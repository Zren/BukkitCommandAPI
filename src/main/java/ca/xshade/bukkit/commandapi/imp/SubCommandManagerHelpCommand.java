/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi.imp;

import ca.xshade.bukkit.commandapi.AbstractCommand;
import ca.xshade.bukkit.commandapi.AnnotatedCommand;
import ca.xshade.bukkit.commandapi.Command;
import ca.xshade.bukkit.commandapi.CommandManager;
import ca.xshade.bukkit.commandapi.CommandManagerCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Admin
 */
public class SubCommandManagerHelpCommand extends AbstractCommand {

    private CommandManagerCommand commandManagerCommand;

    public SubCommandManagerHelpCommand(CommandManagerCommand commandManagerCommand) {
        this.commandManagerCommand = commandManagerCommand;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        int commandsPerPage = 7;
        int page;

        if (args.length == 0) {
            page = 1;
        } else { // args.length >= 1
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(String.format("%s must be an integer.", "page number"));
                return true;
            }
        }

        CommandManager commandManager = commandManagerCommand.getSubCommandManager();

        List<Command> commands = new ArrayList<Command>();
        for (Command command : commandManager.getCommands()) {
            commands.add(command);
        }

        int totalPages = (int) Math.ceil(commands.size() / ((double) commandsPerPage));


        // Limit bounds of user input.
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int startingIndexInclusive = (page - 1) * commandsPerPage;
        int endingIndexExclusive = startingIndexInclusive + commandsPerPage;

        if (commands.size() < endingIndexExclusive) {
            endingIndexExclusive = commands.size();
        }

        List<Command> pagedCommands = commands.subList(startingIndexInclusive, endingIndexExclusive);
        List<String> output = formatOutput(pagedCommands);
        sender.sendMessage(output.toArray(new String[output.size()]));

        return true;
    }

    public List<String> formatOutput(List<Command> commands) {
        List<String> output = new ArrayList<String>();
        CommandManager commandManager = commandManagerCommand.getSubCommandManager();
        List<Command> commandStack = CommandManager.getCommandStack(commandManagerCommand);

        String commandLinePrefix;
        int maximumCommandDepthOnPrefix = 3;
        List<String> formattedCommandPrefixArgs = new ArrayList<String>();
        if (commandStack.size() <= maximumCommandDepthOnPrefix) {
            commandLinePrefix = String.format(ChatColor.DARK_BLUE + "%s", commandStack.get(0).getName());
            for (int i = 1; i < commandStack.size(); i++) {
                commandLinePrefix += " " + String.format(ChatColor.BLUE + "%s", commandStack.get(i).getName());
            }
        } else {
            commandLinePrefix = ChatColor.DARK_BLUE + "...";
        }

        for (Command command : commands) {
            String line = "";
            if (!commandLinePrefix.isEmpty()) {
                line += commandLinePrefix + " ";
            }

            line += String.format(ChatColor.DARK_GREEN + "%s", command.getName());

            AnnotatedCommand annotatedCommand = CommandManager.getCommandAnnotation(command);

            if (annotatedCommand != null && !annotatedCommand.args().isEmpty()) {
                line += " " + String.format(ChatColor.GREEN + "%s", annotatedCommand.args());
            }

            if (annotatedCommand != null && !annotatedCommand.desc().isEmpty()) {
                line += String.format(ChatColor.GRAY + " : %s", annotatedCommand.desc());
            }

            output.add(line);
        }
        return output;
    }
}
