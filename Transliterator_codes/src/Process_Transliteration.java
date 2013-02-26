import java.util.ArrayList;
import java.util.HashMap;


public class Process_Transliteration
{
	static ArrayList<ArrayList<String>> Process_Target(String input, HashMap<String, ArrayList<String>> mapping)
	{
		long gens = 1;
		double k = 0.9;
		int each[] = new int[input.length()];
		int maxp = 0;
		for(int i = 0; i < input.length(); i++)
		{
			ArrayList<String> arr = mapping.get(Character.toString(input.charAt(i)));
			if(arr != null)
			{
				gens *= arr.size();
				each[i] = arr.size();
			}
		}
		System.out.println(gens);
		while(gens > 100000)
		{
			for(int i = 0; i < each.length; i++)
				if(each[i] > each[maxp])
					maxp = i;
			gens /= each[maxp];
			each[maxp] = (int) Math.ceil(each[maxp] * k);
			if(each[maxp] == (int) Math.ceil(each[maxp] * k))
				each[maxp] -= 1;
			//System.out.println(each[maxp]);
			gens *= each[maxp];
			maxp = 0;
		}
		gens = 1;
		ArrayList<ArrayList<String>> trans_words = new ArrayList<ArrayList<String>>();
		for(int index = 0; index < input.length(); index++)
		{
			ArrayList<String> poss = mapping.get(Character.toString(input.charAt(index)));
			if(poss == null)
				continue;
			//gens*=poss.size()*k;
			/*if(gens > 100000)
				return new ArrayList<ArrayList<String>>();*/
			int tot = trans_words.size();
			for(int i = 0; i < tot; i++)
			{
				ArrayList<String> current = trans_words.get(0);
				ArrayList<String> temp = new ArrayList<String>();
				temp = copy(current);
				trans_words.remove(0);
				for(int j = 0; j < each[index]; j++)
				{
					current.add(poss.get(j));
					trans_words.add(current);
					current =  copy(temp);
				}
			}
			if(tot == 0)
				for(int j = 0; j < each[index]; j++)
				{
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(poss.get(j));
					trans_words.add(temp);
				}
		}
		for(int i = 0; i < trans_words.size(); i++)
		{
			String current = "";
			for(int j = 0; j < trans_words.get(i).size(); j++)
				current += trans_words.get(i).get(j);
			trans_words.get(i).add(current);
		}	
		return trans_words;
	}
	private static ArrayList<String> copy(ArrayList<String> temp)
	{
		ArrayList<String> current = new ArrayList<String>();
		for(int i = 0; i < temp.size(); i++)
			current.add(temp.get(i));
		return current;
	}
}