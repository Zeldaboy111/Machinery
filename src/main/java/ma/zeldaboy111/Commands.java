package ma.zeldaboy111;

import ma.zeldaboy111.machinery.commandhandler.Factory;
import ma.zeldaboy111.wand.WandCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equals("factory")) {
            Factory.instance.command(s, args);
        } else if(cmd.getName().equals("wand")) WandCmd.getInstance().execute(s, args);
        return true;
    }
}