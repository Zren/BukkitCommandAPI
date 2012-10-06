/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.bukkit.commandapi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Retains insertion order;
 *
 * @author Admin
 */
public class CommandManager {

    private Set<Command> commands = new LinkedHashSet<Command>();
    private LinkedHashMap<String, Command> commandMap = new LinkedHashMap<String, Command>();

    public Set<String> getAliases() {
        return commandMap.keySet();
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Command getCommand(String alias) {
        return commandMap.get(parseCommandKey(alias));
    }

    private String parseCommandKey(String key) {
        return key.toLowerCase();
    }

    public void sortCommands() {
        LinkedHashMap<String, Command> sortedCommandMap = new LinkedHashMap<String, Command>();
        List<String> keys = new ArrayList<String>(commandMap.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sortedCommandMap.put(key, commandMap.get(key));
        }
        this.commandMap = sortedCommandMap;
    }

    /**
     * Register the specified command using the alias given.
     *
     * @param alias
     * @param command
     * @return
     */
    public CommandManager register(String alias, Command command, String... otherAliases) {
        register(alias, command);

        for (String otherAlias : otherAliases) {
            register(otherAlias, command);
        }
        return this;
    }

    private void register(String alias, Command command) {
        if (command.getName() == null) {
            command.setName(alias);
        }

        commands.add(command);
        commandMap.put(parseCommandKey(alias), command);
        command.addAlias(getCommandStack(command));
    }

    /**
     * Create new instance of specified class checks annotations and registers
     * it. Uses the AnnotatedCommand annotation to get the alias. If the
     * annotation is not present, it defaults to using the class name.
     *
     * @param clazz
     * @return
     */
    public CommandManager register(Class<? extends Command> clazz) {
        Command command;
        try {
            command = clazz.newInstance();

            String alias = "";
            if (clazz.isAnnotationPresent(AnnotatedCommand.class)) {
                AnnotatedCommand annotatedCommand = clazz.getAnnotation(AnnotatedCommand.class);
                alias = annotatedCommand.value();
            }
            if (alias.isEmpty()) {
                alias = clazz.getSimpleName();
            }

            register(alias, command);

        } catch (InstantiationException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Create new instance of specified class using the given alias and
     * registers it.
     *
     * @param alias
     * @param clazz
     * @return
     */
    public CommandManager register(String alias, Class<? extends Command> clazz, String... otherAliases) {
        Command command;
        try {
            command = clazz.newInstance();

            register(alias, command, otherAliases);

        } catch (InstantiationException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Find all methods in the specified class that are annotated with
     * MethodCommand and wrap them in MethodCommandWrapper then register them.
     * Uses the AnnotatedCommand annotation to get the alias. If the annotation
     * is not present, it defaults to using the method name.
     *
     * @param clazz
     * @return
     */
    public CommandManager registerMethodCommands(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.newInstance();
            for (Method method : clazz.getMethods()) {
                registerMethodCommand(obj, method);
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public CommandManager registerMethodCommands(Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            registerMethodCommand(obj, method);
        }
        return this;
    }

    private CommandManager registerMethodCommand(Object classInstance, Method method) {

        if (method.isAnnotationPresent(MethodCommand.class)) {
            String alias = "";
            if (method.isAnnotationPresent(AnnotatedCommand.class)) {
                AnnotatedCommand annotatedCommand = method.getAnnotation(AnnotatedCommand.class);
                alias = annotatedCommand.value();
            }
            if (alias.isEmpty()) {
                alias = method.getName();
            }

            try {
                Class<?> returnType = method.getReturnType();
                if (!returnType.equals(boolean.class)) {
                    throw new Exception("MethodCommand's must have a primitive boolean return type.");
                }

                Class<?>[] parameterType = method.getParameterTypes();
                if (parameterType.length != 2) {
                    throw new Exception("MethodCommand's have exactly two parameters.");
                }

                if (!parameterType[0].equals(CommandSender.class)) {
                    throw new Exception("MethodCommand's first parameter must be of type CommandSender.");
                }

                if (!parameterType[1].equals(String[].class)) {
                    throw new Exception("MethodCommand's second parameter must be of type String[].");
                }

                MethodCommandWrapper command = new MethodCommandWrapper(classInstance, method);
                register(alias, command);
            } catch (Exception e) {
                Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, method.getDeclaringClass() + " " + method.getName(), e);
            }
        }
        return this;
    }

    /**
     *
     * @param alias
     * @param sender
     * @param args
     * @return
     */
    public boolean call(String alias, CommandSender sender, String... args) {
        Command command = getCommand(alias);

        if (command == null) {
            return false;
        }

        return call(command, sender, args);
    }

    /**
     *
     * @param command
     * @param sender
     * @param args
     * @return
     */
    public boolean call(Command command, CommandSender sender, String... args) {
        try {
            return command.execute(sender, args);
        } catch (CommandException e) {
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            return true;
        }
    }

    public static CommandStack getCommandStack(Command command) {
        CommandStack commandStack = new CommandStack();
        Command c = command;
        NestableCommand nc;

        while (true) {
            // Add command to front
            commandStack.add(0, c);

            // Get parent command or break
            if (c instanceof NestableCommand) {
                nc = (NestableCommand) c;

                if (nc.hasParentCommand()) {
                    c = nc.getParentCommand();
                    continue;
                }
            }

            break;
        }

        return commandStack;
    }

    /**
     * Get the AnnotatedCommand annotation if present. Returns null otherwise.
     *
     * @param command
     * @return
     */
    public static AnnotatedCommand getCommandAnnotation(Command command) {
        return getCommandAnnotation(command, AnnotatedCommand.class);
    }

    public static <T extends Annotation> T getCommandAnnotation(Command command, Class<T> clazz) {
        if (command.getClass().isAnnotationPresent(clazz)) {
            return command.getClass().getAnnotation(clazz);
        } else if (command instanceof MethodCommandWrapper) {
            MethodCommandWrapper methodCommandWrapper = (MethodCommandWrapper) command;
            Method method = methodCommandWrapper.getMethod();
            if (method.isAnnotationPresent(clazz)) {
                return method.getAnnotation(clazz);
            }
        }
        return null;
    }

    public static void registerToBukkit(final CommandManager commandManager, Plugin plugin) {
        Server _server = plugin.getServer();

        // Make sure plugin is running on a Craftbukkit server.
        // Include package names here instead of importing up top so the rest doesn't break.
        assert _server instanceof org.bukkit.craftbukkit.CraftServer;
        org.bukkit.craftbukkit.CraftServer server = (org.bukkit.craftbukkit.CraftServer) _server;

        CommandMap commandMap = server.getCommandMap();

        for (final Command command : commandManager.getCommands()) {
            commandMap.register(command.getName(), new RootCommandExecutor(plugin, commandManager, command));
        }
    }
}
