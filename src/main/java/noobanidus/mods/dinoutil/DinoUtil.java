package noobanidus.mods.dinoutil;

import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
@Mod(modid = DinoUtil.MODID, name = DinoUtil.MODNAME, version = DinoUtil.VERSION/*, dependencies = "required-after:fossil"*/)
@SuppressWarnings("WeakerAccess")
public class DinoUtil {
  public static final String MODID = "dinoutil";
  public static final String MODNAME = "DinoUtil";
  public static final String VERSION = "GRADLE:VERSION";

  public static Logger LOG = LogManager.getLogger(MODID);

  @SuppressWarnings("unused")
  @Mod.Instance(DinoUtil.MODID)
  public static DinoUtil instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    ConfigDino.init();
  }

  @SuppressWarnings("unchecked")
  @Mod.EventHandler
  public void init (FMLInitializationEvent event) {
    for (ResourceLocation dino : ConfigDino.DINOS) {
      Class<? extends Entity> clazz = EntityList.getClass(dino);
      if (clazz == null || EntityLiving.class.isAssignableFrom(clazz)) {
        LOG.error("Invalid entity ID: " + dino.toString() + " or not a living entity.");
      } else {
        ConfigDino.DinoConfig conf = ConfigDino.getConfig(dino);
        List<Biome> biomes = conf.biomes().stream().map(o -> {
          Biome b = ForgeRegistries.BIOMES.getValue(o);
          if (b == null) {
            LOG.error("Invalid biome for dino config (" + dino.toString() + "), biome " + o.toString() + " does not exist.");
            return null;
          } else {
            return b;
          }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (biomes.isEmpty()) {
          LOG.error("Invalid configuration for dino config (" + dino.toString() + "), biome list is empty.");
        } else {
          EntityRegistry.addSpawn((Class<? extends EntityLiving>) clazz, conf.weight(), conf.minGroup(), conf.maxGroup(), conf.type(), biomes.toArray(new Biome[0]));
        }
      }
    }
  }
}
