/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;

public class EmptyLoadContext
implements BalmRuntimeLoadContext {
    public static final EmptyLoadContext INSTANCE = new EmptyLoadContext();

    private EmptyLoadContext() {
    }
}

