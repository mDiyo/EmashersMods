package emasher.sockets.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemDusts extends Item
{
	@SideOnly(Side.CLIENT)
	public Icon[] textures;
	
	public static final int NUM_ITEMS = Const.values().length;
	
	public static enum Const
	{
		lime,
		groundBauxite,
		groundCassiterite,
		groundGalena,
		groundGold,
		groundIron,
		groundNativeCopper,
		groundPentlandite,
		groundSilver,
		impureAluminiumDust,
		impureCopperDust,
		impureLeadDust,
		impureNickelDust,
		impureGoldDust,
		impureIronDust,
		impureSilverDust,
		impureTinDust,
		pureAluminiumDust,
		pureCopperDust,
		pureGoldDust,
		pureIronDust,
		pureLeadDust,
		pureNickelDust,
		purePlatinumDust,
		pureSilverDust,
		pureTinDust
	};
	
	public static final String[] NAMES = new String[]
			{
				"lime",
				"groundBauxite",
				"groundCassiterite",
				"groundGalena",
				"groundGold",
				"groundIron",
				"groundNativeCopper",
				"groundPentlandite",
				"groundSilver",
				"impureAluminiumDust",
				"impureCopperDust",
				"impureLeadDust",
				"impureNickelDust",
				"impureGoldDust",
				"impureIronDust",
				"impureSilverDust",
				"impureTinDust",
				"pureAluminiumDust",
				"pureCopperDust",
				"pureGoldDust",
				"pureIronDust",
				"pureLeadDust",
				"pureNickelDust",
				"purePlatinumDust",
				"pureSilverDust",
				"pureTinDust"
			};
	
	public static final String[] ORE_NAMES = new String[]
			{
				"dustQuicklime",
				"groundAluminum",
				"groundTin",
				"groundLead",
				"groundGold",
				"groundIron",
				"groundCopper",
				"groundNickel",
				"groundSilver",
				"dustImpureAluminum",
				"dustImpureCopper",
				"dustImpureLead",
				"dustImpureNickel",
				"dustImpureGold",
				"dustImpureIron",
				"dustImpureSilver",
				"dustImpureTin",
				"dustAluminum",
				"dustCopper",
				"dustGold",
				"dustIron",
				"dustLead",
				"dustNickel",
				"dustPlatinum",
				"dustSilver",
				"dustTin"
			};
	
	public static final String[] NAMES_LOC = new String[]
			{
				"Quicklime",
				"Ground Bauxite Ore",
				"Ground Cassiterite Ore",
				"Ground Galena Ore",
				"Ground Gold Ore",
				"Ground Iron Ore",
				"Ground Native Copper Ore",
				"Ground Pentlandite Ore",
				"Ground Silver Ore",
				"Impure Aluminium Dust",
				"Impure Copper Dust",
				"Impure Lead Dust",
				"Impure Nickel Dust",
				"Impure Gold Dust",
				"Impure Iron Dust",
				"Impure Silver Dust",
				"Impure Tin Dust",
				"Pure Aluminium Dust",
				"Pure Copper Dust",
				"Pure Gold Dust",
				"Pure Iron Dust",
				"Pure Lead Dust",
				"Pure Nickel Dust",
				"Pure Platinum Dust",
				"Pure Silver Dust",
				"Pure Tin Dust"
			};

	public ItemDusts(int id)
	{
		super(id);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setUnlocalizedName("e_dusts");
		setHasSubtypes(true);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage)
	{
		return textures[damage];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		textures = new Icon[NUM_ITEMS];
		
		for(int i = 0; i < NUM_ITEMS; i++)
		{
			textures[i] = ir.registerIcon("sockets:" + NAMES[i]);
		}
		
		this.itemIcon = textures[0];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	 {
		String name = "";
		name = "e_" + NAMES[itemstack.getItemDamage()];
		return getUnlocalizedName() + "." + name;
	 }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{	
		for(int i = 0; i < NUM_ITEMS; i++) par3List.add(new ItemStack(par1, 1, i));
    }

}
