/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BalmClientProxy
extends BalmProxy {
    @Override
    @Nullable
    public Player getClientPlayer() {
        return Minecraft.m_91087_().f_91074_;
    }

    @Override
    public boolean isLocalServer() {
        Minecraft client = Minecraft.m_91087_();
        return client != null && client.m_91090_();
    }

    @Override
    public boolean isConnected() {
        Minecraft client = Minecraft.m_91087_();
        return client != null && client.m_91403_() != null;
    }

    @Override
    public boolean isIngame() {
        Minecraft client = Minecraft.m_91087_();
        return client != null && client.f_91072_ != null;
    }

    @Override
    public boolean isClient() {
        return true;
    }
}

