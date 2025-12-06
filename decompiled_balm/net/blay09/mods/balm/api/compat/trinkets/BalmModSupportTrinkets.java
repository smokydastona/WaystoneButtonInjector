/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.balm.api.compat.trinkets;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface BalmModSupportTrinkets {
    public boolean isEquipped(Player var1, Predicate<ItemStack> var2);

    public ItemStack findEquipped(Player var1, Predicate<ItemStack> var2);

    public List<ItemStack> findAllEquipped(Player var1, Predicate<ItemStack> var2);
}

