package noobanidus.mods.dinoutil;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noobanidus.mods.dinoutil.init.ModItems;
import noobanidus.mods.dinoutil.pouch.GuiHandler;
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

  public static final GuiHandler GUI_HANDLER = new GuiHandler();

  public static Item POUCH_ITEM = null;

  public static Logger LOG = LogManager.getLogger(MODID);

  public static Item.ToolMaterial RAINBOW = EnumHelper.addToolMaterial("dinoutil:rainbow", 4, 666, 4.5f, 3.5f, 20);
  public static Item.ToolMaterial SUGAR = EnumHelper.addToolMaterial("dinoutil:sugar", 4, 666, 4.5f, 0f, 20);


  public static CreativeTabs tab = new CreativeTabs(DinoUtil.MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.pouch);
    }
  };

  @SuppressWarnings("unused")
  @Mod.Instance(DinoUtil.MODID)
  public static DinoUtil instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    ConfigDino.init();
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, GUI_HANDLER);
  }

  @SuppressWarnings("unchecked")
  @Mod.EventHandler
  public void init (FMLInitializationEvent event) {
    for (ResourceLocation dino : ConfigDino.DINOS) {
      EntityEntry entry = ForgeRegistries.ENTITIES.getValue(dino);
      if (entry == null) {
        LOG.error("Invalid entity ID: " + dino.toString());
      } else {
        Class clazz = entry.getEntityClass();
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
    POUCH_ITEM = ForgeRegistries.ITEMS.getValue(new ResourceLocation("mysticalworld", "pearl"));
  }
}
