package ma.zeldaboy111.machinery.factorycreation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FactoryCreator {
    private static FactoryCreator clazz;
    private FactoryCreator() { }
    public static FactoryCreator getInstance() { return clazz == null ? (clazz = new FactoryCreator()) : clazz; }

    public Boolean clickEvent(Player p, Boolean leftClick) {
        ItemStack stack = p.getInventory().getItemInMainHand();
        FactoryLayout layout;
        if(stack == null || stack.getType() == Material.AIR || (layout = checkIfItemIsFactoryItem(stack)) == null) return false;
        int direction = getDirectionFromPlayer(p);
        Location target = getTarget(p, direction);
        if(leftClick) layout.showParticles(p, target, direction);
        else if(layout.canBuild(p.getLocation().getYaw(), target)) layout.build(p.getLocation().getYaw(), target, direction);
        else p.sendMessage("§fFactory§8: §7You cannot build this part here.");
        return true;
    }

    private FactoryLayout checkIfItemIsFactoryItem(ItemStack stack) {
        for(FactoryItem item : FactoryItem.values()) {
            if(stack.equals(item.getStack())) return item.getLayout();
        }
        return null;
    }
    private int getDirectionFromPlayer(Player p) {
        float yaw = p.getLocation().getYaw();
        if(yaw >= 135 && yaw < 225) return 0; // north
        else if(yaw >= 225 && yaw < 315) return 1; // east
        else if(yaw >= 315 || yaw < 45) return 2; // south
        else return 3; // west
    }

    private Location getTarget(Player p, int direction) {
        Location last = null;
        for(int i = 0; i < 5; i++) {
            last = p.getLocation().clone().add(direction == 0 ? -i : direction == 2 ? i : 0, 1, direction == 1 ? -i : direction == 3 ? -i : 0);
            if(last != null && last.getBlock().getType() != Material.AIR && last.getBlock().getType().isSolid()) break;
        }
        //BlockIterator iterator = new BlockIterator(p, 5);
        //Block last = null;
        //while(iterator.hasNext()) {
        //    last = iterator.next();
        //    if(last.getType() != Material.AIR && last.getType().isSolid()) break;
        //}
        return last == null ? p.getLocation() : last;
    }
}
