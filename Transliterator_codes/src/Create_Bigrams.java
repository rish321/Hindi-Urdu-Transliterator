import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Create_Bigrams
{
	static Trie T;
	static Uni_gram uni_probs;
	static Bi_gram sent_bi_probs;
	public static void main(String args[]) throws IOException
	{
		File corpus = new File(args[0]);
		T = new Trie(corpus, corpus);
		//FileWriter fw = new FileWriter(new File(args[1]));
		sent_bi_probs = new Bi_gram(corpus, T.dist);
		ArrayList<String> uniq_Uni = new ArrayList<String>();
		ArrayList<String> uniq_Uni2 = new ArrayList<String>();
		File test = new File(args[1]);
		BufferedReader br = new BufferedReader(new FileReader(test));
		File train = new File(args[2]);
		BufferedReader br2 = new BufferedReader(new FileReader(train));
		FileWriter fw = new FileWriter(new File("/home/rishabh/Documents/Trans/Creating_Resource/Uniq_Bi.ur"));
		FileWriter fw2 = new FileWriter(new File("/home/rishabh/Documents/Trans/Creating_Resource/Uniq_Bi.hi"));
		String str;
		int c = 0;
		while((str = br.readLine()) != null)
		{
			//String spl[] = s.split(" ");
			String st = br2.readLine();
			//br.readLine();
			//String s2 = br.readLine();
			//System.out.println(str);
			String s = str.split("s")[0];
			String s2 = str.split("s")[1];
			if(sent_bi_probs.find_sent_prob(s, s2, T.dist) > 0)
			{
				++c;
				/*for(int i = 0; i < s.split("").length; i++)
					fw.write(s.split("")[i] + " ");
				fw.write("b");
				for(int i = 0; i < s2.split("").length; i++)
					fw.write(s2.split("")[i] + " ");*/
				fw.write(s+" "+s2+"\n");
				fw2.write(st+"\n");
				//System.out.println( + " " + s2.split(""));
				//System.out.println(st);
				insert(uniq_Uni, uniq_Uni2, s, st.split(" ")[0]);
				insert(uniq_Uni, uniq_Uni2, s2, st.split(" ")[1]);
			}
		}
		System.out.println(c);
		System.out.println(uniq_Uni.size() + " " + uniq_Uni2.size());
		for(int i = 0; i < uniq_Uni2.size(); i++)
			System.out.println(uniq_Uni2.get(i));
		br.close();
		br2.close();
		fw.close();
		fw2.close();
		/*System.out.println(T.dist_words.size() + " " + sent_bi_probs.sent_bigram.nnz());
		for(int i = 0; i < T.dist_words.size(); i++)
			for(int j = 0; j < T.dist_words.size(); j++)
				if(sent_bi_probs.find_sent_prob(T.dist_words.get(i), T.dist_words.get(j), T.dist) > 0)
				{
					String print = "";
					for(int k = 0; k < T.dist_words.get(i).length(); k++)
						print += T.dist_words.get(i).charAt(k) + " ";
					print += "s ";
					for(int k = 0; k < T.dist_words.get(j).length(); k++)
						print += T.dist_words.get(j).charAt(k) + " ";
					print = print.substring(0, print.length() - 1);
					fw.write(print + "\n");
				}*/
		//fw.close();
	}
	private static void insert(ArrayList<String> uniq_Uni, ArrayList<String> uniq_Uni2, String s, String s2)
	{
		int i = 0;
		for(i = 0; i < uniq_Uni.size(); i++)
			if(uniq_Uni.get(i).equals(s))
				break;
		if(i == uniq_Uni.size())
		{
			uniq_Uni.add(s);
			uniq_Uni2.add(s2);
		}
	}
}
