package ca.xshade.bukkit.commandapi;

import ca.xshade.bukkit.commandapi.imp.SimpleSubCommandManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    static final List<Class<?>> classes = new ArrayList<Class<?>>();

    static {
        classes.add(ACommand.class);
        classes.add(BCommand.class);
        classes.add(CCommand.class);
        classes.add(DCommand.class);
        classes.add(ECommand.class);
        classes.add(FCommand.class);
        classes.add(GCommand.class);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        new AppTest("a").run();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testSub() {
        CommandManager commandManager = new CommandManager() {
            public CommandManager register(String alias, Command command) {
                System.out.println("RegisteredSub > " + alias);
                super.register(alias, command);
                return this;
            }
        };
        commandManager
                .register("a", new SimpleSubCommandManager()
                    .register("a", new SimpleSubCommandManager()
                        .register("b", BCommand.class)
                        .register(CCommand.class)
                    )
                    .register("b", BCommand.class)
                    .register(CCommand.class)
                )
                .register("b", BCommand.class);
        System.out.println(commandManager.call("a", sender, new String[]{}));
        System.out.println(commandManager.call("a", sender, new String[]{"a"}));
        System.out.println(commandManager.call("a", sender, new String[]{"a", "a"}));
        System.out.println(commandManager.call("a", sender, new String[]{"a", "b"}));
        System.out.println(commandManager.call("a", sender, new String[]{"b"}));
        System.out.println(commandManager.call("b", sender, new String[]{}));
    }

    public void testApp() throws Exception {
        for (Class<?> clazz : classes) {
            CommandManager commandManager = new CommandManager() {
                public CommandManager register(String alias, Command command) {
                    System.out.println("Registered > " + alias);
                    super.register(alias, command);
                    return this;
                }
            };
            if (Command.class.isAssignableFrom(clazz)) {

                Command command = (Command) clazz.newInstance();

                commandManager.register(command.getClass());
                commandManager.register(command.getClass().getName() + "1", command.getClass());
                commandManager.register(command.getClass().getName() + "2", command);
            }

            commandManager.registerMethodCommands(clazz);

            for (String alias : commandManager.getAliases()) {
                commandManager.call(alias, sender, new String[]{"a"});
            }
        }
    }
    static CommandSender sender = new CommandSender() {
        public void sendMessage(String message) {
            System.out.println("Sender > " + message);
        }

        public void sendMessage(String[] messages) {
            for (String message : messages) {
                sendMessage(message);
            }
        }

        public Server getServer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isPermissionSet(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isPermissionSet(Permission perm) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean hasPermission(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean hasPermission(Permission perm) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PermissionAttachment addAttachment(Plugin plugin) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeAttachment(PermissionAttachment attachment) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void recalculatePermissions() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isOp() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setOp(boolean value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
}


abstract class BaseCommand implements Command {

    private String name = null;
    private CommandStack primaryAlias = null;
    public Set<CommandStack> secondaryAliases = new HashSet<CommandStack>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public CommandStack getPrimaryAlias() {
        return primaryAlias;
    }

    public Set<CommandStack> getSecondaryAliases() {
        return secondaryAliases;
    }

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
    
    
    public void addSecondaryAlias(CommandStack alias) {
        secondaryAliases.add(alias);
    }
}
class ACommand extends BaseCommand {

    public boolean execute(CommandSender sender, String[] args) {
        System.out.println(getClass().getName());
        return true;
    }
}

class BCommand extends AbstractCommand {
}

@AnnotatedCommand("c")
class CCommand extends BaseCommand {

    public boolean execute(CommandSender sender, String[] args) {
        System.out.println(getClass().getName());
        return true;
    }
}

@AnnotatedCommand("d")
class DCommand extends BaseCommand {

    public boolean execute(CommandSender sender, String[] args) {
        System.out.println(getClass().getName());
        return true;
    }
}

@AnnotatedCommand("e")
class ECommand extends BaseCommand {

    public boolean execute(CommandSender sender, String[] args) {
        System.out.println(getClass().getName());
        return true;
    }
}

@AnnotatedCommand("f")
class FCommand {
}

@AnnotatedCommand("e")
class GCommand {

    @MethodCommand
    @AnnotatedCommand()
    public boolean h(CommandSender sender, String[] args) {
        System.out.println(getClass().getName() + " h()");
        return true;
    }

    @MethodCommand
    @AnnotatedCommand("z")
    public boolean i(CommandSender sender, String[] args) {
        System.out.println(getClass().getName() + " i()");
        return true;
    }

    @AnnotatedCommand()
    public boolean j(CommandSender sender, String[] args) {
        System.out.println(getClass().getName() + " j()");
        return true;
    }

    @MethodCommand
    public boolean k(CommandSender sender, String[] args) {
        System.out.println(getClass().getName() + " k()");
        return true;
    }

    @MethodCommand
    public void badReturnA(CommandSender sender, String[] args) {
    }
    
    @MethodCommand
    public Boolean badReturnB(CommandSender sender, String[] args) {
        return true;
    }

    @MethodCommand
    public boolean badParamsA() {
        return true;
    }

    @MethodCommand
    public boolean badParamsB(String s, Object obj) {
        return true;
    }
}