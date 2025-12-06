/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mcjty.theoneprobe.api.IProbeHitData
 *  mcjty.theoneprobe.api.IProbeInfo
 *  mcjty.theoneprobe.api.IProbeInfoProvider
 *  mcjty.theoneprobe.api.ITheOneProbe
 *  mcjty.theoneprobe.api.ProbeMode
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.fml.InterModComms
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.compat;

import java.util.function.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;
import org.jetbrains.annotations.Nullable;

public class TheOneProbeIntegration {
    public TheOneProbeIntegration() {
        InterModComms.sendTo((String)"theoneprobe", (String)"getTheOneProbe", TopInitializer::new);
    }

    public static class ProbeInfoProvider
    implements IProbeInfoProvider {
        public ResourceLocation getID() {
            return new ResourceLocation("waystones", "top");
        }

        public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
            BlockEntity tileEntity = level.m_7702_(data.getPos());
            if (!(tileEntity instanceof WarpPlateBlockEntity) && tileEntity instanceof WaystoneBlockEntityBase) {
                boolean isActivated;
                IWaystone waystone = ((WaystoneBlockEntityBase)tileEntity).getWaystone();
                boolean bl = isActivated = !waystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE) || PlayerWaystoneManager.isWaystoneActivated(player, waystone);
                if (isActivated && waystone.hasName() && waystone.isValid()) {
                    info.text((Component)Component.m_237113_((String)waystone.getName()));
                } else {
                    info.text((Component)Component.m_237115_((String)"tooltip.waystones.undiscovered"));
                }
            }
        }
    }

    public static class TopInitializer
    implements Function<ITheOneProbe, Void> {
        @Override
        @Nullable
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider((IProbeInfoProvider)new ProbeInfoProvider());
            }
            return null;
        }
    }
}

