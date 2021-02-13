package ma.zeldaboy111.wand.wandlist;

import ma.zeldaboy111.Utils;
import ma.zeldaboy111.wand.WandCmd;
import ma.zeldaboy111.wand.spells.ISpell;
import ma.zeldaboy111.wand.spells.WolfeRaid;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DruidWand implements IWand {
    private static final ItemStack wandItem = Utils.createItem(Material.STICK, "§2Druid Wand", new String[] { " §7Wand used by the druids", " §7from the Dark Woods"});

    private static final HashMap<UUID, Integer> spellSelectionList = new HashMap<>();
    private static final ISpell[] spells = new ISpell[] { new WolfeRaid() };

    @Override
    public String getName() { return "Druid"; }
    @Override
    public ItemStack getWandItem() { return wandItem; }
    @Override
    public void give(Player p) {
        p.getInventory().addItem(wandItem);
    }

    @Override
    public void cast(Player p) {
        int selectedSpell = getSelectedSpellFromPlayer(p);
        ISpell spell = spells[selectedSpell];
        if(spell != null) spell.cast(p);
    }
    @Override
    public void rotateSpell(Player p) {
        int selectedSpell = getSelectedSpellFromPlayer(p);
        if(selectedSpell+1 < spells.length) selectedSpell += 1;
        else selectedSpell = 0;

        p.sendMessage(WandCmd.prefix + "§2" + getName() + " Wand §8• §7" + spells[selectedSpell].getName());
        p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 10, 2);
        if(spellSelectionList.get(p.getUniqueId()) == null) spellSelectionList.put(p.getUniqueId(), selectedSpell);
        else spellSelectionList.replace(p.getUniqueId(), selectedSpell);
    }

    private int getSelectedSpellFromPlayer(Player p) {
        try {
            return spellSelectionList.get(p.getUniqueId());
        } catch(Exception e) { return 0; }
    }
}
