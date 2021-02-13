package ma.zeldaboy111.machinery.commandhandler;

import ma.zeldaboy111.machinery.factorycreation.FactoryItem;
import ma.zeldaboy111.machinery.factoryhandler.StandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Factory {
    public static final Factory instance = new Factory();

    public void command(CommandSender s, String[] args) {
        if(!(s instanceof Player)) return;
        if(args.length == 1 && args[0].toLowerCase().equals("inventory")) getInventory((Player)s);
        else summonStand((Player)s);
    }
    private void summonStand(Player p) {
        if(!p.hasPermission("factory.use") && !p.isOp()) return;
        StandHandler.create(p.getLocation());
    }

    private void getInventory(Player p) {
        p.getInventory().clear();
        for(FactoryItem item : FactoryItem.values()) item.setInInventory(p.getInventory());
    }

}
