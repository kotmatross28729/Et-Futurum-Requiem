package ganymedes01.etfuturum.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.client.sound.ModSounds;
import ganymedes01.etfuturum.core.utils.Utils;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockMuddyMangroveRoots extends BlockRotatedPillar {

	public BlockMuddyMangroveRoots() {
		super(Material.ground);
		setHardness(0.7F);
		setResistance(0.7F);
		Utils.setBlockSound(this, ModSounds.soundMuddyMangroveRoots);
		setBlockTextureName("muddy_mangrove_roots");
		setBlockName(Utils.getUnlocalisedName("muddy_mangrove_roots"));
		setCreativeTab(EtFuturum.creativeTabBlocks);
		setHarvestLevel("shovel", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int side) {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(this.getTextureName() + "_side");
		field_150164_N = reg.registerIcon(getTextureName() + "_top");
	}
}
