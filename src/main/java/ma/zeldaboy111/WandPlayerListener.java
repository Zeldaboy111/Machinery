package ma.zeldaboy111;

import ma.zeldaboy111.wand.WandList;
import ma.zeldaboy111.wand.wandlist.IWand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandPlayerListener implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) rotateSpell(e.getPlayer(), e);
        else if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) castSpell(e.getPlayer(), e);
    }

    private void rotateSpell(Player p, PlayerInteractEvent e) {
        IWand heldWand = getHeldWandFromPlayer(p);
        if(heldWand == null) return;
        e.setCancelled(true);
        heldWand.rotateSpell(p);
    }
    private void castSpell(Player p, PlayerInteractEvent e) {
        IWand heldWand = getHeldWandFromPlayer(p);
        if(heldWand == null) return;
        e.setCancelled(true);
        heldWand.cast(p);
    }

    private IWand getHeldWandFromPlayer(Player p) {
        IWand offHand = getWandFromItem(p.getInventory().getItemInOffHand().clone());
        IWand mainHandWand = offHand != null ? null : getWandFromItem(p.getInventory().getItemInMainHand().clone());
        return offHand != null ? offHand : mainHandWand;
    }
    private IWand getWandFromItem(ItemStack stack) {
        if(stack == null || stack.getType() == Material.AIR) return null;
        stack.setAmount(1);
        for(WandList wand : WandList.values()) {
            if(wand.getWandItem().equals(stack)) return wand.getWand();
        }
        return null;
    }

}
