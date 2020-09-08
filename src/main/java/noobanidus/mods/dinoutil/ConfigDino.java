package noobanidus.mods.dinoutil;

import com.google.common.collect.Sets;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigDino {
  public static Path PATH = Paths.get("config", "dinoutil.cfg");
  public static Set<ResourceLocation> DINOS = Sets.newHashSet(new ResourceLocation("fossil", "fossil.alligator_gar"), new ResourceLocation("fossil", "fossil.allosaurus"), new ResourceLocation("fossil", "fossil.ankylosaurus"), new ResourceLocation("fossil", "fossil.anu"), new ResourceLocation("fossil", "fossil.anubite"), new ResourceLocation("fossil", "fossil.arthropleura"), new ResourceLocation("fossil", "fossil.brachiosaurus"), new ResourceLocation("fossil", "fossil.ceratosaurus"), new ResourceLocation("fossil", "fossil.citipati"), new ResourceLocation("fossil", "fossil.coelacanth"), new ResourceLocation("fossil", "fossil.compsognathus"), new ResourceLocation("fossil", "fossil.confuciusornis"), new ResourceLocation("fossil", "fossil.crassigyrinus"), new ResourceLocation("fossil", "fossil.deinonychus"), new ResourceLocation("fossil", "fossil.dilophosaurus"), new ResourceLocation("fossil", "fossil.diplocaulus"), new ResourceLocation("fossil", "fossil.dodo"), new ResourceLocation("fossil", "fossil.dryosaurus"), new ResourceLocation("fossil", "fossil.edaphosaurus"), new ResourceLocation("fossil", "fossil.elasmotherium"), new ResourceLocation("fossil", "fossil.failuresaurus"), new ResourceLocation("fossil", "fossil.friendlypigzombie"), new ResourceLocation("fossil", "fossil.gallimimus"), new ResourceLocation("fossil", "fossil.gastornis"), new ResourceLocation("fossil", "fossil.henodus"), new ResourceLocation("fossil", "fossil.icthyosaurus"), new ResourceLocation("fossil", "fossil.kelenken"), new ResourceLocation("fossil", "fossil.liopleurodon"), new ResourceLocation("fossil", "fossil.mammoth"), new ResourceLocation("fossil", "fossil.megalania"), new ResourceLocation("fossil", "fossil.megaloceros"), new ResourceLocation("fossil", "fossil.megalodon"), new ResourceLocation("fossil", "fossil.megalograptus"), new ResourceLocation("fossil", "fossil.meganeura"), new ResourceLocation("fossil", "fossil.mosasaurus"), new ResourceLocation("fossil", "fossil.nautilus"), new ResourceLocation("fossil", "fossil.pachycephalosaurus"), new ResourceLocation("fossil", "fossil.parasaurolophus"), new ResourceLocation("fossil", "fossil.phorusrhacos"), new ResourceLocation("fossil", "fossil.platybelodon"), new ResourceLocation("fossil", "fossil.plesiosaur"), new ResourceLocation("fossil", "fossil.pterosaur"), new ResourceLocation("fossil", "fossil.quagga"), new ResourceLocation("fossil", "fossil.sarcosuchus"), new ResourceLocation("fossil", "fossil.sentrypigman"), new ResourceLocation("fossil", "fossil.smilodon"), new ResourceLocation("fossil", "fossil.spinosaurus"), new ResourceLocation("fossil", "fossil.stegosaurus"), new ResourceLocation("fossil", "fossil.sturgeon"), new ResourceLocation("fossil", "fossil.tarslime"), new ResourceLocation("fossil", "fossil.therizinosaurus"), new ResourceLocation("fossil", "fossil.tiktaalik"), new ResourceLocation("fossil", "fossil.titanis"), new ResourceLocation("fossil", "fossil.triceratops"), new ResourceLocation("fossil", "fossil.tyrannosaurus"), new ResourceLocation("fossil", "fossil.velociraptor"));

  public static Configuration CONFIG = null;

  public static Map<ResourceLocation, DinoConfig> CONFIGS = new HashMap<>();

  private static String[] BEAN_CONFIG_RAW = null;
  private static List<WeightedBean> BEAN_CONFIG = null;

  public static class WeightedBean extends WeightedRandom.Item {
    private Item item;
    private int meta;
    private int count;

    public WeightedBean(Item item, int count, int meta, int weight) {
      super(weight);
      this.item = item;
      this.count = count;
      this.meta = meta;
    }

    public ItemStack getItem () {
      return new ItemStack(item, count, meta);
    }

    @Nullable
    public static WeightedBean fromString (String string) {
      if (string.isEmpty()) {
        return null;
      }
      String[] stuff = string.split(";");
      if (stuff.length != 3) {
        throw new IllegalStateException("Config requires three values separated by ;, got: " + string);
      }
      String[] itemStuff = stuff[0].split(":");
      if (itemStuff.length != 3) {
        throw new IllegalStateException("Item config requires three values (mod, item, meta)");
      }
      ResourceLocation rl = new ResourceLocation(itemStuff[0], itemStuff[1]);
      Item item = ForgeRegistries.ITEMS.getValue(rl);
      if (item == null) {
        throw new IllegalStateException("Item specified in config does not exist: " + rl);
      }
      int meta = -1;
      int weight = -1;
      int count = -1;
      try {
        meta = Integer.valueOf(itemStuff[2]);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Meta specified for item is invalid: " + stuff[0], e);
      }
      try {
        count = Integer.valueOf(stuff[1]);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Count specified for row is invalid: " + string, e);
      }
      try {
        weight = Integer.valueOf(stuff[1]);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Weight specified for row is invalid: " + string, e);
      }
      return new WeightedBean(item, count, meta, weight);
    }
  }

  public static String[] getBeanConfigRaw () {
    if (BEAN_CONFIG_RAW == null) {
      BEAN_CONFIG_RAW = CONFIG.getStringList("bean_config", "beans", new String[]{"minecraft:diamond:0;1;1", "minecraft:coal:0;3;20"}, "a list of mod:item:meta;count;weight");
    }
    return BEAN_CONFIG_RAW;
  }

  public static List<WeightedBean> getBeanConfig () {
    if (BEAN_CONFIG == null) {
      BEAN_CONFIG = Stream.of(getBeanConfigRaw()).map(WeightedBean::fromString).filter(Objects::nonNull).collect(Collectors.toList());
    }
    return BEAN_CONFIG;
  }

  public static Random random = new Random();

  public static WeightedBean getRandomBean () {
    return WeightedRandom.getRandomItem(random, getBeanConfig());
  }

  public static float getGrowthChance (float value) {
    return value * CONFIG.getFloat("bean_growth", "bean", 0.65f, 0, Float.MAX_VALUE, "the value to multiply growth by (default 65% of vanilla growth)");
  }

  public static void init () {
    CONFIG = new Configuration(PATH.toFile());
    for (ResourceLocation entity : DINOS) {
      String category = entity.getPath().replace("fossil.", "");
      DinoConfig config = new DinoConfig(category);
      config.init();
      CONFIGS.put(entity, config);
    }
    getBeanConfigRaw();
    getGrowthChance(1.0f);

    CONFIG.save();
  }

  public static DinoConfig getConfig (ResourceLocation dino) {
    return CONFIGS.get(dino);
  }

  public static class DinoConfig {
    private final String category;
    private List<ResourceLocation> biomeNames = null;
    private int minGroup = -1;
    private int maxGroup = -1;
    private int weight = -1;
    private EnumCreatureType type = null;

    public DinoConfig(String category) {
      this.category = category;
    }

    public int minGroup () {
      if (minGroup == -1) {
        minGroup = CONFIG.getInt("minGroup", category, 1, 0, Integer.MAX_VALUE, "Minimum number of entities to spawn in a group");
      }
      return minGroup;
    }

    public int maxGroup () {
      if (maxGroup == -1) {
        maxGroup = CONFIG.getInt("maxGroup", category, 1, 0, Integer.MAX_VALUE, "Maximum number of entities to spawn in a group");
      }
      return maxGroup;
    }

    public int weight () {
      if (weight == -1) {
        weight = CONFIG.getInt("weight", category, 1, 0, Integer.MAX_VALUE, "Spawn rate of the entity [0 = no spawn]");
      }
      return minGroup;
    }

    public EnumCreatureType type () {
      if (type == null) {
        String value = CONFIG.getString("type", category, EnumCreatureType.CREATURE.name().toLowerCase(), "Spawn type, one of ['creature', 'monster']").toLowerCase();
        for (EnumCreatureType val : EnumCreatureType.values()) {
          if (val.name().toLowerCase().equals(value)) {
            type = val;
            break;
          }
        }
        if (type == null) {
          type = EnumCreatureType.CREATURE;
        }
      }

      return type;
    }

    public List<ResourceLocation> biomes () {
      if (biomeNames == null) {
        String[] biomes = CONFIG.getStringList("biomes", category, new String[]{}, "List of biomes (modid:biome_name) this entity should spawn in");
        biomeNames = Stream.of(biomes).map(ResourceLocation::new).collect(Collectors.toList());
      }

      return biomeNames;
    }

    public void init () {
      minGroup();
      maxGroup();
      weight();
      biomes();
      type();
    }
  }
}
