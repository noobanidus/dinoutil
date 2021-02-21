package noobanidus.mods.dinoutil;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import noobanidus.mods.dinoutil.init.ModItems;

@Mod.EventBusSubscriber(modid=DinoUtil.MODID, value = Side.CLIENT)
public class Events {
  @SubscribeEvent
  public static void onModelLoad (ModelRegistryEvent event) {
    ModelLoader.setCustomModelResourceLocation(ModItems.pouch, 0, new ModelResourceLocation(ModItems.pouch.getRegistryName(), "inventory"));
    ModelLoader.setCustomModelResourceLocation(ModItems.rainbow_axe, 0, new ModelResourceLocation(ModItems.rainbow_axe.getRegistryName(), "inventory"));
    ModelLoader.setCustomModelResourceLocation(ModItems.magic_bean, 0, new ModelResourceLocation(ModItems.magic_bean.getRegistryName(), "inventory"));
    ModelLoader.setCustomModelResourceLocation(ModItems.sugar_pin, 0, new ModelResourceLocation(ModItems.sugar_pin.getRegistryName(), "inventory"));

  }
}
