/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.waystones.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TeleportEffectMessage {
    private final BlockPos pos;

    public TeleportEffectMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(TeleportEffectMessage message, FriendlyByteBuf buf) {
        buf.m_130064_(message.pos);
    }

    public static TeleportEffectMessage decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.m_130135_();
        return new TeleportEffectMessage(pos);
    }

    public static void handle(Player player, TeleportEffectMessage message) {
        Level level = player.m_9236_();
        if (level != null) {
            for (int i = 0; i < 128; ++i) {
                level.m_7106_((ParticleOptions)ParticleTypes.f_123760_, (double)message.pos.m_123341_() + (level.f_46441_.m_188500_() - 0.5) * 3.0, (double)message.pos.m_123342_() + level.f_46441_.m_188500_() * 3.0, (double)message.pos.m_123343_() + (level.f_46441_.m_188500_() - 0.5) * 3.0, (level.f_46441_.m_188500_() - 0.5) * 2.0, -level.f_46441_.m_188500_(), (level.f_46441_.m_188500_() - 0.5) * 2.0);
            }
        }
    }
}

