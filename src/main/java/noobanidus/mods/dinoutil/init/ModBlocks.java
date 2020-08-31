package noobanidus.mods.dinoutil.init;


import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import noobanidus.mods.dinoutil.DinoUtil;
import noobanidus.mods.dinoutil.bean.BlockBeanCrop;

@Mod.EventBusSubscriber(modid= DinoUtil.MODID)
public class ModBlocks {
  public static Block bean_crop;

  @SubscribeEvent
  public static void onBlockRegister(RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> registry = event.getRegistry();
    registry.register(bean_crop = new BlockBeanCrop().setRegistryName(DinoUtil.MODID, "bean_crop").setTranslationKey("dinoutil.bean_crop").setCreativeTab(DinoUtil.tab));
  }
}
