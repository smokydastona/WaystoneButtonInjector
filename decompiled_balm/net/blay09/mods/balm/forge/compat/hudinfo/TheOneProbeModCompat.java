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
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraftforge.fml.InterModComms
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.compat.hudinfo;

import java.util.List;
import java.util.function.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.InterModComms;
import org.jetbrains.annotations.Nullable;

public class TheOneProbeModCompat {
    public static void register() {
        InterModComms.sendTo((String)"theoneprobe", (String)"getTheOneProbe", BalmTheOneProbeInitializer::new);
    }

    private record TheOneProbeHudInfoInfoOutput(IProbeInfo info) implements HudInfoOutput
    {
        @Override
        public void text(Component component) {
            this.info.text(component);
        }

        @Override
        public void progress(float progress) {
            this.info.progress((int)(progress * 100.0f), 100);
        }

        @Override
        public void progress(int progress, int maxProgress) {
            this.info.progress(progress, maxProgress);
        }
    }

    private static class BalmProbeInfoProvider
    implements IProbeInfoProvider {
        private static final ResourceLocation ID = new ResourceLocation("balm", "top");

        private BalmProbeInfoProvider() {
        }

        public ResourceLocation getID() {
            return ID;
        }

        public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData) {
            CommonBalmModSupportHudInfo modSupport = (CommonBalmModSupportHudInfo)Balm.getModSupport().hudInfo();
            List<BlockInfoProvider> blockInfoProviders = modSupport.getBlockInfoProviders(state.m_60734_());
            if (blockInfoProviders.isEmpty()) {
                return;
            }
            TheOneProbeHudInfoInfoOutput output = new TheOneProbeHudInfoInfoOutput(info);
            BlockInfoContext context = new BlockInfoContext(level, hitData.getPos(), state, level.m_7702_(hitData.getPos()), new BlockHitResult(hitData.getHitVec(), hitData.getSideHit(), hitData.getPos(), false), player);
            for (BlockInfoProvider blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }
    }

    public static class BalmTheOneProbeInitializer
    implements Function<ITheOneProbe, Void> {
        @Override
        @Nullable
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider((IProbeInfoProvider)new BalmProbeInfoProvider());
            }
            return null;
        }
    }
}

