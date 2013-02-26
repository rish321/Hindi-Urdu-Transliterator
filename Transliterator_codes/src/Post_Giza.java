import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;


public class Post_Giza
{
	public static void main (String args[]) throws IOException
	{
		ArrayList<Maps> Mapps = new ArrayList<Maps>();
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		@SuppressWarnings("resource")
		FileWriter fw = new FileWriter(new File(args[1]));
		String line;
		while((line = br.readLine()) != null)
		{
			fw = new FileWriter(new File(args[2]), true);
			if(!line.startsWith("#") && !line.startsWith("NULL"))
			{
				//System.out.println(line);
				String mapper[], mapped[];
				String chunktarget[] = line.split(" ");
				//System.out.println(line);
				line = br.readLine();
				//System.out.println(line);
				if(line.contains("- ({ 1 2 3"))
					continue;
				String chunksource[] = line.split("}");
				mapper = new String[max(chunktarget.length, chunksource.length-2)];
				mapped = new String[max(chunktarget.length, chunksource.length-2)];
				for(int i = 0; i < chunktarget.length; i++)
					mapped[i] = chunktarget[i];
				String nos = chunksource[0].substring(8);
				String indi[] = nos.split(" ");
				for(int i = 0; i < indi.length; i++)
					if(indi[i].length() != 0)
						mapper[Integer.parseInt(indi[i]) - 1] = "";
				int last = 0, ad = 0;
				for(int i = 1; i < chunksource.length-1; i++)
				{
					if(chunksource[i].length() > 7)
						indi = chunksource[i].substring(7, chunksource[i].length() - 1 ).split(" ");
					else
						indi = chunksource[i].substring(6, chunksource[i].length() - 1 ).split(" ");
					for(int j = 0; j < indi.length; j++)
						if(indi[j].length() == 0)
						{
							mapped = ArrayUtils.add(mapped, last, "blk");
							mapper = ArrayUtils.add(mapper, last, "");
							mapper[last++] = chunksource[i].substring(2, 3);
							ad++;
						}
						else
						{
							mapper[Integer.parseInt(indi[j]) - 1 + ad] = chunksource[i].substring(2, 3);
							last++;
						}
					if(indi.length > 1)
					{
						int first = Integer.parseInt(indi[0]) - 1 + ad;
						for(int j = 1; j < indi.length; j++)
						{
							mapped[first] += mapped[first+j];
							mapper = ArrayUtils.remove(mapper, first+j);
							mapped = ArrayUtils.remove(mapped, first+j);
							last --;
							ad --;
						}
					}
				}
				/*for(int i = 0; i < mapper.length; i++)
					if(mapper[i] == null)
						mapper[i] = "blk";
					else if(mapper[i].equals(""))
						if(i != 0)
						{
							mapped[i-1] += mapped[i];
							mapper = ArrayUtils.remove(mapper, i);
							mapped = ArrayUtils.remove(mapped, i);
						}
						else
							mapper[i] = "blk";*/
				for(int i = 0; i < mapper.length; i++)
					if(mapper[i] != null && mapped[i] != null)
					{
						if(mapped[i].endsWith("null"))
							mapped[i] = "blk";
						else if(mapped[i].startsWith("blk") && mapped[i].length()>3)
							mapped[i] = mapped[i].substring(3);
						fw.write(mapper[i] + "\t" + mapped[i] + "\n");
					}
				Maps temp = new Maps();
				temp.mapped = mapped;
				temp.mapper = mapper;
				Mapps.add(temp);
				fw.close();
			}
		}
		br.close();
		//Bi_gram char_bi_probs;
		Hash_Map Hindi_Urd = new Hash_Map(new File(args[3]), new File(args[4]), new File(args[5]), new File(args[2]));
		//System.out.println(Hindi_Urd.get_list("ب"));
		Bi_gram bgrm = new Bi_gram(Hindi_Urd.h);
		for(int j = 0; j < Mapps.size(); j++)
		{
			String[] mapper = Mapps.get(j).mapper;
			String[] mapped = Mapps.get(j).mapped;
			for(int i = 0; i < mapper.length; i++)
				if(mapper[i] != null && mapper[i].equals(""))
				{
					if(i == 0)
					{
						bgrm.put("", mapped[i+1], Hindi_Urd.h);
						bgrm.put(mapped[i], mapped[i+1], Hindi_Urd.h);
					}
					else if(i == mapper.length - 1)
					{
						bgrm.put(mapped[i-1], mapped[i], Hindi_Urd.h);
						bgrm.put(mapped[i], "", Hindi_Urd.h);
					}
					else
					{
						bgrm.put(mapped[i-1], mapped[i], Hindi_Urd.h);
						bgrm.put(mapped[i], mapped[i+1], Hindi_Urd.h);
					}
				}
		}
		Bi_gram.normalize(bgrm.un_smoothed_char_bigram);
		fw.close();
		fw = new FileWriter(new File(args[2]));
		for(int j = 0; j < Mapps.size(); j++)
		{
			String[] mapper = Mapps.get(j).mapper;
			String[] mapped = Mapps.get(j).mapped;
			for(int i = 0; i < mapper.length; i++)
				if(mapper[i] != null && mapper[i].equals(""))
				{
					if(i == 0)
					{
						if(bgrm.get("", mapped[i], Hindi_Urd.h) < bgrm.get(mapped[i], mapped[i+1], Hindi_Urd.h))
						{
							mapped [i+1] = mapped[i] + mapped[i+1];
							mapper = ArrayUtils.remove(mapper, i);
							mapped = ArrayUtils.remove(mapped, i);
						}
					}
					else if(i == mapper.length - 1)
					{
						if(bgrm.get(mapped[i-1], mapped[i], Hindi_Urd.h) > bgrm.get(mapped[i], "", Hindi_Urd.h))
						{
							mapped [i-1] += mapped[i];
							mapper = ArrayUtils.remove(mapper, i);
							mapped = ArrayUtils.remove(mapped, i);
						}
					}
					else
					{
						//System.out.println(bgrm.get(mapped[i], mapped[i+1], Hindi_Urd.h) + " "+ mapped[i-1] + mapped[i] + mapped[i+1] + " " + bgrm.get(mapped[i - 1], mapped[i], Hindi_Urd.h));
						if(bgrm.get(mapped[i - 1], mapped[i], Hindi_Urd.h) > bgrm.get(mapped[i], mapped[i+1], Hindi_Urd.h))
							mapped[i-1] += mapped[i];
						else
							mapped [i+1] = mapped[i] + mapped[i+1];
						mapper = ArrayUtils.remove(mapper, i);
						mapped = ArrayUtils.remove(mapped, i);
					}
				}
			for(int i = 0; i < mapper.length; i++)
				if(!(mapper[i] == null && mapped[i] == null))
				{
					if(mapper[i] == null || mapper[i].equals(""))
						mapper[i] = "blk";
					fw.write(mapper[i] +"\t" + mapped[i] + "\n");
				}
			fw.write("\n");
			//System.out.println();
		}
		Hindi_Urd = new Hash_Map(new File(args[3]), new File(args[4]), new File(args[5]), new File(args[2]));
		//Bi_gram.print(bgrm.un_smoothed_char_bigram, Hindi_Urd.lang2_chars);
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
