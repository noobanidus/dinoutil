package noobanidus.mods.dinoutil.mixins;

import fossilsarcheology.server.entity.prehistoric.EntityPrehistoric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityPrehistoric.class)
public class MixinEntityPrehistoric {
  @Overwrite
  public boolean canDespawn () {
    return true;
  }
}
