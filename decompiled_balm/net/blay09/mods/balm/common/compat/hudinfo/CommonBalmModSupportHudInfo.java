/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.balm.common.compat.hudinfo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CommonBalmModSupportHudInfo
implements BalmModSupportHudInfo {
    private final List<BlockInfoProvider> globalBlockInfoProviders = new ArrayList<BlockInfoProvider>();
    private final Multimap<Block, BlockInfoProvider> blockInfoProviders = ArrayListMultimap.create();

    @Override
    public void registerGlobalBlockInfo(ResourceLocation identifier, BlockInfoProvider provider) {
        this.globalBlockInfoProviders.add(provider);
    }

    @Override
    public void registerBlockInfo(ResourceLocation identifier, Block block, BlockInfoProvider provider) {
        this.blockInfoProviders.put((Object)block, (Object)provider);
    }

    public List<BlockInfoProvider> getBlockInfoProviders(Block block) {
        ArrayList<BlockInfoProvider> result = new ArrayList<BlockInfoProvider>();
        result.addAll(this.blockInfoProviders.get((Object)block));
        result.addAll(this.globalBlockInfoProviders);
        return result;
    }
}

