package com.mash.disableenderpearl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EnderPearlConfig {
    private static final File CONFIG_FILE = new File("config/disable_enderpearl.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    // Publicly accessible list used in the mixin
    public static Set<Identifier> blockedDimensions = new HashSet<>();

    public static void load() {

        try {
            if (!CONFIG_FILE.exists()) {
                // Default config
                EnderPearlConfigData defaultData = new EnderPearlConfigData();
                defaultData.blocked_dimensions = Collections.singleton("minecraft:the_nether");

                String json = GSON.toJson(defaultData);
                CONFIG_FILE.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                    writer.write(json);
                }
            }

            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                EnderPearlConfigData data = GSON.fromJson(reader, EnderPearlConfigData.class);

                // ✅ Replace the existing set instead of adding to it
                blockedDimensions.clear();
                for (String id : data.blocked_dimensions) {
                    blockedDimensions.add(Identifier.of(id));
                }

                System.out.println(GREEN + "[DisableEnderPearl]" + RESET + " Loaded blocked dimensions: " + blockedDimensions);
            }
        } catch (Exception e) {
            System.err.println(GREEN + "[DisableEnderPearl]" + RESET + " Failed to load config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class EnderPearlConfigData {
        public Set<String> blocked_dimensions;
    }
}
