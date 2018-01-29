package com.jeeplus.modules.sys.utils;

import java.util.List;

import com.jeeplus.modules.sys.entity.Dict;

public class ToolUtils {
	
	public static double convertPriceLimited(String Currency, int Type)
	{
		
		List<Dict> dictList = DictUtils.getDictList("PR_PRICELIMITED"+ Integer.toString(Type));
		try{
			for (Dict dict : dictList)
			{
				if(dict.getLabel().equals(Currency))
				{
					return Double.parseDouble(dict.getValue());
					
				}
			}
		}catch(Exception e)
		{
			
		}
		
		return 0;
	}
	
	public boolean judge(double price,String Currency, int Type, String compare)
	{
		
		List<Dict> dictList = DictUtils.getDictList("PR_PRICELIMITED"+ Integer.toString(Type));
		try{
			for (Dict dict : dictList)
			{
				if(dict.getLabel().equals(Currency))
				{
					if(price> Double.parseDouble(dict.getValue()))
					{
						if(compare.equals("Greater"))
						return true;
						else{
							return false;
						}
						
					}else {
						if(compare.equals("LessEqual"))
						return true;
						else return false;
					}
					
					
				}
			}
		}catch(Exception e)
		{
			
		}
		
		return true;
	}
	public boolean validateSiteIsHk(String site)
	{
		
		List<Dict> dictList = DictUtils.getDictList("HK_Site_Code");
		if(dictList.size()>0)
			if(dictList.get(0).getLabel().equals(site))return true;
		return false;
	}
	
	public boolean validateSiteIsNotHk(String site)
	{
		
		List<Dict> dictList = DictUtils.getDictList("HK_Site_Code");
		if(dictList.size()>0)
			if(!dictList.get(0).getLabel().equals(site))return true;
		return false;
	}
	
}
