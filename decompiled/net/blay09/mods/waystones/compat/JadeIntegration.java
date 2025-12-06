/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  snownee.jade.api.BlockAccessor
 *  snownee.jade.api.IBlockComponentProvider
 *  snownee.jade.api.ITooltip
 *  snownee.jade.api.IWailaClientRegistration
 *  snownee.jade.api.IWailaPlugin
 *  snownee.jade.api.WailaPlugin
 *  snownee.jade.api.config.IPluginConfig
 */
package net.blay09.mods.waystones.compat;

import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.compat.WaystonesWailaUtils;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(value="waystones")
public class JadeIntegration
implements IWailaPlugin {
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent((IBlockComponentProvider)new WaystoneDataProvider(), WaystoneBlockBase.class);
    }

    private static class WaystoneDataProvider
    implements IBlockComponentProvider {
        private WaystoneDataProvider() {
        }

        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            WaystonesWailaUtils.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), arg_0 -> ((ITooltip)tooltip).add(arg_0));
        }

        public ResourceLocation getUid() {
            return WaystonesWailaUtils.WAYSTONE_UID;
        }
    }
}

