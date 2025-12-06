/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mcp.mobius.waila.api.IBlockAccessor
 *  mcp.mobius.waila.api.IBlockComponentProvider
 *  mcp.mobius.waila.api.IClientRegistrar
 *  mcp.mobius.waila.api.IPluginConfig
 *  mcp.mobius.waila.api.ITooltip
 *  mcp.mobius.waila.api.ITooltipComponent
 *  mcp.mobius.waila.api.IWailaClientPlugin
 *  mcp.mobius.waila.api.component.ProgressArrowComponent
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.balm.common.compat.hudinfo;

import java.util.List;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.ITooltipComponent;
import mcp.mobius.waila.api.IWailaClientPlugin;
import mcp.mobius.waila.api.component.ProgressArrowComponent;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public class WTHITModCompat
implements IWailaClientPlugin {
    public void register(IClientRegistrar registrar) {
        registrar.body((IBlockComponentProvider)new BalmBlockComponentProvider(), Block.class);
    }

    private static class BalmBlockComponentProvider
    implements IBlockComponentProvider {
        private BalmBlockComponentProvider() {
        }

        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            Block block = accessor.getBlock();
            CommonBalmModSupportHudInfo modSupport = (CommonBalmModSupportHudInfo)Balm.getModSupport().hudInfo();
            List<BlockInfoProvider> blockInfoProviders = modSupport.getBlockInfoProviders(block);
            if (blockInfoProviders.isEmpty()) {
                return;
            }
            WTHITHudInfoOutput output = new WTHITHudInfoOutput(tooltip);
            BlockInfoContext context = new BlockInfoContext(accessor.getWorld(), accessor.getPosition(), accessor.getBlockState(), accessor.getBlockEntity(), accessor.getBlockHitResult(), accessor.getPlayer());
            for (BlockInfoProvider blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }
    }

    private record WTHITHudInfoOutput(ITooltip tooltip) implements HudInfoOutput
    {
        @Override
        public void text(Component component) {
            this.tooltip.addLine(component);
        }

        @Override
        public void progress(float progress) {
            this.tooltip.addLine((ITooltipComponent)new ProgressArrowComponent(progress));
        }
    }
}

