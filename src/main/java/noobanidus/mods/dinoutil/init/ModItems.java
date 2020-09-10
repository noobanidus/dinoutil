package noobanidus.mods.dinoutil.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import noobanidus.mods.dinoutil.DinoUtil;
import noobanidus.mods.dinoutil.axe.ItemRainbowAxe;
import noobanidus.mods.dinoutil.pouch.ItemPouch;

@Mod.EventBusSubscriber(modid = DinoUtil.MODID)
public class ModItems {
  public static Item pouch, rainbow_axe, magic_bean;

  @SubscribeEvent
  public static void onItemRegister(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();
    registry.register(pouch = new ItemPouch("coin_pouch").setCreativeTab(DinoUtil.tab));
    registry.register(rainbow_axe = new ItemRainbowAxe("rainbow_axe").setCreativeTab(DinoUtil.tab));
    registry.register(magic_bean = new ItemSeeds(ModBlocks.bean_crop, Blocks.FARMLAND).setRegistryName(new ResourceLocation(DinoUtil.MODID, "magic_bean")).setTranslationKey("dinoutil.magic_bean").setCreativeTab(DinoUtil.tab));
    registry.register(sugar_pin = new Itemsugar_pin("sugar_pin").setCreativeTab(DinoUtil.tab));

  }
}
