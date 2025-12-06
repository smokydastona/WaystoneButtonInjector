/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntimeFactory;
import net.blay09.mods.balm.forge.client.ForgeBalmClientRuntime;

public class ForgeBalmClientRuntimeFactory
implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime create() {
        return new ForgeBalmClientRuntime();
    }
}

