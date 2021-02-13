package ma.zeldaboy111;

import ma.zeldaboy111.machinery.factorycreation.FactoryCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void playerInteractArmorStand(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void playerClickEvent(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_BLOCK) e.setCancelled(FactoryCreator.getInstance().clickEvent(e.getPlayer(), true));
        else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) e.setCancelled(FactoryCreator.getInstance().clickEvent(e.getPlayer(), false));
    }

}
