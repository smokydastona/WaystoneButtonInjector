/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.common;

import java.util.HashMap;
import java.util.Map;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.minecraft.resources.ResourceLocation;

public class CommonBalmLootTables
implements BalmLootTables {
    public final Map<ResourceLocation, BalmLootModifier> lootModifiers = new HashMap<ResourceLocation, BalmLootModifier>();

    @Override
    public void registerLootModifier(ResourceLocation identifier, BalmLootModifier modifier) {
        this.lootModifiers.put(identifier, modifier);
    }
}

