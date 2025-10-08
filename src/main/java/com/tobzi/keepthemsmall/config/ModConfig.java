package com.tobzi.keepthemsmall.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tobzi.keepthemsmall.KeepThemSmall;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModConfig {

    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("keepthemsmall.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static List<String> GLOBAL_NAMES = new ArrayList<>();
    public static List<SpecificNameEntry> SPECIFIC_NAMES = new ArrayList<>();

    private static class SpecificNameEntry {
        String entity;
        List<String> names;
    }

    private static class ConfigData {
        List<String> global_names;
        List<SpecificNameEntry> specific_names;
    }

    public static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try {
            String json = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);
            ConfigData configData = GSON.fromJson(json, ConfigData.class);

            GLOBAL_NAMES = configData.global_names.stream()
                    .map(String::toLowerCase)
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.toList());

            SPECIFIC_NAMES = configData.specific_names;
            for (SpecificNameEntry entry : SPECIFIC_NAMES) {
                if (entry.entity == null || entry.entity.isEmpty() || entry.names == null) continue;
                entry.names = entry.names.stream()
                        .map(String::toLowerCase)
                        .filter(name -> !name.isEmpty())
                        .collect(Collectors.toList());
            }
            SPECIFIC_NAMES.removeIf(entry -> entry.entity.isEmpty() || entry.names.isEmpty());

        } catch (IOException e) {
            KeepThemSmall.LOGGER.error("Failed to load JSON config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_DIR);

            ConfigData configData = new ConfigData();

            configData.global_names = new ArrayList<>(Arrays.asList(""));
            configData.specific_names = new ArrayList<>(Arrays.asList(
                    createSpecificEntry("", Arrays.asList(""))
            ));

            String json = GSON.toJson(configData);
            Files.writeString(CONFIG_FILE, json, StandardCharsets.UTF_8);

        } catch (IOException e) {
            KeepThemSmall.LOGGER.error("Failed to save JSON config", e);
        }
    }

    private static SpecificNameEntry createSpecificEntry(String entityId, List<String> names) {
        SpecificNameEntry entry = new SpecificNameEntry();
        entry.entity = entityId;
        entry.names = names;
        return entry;
    }

    public static boolean shouldStayBaby(PassiveEntity entity) {
        if (!entity.isBaby() || entity.getCustomName() == null) {
            return false;
        }

        String lowerCaseName = entity.getCustomName().getString().toLowerCase();

        if (GLOBAL_NAMES.contains(lowerCaseName)) {
            return true;
        }

        String entityId = EntityType.getId(entity.getType()).toString();
        for (SpecificNameEntry entry : SPECIFIC_NAMES) {
            if (entry.entity.equals(entityId)) {
                if (entry.names.contains(lowerCaseName)) {
                    return true;
                }
            }
        }

        return false;
    }
}