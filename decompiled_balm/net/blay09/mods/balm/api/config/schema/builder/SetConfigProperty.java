/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.network.FriendlyByteBuf
 */
package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.blay09.mods.balm.api.config.PrimitiveConfigCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredSet;
import net.blay09.mods.balm.api.config.schema.builder.AbstractConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public class SetConfigProperty<T>
extends AbstractConfigProperty<Set<T>>
implements ConfiguredSet<T> {
    private final Class<T> nestedType;
    private final Set<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<FriendlyByteBuf, List<T>> streamCodec;

    public SetConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, Set<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = PrimitiveConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, PrimitiveConfigCodecs.streamCodec(nestedType));
    }

    @Override
    public Class<?> type() {
        return Set.class;
    }

    @Override
    public Codec<Set<T>> codec() {
        return this.codec.xmap(Set::copyOf, List::copyOf);
    }

    @Override
    public StreamCodec<FriendlyByteBuf, Set<T>> streamCodec() {
        return this.streamCodec.map(Set::copyOf, List::copyOf);
    }

    @Override
    public Class<T> nestedType() {
        return this.nestedType;
    }

    @Override
    public Set<T> defaultValue() {
        return this.defaultValue;
    }
}

