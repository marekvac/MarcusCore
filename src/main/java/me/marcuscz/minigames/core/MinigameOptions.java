package me.marcuscz.minigames.core;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public abstract class MinigameOptions {

    public String filename;

    public static <T extends MinigameOptions> T load(Supplier<T> c) throws FileNotFoundException {
        T settings = c.get();
        File f = new File(Core.getInstance().getDataFolder().getPath() + File.separator + settings.filename);
        Gson gson = Core.gson;
        if (f.exists()) {
            FileReader fileReader = new FileReader(f);
            settings = gson.fromJson(fileReader, (Type) c.get().getClass());
        } else {
            settings.save();
        }
        return settings;
    }

    public void save() {
        File f = new File(Core.getInstance().getDataFolder().getPath() + File.separator + filename);
        Gson gson = Core.gson;
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(gson.toJson(this));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LocationAdapter extends TypeAdapter<Location> {

        @Override
        public void write(JsonWriter out, Location value) throws IOException {
            if (value == null || value.getWorld() == null) {
                out.nullValue();
            } else {
                out.beginObject();
                out.name("x");
                out.value(value.getX());
                out.name("y");
                out.value(value.getY());
                out.name("z");
                out.value(value.getZ());
                out.name("yaw");
                out.value(value.getYaw());
                out.name("pitch");
                out.value(value.getPitch());
                out.name("world");
                out.value(value.getWorld().getName());
                out.endObject();
            }
        }

        @Override
        public Location read(JsonReader in) throws IOException {
            in.beginObject();
            String fieldname = null;
            double x = 0;
            double y = 0;
            double z = 0;
            float yaw = 0;
            float pitch = 0;
            String world = null;
            while (in.hasNext()) {
                JsonToken token = in.peek();

                if (token.equals(JsonToken.NAME)) {
                    fieldname = in.nextName();
                }

                if ("x".equals(fieldname)) {
                    in.peek();
                    x = in.nextDouble();
                }
                if ("y".equals(fieldname)) {
                    in.peek();
                    y = in.nextDouble();
                }
                if ("z".equals(fieldname)) {
                    in.peek();
                    z = in.nextDouble();
                }
                if ("yaw".equals(fieldname)) {
                    in.peek();
                    yaw = (float) in.nextDouble();
                }
                if ("pitch".equals(fieldname)) {
                    in.peek();
                    pitch = (float) in.nextDouble();
                }
                if ("world".equals(fieldname)) {
                    in.peek();
                    world = in.nextString();
                }
            }
            in.endObject();
            if (world == null) return null;
            return new Location(Core.getInstance().getServer().getWorld(world),x,y,z,yaw,pitch);
        }
    }

}
