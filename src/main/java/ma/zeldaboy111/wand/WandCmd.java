package ma.zeldaboy111.wand;

import ma.zeldaboy111.wand.wandlist.IWand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WandCmd {
    private static WandCmd clazz;
    private WandCmd() { }
    public static WandCmd getInstance() { return clazz == null ? (clazz = new WandCmd()) : clazz; }

    public static final String prefix = "§6Wands§8: §7";


    public void execute(CommandSender s, String[] args) {
        if(!(s instanceof Player)) s.sendMessage(prefix + "This command can only be executed by a player.");
        else if(args.length < 1) sendWandList(s);
        else giveWand((Player)s, args[0].toLowerCase());
    }
    private void sendWandList(CommandSender s) {
        s.sendMessage(prefix + "This is the full list of all wands: " + getWandStringList() + ".");
    }
    private String getWandStringList() {
        WandList[] wands = WandList.values();
        String wandString = "";
        for(WandList wand : wands) wandString += "§e" + wand.getName() + "§7, ";
        return wandString.substring(0, wandString.length()-2);
    }

    private void giveWand(Player p, String wandAsString) {
        IWand wand = getWandFromString(wandAsString);
        if(wand == null) p.sendMessage(prefix + "The wand §e" + wandAsString.toLowerCase() + " §7is not found.");
        else {
            wand.give(p);
            p.sendMessage(prefix + "Given the §e" + wand.getName() + " Wand §7to you.");
        }
    }
    private IWand getWandFromString(String wandAsString) {
        IWand wand = null;
        WandList[] wands = WandList.values();
        for(WandList wand1 : wands) {
            if(wand1.getName().equalsIgnoreCase(wandAsString)) { wand = wand1.getWand(); break; }
        }
        return wand;
    }
    
}
