package emasher.sockets.modules;

import java.util.List;

import ic2.api.energy.tile.IEnergyAcceptor;
import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModEnergyExpansion extends SocketModule
{
	public ModEnergyExpansion(int id)
	{
		super(id, "sockets:enExpansion", "sockets:enExpansionIn", "sockets:enExpansionOut");
	}

	@Override
	public String getLocalizedName()
	{
		return "Energy Storage Upgrade";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Adds 100 000 MJ");
		l.add("of extra energy storage");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_WHITE + "Configure if input or output or neither");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "pdp", "ggg", "pbp", Character.valueOf('g'), Item.ingotGold, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('d'), Item.diamond, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	/*@Override
	public boolean hasTankIndicator() {return true; }*/
	
	/*@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }*/
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return config.meta != 0; }
	
	@Override
	public boolean acceptsEnergy(SideConfig config) { return config.meta == 1; }
	
	@Override
	public boolean outputsEnergy(SideConfig config) { return config.meta == 2; }
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		config.meta++;
		if(config.meta == 3) config.meta = 0;
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public int getPowerRequested(SideConfig config, SocketTileAccess ts)
	{
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSLatch(i)) canIntake = false;
		}
		
		if(canIntake && acceptsEnergy(config))
		{
			return 100;
		}
		
		return 0;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		
		boolean allOff = true;
		if(outputsEnergy(config))
		{
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						outputEnergy(config, ts, side);
						return;
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						outputEnergy(config, ts, side);
						
						return;
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				outputEnergy(config, ts, side);
				
			}
		}
			
	}
	
	public void outputEnergy(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		int outputs;
		switch(config.tank)
		{
		case 0: outputs = 32; break;
		case 1: outputs = 128; break;
		default: outputs = 512;
		}
		
		ts.outputEnergy(100, outputs, side);
	}
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.setMaxEnergyStored(ts.getMaxEnergyStored() + 100000);
	}
	
	@Override
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.setMaxEnergyStored(ts.getMaxEnergyStored() - 100000);
	}
}
