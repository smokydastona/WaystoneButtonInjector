/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.LootTable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.blay09.mods.balm.mixin;

import java.util.List;
import java.util.Map;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LootTable.class})
public class LootTableMixin {
    @Inject(method={"getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"}, at={@At(value="RETURN")}, cancellable=true)
    public void getRandomItems(LootContext lootContext, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
        List drops = (List)callbackInfo.getReturnValue();
        Map<ResourceLocation, BalmLootModifier> lootModifiers = ((CommonBalmLootTables)Balm.getLootTables()).lootModifiers;
        for (BalmLootModifier modifier : lootModifiers.values()) {
            modifier.apply(lootContext, drops);
        }
    }
}

