/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.phys.BlockHitResult
 *  snownee.jade.api.BlockAccessor
 *  snownee.jade.api.IBlockComponentProvider
 *  snownee.jade.api.ITooltip
 *  snownee.jade.api.IWailaClientRegistration
 *  snownee.jade.api.IWailaPlugin
 *  snownee.jade.api.WailaPlugin
 *  snownee.jade.api.config.IPluginConfig
 *  snownee.jade.api.ui.IElement
 *  snownee.jade.impl.ui.ProgressArrowElement
 */
package net.blay09.mods.balm.forge.compat.hudinfo;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ProgressArrowElement;

@WailaPlugin(value="balm")
public class ForgeJadeModCompat
implements IWailaPlugin {
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent((IBlockComponentProvider)new BalmBlockComponentProvider(), Block.class);
    }

    private static class BalmBlockComponentProvider
    implements IBlockComponentProvider {
        private static final ResourceLocation ID = new ResourceLocation("balm", "jade");

        private BalmBlockComponentProvider() {
        }

        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            Block block = accessor.getBlock();
            CommonBalmModSupportHudInfo modSupport = (CommonBalmModSupportHudInfo)Balm.getModSupport().hudInfo();
            List<BlockInfoProvider> blockInfoProviders = modSupport.getBlockInfoProviders(block);
            if (blockInfoProviders.isEmpty()) {
                return;
            }
            JadeHudInfoOutput output = new JadeHudInfoOutput(tooltip);
            BlockInfoContext context = new BlockInfoContext(accessor.getLevel(), accessor.getPosition(), accessor.getBlockState(), accessor.getBlockEntity(), (BlockHitResult)accessor.getHitResult(), accessor.getPlayer());
            for (BlockInfoProvider blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }

        public ResourceLocation getUid() {
            return ID;
        }
    }

    private record JadeHudInfoOutput(ITooltip tooltip) implements HudInfoOutput
    {
        @Override
        public void text(Component component) {
            this.tooltip.add(component);
        }

        @Override
        public void progress(float progress) {
            this.tooltip.add((IElement)new ProgressArrowElement(progress));
        }
    }
}

