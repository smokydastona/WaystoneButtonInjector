/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mcp.mobius.waila.api.IBlockAccessor
 *  mcp.mobius.waila.api.IBlockComponentProvider
 *  mcp.mobius.waila.api.IPluginConfig
 *  mcp.mobius.waila.api.IRegistrar
 *  mcp.mobius.waila.api.ITooltip
 *  mcp.mobius.waila.api.IWailaPlugin
 *  mcp.mobius.waila.api.TooltipPosition
 */
package net.blay09.mods.waystones.compat;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.compat.WaystonesWailaUtils;

public class WTHITIntegration
implements IWailaPlugin {
    public void register(IRegistrar registrar) {
        registrar.addComponent((IBlockComponentProvider)new WaystoneDataProvider(), TooltipPosition.BODY, WaystoneBlockBase.class);
    }

    private static class WaystoneDataProvider
    implements IBlockComponentProvider {
        private WaystoneDataProvider() {
        }

        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            WaystonesWailaUtils.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), arg_0 -> ((ITooltip)tooltip).addLine(arg_0));
        }
    }
}

