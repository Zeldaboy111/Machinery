package ma.zeldaboy111.machinery.factorycreation;

import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public enum FactoryItem {
    STRAIGHT(Material.BLACK_CONCRETE, "§cStraight §8(§c3§8)", new String[] { "§7", " §7Left-click to show path", " §7Right-click to place path" }, 0, FactoryLayout.STRAIGHT),
    CORNER_L(Material.BLACK_CONCRETE, "§cCorner §8(§cL §8| §c3§8)", new String[] { "§7", " §7Left-click to show path", " §7Right-click to place path" }, 1, FactoryLayout.CORNER_L),
    CORNER_R(Material.BLACK_CONCRETE, "§cCorner §8(§cR §8| §c3§8)", new String[] { "§7", " §7Left-click to show path", " §7Right-click to place path" }, 2, FactoryLayout.CORNER_R),
    OAK_GENERATOR(Material.OAK_WOOD, "§cGenerator §8(§cOak Wood§8)", new String[] { "§7", " §7Left-click to show generator", " §7Right-click to place generator" }, 8, FactoryLayout.OAK_GENERATOR, FactoryGenerator.OAK_WOOD),

    ;

    private ItemStack stack;
    private int slot;
    private FactoryLayout layout;
    private FactoryGenerator generator;

    FactoryItem(Material material, String name, String[] lore, int slot, FactoryLayout layout) {
        if(material != null) createStack(material, name, lore == null || lore.length < 1 ? null : lore);
        this.slot = slot;
        this.layout = layout;
        this.generator = null;
    }
    FactoryItem(Material material, String name, String[] lore, int slot, FactoryLayout layout, FactoryGenerator generator) {
        if(material != null) createStack(material, name, lore == null || lore.length < 1 ? null : lore);
        this.slot = slot;
        this.layout = layout;
        this.generator = generator;
    }
    private void createStack(@NotNull Material material, String name, String[] lore) {
        stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if(name != null && !name.trim().equals("")) meta.setDisplayName(name);
        if(lore != null) meta.setLore(getLore(lore));
        stack.setItemMeta(meta);
    }
    private ArrayList<String> getLore(@NotNull String[] lore) {
        ArrayList<String> newLore = new ArrayList<>();
        for(String s : lore) newLore.add(s);
        return newLore;
    }
    public Boolean isGenerator() { return generator != null; }
    public FactoryGenerator getGenerator() { return isGenerator() ? generator : null; }

    public void setInInventory(Inventory i) {
        if(slot < 0 || slot >= i.getSize() || stack == null || stack.getType() == Material.AIR) return;
        i.setItem(slot, stack);
    }
    public ItemStack getStack() { return stack == null ? null : stack.clone(); }
    public FactoryLayout getLayout() { return layout; }

}
