/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.client;

import java.util.ServiceLoader;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntimeFactory;

public class BalmClientRuntimeSpi {
    public static BalmClientRuntime create() {
        ServiceLoader<BalmClientRuntimeFactory> loader = ServiceLoader.load(BalmClientRuntimeFactory.class);
        BalmClientRuntimeFactory factory = loader.findFirst().orElseThrow();
        return factory.create();
    }
}

