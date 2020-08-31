package noobanidus.mods.dinoutil.bean;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noobanidus.mods.dinoutil.ConfigDino;
import noobanidus.mods.dinoutil.init.ModItems;

import java.util.Random;

public class BlockBeanCrop extends BlockCrops {
  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    this.checkAndDropBlock(worldIn, pos, state);

    if (!worldIn.isAreaLoaded(pos, 1)) return;
    if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
      int i = this.getAge(state);

      if (i < this.getMaxAge()) {
        float f = ConfigDino.getGrowthChance(getGrowthChance(this, worldIn, pos));

        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0)) {
          worldIn.setBlockState(pos, this.withAge(i + 1), 2);
          net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
      }
    }
  }

  @Override
  protected Item getSeed() {
    return ModItems.magic_bean;
  }

  @Override
  protected Item getCrop() {
    return ModItems.magic_bean;
  }

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    Random rand = world instanceof World ? ((World) world).rand : RANDOM;

    int count = quantityDropped(state, fortune, rand);
    for (int i = 0; i < count; i++) {
      Item item = this.getItemDropped(state, rand, fortune);
      if (item != Items.AIR) {
        drops.add(new ItemStack(item, 1, this.damageDropped(state)));
      }
    }

    int age = getAge(state);
    if (age == getMaxAge()) {
      drops.add(ConfigDino.getRandomBean().getItem());
    }
  }
}
