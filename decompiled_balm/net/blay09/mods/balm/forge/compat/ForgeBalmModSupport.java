/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.forge.compat;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.common.compat.NoopTrinkets;
import net.blay09.mods.balm.common.compat.TrinketsMultiplexer;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;

public class ForgeBalmModSupport
implements BalmModSupport {
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();

    public ForgeBalmModSupport(BalmRuntime runtime) {
        this.trinkets = runtime.modProxy().with("curios", "net.blay09.mods.balm.forge.compat.trinkets.CuriosIntegration").withMultiplexer(TrinketsMultiplexer::new).withFallback(new NoopTrinkets()).buildLazily();
    }

    @Override
    public BalmModSupportTrinkets trinkets() {
        return this.trinkets.get();
    }

    @Override
    public BalmModSupportHudInfo hudInfo() {
        return this.hudInfo;
    }
}

