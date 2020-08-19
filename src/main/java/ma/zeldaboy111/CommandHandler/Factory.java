package ma.zeldaboy111.CommandHandler;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Factory {
    public static final Factory instance = new Factory();

    public double speed = 0.25;

    public void command(CommandSender s, String[] args) {
        if(!(s instanceof Player)) return;
        Player p = (Player)s;
        if(!p.hasPermission("factory.use") && !p.isOp()) return;

        new ma.zeldaboy111.FactoryHandler.Factory(p.getLocation(), Material.OAK_LOG);
        //new Movement(p, Material.OAK_LOG);

    }

}
