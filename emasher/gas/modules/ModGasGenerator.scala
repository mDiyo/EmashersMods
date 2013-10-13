package emasher.gas.modules

import emasher.api.SocketModule;
import emasher.sockets.SocketsMod;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.core.EmasherCore;
import emasher.gas.EmasherGas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import emasher.gas.fluids.FluidGas;
import emasher.api._;

class ModGasGenerator(id: Int) extends SocketModule(id, "gascraft:gasGenerator", "gascraft:gasGeneratorActive")
{
	override def getLocalizedName = "Gas Generator";
	
	override def getToolTip(l: List[Object])
	{
		l.add("Generates power when fuled");
		l.add("with certain gases");
	}
	
	override def getIndicatorKey(l: List[Object])
	{
		l.add(SocketsMod.PREF_BLUE + "Fuel Tank");
		l.add(SocketsMod.PREF_AQUA + "Outputs Variable MJ/t");
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
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('f'), Block.furnaceIdle, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 5)));
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
				
				if(config.inventory != 0)
				{
					if(! config.rsControl(0) || outputSmoke(ts))
					{
						config.inventory = 0;
						config.rsControl(0) = false;
					}
				}
				var fluidRec = null.asInstanceOf[FluidStack];
				if(fluid != null) fluidRec = GeneratorFuelRegistry.getFuel(fluid.getFluid.getName);
				if(config.inventory == 0 && fluid != null && GeneratorFuelRegistry.isFuel(fluid.getFluid.getName) && fluid.amount >= fluidRec.amount
						&& ts.powerHandler.getEnergyStored() < ts.powerHandler.getMaxEnergyStored())
				{
					config.inventory = GeneratorFuelRegistry.getEnergyPerTick(fluid.getFluid.getName);
					config.meta = GeneratorFuelRegistry.getBurnTime(fluid.getFluid.getName);
					config.rsControl(0) = GeneratorFuelRegistry.producesSmoke(fluid.getFluid.getName);
					ts.drainInternal(config.tank, fluidRec.amount, true);
					
				}
				
				ts.sendClientSideState(side.ordinal);
			}
			else
			{				
				if(ts.powerHandler.getEnergyStored() < ts.powerHandler.getMaxEnergyStored())
				{
					config.meta -= 1;
					ts.powerHandler.addEnergy(config.inventory);
				}
				else
				{
					config.meta = 0;
					if(config.inventory != 0) ts.sendClientSideState(side.ordinal);
					config.inventory = 0;
				}
			}
		}
		else
		{
			config.meta = 0;
			if(config.inventory != 0) ts.sendClientSideState(side.ordinal);
			config.inventory = 0;
		}
	}
	
	def outputSmoke(ts: SocketTileAccess):
	Boolean = 
	{
		var te = ts.worldObj.getBlockTileEntity(ts.xCoord, ts.yCoord + 1, ts.zCoord);
		if(te != null && te.isInstanceOf[IGasReceptor])
		{
			if(te.asInstanceOf[IGasReceptor].recieveGas(new FluidStack(EmasherGas.fluidSmoke, 4000), ForgeDirection.DOWN, false) == 4000)
			{
				te.asInstanceOf[IGasReceptor].recieveGas(new FluidStack(EmasherGas.fluidSmoke, 4000), ForgeDirection.DOWN, true)
				true;
			}
			else false;
		}
		else if(ts.worldObj.isAirBlock(ts.xCoord, ts.yCoord + 1, ts.zCoord))
		{
			ts.worldObj.setBlock(ts.xCoord, ts.yCoord + 1, ts.zCoord, EmasherGas.smoke.blockID);
			true;
		}
		else false;
	}
	
	
}