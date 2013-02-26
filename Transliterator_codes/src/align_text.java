import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class align_text
{
	public static void main(String args[]) throws IOException
	{
		//int max = 4;
		//String path = "/home/rishabh/transliterator/";
		Hash_Map Hindi_Urdu;
		Hindi_Urdu = new Hash_Map(new File(args[0]), new File(args[1]));
		Char_Map Urdu_Freq;
		Urdu_Freq = new Char_Map(new File(args[1]), new File(args[2]));
		BufferedReader br = new BufferedReader(new FileReader(new File(args[3])));
		FileWriter fw = new FileWriter(new File(args[4]));
		String s = "";
		//int c = 0;
		int init = 1;
		s = br.readLine();
		while(s != null)
		{
			//if(!s.equals(""))
			{
				String spl[] = s.split("\t");
				//System.out.print(spl[0] +" "+ spl[1] + " ");
				int pos = 0;
				if(init == 1)
					pos = 0;
				else if(init == 0)
					pos = 1;
				s = br.readLine();
				//System.out.println("skjd" + s);
				if(s == null || s.equals(""))
				{
					pos = 2;
					init = 1;
				}
				//System.out.println(pos);
				String arrange = "";
				String pass = spl[0];
				//System.out.println(Hindi_Urdu.get(pass));
				if(Hindi_Urdu.get(pass) != null)
				{
					arrange = arrangement(Urdu_Freq, Hindi_Urdu.get(pass), pos);
					//System.out.println(spl[0] + "\t" + arrange + "\t" + spl[1] + "\n");
					fw.write(spl[0] + "\t" + arrange + "\t" + spl[1] + "\n");
				}
				if(pos != 2)
					init = 0;
			}
			/*else
				init = 1;*/

			/*if(spl[0].length() == spl[1].length())
			{
				++c;
				for(int i = 0; i < spl[0].length(); i++)
				{
					String arrange = "";
					int pos = 0;
					if(i == 0)
						pos = 0;
					else if(i == spl[0].length() - 1)
						pos = 2;
					else
						pos = 1;
					//System.out.println(spl[0].charAt(i));
					String pass = Integer.toString((int)spl[0].charAt(i));
					if(i + 1 < spl[0].length() && (int)spl[0].charAt(i+1) == 2364)
					{
						pass += " " + Integer.toString((int)spl[0].charAt(i+1));
						i++;
					}
					else if(i+2 < spl[0].length() && (((int)spl[0].charAt(i) == 2330 && (int)spl[0].charAt(i+1) == 2381 && (int)spl[0].charAt(i+2) == 2331) || ((int)spl[0].charAt(i) == 2336 && (int)spl[0].charAt(i+1) == 2381 && (int)spl[0].charAt(i+2) == 2336)))
					{
						pass += " " + Integer.toString((int)spl[0].charAt(i+1)) + " " + Integer.toString((int)spl[0].charAt(i+2));
						i += 2;
					}
					//System.out.println(pass);
					if(Hindi_Urdu.get(pass) != null)
					{
						arrange = arrangement(Urdu_Freq, Hindi_Urdu.get(pass), pos, max);
						fw.write(spl[0].charAt(i) + "\t" + arrange + "\t" + spl[1].charAt(i) + "\n");
					}
				}
				fw.write("\n");
			}*/
		}
		//System.out.println(c);
		fw.close();
		br.close();
		//System.out.println(Hindi_Urdu.get("ा"));
		
	}
	static String arrangement(Char_Map urdu_Freq, ArrayList<String> urdu_map, int index)
	{
		//System.out.println("ldksd" + urdu_map);
		int min = 4;
		int max = (int)Math.ceil(urdu_map.size()*0.2);
		max = max(min, max);
		//System.out.println(max);
		int sort[] = new int[max];
		String sort_char[] = new String[max];
		for(int i = 0; i < max; i++)
			sort_char[i] = "";
		String arrange = "";
		//System.out.println(urdu_map.size());
		for(int i = 0; i < min(max, urdu_map.size()); i++)
		{
			//System.out.println(i);
			sort[i] = urdu_Freq.get(urdu_map.get(i))[index];
			sort_char[i] = urdu_map.get(i);
			/*String split[] = urdu_map.get(i).split(" ");
			for(int j = 0; j < split.length; j++)
			{	
				//System.out.print(Integer.parseInt(split[j]));
				sort_char[i] += Character.toString((char) Integer.parseInt(split[j]));
			}*/
		}
		//System.out.println();
		//Arrays.sort(sort);
		/*for(int i = 0; i < sort_char.length; i++)
			System.out.print(sort_char[i]);*/
		sortarr(sort, sort_char);
		for(int i = 0; i < sort.length; i++)
			if(!sort_char[i].equals(""))
				arrange += sort_char[i] + "_";
		/*else
				arrange += "|\t";*/
		arrange = arrange.substring(0, arrange.length() - 1);
		return arrange;
	}
	static void sortarr(int[] sort, String[] sort_char)
	{
		for(int i = 0; i < sort.length; i++)
			for(int j = i; j < sort.length; j++)
				if(sort[i] < sort[j])
				{
					int temp = sort[i];
					sort[i] = sort[j];
					sort[j] = temp;
					String temp_str = sort_char[i];
					sort_char[i] = sort_char[j];
					sort_char[j] = temp_str;
				}
		int start = sort.length - 1;
		for(int i = 0; i < sort.length; i++)
			if(sort[i] == 0)
				if(sort_char[i] == null)
					start = i;
				else if(start < i)
				{
					String temp = sort_char[i];
					sort_char[i] = sort_char[start];
					sort_char[start] = temp;
					start++;
				}
	}
	static int min(int length, int length2)
	{
		if(length < length2)
			return length;
		return length2;
	}
	static int max(int length, int length2)
	{
		if(length > length2)
			return length;
		return length2;
	}
}
