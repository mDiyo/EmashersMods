package emasher.gas.modules

import emasher.api._;
import emasher.core._;
import emasher.sockets._;
import java.util._;
import net.minecraft.item.crafting._;
import net.minecraftforge.oredict._;
import net.minecraftforge.common._;
import net.minecraft.item._;
import net.minecraft.block._;
import emasher.gas.fluids.FluidGas;

class ModGasTurbine(id: Int) extends SocketModule(id, "gascraft:gasTurbine", "gascraft:gasTurbineActive")
{
	override def getLocalizedName = "Gas Turbine";
	
	override def getToolTip(l: List[Object])
	{
		l.add("Generates power when certain");
		l.add("gases are input into it");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Fuel Tank");
		l.add(SocketsMod.PREF_AQUA + "Outputs 1 MJ/t");
	}
	
	override def getCurrentTexture(config: SideConfig):
	Int = 
	{
		if(config.meta == 0) 0;
		else 1;
	}
	
	override def addRecipe
	{
		CraftingManager.getInstance().getRecipeList().asInstanceOf[List[Object]].add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ipi", "ufu", " b ", Character.valueOf('p'), EmasherCore.psu, Character.valueOf('i'), Item.ingotIron,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('f'), Block.glass, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 5)));
	}
	
	override def hasTankIndicator = true;
	
	override def isMachine = true;
	
	override def canBeInstalled(ts: SocketTileAccess, side: ForgeDirection):
	Boolean = 
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false;
		
		for(i <- 0 to 5)
		{
			var m = ts.getSide(ForgeDirection.getOrientation(i))
			if(m != null && m.isMachine)
			{
				return false;
			}
		}
		
		true;
	}
	
	override def updateSide(config: SideConfig, ts: SocketTileAccess, side: ForgeDirection)
	{
		if(config.tank >= 0 && config.tank < 3)
		{
			if(config.meta <= 0)
			{
				var fluid = ts.getFluidInTank(config.tank);
				if(config.rsControl(1)) config.rsControl(1) = false;
				
				
				if(fluid != null && fluid.getFluid.isInstanceOf[FluidGas] && fluid.amount > 250 && ts.powerHandler.getEnergyStored() < ts.powerHandler.getMaxEnergyStored())
				{
					
					config.rsControl(1) = true;
					config.meta = 20;
					ts.drainInternal(config.tank, 250, true);
					
				}
				
				ts.sendClientSideState(side.ordinal);
			}
			else
			{				
				if(ts.powerHandler.getEnergyStored() < ts.powerHandler.getMaxEnergyStored())
				{
					config.meta -= 1;
					ts.powerHandler.addEnergy(0.25F);
					
				}
				else
				{
					config.meta = 0;
					if(config.rsControl(1)) ts.sendClientSideState(side.ordinal);
					config.rsControl(1) = false;
				}
			}
		}
		else
		{
			config.meta = 0;
			if(config.rsControl(1)) ts.sendClientSideState(side.ordinal);
			config.rsControl(1) = false;
		}
	}
}