package ma.zeldaboy111.FactoryHandler;

import ma.zeldaboy111.FactoryMaterials;
import ma.zeldaboy111.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void moveStand(double xIncrement, double yIncrement, double zIncrement) {
        stand.teleport(stand.getLocation().clone().add(xIncrement, yIncrement, zIncrement));
    }
    public Location getStandBlockLocation() { return stand.getLocation().getBlock().getLocation().clone(); }
}
