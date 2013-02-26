import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Char_Map
{
	HashMap<String, int[]> Urdu_Freq;
	Char_Map(File mapper, File corpus) throws IOException
	{
		Urdu_Freq = new HashMap<String, int[]>();
		BufferedReader br_urdu = new BufferedReader(new FileReader(mapper));
		String input;
		while((input = br_urdu.readLine()) != null)
		{
			String splits[] = input.split("\t");
			for(int i = 0; i < splits.length; i++)
			{
				int[] temp = new int[3];
				Urdu_Freq.put(splits[i], temp);
			}
		}
		br_urdu.close();
		BufferedReader br = new BufferedReader(new FileReader(corpus));
		while((input = br.readLine()) != null)
		{
			String[] splits = input.split(" ");
			for(int i = 0; i < splits.length; i++)
				for(int j = 0; j < splits[i].length(); j++)
					if(Urdu_Freq.get(Integer.toString((int)splits[i].charAt(j))) != null)
					{
						int temp[] = Urdu_Freq.get(Integer.toString((int)splits[i].charAt(j)));
						if(j == 0)
							temp[0]++;
						else if(j == splits[i].length() - 1)
							temp[2]++;
						else
							temp[1]++;
						Urdu_Freq.put((Integer.toString((int)splits[i].charAt(j))), temp);
					}
					else if(j+1 < splits[i].length())
					{
						if(Urdu_Freq.get(((int)splits[i].charAt(j)) + " " + (int)splits[i].charAt(j+1)) != null)
						{
							int temp[] = Urdu_Freq.get((splits[i].charAt(j)) + " " + splits[i].charAt(j+1));
							if(j == 0)
								temp[0]++;
							else if(j == splits[i].length() - 1)
								temp[2]++;
							else
								temp[1]++;
							Urdu_Freq.put((splits[i].charAt(j)) + " " + splits[i].charAt(j+1), temp);
							j++;
						}
					}
		}
		br.close();
	}
	int[] get(String key)
	{
		return Urdu_Freq.get(key);
	}
}