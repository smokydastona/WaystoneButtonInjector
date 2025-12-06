/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotResult
 */
package net.blay09.mods.balm.forge.compat.trinkets;

import java.util.List;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CuriosIntegration
implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).resolve().flatMap(trinkets -> trinkets.findFirstCurio(predicate)).map(SlotResult::stack).orElse(ItemStack.f_41583_);
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).resolve().map(trinkets -> trinkets.findCurios(predicate).stream().map(SlotResult::stack).toList()).orElse(List.of());
    }
}

