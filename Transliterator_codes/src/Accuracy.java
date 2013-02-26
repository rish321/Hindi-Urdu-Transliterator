import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Accuracy
{
	public static void main (String args[]) throws IOException
	{
		System.out.println(Accuracy.accurate(new File(args[0])));
	}
	public static double accurate(File parallel) throws IOException
	{
		HashMap<String, ArrayList<Generated>> Map = new HashMap<String, ArrayList<Generated>>();
		ArrayList<String> distinct = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(parallel));
		String s;
		while((s = br.readLine()) != null)
		{
			String str[] = s.split("\t");
			Generated temp = new Generated();
			str[1] = str[1].replaceAll("_", "");
			str[2] = str[2].replaceAll("-", "");
			temp.Intended = str[1];
			if(str.length < 3)
				temp.generated = "";
			else
				temp.generated = str[2];
			ArrayList<Generated> tmp = new ArrayList<Generated>();
			if(Map.get(str[0]) != null)
				tmp = Map.get(str[0]);
			else
				distinct.add(str[0]);
			tmp.add(temp);
			Map.put(str[0], tmp);
		}
		br.close();
		int corr = 0;
		for(int i = 0; i < distinct.size(); i++)
		{
			//System.out.println(distinct.get(i));
			ArrayList<Generated> tmp = Map.get(distinct.get(i));
			for(int j = 0; j < tmp.size(); j++)
			{
				int k = 0;
				for(k = 0; k < tmp.size(); k++)
					if(tmp.get(j).generated.equals(tmp.get(k).Intended))
					{
						System.out.println(tmp.get(j).generated);
						corr++;
						break;
					}
				if(k != tmp.size())
					break;
			}
		}
		System.out.println(corr + " " + distinct.size());
		return ((double)corr/(double)distinct.size() * 100);
	}
}
