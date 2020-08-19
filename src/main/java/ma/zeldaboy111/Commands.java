package ma.zeldaboy111;

import ma.zeldaboy111.CommandHandler.Factory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equals("factory")) {
            Factory.instance.command(s, args);
        }
        return true;
    }
}