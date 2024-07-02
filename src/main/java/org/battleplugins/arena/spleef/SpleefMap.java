package org.battleplugins.arena.spleef;

import org.battleplugins.arena.Arena;
import org.battleplugins.arena.competition.Competition;
import org.battleplugins.arena.competition.map.LiveCompetitionMap;
import org.battleplugins.arena.competition.map.MapFactory;
import org.battleplugins.arena.competition.map.MapType;
import org.battleplugins.arena.competition.map.options.Bounds;
import org.battleplugins.arena.competition.map.options.Spawns;
import org.battleplugins.arena.config.ArenaOption;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpleefMap extends LiveCompetitionMap {
    static final MapFactory FACTORY = MapFactory.create(SpleefMap.class, SpleefMap::new);

    @ArenaOption(name = "layers", description = "The layers for this spleef map.")
    private List<Layer> layers;

    public SpleefMap() {
    }

    public SpleefMap(String name, Arena arena, MapType type, String world, @Nullable Bounds bounds, @Nullable Spawns spawns) {
        super(name, arena, type, world, bounds, spawns);
    }

    @Override
    public Competition<?> createCompetition(Arena arena) {
        if (!(arena instanceof SpleefArena spleefArena)) {
            throw new IllegalArgumentException("Arena must be a spleef arena!");
        }

        return new SpleefCompetition(spleefArena, arena.getType(), this);
    }

    public List<Layer> getLayers() {
        return this.layers == null ? List.of() : List.copyOf(this.layers);
    }

    public void addLayer(Layer layer) {
        if (this.layers == null) {
            this.layers = new ArrayList<>();
        }

        this.layers.add(layer);
    }

    public void addLayer(int index, Layer layer) {
        if (this.layers == null) {
            this.layers = new ArrayList<>();
        }

        this.layers.add(index, layer);
    }

    public void removeLayer(Layer layer) {
        this.layers.remove(layer);
    }

    public void clearLayers() {
        this.layers.clear();
    }

    public static class Layer {
        @ArenaOption(name = "bounds", description = "The bounds of this layer.", required = true)
        private Bounds bounds;

        @ArenaOption(name = "block-data", description = "The block data of this layer.", required = true)
        private BlockData blockData;

        public Layer() {
        }

        public Layer(Bounds bounds, BlockData blockData) {
            this.bounds = bounds;
            this.blockData = blockData;
        }

        public Bounds getBounds() {
            return this.bounds;
        }

        public BlockData getBlockData() {
            return this.blockData;
        }
    }
}
