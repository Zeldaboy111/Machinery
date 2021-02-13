package ma.zeldaboy111.wand.spells;

import org.bukkit.entity.Player;

public interface ISpell {
    String getName();
    void cast(Player p);

}
