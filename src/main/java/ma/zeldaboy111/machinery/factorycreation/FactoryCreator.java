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
        int direction = getDirectionFromPlayer(p);
        Location target = getTarget(p, direction);
        Location isTargetGenerator = getTargetGenerator(target);
        FactoryItem item = null;

        System.out.println(isTargetGenerator);
        if((!leftClick || isTargetGenerator != null) && (stack == null || stack.getType() == Material.AIR || (item = checkIfItemIsFactoryItem(stack)) == null)) return false;
        if(target == null) p.sendMessage("§fFactory§8: §7You cannot build this part here.");
        else if(leftClick && isTargetGenerator != null) {
            FactoryManager.getInstance().removeGenerator(isTargetGenerator);
            return false;
        }
        else if(leftClick) item.getLayout().showParticles(p, target, direction);
        else if(item.getLayout().canBuild(direction, target)) item.getLayout().build(target, direction);
        else p.sendMessage("§fFactory§8: §7You cannot build this part here.");
        return true;
    }
    private Location getTargetGenerator(Location target) {
        for(FactoryItem item : FactoryItem.values()) {
            Location loc;
            if(item.isGenerator() && (loc = item.getLayout().isStructure(target)) != null) return loc;
        }
        return null;
    }

    private FactoryItem checkIfItemIsFactoryItem(ItemStack stack) {
        for(FactoryItem item : FactoryItem.values()) {
            if(stack.equals(item.getStack())) return item;
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
        Location check = getLocation(p.getLocation(), direction, -1);
        if(check.getBlock().getType() != Material.AIR && check.getBlock().getType().isSolid()) return getLocation(p.getLocation(), direction, 0);
        check = getLocation(p.getLocation(), direction, -2);
        if(check.getBlock().getType() != Material.AIR && check.getBlock().getType().isSolid()) return getLocation(p.getLocation(), direction, -1);
        return null;
    }
    private Location getLocation(Location loc, int direction, int y) {
        return loc.getBlock().getLocation().clone().add(direction == 3 ? -1 : direction == 1 ? 1 : 0, y, direction == 0 ? -1 : direction == 2 ? 1 : 0);
    }
}
