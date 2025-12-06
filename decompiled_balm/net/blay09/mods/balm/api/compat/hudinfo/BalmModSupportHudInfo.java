/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.balm.api.compat.hudinfo;

import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface BalmModSupportHudInfo {
    public void registerGlobalBlockInfo(ResourceLocation var1, BlockInfoProvider var2);

    public void registerBlockInfo(ResourceLocation var1, Block var2, BlockInfoProvider var3);
}

