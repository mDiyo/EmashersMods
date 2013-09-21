package emasher.sockets.modules;

import java.util.List;

import ic2.api.energy.tile.IEnergyAcceptor;
import cpw.mods.fml.common.registry.GameRegistry;
import buildcraft.api.power.IPowerReceptor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModEnergyOutput extends SocketModule
{

	public ModEnergyOutput(int id)
	{
		super(id, "sockets:energyOutput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Energy Output";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Outputs either MJ or EU");
		l.add("to adjacent cables/machines/etc.");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " g ", " b ", Character.valueOf('g'), Item.goldNugget, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide);
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " g ", " b ", Character.valueOf('g'), "ingotCopper", Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean outputsEnergy(SideConfig config) { return true; }
	
	/*@Override
	public boolean hasTankIndicator() {return true; }*/
	
	/*@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }*/
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return true; }
	
	
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		
		boolean allOff = true;
		
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

}
