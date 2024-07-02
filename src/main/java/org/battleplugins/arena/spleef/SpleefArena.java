package org.battleplugins.arena.spleef;

import org.battleplugins.arena.Arena;
import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.command.ArenaCommandExecutor;
import org.battleplugins.arena.competition.map.MapFactory;
import org.battleplugins.arena.competition.phase.CompetitionPhaseType;
import org.battleplugins.arena.config.ArenaOption;
import org.battleplugins.arena.event.ArenaEventHandler;
import org.battleplugins.arena.event.arena.ArenaPhaseCompleteEvent;
import org.battleplugins.arena.event.arena.ArenaPhaseStartEvent;
import org.battleplugins.arena.options.ArenaOptionType;
import org.battleplugins.arena.options.types.BooleanArenaOption;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.time.Duration;

public class SpleefArena extends Arena {

    @ArenaOption(name = "layer-decay-delay", description = "The delay before each layer decays.")
    private Duration layerDecayDelay = Duration.ofMinutes(2);

    @ArenaOption(name = "layer-decay-time", description = "The time it takes to decay and entire layer.")
    private Duration layerDecayTime = Duration.ofMinutes(1);

    @Override
    public ArenaCommandExecutor createCommandExecutor() {
        return new SpleefExecutor(this);
    }

    @Override
    public MapFactory getMapFactory() {
        return SpleefMap.FACTORY;
    }

    @ArenaEventHandler
    public void onPhaseStart(ArenaPhaseStartEvent event) {
        if (!CompetitionPhaseType.INGAME.equals(event.getPhase().getType())) {
            return;
        }

        if (event.getCompetition() instanceof SpleefCompetition spleefCompetition) {
            spleefCompetition.beginLayerDecay();
        }
    }

    @ArenaEventHandler
    public void onPhaseComplete(ArenaPhaseCompleteEvent event) {
        if (!CompetitionPhaseType.INGAME.equals(event.getPhase().getType())) {
            return;
        }

        if (event.getCompetition() instanceof SpleefCompetition spleefCompetition) {
            spleefCompetition.stopLayerDecay();
        }
    }

    @ArenaEventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event, ArenaPlayer player) {
        if (player.getCompetition().option(ArenaOptionType.BLOCK_BREAK).map(BooleanArenaOption::isEnabled).orElse(true)) {
            return; // Game already allows block breaking
        }

        SpleefMap spleefMap = (SpleefMap) player.getCompetition().getMap();
        for (SpleefMap.Layer layer : spleefMap.getLayers()) {
            if (layer.getBounds().isInside(event.getBlock().getLocation())) {
                event.setCancelled(false);
                return;
            }
        }
    }

    public Duration getLayerDecayDelay() {
        return this.layerDecayDelay;
    }

    public Duration getLayerDecayTime() {
        return this.layerDecayTime;
    }
}
