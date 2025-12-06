/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api;

import java.util.ServiceLoader;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeFactory;

public class BalmRuntimeSpi {
    public static BalmRuntime create() {
        ServiceLoader<BalmRuntimeFactory> loader = ServiceLoader.load(BalmRuntimeFactory.class);
        BalmRuntimeFactory factory = loader.findFirst().orElseThrow();
        return factory.create();
    }
}

