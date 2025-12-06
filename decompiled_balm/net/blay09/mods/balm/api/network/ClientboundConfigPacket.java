/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.api.network;

import java.util.List;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.PropertyAwareConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundConfigPacket(BalmConfigSchema schema, LoadedConfig config) {
    public static ClientboundConfigPacket decode(FriendlyByteBuf buf) {
        ResourceLocation identifier = ByteBufCodecs.RESOURCE_LOCATION.decode(buf);
        BalmConfigSchema schema = Balm.getConfig().getSchema(identifier);
        if (schema == null) {
            throw new RuntimeException("Received config packet for unknown schema: " + String.valueOf(identifier));
        }
        LoadedTableConfig config = new LoadedTableConfig();
        int rootPropertyCount = buf.m_130242_();
        for (int j = 0; j < rootPropertyCount; ++j) {
            String property = buf.m_130277_();
            ConfiguredProperty<?> propertySchema = schema.findRootProperty(property);
            ClientboundConfigPacket.decodePropertyInto(propertySchema, buf, config);
        }
        int categoryCount = buf.m_130242_();
        for (int i = 0; i < categoryCount; ++i) {
            String category = buf.m_130277_();
            int propertyCount = buf.m_130242_();
            for (int j = 0; j < propertyCount; ++j) {
                String property = buf.m_130277_();
                ConfiguredProperty<?> propertySchema = schema.findProperty(category, property);
                ClientboundConfigPacket.decodePropertyInto(propertySchema, buf, config);
            }
        }
        return new ClientboundConfigPacket(schema, config);
    }

    public static void encode(ClientboundConfigPacket packet, FriendlyByteBuf buf) {
        ByteBufCodecs.RESOURCE_LOCATION.encode(buf, packet.schema.identifier());
        List<ConfiguredProperty> rootProperties = packet.schema.rootProperties().stream().filter(ConfiguredProperty::synced).toList();
        buf.m_130130_(rootProperties.size());
        for (ConfiguredProperty rootProperty : rootProperties) {
            buf.m_130070_(rootProperty.name());
            ClientboundConfigPacket.encodeProperty(rootProperty, buf, packet.config);
        }
        List<ConfigCategory> categories = packet.schema.categories().stream().filter(ConfigSync::hasSyncedProperties).toList();
        buf.m_130130_(categories.size());
        for (ConfigCategory category : categories) {
            buf.m_130070_(category.name());
            List<ConfiguredProperty> properties = category.properties().stream().filter(ConfiguredProperty::synced).toList();
            buf.m_130130_(properties.size());
            for (ConfiguredProperty property : properties) {
                buf.m_130070_(property.name());
                ClientboundConfigPacket.encodeProperty(property, buf, packet.config);
            }
        }
    }

    private static <T> void decodePropertyInto(ConfiguredProperty<T> property, FriendlyByteBuf buf, MutableLoadedConfig config) {
        T value = property.streamCodec().decode(buf);
        config.setRaw(property, value);
    }

    private static <T> void encodeProperty(ConfiguredProperty<T> property, FriendlyByteBuf buf, LoadedConfig config) {
        T value = config.getRaw(property);
        property.streamCodec().encode(buf, value);
    }

    public static void handle(Player player, ClientboundConfigPacket packet) {
        Predicate<ConfiguredProperty<?>> predicate;
        MutableLoadedConfig localConfig = Balm.getConfig().getLocalConfig(packet.schema);
        MutableLoadedConfig newConfig = localConfig.copy();
        Object object = packet.config;
        if (object instanceof PropertyAwareConfig) {
            PropertyAwareConfig propertyAwareConfig = (PropertyAwareConfig)object;
            predicate = propertyAwareConfig::hasProperty;
        } else {
            predicate = it -> true;
        }
        Predicate<ConfiguredProperty<?>> propertyFilter = predicate;
        newConfig.applyFrom(packet.schema, packet.config, propertyFilter);
        object = Balm.getConfig();
        if (object instanceof AbstractBalmConfig) {
            AbstractBalmConfig config = (AbstractBalmConfig)object;
            config.setActiveConfig(packet.schema, newConfig);
        }
    }
}

