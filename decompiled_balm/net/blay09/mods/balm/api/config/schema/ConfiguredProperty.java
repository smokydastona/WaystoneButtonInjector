/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.network.FriendlyByteBuf
 */
package net.blay09.mods.balm.api.config.schema;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public interface ConfiguredProperty<T> {
    public BalmConfigSchema parentSchema();

    public String category();

    public String name();

    public String comment();

    public boolean synced();

    public Class<?> type();

    public Codec<T> codec();

    public StreamCodec<FriendlyByteBuf, T> streamCodec();

    public T defaultValue();

    default public T getRaw(LoadedConfig config) {
        return config.getRaw(this);
    }

    default public void setRaw(MutableLoadedConfig config, T value) {
        config.setRaw(this, value);
    }
}

