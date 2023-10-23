package codyhuh.spawnlist;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mod(SpawnList.MOD_ID)
public class SpawnList {
    public static final String MOD_ID = "spawnlist";

    public SpawnList() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(this::printSpawns);
    }

    private void printSpawns(ServerStartingEvent e) {
        try {
            File file = new File("spawns.txt");
            if (file.createNewFile()) {
                System.out.println(file.getName() + " created!");
            } else {
                System.out.println("File already exists at.");
            }
        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("spawns.txt");
            myWriter.write("-- Entity Spawns --");

            for (var biomes : ForgeRegistries.BIOMES.getEntries()) {
                Biome biome = biomes.getValue();
                String biomePath = biomes.getKey().location().getPath();

                myWriter.write("\r\n\r\n" + biomePath.toUpperCase() + " contains the following spawns:");

                for (MobCategory value : MobCategory.values()) {
                    if (!biome.getMobSettings().getMobs(MobCategory.byName(value.getName())).isEmpty()) {
                        myWriter.write("\r\n  " + value);
                    }

                    for (MobSpawnSettings.SpawnerData data : biome.getMobSettings().getMobs(MobCategory.byName(value.getName())).unwrap()) {
                        myWriter.write("\r\n   - " + Registry.ENTITY_TYPE.getKey(data.type).getPath());
                    }
                }
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }
    }

}
