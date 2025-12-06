/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.electronwill.nightconfig.core.EnumGetMethod
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.common.ForgeConfigSpec
 *  net.minecraftforge.common.ForgeConfigSpec$Builder
 *  net.minecraftforge.common.ForgeConfigSpec$ConfigValue
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.ModContainer
 *  net.minecraftforge.fml.ModList
 *  net.minecraftforge.fml.config.IConfigSpec
 *  net.minecraftforge.fml.config.ModConfig
 *  net.minecraftforge.fml.config.ModConfig$Type
 *  net.minecraftforge.fml.loading.FMLPaths
 */
package net.blay09.mods.balm.forge.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredBoolean;
import net.blay09.mods.balm.api.config.schema.ConfiguredDouble;
import net.blay09.mods.balm.api.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.api.config.schema.ConfiguredFloat;
import net.blay09.mods.balm.api.config.schema.ConfiguredInt;
import net.blay09.mods.balm.api.config.schema.ConfiguredList;
import net.blay09.mods.balm.api.config.schema.ConfiguredLong;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.ConfiguredResourceLocation;
import net.blay09.mods.balm.api.config.schema.ConfiguredSet;
import net.blay09.mods.balm.api.config.schema.ConfiguredString;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.event.ConfigLoadedEvent;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.blay09.mods.balm.common.config.ConfigLocalization;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.config.LoadedForgeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeBalmConfig
extends AbstractBalmConfig {
    private static final Map<ResourceLocation, Table<String, String, ForgeConfigSpec.ConfigValue<?>>> properties = new ConcurrentHashMap();
    private static final Map<ResourceLocation, ModConfig> modConfigs = new ConcurrentHashMap<ResourceLocation, ModConfig>();

    private static ForgeConfigSpec.ConfigValue<?> addPropertyToSpec(ConfiguredProperty<?> property, ForgeConfigSpec.Builder spec) {
        spec.comment(property.comment());
        spec.translation(ConfigLocalization.forProperty(property));
        if (property instanceof ConfiguredBoolean) {
            ConfiguredBoolean configuredBoolean = (ConfiguredBoolean)property;
            return spec.define(configuredBoolean.name(), ((Boolean)configuredBoolean.defaultValue()).booleanValue());
        }
        if (property instanceof ConfiguredDouble) {
            ConfiguredDouble configuredDouble = (ConfiguredDouble)property;
            return spec.define(configuredDouble.name(), (Object)((Double)configuredDouble.defaultValue()));
        }
        if (property instanceof ConfiguredEnum) {
            ConfiguredEnum configuredEnum = (ConfiguredEnum)property;
            return ForgeBalmConfig.defineEnum(spec, configuredEnum);
        }
        if (property instanceof ConfiguredFloat) {
            ConfiguredFloat configuredFloat = (ConfiguredFloat)property;
            return spec.define(configuredFloat.name(), (Object)((Float)configuredFloat.defaultValue()).doubleValue());
        }
        if (property instanceof ConfiguredInt) {
            ConfiguredInt configuredInt = (ConfiguredInt)property;
            return spec.define(configuredInt.name(), (Object)((Integer)configuredInt.defaultValue()));
        }
        if (property instanceof ConfiguredList) {
            ConfiguredList configuredList = (ConfiguredList)property;
            return spec.defineListAllowEmpty(configuredList.name(), ForgeBalmConfig.mapConfigCollectionToNeoForge((Collection)configuredList.defaultValue()), it -> ForgeBalmConfig.validateListElement(configuredList, it));
        }
        if (property instanceof ConfiguredLong) {
            ConfiguredLong configuredLong = (ConfiguredLong)property;
            return spec.define(configuredLong.name(), (Object)((Long)configuredLong.defaultValue()));
        }
        if (property instanceof ConfiguredResourceLocation) {
            ConfiguredResourceLocation configuredResourceLocation = (ConfiguredResourceLocation)property;
            return spec.define(configuredResourceLocation.name(), (Object)((ResourceLocation)configuredResourceLocation.defaultValue()).toString());
        }
        if (property instanceof ConfiguredSet) {
            ConfiguredSet configuredSet = (ConfiguredSet)property;
            return spec.defineListAllowEmpty(configuredSet.name(), ForgeBalmConfig.mapConfigCollectionToNeoForge((Collection)configuredSet.defaultValue()), it -> ForgeBalmConfig.validateSetElement(configuredSet, it));
        }
        if (property instanceof ConfiguredString) {
            ConfiguredString configuredString = (ConfiguredString)property;
            return spec.define(configuredString.name(), (Object)((String)configuredString.defaultValue()));
        }
        throw new IllegalStateException("Unexpected value: " + String.valueOf(property));
    }

    public static List<?> mapConfigCollectionToNeoForge(Collection<?> values) {
        return values.stream().map(ForgeBalmConfig::mapConfigValueToNeoForge).toList();
    }

    public static Object mapConfigValueToNeoForge(Object value) {
        if (value instanceof ResourceLocation) {
            ResourceLocation resourceLocation = (ResourceLocation)value;
            return resourceLocation.toString();
        }
        if (value instanceof Float) {
            Float floatValue = (Float)value;
            return floatValue.doubleValue();
        }
        if (value instanceof Set) {
            Set set = (Set)value;
            return ForgeBalmConfig.mapConfigCollectionToNeoForge(set);
        }
        if (value instanceof List) {
            List list = (List)value;
            return ForgeBalmConfig.mapConfigCollectionToNeoForge(list);
        }
        return value;
    }

    public static List<?> mapConfigListFromNeoForge(ConfiguredList<?> property, List<?> value) {
        return value.stream().map(it -> ForgeBalmConfig.mapConfigValueFromNeoForge(property.nestedType(), it)).toList();
    }

    public static Set<?> mapConfigSetFromNeoForge(ConfiguredSet<?> property, List<?> value) {
        return value.stream().map(it -> ForgeBalmConfig.mapConfigValueFromNeoForge(property.nestedType(), it)).collect(Collectors.toSet());
    }

    public static Object mapConfigValueFromNeoForge(ConfiguredProperty<?> property, Object value) {
        if (property instanceof ConfiguredResourceLocation) {
            return new ResourceLocation((String)value);
        }
        if (property instanceof ConfiguredFloat) {
            return Float.valueOf(((Double)value).floatValue());
        }
        if (property instanceof ConfiguredList) {
            ConfiguredList listProperty = (ConfiguredList)property;
            return ForgeBalmConfig.mapConfigListFromNeoForge(listProperty, (List)value);
        }
        if (property instanceof ConfiguredSet) {
            ConfiguredSet setProperty = (ConfiguredSet)property;
            return ForgeBalmConfig.mapConfigSetFromNeoForge(setProperty, (List)value);
        }
        return value;
    }

    private static Object mapConfigValueFromNeoForge(Class<?> nestedType, Object value) {
        if (nestedType == ResourceLocation.class) {
            return new ResourceLocation((String)value);
        }
        if (nestedType == Float.class) {
            return Float.valueOf(((Double)value).floatValue());
        }
        if (nestedType.isEnum() && value instanceof String) {
            return ForgeBalmConfig.stringToEnum(value, nestedType);
        }
        return value;
    }

    private static <T> boolean validateListElement(ConfiguredList<T> configuredList, Object value) {
        return ForgeBalmConfig.validateCollectionElement(configuredList.nestedType(), value);
    }

    private static <T> boolean validateSetElement(ConfiguredSet<T> configuredSet, Object value) {
        return ForgeBalmConfig.validateCollectionElement(configuredSet.nestedType(), value);
    }

    private static <T> boolean validateCollectionElement(Class<T> nestedType, Object value) {
        if (nestedType == Boolean.class) {
            return value instanceof Boolean || "true".equals(value) || "false".equals(value);
        }
        if (nestedType == Double.class) {
            try {
                return value instanceof Double || !Double.isNaN(Double.parseDouble(value.toString()));
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        if (nestedType == Float.class) {
            try {
                return value instanceof Float || !Float.isNaN(Float.parseFloat(value.toString()));
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        if (nestedType == Integer.class) {
            try {
                if (value instanceof Integer) {
                    return true;
                }
                Integer.parseInt(value.toString());
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        if (nestedType == Long.class) {
            try {
                if (value instanceof Long) {
                    return true;
                }
                Long.parseLong(value.toString());
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        if (nestedType == ResourceLocation.class) {
            return value instanceof String && ResourceLocation.m_135820_((String)value.toString()) != null;
        }
        if (nestedType == String.class) {
            return value instanceof String;
        }
        if (nestedType.isEnum()) {
            return value instanceof String && ForgeBalmConfig.validateEnum(value, nestedType);
        }
        throw new IllegalArgumentException("Unsupported type " + String.valueOf(nestedType));
    }

    private static <T extends Enum<T>> boolean validateEnum(Object value, Class<?> unknownClass) {
        if (unknownClass.isEnum()) {
            return EnumGetMethod.NAME_IGNORECASE.validate(value, unknownClass);
        }
        throw new IllegalArgumentException("Not an enum class: " + unknownClass.getName());
    }

    private static <T extends Enum<T>> T stringToEnum(Object value, Class<?> unknownClass) {
        if (unknownClass.isEnum()) {
            return (T)EnumGetMethod.NAME_IGNORECASE.get(value, unknownClass);
        }
        throw new IllegalArgumentException("Not an enum class: " + unknownClass.getName());
    }

    private static <T extends Enum<T>> ForgeConfigSpec.ConfigValue<T> defineEnum(ForgeConfigSpec.Builder spec, ConfiguredEnum<T> configuredEnum) {
        return spec.defineEnum(configuredEnum.name(), (Enum)configuredEnum.defaultValue(), EnumGetMethod.NAME_IGNORECASE);
    }

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        String stringType;
        super.registerConfig(schema);
        String namespace = schema.identifier().m_135827_();
        ModContainer modContainer = (ModContainer)ModList.get().getModContainerById(namespace).orElseThrow(() -> new IllegalStateException("Mod container for " + namespace + " not found when registering config."));
        IEventBus eventBus = BalmLoadContexts.get(namespace).map(it -> ((ForgeLoadContext)it).modEventBus()).orElse(null);
        if (eventBus == null) {
            throw new IllegalStateException("Missing event bus for " + namespace + " when registering config.");
        }
        eventBus.addListener(event -> {
            ModConfig modConfig = event.getConfig();
            ResourceLocation identifier = new ResourceLocation(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals((Object)identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                LoadedForgeConfig wrappedConfig = new LoadedForgeConfig(schema, modConfig, properties.get(schema.identifier()));
                this.setLocalConfig(schema, wrappedConfig);
                this.setActiveConfig(schema, wrappedConfig);
                this.fireConfigLoadHandlers(schema, wrappedConfig);
                Balm.getEvents().fireEvent(new ConfigLoadedEvent(schema));
            }
        });
        eventBus.addListener(event -> {
            ModConfig modConfig = event.getConfig();
            ResourceLocation identifier = new ResourceLocation(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals((Object)identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                LoadedForgeConfig wrappedConfig = new LoadedForgeConfig(schema, modConfig, properties.get(schema.identifier()));
                this.setLocalConfig(schema, wrappedConfig);
                this.updateActiveFromLocal(schema, wrappedConfig);
                Balm.getEvents().fireEvent(new ConfigReloadedEvent(schema));
            }
        });
        ModConfig.Type configType = switch (stringType = schema.identifier().m_135815_()) {
            case "common" -> ModConfig.Type.COMMON;
            case "client" -> ModConfig.Type.CLIENT;
            default -> throw new IllegalArgumentException("Unsupported config type: " + stringType + " - only 'common' and 'client' are supported.");
        };
        Pair<ForgeConfigSpec, HashBasedTable<String, String, ForgeConfigSpec.ConfigValue<?>>> mappedConfigSpec = this.mapToConfigSpec(schema);
        properties.put(schema.identifier(), (Table)mappedConfigSpec.getSecond());
        modContainer.addConfig(new ModConfig(configType, (IConfigSpec)mappedConfigSpec.getFirst(), modContainer));
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        super.saveLocalConfig(schema, config);
        ModConfig modConfig = modConfigs.get(schema.identifier());
        if (modConfig == null) {
            throw new IllegalStateException("Backing config not available for " + String.valueOf(schema.identifier()));
        }
        LoadedForgeConfig wrappedConfig = new LoadedForgeConfig(schema, modConfig, properties.get(schema.identifier()));
        wrappedConfig.applyFrom(schema, config);
        ((ForgeConfigSpec)modConfig.getSpec()).save();
    }

    private Pair<ForgeConfigSpec, HashBasedTable<String, String, ForgeConfigSpec.ConfigValue<?>>> mapToConfigSpec(BalmConfigSchema schema) {
        ForgeConfigSpec.Builder spec = new ForgeConfigSpec.Builder();
        HashBasedTable properties = HashBasedTable.create();
        for (ConfiguredProperty<?> rootProperty : schema.rootProperties()) {
            properties.put((Object)"", (Object)rootProperty.name(), ForgeBalmConfig.addPropertyToSpec(rootProperty, spec));
        }
        for (ConfigCategory category : schema.categories()) {
            spec.push(category.name());
            for (ConfiguredProperty<?> property : category.properties()) {
                properties.put((Object)category.name(), (Object)property.name(), ForgeBalmConfig.addPropertyToSpec(property, spec));
            }
            spec.pop();
        }
        return Pair.of((Object)spec.build(), (Object)properties);
    }
}

