/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.notoml;

import net.blay09.mods.balm.notoml.Notoml;
import net.blay09.mods.balm.notoml.NotomlError;
import net.blay09.mods.balm.notoml.NotomlParseBuffer;
import net.blay09.mods.balm.notoml.NotomlStateMachine;
import net.blay09.mods.balm.notoml.NotomlTokenConsumer;

public class NotomlParser {
    public static Notoml parse(String input) {
        final Notoml result = new Notoml();
        NotomlStateMachine stateMachine = new NotomlStateMachine();
        NotomlParseBuffer parseBuffer = new NotomlParseBuffer(input);
        NotomlTokenConsumer tokenConsumer = new NotomlTokenConsumer(){

            @Override
            public void onPropertyParsed(String category, String property, Object value) {
                result.setProperty(category, property, value);
            }

            @Override
            public void onCommentParsed(String category, String property, String comment) {
                result.setComment(category, property, comment);
            }

            @Override
            public void onError(NotomlError error) {
                result.addError(error);
            }
        };
        while (parseBuffer.canRead() && stateMachine.next(parseBuffer, tokenConsumer)) {
        }
        return result;
    }
}

