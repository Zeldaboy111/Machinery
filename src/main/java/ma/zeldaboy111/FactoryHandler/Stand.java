package ma.zeldaboy111.FactoryHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Stand {
    private Movement movement;

    private ArmorStand stand;

    public Stand(Location loc) {
        Location location = loc.getBlock().getLocation().add(0.5, -1.43, 0.5);
        this.stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setHelmet(new ItemStack(Material.OAK_LOG));
        stand.setGravity(false);
        stand.setVisible(false);
        movement = new Movement(this);
        movement.startMoving();
    }

    public ArmorStand getStand() { return stand; }
    public void collect() {
        dropItem();
        stand.remove();
        stand.setHealth(0.0);
    }
    public void moveStand(Location newLocation) {
        stand.teleport(newLocation);
    }
    public void updateItem(Material material) {
        if(material == null || material == Material.AIR) return;
        stand.setHelmet(new ItemStack(material));
    }
    public Location getStandBlockLocation() { return stand.getLocation().getBlock().getLocation().clone(); }
    public Location getLocation() { return stand.getLocation().clone(); }

    private void dropItem() {
        ItemStack itemToDrop = stand.getHelmet();
        Location locationToDrop = getLocation().add(0.0, 1.0, 0.0);
        Block block = locationToDrop.getBlock();
        if(block.getState() instanceof Hopper) {
            if(tryAddItemToHopper((Hopper)block.getState(), itemToDrop)) return;
        }
        Item droppedItem = locationToDrop.getWorld().dropItem(locationToDrop, itemToDrop);
        droppedItem.setVelocity(new Vector(0.0, 0.0, 0.0));
    }
    private Boolean tryAddItemToHopper(Hopper hopper, ItemStack itemToDrop) {
        Inventory inventory = hopper.getInventory();
        for(int i = 0; i < 5; i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, itemToDrop);
                return true;
            }
        }
        return false;
    }
}
