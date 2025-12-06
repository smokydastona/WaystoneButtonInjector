/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 */
package net.blay09.mods.waystones.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class AttunedShardJeiRecipe {
    private final List<ItemStack> inputs = new ArrayList<ItemStack>();
    private final ItemStack output;

    public AttunedShardJeiRecipe() {
        this.inputs.add(new ItemStack((ItemLike)Items.f_42484_));
        this.inputs.add(new ItemStack((ItemLike)ModItems.warpDust));
        this.inputs.add(new ItemStack((ItemLike)ModItems.warpDust));
        this.inputs.add(new ItemStack((ItemLike)ModItems.warpDust));
        this.inputs.add(new ItemStack((ItemLike)ModItems.warpDust));
        this.output = new ItemStack((ItemLike)ModItems.attunedShard);
    }

    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }
}

