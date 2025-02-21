package ganymedes01.etfuturum.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.client.sound.ModSounds;
import ganymedes01.etfuturum.lib.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class BlockWeepingVines extends BaseBlock implements IShearable {

	private IIcon topIcon;

	public BlockWeepingVines() {
		super(Material.plants);
		setBlockSound(ModSounds.soundWeepingVines);
		setNames("weeping_vines");
		setCreativeTab(EtFuturum.creativeTabBlocks);
		setTickRandomly(true);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);
		//We only want the bottom vine to handle updates.
		if (rand.nextFloat() <= .1F && world.getBlockMetadata(x, y, z) == 0 && world.getBlock(x, y - 1, z).getMaterial() == Material.air) {
			for (int i = 1; i <= 25; i++) {
				//If there are 25 vines below this, stop.
				if (world.getBlock(x, y + i, z) != this) {
					break;
				}
				if (i == 25) {
					return;
				}
			}
			world.setBlock(x, y - 1, z, this, 0, 2);
		}
	}

	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		return true;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		//We do this because we don't want the block to use this logic if it's broken by shears. Without BugTorch installed this will run even when shearing them.
		//This is because Forge made a huge sin and shearing does not cancel the normal block drops. This causes duping.
		if (harvesters.get() == null || (harvesters.get().getHeldItem() != null && !(harvesters.get().getHeldItem().getItem() instanceof ItemShears))) {
			if (random.nextFloat() < (.33F + (fortune * .22F))) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int quantityDropped(Random random) {
		//Just to have all the logic in one spot. This should only fire when not broken by a player, so 0 for the args should be fine.
		return quantityDropped(0, 0, random);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.getBlock(x, y + 1, z).isOpaqueCube() || world.getBlock(x, y + 1, z) == this;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return canPlaceBlockAt(world, x, y, z);
	}

	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
		if (!canBlockStay(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			setVineToAir(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	public void setVineToAir(World world, int x, int y, int z) {
		if (!world.isRemote) {
			this.dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if (world.getBlock(x, y - 1, z) == this) {
			setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1F, 0.9F);
		} else {
			setBlockBounds(0.25F, 0.6F, 0.25F, 0.75F, 1F, 0.75F);
		}
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderIDs.EXTENDED_CROSSED_SQUARES;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_plant");
		topIcon = p_149651_1_.registerIcon(getTextureName());
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta) {
		if (world.getBlock(x, y - 1, z) != this) {
			return topIcon;
		}
		return blockIcon;
	}
}
