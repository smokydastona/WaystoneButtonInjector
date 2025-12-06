/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.notoml;

import net.blay09.mods.balm.notoml.NotomlError;
import net.blay09.mods.balm.notoml.NotomlParseBuffer;
import net.blay09.mods.balm.notoml.NotomlParserState;
import net.blay09.mods.balm.notoml.NotomlTokenConsumer;

public class NotomlStateMachine {
    private NotomlParserState state = NotomlParserState.None;

    public void transition(NotomlParserState state) {
        this.state = state;
    }

    public void end() {
        this.state = null;
    }

    public boolean next(NotomlParseBuffer buffer, NotomlTokenConsumer consumer) {
        try {
            this.state.next(this, buffer, consumer);
        }
        catch (Exception e) {
            consumer.emitError(new NotomlError(e.getMessage()).at(buffer.getLine()));
            buffer.readUntil("\n");
            this.state = NotomlParserState.None;
        }
        return this.state != null;
    }
}

