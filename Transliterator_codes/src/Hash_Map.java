import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Hash_Map
{
	HashMap<String, ArrayList<String>> Hindi_Urdu;
	HashMap<String, ArrayList<Freq>> hind_urd;
	HashMap<String, Double> Mapping_pbt;
	Hashtable<String, Integer> h;
	ArrayList<String> lang1_chars = new ArrayList<String>();
	ArrayList<String> lang2_chars = new ArrayList<String>();
	Hash_Map(File mapper, File mapped, File pbt, File any) throws IOException
	{
		h = new Hashtable<String, Integer>();
		hind_urd = new HashMap<String, ArrayList<Freq>>();
		BufferedReader br = new BufferedReader(new FileReader(any));
		FileWriter lang1 = new FileWriter(mapper);
		FileWriter lang2 = new FileWriter(mapped);
		FileWriter prob = new FileWriter(pbt);
		String line;
		while((line = br.readLine()) != null)
		{
			if(line.equals(""))
				continue;
			String spl[] = line.split("\t");
			if(spl.length != 2)
				continue;
			ArrayList<Freq> temp = new ArrayList<Freq>();
			insert(lang2_chars, spl[1]);
			Freq frq = new Freq();
			frq.character = spl[1];
			frq.freq = 1;
			temp.add(frq);
			if(hind_urd.get(spl[0]) != null)
				temp = addfreq(hind_urd.get(spl[0]), spl[1]);
			else
				lang1_chars.add(spl[0]);
			hind_urd.put(spl[0], temp);
		}
		int max_mapping = 0;
		for(int i = 0; i < lang1_chars.size(); i++)
		{
			//h.put(lang1_chars.get(i), i);
			lang1.write(lang1_chars.get(i) + "\n");
			ArrayList<Freq> temp= hind_urd.get(lang1_chars.get(i));
			if(temp.size() > max_mapping)
				max_mapping = temp.size();
			String rint = "", pbtlty = "";
			int tot = 0;
			for(int j = 0; j < temp.size(); j++)
				tot += temp.get(j).freq;
			for(int j = 0; j < temp.size(); j++)
			{
				rint += temp.get(j).character + "\t";
				pbtlty += (double)temp.get(j).freq/(double)tot + "\t";
			}
			rint = rint.substring(0, rint.length() - 1);
			pbtlty = pbtlty.substring(0, pbtlty.length() - 1);
			lang2.write(rint + "\n");
			prob.write(pbtlty + "\n");
		}
		//lang2_chars.add("");
		for(int i = 0; i < lang2_chars.size(); i++)
			h.put(lang2_chars.get(i), i);
		//System.out.println(max_mapping);
		lang1.close();
		lang2.close();
		prob.close();
		br.close();
	}
	private void insert(ArrayList<String> lang2_chars, String string)
	{
		int i;
		for(i = 0; i < lang2_chars.size(); i++)
			if(lang2_chars.get(i).equals(string))
				break;
		if(i == lang2_chars.size())
			lang2_chars.add(string);
	}
	Hash_Map(File mapper, File mapped) throws IOException
	{
		Hindi_Urdu = new HashMap<String, ArrayList<String>>();
		BufferedReader br_hindi = new BufferedReader(new FileReader(mapper));
		BufferedReader br_urdu = new BufferedReader(new FileReader(mapped));
		String input_hindi, input_urdu;
		while((input_hindi = br_hindi.readLine()) != null)
		{
			input_urdu = br_urdu.readLine();
			String[] obj_urdu = input_urdu.split("\t");
			ArrayList<String> urdu_list = new ArrayList<String>();
			for(int i = 0; i < obj_urdu.length; i++)
				urdu_list.add(obj_urdu[i]);
			Hindi_Urdu.put(input_hindi, urdu_list);
		}
		br_hindi.close();
		br_urdu.close();
	}
	Hash_Map(File mapper, File mapped, File pbt) throws IOException
	{
		Mapping_pbt = new HashMap<String, Double>();
		Hindi_Urdu = new HashMap<String, ArrayList<String>>();
		BufferedReader br_hindi = new BufferedReader(new FileReader(mapper));
		BufferedReader br_urdu = new BufferedReader(new FileReader(mapped));
		BufferedReader urdu_pbt = new BufferedReader(new FileReader(pbt));
		String input_hindi, input_urdu, urdu_prob;
		while((input_hindi = br_hindi.readLine()) != null)
		{
			input_urdu = br_urdu.readLine();
			urdu_prob = urdu_pbt.readLine();
			String[] obj_urdu = input_urdu.split("\t");
			String[] prob_urdu = urdu_prob.split("\t");
			ArrayList<String> urdu_list = new ArrayList<String>();
			for(int i = 0; i < obj_urdu.length; i++)
			{
				urdu_list.add(obj_urdu[i]);
				Mapping_pbt.put(input_hindi + " " + obj_urdu[i], Double.parseDouble(prob_urdu[i]));
			}
			Hindi_Urdu.put(input_hindi, urdu_list);
		}
		br_hindi.close();
		br_urdu.close();
		urdu_pbt.close();
	}
	public Hash_Map(File mapper, File mapped, File pbt, int perage) throws NumberFormatException, IOException
	{
		Mapping_pbt = new HashMap<String, Double>();
		Hindi_Urdu = new HashMap<String, ArrayList<String>>();
		BufferedReader br_hindi = new BufferedReader(new FileReader(mapper));
		BufferedReader br_urdu = new BufferedReader(new FileReader(mapped));
		BufferedReader urdu_pbt = new BufferedReader(new FileReader(pbt));
		String input_hindi, input_urdu, urdu_prob;
		while((input_hindi = br_hindi.readLine()) != null)
		{
			input_urdu = br_urdu.readLine();
			urdu_prob = urdu_pbt.readLine();
			String[] obj_urdu = input_urdu.split("\t");
			String[] prob_urdu = urdu_prob.split("\t");
			ArrayList<String> urdu_list = new ArrayList<String>();
			for(int i = 0; i < Math.ceil(((double)perage/(double)100)*obj_urdu.length); i++)
			{
				obj_urdu[i] = obj_urdu[i].replaceAll("blk", "");
				urdu_list.add(obj_urdu[i]);
				Mapping_pbt.put(input_hindi + " " + obj_urdu[i], Double.parseDouble(prob_urdu[i]));
			}
			Hindi_Urdu.put(input_hindi, urdu_list);
		}
		br_hindi.close();
		br_urdu.close();
		urdu_pbt.close();
	}
	ArrayList<String> get(String key)
	{
		return Hindi_Urdu.get(key);
	}
	ArrayList<String> get_list(String key)
	{
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<Freq> frq = hind_urd.get(key);
		if(frq == null)
			return null;
		int i;
		for(i = 0; i < frq.size(); i++)
			temp.add(frq.get(i).character);
		return temp;
	}
	static ArrayList<Freq> addfreq(ArrayList<Freq> arrayList, String spl)
	{
		int i;
		for(i = 0; i < arrayList.size(); i++)
			if(spl.equals(arrayList.get(i).character))
				break;
		if(i == arrayList.size())
		{
			Freq temp = new Freq();
			temp.character = spl;
			temp.freq = 1;
			arrayList.add(temp);
			return arrayList;
		}
		else
		{
			for(int j = 0; j <= i; j++)
				if(arrayList.get(j).freq <= arrayList.get(i).freq+1)
				{
					Freq temp = arrayList.get(i);
					temp.freq ++;
					arrayList.add(j, temp);
					arrayList.remove(i+1);
					return arrayList;
				}
		}
		return arrayList;
	}
}
