package org.battleplugins.arena.spleef;

import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.event.action.EventAction;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GiveShovelAction extends EventAction {
    private static final String SHOVEL_KEY = "shovel";

    public GiveShovelAction(Map<String, String> params, String... requiredKeys) {
        super(params, requiredKeys);
    }

    @Override
    public void call(ArenaPlayer arenaPlayer) {
        ItemStack shovel = ArenaSpleef.getInstance().getMainConfig().getShovel(this.get(SHOVEL_KEY));
        if (shovel == null) {
            ArenaSpleef.getInstance().getSLF4JLogger().warn("Invalid shovel " + this.get(SHOVEL_KEY) + ". Not giving shovel to player.");
            return;
        }

        arenaPlayer.getPlayer().getInventory().addItem(shovel);
    }
}
