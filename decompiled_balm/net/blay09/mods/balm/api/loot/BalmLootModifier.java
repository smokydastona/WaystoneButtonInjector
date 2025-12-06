/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 */
package net.blay09.mods.balm.api.loot;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

@FunctionalInterface
public interface BalmLootModifier {
    public void apply(LootContext var1, List<ItemStack> var2);
}

