/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.Direction
 */
package net.blay09.mods.balm.api.provider;

import com.mojang.datafixers.util.Pair;
import java.util.Collections;
import java.util.List;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.minecraft.core.Direction;

@Deprecated(forRemoval=true, since="1.21.5")
public interface BalmProviderHolder {
    @Deprecated(forRemoval=true, since="1.21.5")
    default public List<BalmProvider<?>> getProviders() {
        return Collections.emptyList();
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
        return Collections.emptyList();
    }
}

