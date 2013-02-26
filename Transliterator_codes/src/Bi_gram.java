import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


public class Bi_gram
{
	double[][] smoothed_char_bigram;
	double[][] un_smoothed_char_bigram;
	SparseMatrix sent_bigram;
	ArrayList<String> unique_char_bigrams;
	Hashtable<String, Integer> u_bi_gram_h;
	//String[] sent_bigram;
	int[] lin_count;
	public Bi_gram(Hashtable<Character, Integer> h, File corpus, ArrayList<String> dist_chars) throws IOException
	{
		smoothed_char_bigram = new double[h.size()][h.size()];
		un_smoothed_char_bigram = new double[h.size()][h.size()];
		unique_char_bigrams = new ArrayList<String>();
		u_bi_gram_h = new Hashtable<String, Integer>();
		lin_count = new int[h.size()];
		BufferedReader urdu_text = new BufferedReader(new FileReader(corpus));
		//int c = 0;
		String s;
		String[] str;
		while((s = urdu_text.readLine()) != null)
		{
			s = s.replaceAll("[.*']", "");
			str = s.split(" ");
			while(str != null && str.length > 0)
			{
				add_count(" " + str[str.length-1] + " ", h);
				str = Arrays.copyOf(str, str.length-1);
			}
		}
		urdu_text.close();
		for(int i = 0; i < h.size(); i++)
			for(int j = 0; j < h.size(); j++)
				smoothed_char_bigram[i][j] = (smoothed_char_bigram[i][j] + 1)/(double)(lin_count[i]+h.size());
		for(int i = 0; i < h.size(); i++)
			for(int j = 0; j < h.size(); j++)
			{
				un_smoothed_char_bigram[i][j] /= lin_count[i];
				if(un_smoothed_char_bigram[i][j] != 0)
				{
					unique_char_bigrams.add(dist_chars.get(i) + dist_chars.get(j));
					u_bi_gram_h.put(dist_chars.get(i) + dist_chars.get(j), u_bi_gram_h.size());
				}
			}
	}
	public Bi_gram(File corpus, Hashtable<String, Integer> dist) throws IOException
	{
		//FileWriter fw = new FileWriter(new File("test"));
		sent_bigram = new SparseMatrix(dist.size());
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
			for(int i = 0; i < str.length - 1; i++)
			{
				sent_bigram.put(dist.get(str[i]), dist.get(str[i+1]), sent_bigram.get(dist.get(str[i]), dist.get(str[i+1]))+1);
				lin_count[dist.get(str[i])]++;
			}
		}
		urdu_text.close();
		/*System.out.println("bigram");
		for(int i = 0; i < dist.size(); i++)
			for(int j = 0; j < dist.size(); j++)
				sent_bigram.put(i, j, sent_bigram.get(i, j)/lin_count[i])*/;
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
	public Bi_gram(Hashtable<String, Integer> h)
	{
		un_smoothed_char_bigram = new double[h.size()][h.size()];
	}
	public void put(String posx, String posy, Hashtable<String, Integer> h)
	{
		if(posy != null && posx != null && h.get(posx) != null && h.get(posy) != null)
			un_smoothed_char_bigram[h.get(posx)][h.get(posy)]++;
	}
	public double get(String posx, String posy, Hashtable<String, Integer> h)
	{
		if(posy != null && posx != null && h.get(posx) != null && h.get(posy) != null)
			return un_smoothed_char_bigram[h.get(posx)][h.get(posy)];
		return 0;
	}
	private void add_count(String word, Hashtable<Character, Integer> h)
	{
		for(int i = 0; i < word.length()-1; i++)
		{
			un_smoothed_char_bigram[h.get(word.charAt(i))][h.get(word.charAt(i+1))]++;
			smoothed_char_bigram[h.get(word.charAt(i))][h.get(word.charAt(i+1))]++;
			lin_count[h.get(word.charAt(i))]++;
		}
	}
	public double find_smoothed_char_prob(String word, Hashtable<Character, Integer> h)
	{
		double init = 1;
		for(int i = 0; i < word.length()-1; i++)
			init*=smoothed_char_bigram[h.get(word.charAt(i))][h.get(word.charAt(i+1))];
		return init;
	}
	public double find_unsmoothed_char_prob(String word, Hashtable<Character, Integer> h)
	{
		double init = 1;
		for(int i = 0; i < word.length()-1; i++)
			init*=un_smoothed_char_bigram[h.get(word.charAt(i))][h.get(word.charAt(i+1))];
		return init;
	}
	public double find_sent_prob(String word1, String word2, Hashtable<String, Integer> dist)
	{
		if(dist.get(word1) == null || dist.get(word2) == null)
			return 0;
		int row = dist.get(word1);
		int col_num = dist.get(word2);
		/*String[] col = sent_bigram[row].split(" ");
		return Double.parseDouble(col[col_num]);*/
		return sent_bigram.get(row, col_num)/lin_count[row];
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
