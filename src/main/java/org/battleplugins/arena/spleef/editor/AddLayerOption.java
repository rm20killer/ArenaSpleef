package org.battleplugins.arena.spleef.editor;

import org.battleplugins.arena.editor.type.EditorKey;

public enum AddLayerOption implements EditorKey {
    MIN("min"),
    MAX("max"),
    BLOCK_DATA("blockData");

    private final String key;

    AddLayerOption(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
