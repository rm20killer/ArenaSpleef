package org.battleplugins.arena.spleef.editor;

import io.papermc.paper.math.Position;
import org.battleplugins.arena.BattleArena;
import org.battleplugins.arena.competition.map.options.Bounds;
import org.battleplugins.arena.config.ParseException;
import org.battleplugins.arena.editor.ArenaEditorWizard;
import org.battleplugins.arena.editor.ArenaEditorWizards;
import org.battleplugins.arena.editor.stage.PositionInputStage;
import org.battleplugins.arena.editor.stage.TextInputStage;
import org.battleplugins.arena.editor.type.MapOption;
import org.battleplugins.arena.messages.Messages;
import org.battleplugins.arena.spleef.SpleefMap;
import org.battleplugins.arena.spleef.SpleefMessages;
import org.bukkit.Bukkit;

import java.io.IOException;

public final class SpleefEditorWizards {
    public static final ArenaEditorWizard<AddLayerContext> ADD_LAYER = ArenaEditorWizards.createWizard(AddLayerContext::new)
            .addStage(MapOption.MIN_POS, new PositionInputStage<>(SpleefMessages.LAYER_SET_MIN_POSITION, ctx -> ctx::setMin))
            .addStage(MapOption.MAX_POS, new PositionInputStage<>(SpleefMessages.LAYER_SET_MAX_POSITION, ctx -> ctx::setMax))
            .addStage(AddLayerOption.BLOCK_DATA, new TextInputStage<>(
                    SpleefMessages.LAYER_SET_BLOCK_DATA,
                    SpleefMessages.LAYER_SET_BLOCK_DATA_INVALID,
                    (ctx, str) -> {
                        try {
                            Bukkit.createBlockData(str);
                        } catch (IllegalArgumentException e) {
                            return false;
                        }

                        return true;
                    },
                    ctx -> str -> ctx.setBlockData(Bukkit.createBlockData(str))
            ))
            .onCreationComplete(ctx -> {
                Position min = ctx.getMin();
                Position max = ctx.getMax();

                // Sanitize min max
                if (min.blockX() > max.blockX()) {
                    int temp = min.blockX();
                    min = Position.block(max.blockX(), min.blockY(), min.blockZ());
                    max = Position.block(temp, max.blockY(), max.blockZ());
                }

                if (min.blockY() > max.blockY()) {
                    int temp = min.blockY();
                    min = Position.block(min.blockX(), max.blockY(), min.blockZ());
                    max = Position.block(max.blockX(), temp, max.blockZ());
                }

                if (min.blockZ() > max.blockZ()) {
                    int temp = min.blockZ();
                    min = Position.block(min.blockX(), min.blockY(), max.blockZ());
                    max = Position.block(max.blockX(), max.blockY(), temp);
                }

                Bounds bounds = new Bounds(min, max);

                SpleefMap map = ctx.getMap();
                SpleefMap.Layer layer = new SpleefMap.Layer(bounds, ctx.getBlockData());
                map.addLayer(layer);

                try {
                    map.save();
                } catch (ParseException | IOException e) {
                    BattleArena.getInstance().error("Failed to save map file for arena {}", ctx.getArena().getName(), e);
                    Messages.MAP_FAILED_TO_SAVE.send(ctx.getPlayer(), map.getName());
                    return;
                }

                SpleefMessages.LAYER_ADDED.send(ctx.getPlayer());
            });
}
