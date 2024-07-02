package org.battleplugins.arena.spleef;

import org.battleplugins.arena.Arena;
import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.competition.Competition;
import org.battleplugins.arena.event.action.EventAction;

import java.util.Map;

public class PasteLayersAction extends EventAction {
    public PasteLayersAction(Map<String, String> params) {
        super(params);
    }

    @Override
    public void call(ArenaPlayer arenaPlayer) {
    }

    @Override
    public void postProcess(Arena arena, Competition<?> competition) {
        if (!(competition instanceof SpleefCompetition spleefCompetition)) {
            return;
        }

        spleefCompetition.pasteLayers();
    }
}
