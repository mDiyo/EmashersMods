package emasher.sockets.modules;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModDFBlade extends SocketModule
{

	public ModDFBlade(int id)
	{
		super(id, "sockets:DFBlade_IN", "sockets:DFBlade");
	}

	@Override
	public String getLocalizedName()
	{
		return "Deadly Flashing Blade";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Hurts entities within one block");
		l.add("on a sustained internal redstone signal");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " s ", "ipi", " b ", Character.valueOf('i'), Item.ingotIron, Character.valueOf('s'), Item.swordDiamond,
				Character.valueOf('p'), Block.pistonBase, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public int getCurrentTexture(SideConfig config)
	{
		if(config.tank < 1) return 0;
		else return 1;
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.tank == 1)
		{
			config.meta++;
			if(config.meta == 20)
			{
				config.meta = 0;
				double x = ts.xCoord + side.offsetX;
				double y = ts.yCoord + side.offsetY;
				double z = ts.zCoord + side.offsetZ;
				
				List l = ts.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1, z + 1));
				for(Object o : l)
				{
					if(o instanceof EntityLiving)
					{
						EntityLiving live = (EntityLiving)o;
						live.attackEntityFrom(DamageSource.generic, 10);
						
		
					}
				}
			}
		}
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index])
		{
			config.tank = 1;
			
		}
		else if(! on && config.rsControl[index])
		{
			boolean turnOff = true;
			
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i] && ts.getRSControl(i)) turnOff = false;
				if(config.rsLatch[i] && ts.getRSLatch(i)) turnOff = false;
			}
			
			if(turnOff)
			{
				config.tank = 0;
				config.meta = 0;
			}
		}
		
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public void onRSLatchChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsLatch[index])
		{
			config.tank = 1;
			
		}
		else if(! on && config.rsLatch[index])
		{
			boolean turnOff = true;
			
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i] && ts.getRSControl(i)) turnOff = false;
				if(config.rsLatch[i] && ts.getRSLatch(i)) turnOff = false;
			}
			
			if(turnOff)
			{
				config.tank = 0;
				config.meta = 0;
			}
		}
		
		ts.sendClientSideState(side.ordinal());
	}

}
