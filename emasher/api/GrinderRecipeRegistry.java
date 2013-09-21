package emasher.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrinderRecipeRegistry
{
	public static class GrinderRecipe
	{
		/*
		 * input - either an OreDictionary string, or an ItemStack
		 * output - an ItemStack
		 * 
		 */
		private Object input;
		private ItemStack output;
		
		public GrinderRecipe(ItemStack input, ItemStack output)
		{
			this.input = input;
			this.output = output;
		}
		
		public GrinderRecipe(String input, ItemStack output)
		{
			this.input = input;
			this.output = output;
		}
		
		public Object getInput()
		{
			return input;
		}
		
		public ItemStack getOutput()
		{
			return output;
		}
		
	}
	
	private static ArrayList<GrinderRecipe> recipes = new ArrayList<GrinderRecipe>();
	
	public static void registerRecipe(GrinderRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static void registerRecipe(ItemStack input, ItemStack output) { registerRecipe(new GrinderRecipe(input, output)); }
	public static void registerRecipe(String input, ItemStack output) { registerRecipe(new GrinderRecipe(input, output)); }
	
	public static GrinderRecipe getRecipe(Object input)
	{
		if(input instanceof ItemStack)
		{
			int oreID = OreDictionary.getOreID((ItemStack)input);
			for(GrinderRecipe r:recipes)
			{
				int otherID = -1;
				
				if(r.getInput() instanceof ItemStack)
				{
					otherID = OreDictionary.getOreID((ItemStack)r.getInput());
				}
				else if(r.getInput() instanceof String)
				{
					otherID = OreDictionary.getOreID((String)r.getInput());
				}
				
				if((otherID != -1 && otherID == oreID) || (r.getInput() instanceof ItemStack && ((ItemStack)input).isItemEqual((ItemStack)r.getInput())))
				{
					return r;
				}
				
			}
		}
		else if(input instanceof String)
		{
			int oreID = OreDictionary.getOreID((String)input);
			for(GrinderRecipe r:recipes)
			{
				int otherID = -1;
				
				if(r.getInput() instanceof ItemStack)
				{
					otherID = OreDictionary.getOreID((ItemStack)r.getInput());
				}
				else if(r.getInput() instanceof String)
				{
					otherID = OreDictionary.getOreID((String)r.getInput());
				}
				
				if(otherID != -1 && otherID == oreID)
				{
					return r;
				}
			}
		}
		
		return null;
	}
}
