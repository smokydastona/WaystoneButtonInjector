/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.network;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Synced;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval=true, since="1.21.5")
public class SyncConfigMessage<TData> {
    private final TData data;

    public SyncConfigMessage(TData data) {
        this.data = data;
    }

    public static <TData> Supplier<TData> createDeepCopyFactory(Supplier<TData> from, Supplier<TData> factory) {
        return () -> {
            Object to = factory.get();
            ConfigReflection.deepCopy(from.get(), to);
            return to;
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> Function<FriendlyByteBuf, TMessage> createDecoder(Class<?> clazz, Function<TData, TMessage> messageFactory, Supplier<TData> dataFactory) {
        return buf -> {
            Object data = dataFactory.get();
            SyncConfigMessage.readSyncedFields(buf, data, false);
            return (SyncConfigMessage)messageFactory.apply(data);
        };
    }

    private static <TData> void readSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            try {
                Object value;
                if (String.class.isAssignableFrom(type)) {
                    value = buf.m_130277_();
                } else if (Enum.class.isAssignableFrom(type)) {
                    value = type.getEnumConstants()[buf.readByte()];
                } else if (Integer.TYPE.isAssignableFrom(type)) {
                    value = buf.readInt();
                } else if (Float.TYPE.isAssignableFrom(type)) {
                    value = Float.valueOf(buf.readFloat());
                } else if (Double.TYPE.isAssignableFrom(type)) {
                    value = buf.readDouble();
                } else if (Boolean.TYPE.isAssignableFrom(type)) {
                    value = buf.readBoolean();
                } else if (Long.TYPE.isAssignableFrom(type)) {
                    value = buf.readLong();
                } else {
                    value = field.get(data);
                    SyncConfigMessage.readSyncedFields(buf, value, field.getAnnotation(Synced.class) != null);
                }
                field.set(data, value);
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<TMessage, FriendlyByteBuf> createEncoder(Class<TData> clazz) {
        return (message, buf) -> {
            Object data = message.getData();
            SyncConfigMessage.writeSyncedFields(buf, data, false);
        };
    }

    private static <TData> void writeSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            try {
                Object value = field.get(data);
                if (String.class.isAssignableFrom(type)) {
                    buf.m_130070_((String)value);
                    continue;
                }
                if (Enum.class.isAssignableFrom(type)) {
                    buf.writeByte(((Enum)value).ordinal());
                    continue;
                }
                if (Integer.TYPE.isAssignableFrom(type)) {
                    buf.writeInt(((Integer)value).intValue());
                    continue;
                }
                if (Float.TYPE.isAssignableFrom(type)) {
                    buf.writeFloat(((Float)value).floatValue());
                    continue;
                }
                if (Double.TYPE.isAssignableFrom(type)) {
                    buf.writeDouble(((Double)value).doubleValue());
                    continue;
                }
                if (Boolean.TYPE.isAssignableFrom(type)) {
                    buf.writeBoolean(((Boolean)value).booleanValue());
                    continue;
                }
                if (Long.TYPE.isAssignableFrom(type)) {
                    buf.writeLong(((Long)value).longValue());
                    continue;
                }
                SyncConfigMessage.writeSyncedFields(buf, field.get(data), field.getAnnotation(Synced.class) != null);
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TMessage extends SyncConfigMessage<TData>, TData extends BalmConfigData> void register(ResourceLocation resourceLocation, Class<TMessage> messageClass, Function<TData, TMessage> messageFactory, Class<TData> dataClass, Supplier<TData> dataFactory) {
        Supplier<BalmConfigData> copyFactory = SyncConfigMessage.createDeepCopyFactory(() -> Balm.getConfig().getBackingConfig(dataClass), dataFactory);
        Balm.getNetworking().registerClientboundPacket(resourceLocation, messageClass, (message, buf) -> {
            BalmConfigData data = (BalmConfigData)message.getData();
            SyncConfigMessage.writeSyncedFields(buf, data, false);
        }, buf -> {
            BalmConfigData data = (BalmConfigData)copyFactory.get();
            SyncConfigMessage.readSyncedFields(buf, data, false);
            return (SyncConfigMessage)messageFactory.apply(data);
        }, Balm.getConfig()::handleSync);
    }

    public TData getData() {
        return this.data;
    }
}

