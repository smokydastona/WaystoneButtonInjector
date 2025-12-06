/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.saveddata.SavedData
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.worldgen.namegen;

import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.GenerateWaystoneNameEvent;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.worldgen.namegen.CustomNameGenerator;
import net.blay09.mods.waystones.worldgen.namegen.INameGenerator;
import net.blay09.mods.waystones.worldgen.namegen.MixedNameGenerator;
import net.blay09.mods.waystones.worldgen.namegen.MrPorkNameGenerator;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;
import net.blay09.mods.waystones.worldgen.namegen.RomanNumber;
import net.blay09.mods.waystones.worldgen.namegen.SequencedNameGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class NameGenerator
extends SavedData {
    private static final String DATA_NAME = "waystones_NameGenerator";
    private static final String USED_NAMES = "UsedNames";
    private static final NameGenerator clientStorageCopy = new NameGenerator();
    private final Set<String> usedNames = Sets.newHashSet();

    private INameGenerator getNameGenerator(NameGenerationMode nameGenerationMode) {
        switch (nameGenerationMode) {
            case MIXED: {
                return new MixedNameGenerator(new MrPorkNameGenerator(), new CustomNameGenerator(false, this.usedNames));
            }
            case RANDOM_ONLY: {
                return new MrPorkNameGenerator();
            }
            case PRESET_ONLY: {
                return new CustomNameGenerator(true, this.usedNames);
            }
        }
        return new SequencedNameGenerator(new CustomNameGenerator(false, this.usedNames), new MrPorkNameGenerator());
    }

    public synchronized String getName(IWaystone waystone, RandomSource rand, NameGenerationMode nameGenerationMode) {
        INameGenerator nameGenerator = this.getNameGenerator(nameGenerationMode);
        String originalName = nameGenerator.randomName(rand);
        if (originalName == null) {
            originalName = Objects.requireNonNull(new MrPorkNameGenerator().randomName(rand));
        }
        String name = this.resolveDuplicate(originalName);
        GenerateWaystoneNameEvent event = new GenerateWaystoneNameEvent(waystone, name);
        Balm.getEvents().fireEvent((Object)event);
        name = event.getName();
        this.usedNames.add(name);
        this.m_77762_();
        return name;
    }

    private String resolveDuplicate(String name) {
        Object tryName = name;
        int i = 1;
        while (this.usedNames.contains(tryName)) {
            tryName = name + " " + RomanNumber.toRoman(i);
            ++i;
        }
        return tryName;
    }

    public static NameGenerator load(CompoundTag compound) {
        NameGenerator nameGenerator = new NameGenerator();
        ListTag tagList = compound.m_128437_(USED_NAMES, 8);
        for (Tag tag : tagList) {
            nameGenerator.usedNames.add(tag.m_7916_());
        }
        return nameGenerator;
    }

    public CompoundTag m_7176_(CompoundTag compound) {
        ListTag tagList = new ListTag();
        for (String entry : this.usedNames) {
            tagList.add((Object)StringTag.m_129297_((String)entry));
        }
        compound.m_128365_(USED_NAMES, (Tag)tagList);
        return compound;
    }

    public static NameGenerator get(@Nullable MinecraftServer server) {
        if (server != null) {
            ServerLevel overworld = server.m_129880_(Level.f_46428_);
            return (NameGenerator)Objects.requireNonNull(overworld).m_8895_().m_164861_(NameGenerator::load, NameGenerator::new, DATA_NAME);
        }
        return clientStorageCopy;
    }
}

