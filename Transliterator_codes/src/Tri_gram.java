import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;


public class Tri_gram
{
	double[][] smoothed_char_trigram;
	double[][] un_smoothed_char_trigram;
	SparseMatrix sent_trigram;
	//String[] sent_trigram;
	int[] lin_count;
	public Tri_gram(Hashtable<Character, Integer> h, File corpus, Hashtable<String, Integer> u_bi_gram_h) throws IOException
	{
		smoothed_char_trigram = new double[u_bi_gram_h.size()][h.size()];
		un_smoothed_char_trigram = new double[u_bi_gram_h.size()][h.size()];
		lin_count = new int[u_bi_gram_h.size()];
		BufferedReader urdu_text = new BufferedReader(new FileReader(corpus));
		//int c = 0;
		String s;
		String[] str;
		while((s = urdu_text.readLine()) != null)
		{
			s = s.replaceAll("[.*']", "");
			str = s.split(" ");
			for(int i = 0; i < str.length; i++)
				add_count("  " + str[i], h, u_bi_gram_h);
			/*while(str != null && str.length > 0)
			{
				add_count(" " + str[str.length-1] + " ", h);
				str = Arrays.copyOf(str, str.length-1);
			}*/
			
		}
		urdu_text.close();
		for(int i = 0; i < u_bi_gram_h.size(); i++)
			for(int j = 0; j < h.size(); j++)
				smoothed_char_trigram[i][j] = (smoothed_char_trigram[i][j] + 1)/(double)(lin_count[i]+h.size());
		for(int i = 0; i < u_bi_gram_h.size(); i++)
			for(int j = 0; j < h.size(); j++)
				un_smoothed_char_trigram[i][j] /= lin_count[i];
	}
	public Tri_gram(File corpus, Hashtable<String, Integer> dist, Bi_gram sent_bigram) throws IOException
	{
		//FileWriter fw = new FileWriter(new File("test"));
		sent_trigram = new SparseMatrix(Math.max(dist.size(), sent_bigram.sent_bigram.nnz()));
		BufferedReader urdu_text = new BufferedReader(new FileReader(corpus));
		String s;
		String[] str;
		lin_count = new int[dist.size()];
		//int c = 0;
		while((s = urdu_text.readLine()) != null)
		{
			//System.out.println(++c);
			s = s.replaceAll("[.*']", "");
			str = s.split(" ");
			for(int i = 0; i < str.length - 2; i++)
			{
				sent_trigram.put(dist.get(str[i]), dist.get(str[i+1]), sent_trigram.get(dist.get(str[i]), dist.get(str[i+1]))+1);
				lin_count[dist.get(str[i])]++;
			}
		}
		urdu_text.close();
		for(int i = 0; i < dist.size(); i++)
			for(int j = 0; j < dist.size(); j++)
				sent_trigram.put(i, j, sent_trigram.get(i, j)/lin_count[i]);
	}
	public static void print(double[][] table)
	{
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[i].length; j++)
				System.out.print(table[i][j] + "\t");
			System.out.println();
		}
	}
	public static void print(double[][] table, ArrayList<String> h)
	{
		for(int i = 0; i < table.length; i++)
			System.out.print(h.get(i) + "\t");
		System.out.println();
		for(int i = 0; i < table.length; i++)
		{
			System.out.print(h.get(i) + "\t");
			for(int j = 0; j < table[i].length; j++)
				System.out.print(table[i][j] + "\t");
			System.out.println();
		}
	}
	public Tri_gram(Hashtable<String, Integer> h)
	{
		un_smoothed_char_trigram = new double[h.size()][h.size()];
	}
	public void put(String posx, String posy, Hashtable<String, Integer> h)
	{
		if(posy != null && posx != null && h.get(posx) != null && h.get(posy) != null)
			un_smoothed_char_trigram[h.get(posx)][h.get(posy)]++;
	}
	public double get(String posx, String posy, Hashtable<String, Integer> h)
	{
		if(posy != null && posx != null && h.get(posx) != null && h.get(posy) != null)
			return un_smoothed_char_trigram[h.get(posx)][h.get(posy)];
		return 0;
	}
	private void add_count(String word, Hashtable<Character, Integer> h, Hashtable<String, Integer> u_bi_gram_h)
	{
		for(int i = 0; i < word.length()-2; i++)
		{
			un_smoothed_char_trigram[u_bi_gram_h.get(word.substring(i, i+2))][h.get(word.charAt(i+2))]++;
			smoothed_char_trigram[u_bi_gram_h.get(word.substring(i, i+2))][h.get(word.charAt(i+2))]++;
			lin_count[u_bi_gram_h.get(word.substring(i, i+2))]++;
		}
	}
	public double find_smoothed_char_prob(String word, Hashtable<Character, Integer> h)
	{
		double init = 1;
		for(int i = 0; i < word.length()-1; i++)
			init*=smoothed_char_trigram[h.get(word.charAt(i))][h.get(word.charAt(i+1))];
		return init;
	}
	public double find_unsmoothed_char_prob(String word, Hashtable<Character, Integer> h, Hashtable<String, Integer> u_bi_gram_h)
	{
		double init = 1;
		for(int i = 0; i < word.length()-2; i++)
		{
			//System.out.println(u_bi_gram_h.get(word.substring(i, i+2)) + " " + h.get(word.charAt(i+2)) + " " + word.substring(i, i+2) + " " + word.charAt(i+2));
			if(u_bi_gram_h.get(word.substring(i, i+2)) == null || h.get(word.charAt(i+2)) == null)
				init *= 0.0000000001;
			else
				init*=un_smoothed_char_trigram[u_bi_gram_h.get(word.substring(i, i+2))][h.get(word.charAt(i+2))];
		}
		return init;
	}
	public double find_sent_prob(String word1, String word2, Hashtable<String, Integer> dist)
	{
		if(dist.get(word1) == null || dist.get(word2) == null)
			return 0;
		int row = dist.get(word1);
		int col_num = dist.get(word2);
		/*String[] col = sent_trigram[row].split(" ");
		return Double.parseDouble(col[col_num]);*/
		return sent_trigram.get(row, col_num);
	}
	public static int nthOccurrence(String str, char c, int n)
	{
	    int pos = str.indexOf(c, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos+1);
	    return pos;
	}
	public static void normalize(double[][] table)
	{
		for(int i = 0; i < table.length; i++)
		{
			double total = 0;
			for(int j = 0; j < table.length; j++)
				total += table[i][j];
			for(int j = 0; j < table.length; j++)
				table[i][j] /= total;
		}
				
	}
}
