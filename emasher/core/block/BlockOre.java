package emasher.core.block;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;

public class BlockOre extends Block
{
	
	private static Icon[] textures;
	private int numBlocks;

	public BlockOre(int par1, int par2, Material par4Material) 
	{
		super(par1, par4Material);
		this.setCreativeTab(EmasherCore.tabEmasher);
		numBlocks = 8;
		textures = new Icon[numBlocks];
	}
	
	@Override
	public Icon getIcon(int side, int meta)
	{
		return textures[meta];
	}
	
	@Override
    public void registerIcons(IconRegister ir)
    {
		this.blockIcon = ir.registerIcon("emashercore:oreBauxite");
		textures[0] = blockIcon;
		textures[1] = ir.registerIcon("emashercore:oreCassiterite");
		textures[2] = ir.registerIcon("emashercore:oreEmery");
		textures[3] = ir.registerIcon("emashercore:oreGalena");
		textures[4] = ir.registerIcon("emashercore:oreNativeCopper");
		textures[5] = ir.registerIcon("emashercore:orePentlandite");
		textures[6] = ir.registerIcon("emashercore:oreRuby");
		textures[7] = ir.registerIcon("emashercore:oreSapphire");
    }
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int f)
    {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		
		if(meta == 0 || meta == 1 || meta == 3 || meta == 4 || meta == 5)
		{
			result.add(new ItemStack(EmasherCore.ore, 1, meta));
		}
		else
		{
			switch(meta)
			{
			case 2: result.add(new ItemStack(EmasherCore.gem, f + 1, 0));
			break;
			case 6: result.add(new ItemStack(EmasherCore.gem, f + 1, 1));
			break;
			case 7: result.add(new ItemStack(EmasherCore.gem, f + 1, 2));
			}
		}
		
		return result;
    }
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
		return true;
    }
	
	/*@Override
	 public int idDropped(int par1, Random par2Random, int par3)
	 {
		return this.blockID;
		
	 }
	
	@Override
	public int damageDropped(int par1)
    {
        return par1;
    }*/
	
	
	 

	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{	
		for(int i = 0; i < 8; i++) par3List.add(new ItemStack(par1, 1, i));
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return new ItemStack(this.blockID, 1, meta);
	}
}
