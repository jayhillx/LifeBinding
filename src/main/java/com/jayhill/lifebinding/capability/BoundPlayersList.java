package com.jayhill.lifebinding.capability;

import com.google.common.base.Charsets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("all")
public class BoundPlayersList {

    private static Map<UUID, String> map = new HashMap();

    private static final Path saveFile = FMLLoader.getGamePath().resolve("boundplayers.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = LogManager.getLogger(UsernameCache.class);
    private static final Marker USRCACHE = MarkerManager.getMarker("USERNAMECACHE");

    private BoundPlayersList() {}

    /** Set a player's current username. */
    public static void setUsername(UUID uuid, String string) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(string);

        map.put(uuid, string);
        save();
    }

    /** Remove a player's username from the cache. */
    public static boolean removeUsername(UUID uuid) {
        Objects.requireNonNull(uuid);

        if (map.remove(uuid) != null) {
            save();
        }
        return false;
    }

    /** Check if the cache contains the given player's username. */
    public static boolean containsUUID(UUID uuid) {
        Objects.requireNonNull(uuid);
        return map.containsKey(uuid);
    }

    /** * Save the cache to file. */
    protected static void save() {
        new SaveThread(gson.toJson(map)).start();
    }

    /** Load the cache from file. */
    public static void load() {
        if (!Files.exists(saveFile)) return;

        try (final BufferedReader reader = Files.newBufferedReader(saveFile, Charsets.UTF_8)) {
            @SuppressWarnings("serial")
            Type type = new TypeToken<Map<UUID, String>>(){}.getType();
            map = gson.fromJson(reader, type);
        }

        catch (JsonSyntaxException | IOException e) {
            LOGGER.error(USRCACHE,"Could not parse username cache file as valid json, deleting file {}", saveFile, e);
            try {
                Files.delete(saveFile);
            }
            catch (IOException e1) {
                LOGGER.error(USRCACHE,"Could not delete file {}", saveFile.toString());
            }
        }

        finally {
            if (map == null) {
                map = new HashMap<>();
            }
        }
    }

    private static class SaveThread extends Thread {
        /** The data that will be saved to disk. */
        private final String data;

        public SaveThread(String data){
            this.data = data;
        }

        @Override
        public void run() {
            try {
                synchronized (saveFile) {
                    Files.write(saveFile, data.getBytes(StandardCharsets.UTF_8));
                }
            }
            catch (IOException e) {
                LOGGER.error(USRCACHE, "Failed to save username cache to file!", e);
            }
        }
    }

}