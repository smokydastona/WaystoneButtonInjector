/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.network.NetworkDirection
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.NetworkHooks
 *  net.minecraftforge.network.PacketDistributor
 *  net.minecraftforge.network.simple.SimpleChannel
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.blay09.mods.balm.forge.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.blay09.mods.balm.forge.network.NetworkChannels;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForgeBalmNetworking
implements BalmNetworking {
    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmNetworking.class);
    private static final Map<Class<?>, MessageRegistration<?>> messagesByClass = new ConcurrentHashMap();
    private static final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new ConcurrentHashMap();
    private static final Map<String, Integer> discriminatorCounter = new ConcurrentHashMap<String, Integer>();
    private static NetworkEvent.Context replyContext;

    private static int nextDiscriminator(String modId) {
        return discriminatorCounter.compute(modId, (key, prev) -> prev != null ? prev + 1 : 0);
    }

    @Override
    public void allowClientOnly(String modId) {
        NetworkChannels.allowClientOnly(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
        NetworkChannels.allowServerOnly(modId);
    }

    @Override
    public void openMenu(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer) {
            if (menuProvider instanceof BalmMenuProvider) {
                BalmMenuProvider balmMenuProvider = (BalmMenuProvider)menuProvider;
                NetworkHooks.openScreen((ServerPlayer)((ServerPlayer)player), (MenuProvider)menuProvider, buf -> balmMenuProvider.writeScreenOpeningData((ServerPlayer)player, (FriendlyByteBuf)buf));
            } else {
                NetworkHooks.openScreen((ServerPlayer)((ServerPlayer)player), (MenuProvider)menuProvider);
            }
        }
    }

    @Override
    public void defineNetworkVersion(String modId, String version) {
        NetworkChannels.defineNetworkVersion(modId, version);
    }

    @Override
    public <T> void reply(T message) {
        if (replyContext == null) {
            throw new IllegalStateException("No context to reply to");
        }
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.reply(message, replyContext);
    }

    @Override
    public <T> void sendTo(Player player, T message) {
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), message);
    }

    @Override
    public <T> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.m_46745_(pos)), message);
    }

    @Override
    public <T> void sendToTracking(Entity entity, T message) {
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    @Override
    public <T> void sendToAll(MinecraftServer server, T message) {
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    @Override
    public <T> void sendToServer(T message) {
        if (!Balm.getProxy().isConnected()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }
        MessageRegistration<T> messageRegistration = this.getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.sendToServer(message);
    }

    private <T> MessageRegistration<T> getMessageRegistrationOrThrow(T message) {
        MessageRegistration<?> messageRegistration = messagesByClass.get(message.getClass());
        if (messageRegistration == null) {
            throw new IllegalArgumentException("Cannot send message " + String.valueOf(message.getClass()) + " as it is not registered");
        }
        return messageRegistration;
    }

    @Override
    public <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ClientboundMessageRegistration<T> messageRegistration = new ClientboundMessageRegistration<T>(identifier, clazz, encodeFunc, decodeFunc, handler);
        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.registerMessage(ForgeBalmNetworking.nextDiscriminator(identifier.m_135827_()), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = (NetworkEvent.Context)contextSupplier.get();
            if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                logger.warn("Received {} on incorrect side {}", (Object)identifier, (Object)context.getDirection());
                return;
            }
            context.enqueueWork(() -> handler.accept(BalmClient.getClientPlayer(), message));
            context.setPacketHandled(true);
        });
    }

    @Override
    public <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        ServerboundMessageRegistration<T> messageRegistration = new ServerboundMessageRegistration<T>(identifier, clazz, encodeFunc, decodeFunc, handler);
        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);
        SimpleChannel channel = NetworkChannels.get(identifier.m_135827_());
        channel.registerMessage(ForgeBalmNetworking.nextDiscriminator(identifier.m_135827_()), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = (NetworkEvent.Context)contextSupplier.get();
            if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER) {
                logger.warn("Received {} on incorrect side {}", (Object)identifier, (Object)context.getDirection());
                return;
            }
            context.enqueueWork(() -> {
                replyContext = context;
                ServerPlayer player = context.getSender();
                handler.accept(player, message);
                replyContext = null;
            });
            context.setPacketHandled(true);
        });
    }
}

