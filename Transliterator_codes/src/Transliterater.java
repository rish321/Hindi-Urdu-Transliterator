import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Transliterater
{
	static Trie T;
	static Hash_Map Hindi_Urdu;
	static Bi_gram char_bi_probs;
	static Bi_gram sent_bi_probs;
	static Uni_gram uni_probs;
	public static void main(String args[]) throws IOException
	{
		//String path = "/home/rishabh/transliterator/";
		//build the trie
		File corpus = new File(args[0]);
		T = new Trie(new File(args[1]), corpus);
		System.out.println("Trie/Dictionary initialized");
		char_bi_probs = new Bi_gram(T.h, corpus, T.dist_chars);
		System.out.println("done char bigram");
		sent_bi_probs = new Bi_gram(corpus, T.dist);
		//System.out.println(sent_bi_probs.sent_bigram + " " + sent_bi_probs.sent_bigram.nnz());
		System.out.println("done sent_bi_prob");
		uni_probs = new Uni_gram(corpus, T.dist);
		System.out.println("done uni prob");
		//uni_probs.prnt();
		Hindi_Urdu = new Hash_Map(new File(args[2]), new File(args[3]));
		System.out.println("Hashmap initialized");
		System.out.println("Initialization done");
		BufferedReader br = new BufferedReader(new FileReader(new File(args[4])));
		FileWriter fw = new FileWriter(new File(args[5]));
		String input;
		while((input = br.readLine()) !=  null)
		{
			String[] words = input.split(" ");
			for(int i = 0; i < words.length; i++)
			{
				//System.out.println(words[i]);
				ArrayList<ArrayList<String>> Combos = new ArrayList<ArrayList<String>>();
				for(int j = 0; j < words[i].length(); j++)
					if(words[i].length() > j+2 && (((int)words[i].charAt(j) == 2330 && (int)words[i].charAt(j+1) == 2381 && (int)words[i].charAt(j+2) == 2331) || ((int)words[i].charAt(j) == 2336 && (int)words[i].charAt(j+1) == 2381 && (int)words[i].charAt(j+2) == 2336)))
					{
						//System.out.println("here");
						if(Hindi_Urdu.get((int)words[i].charAt(j) + " " + (int)words[i].charAt(j+1) + " " + (int)words[i].charAt(j+2)) != null)
							Combos.add(Hindi_Urdu.get((int)words[i].charAt(j) + " " + (int)words[i].charAt(j+1) + " " + (int)words[i].charAt(j+2)));
						j+=2;
					}
					else if(words[i].length() > j+1 && (int)words[i].charAt(j+1) == 2364)
					{
						//System.out.println(Hindi_Urdu.get((int)words[i].charAt(j) + " " + (int)words[i].charAt(j+1)));
						if(Hindi_Urdu.get((int)words[i].charAt(j) + " " + (int)words[i].charAt(j+1)) != null)
							Combos.add(Hindi_Urdu.get((int)words[i].charAt(j) + " " + (int)words[i].charAt(j+1)));
						j++;
					}
					else
					{
						//System.out.println(words[i].charAt(j) + " " + Integer.toString((int)words[i].charAt(j)) + " " + (int)words[i].charAt(j) + " " + Hindi_Urdu.get(Integer.toString((int)words[i].charAt(j))));
						if(Hindi_Urdu.get(Integer.toString((int)words[i].charAt(j))) != null)
							Combos.add(Hindi_Urdu.get(Integer.toString((int)words[i].charAt(j))));
					}
				ArrayList<String> words_urdu = new ArrayList<String>();
				ArrayList<String> all_words_urdu = new ArrayList<String>();
				//System.out.println(Combos);
				String optimal = "";
				if(Combos.size() != 0)
				{
					Process_Urdu(Combos, 0, "", words_urdu, all_words_urdu);
					if(words_urdu.size() == 0)
						optimal = most_optimum(all_words_urdu);
					else if(words_urdu.size() != 1)
						optimal = most_optimum(words_urdu);
					else
						optimal = words_urdu.get(0);
					optimal = split(optimal);
				}
				//System.out.println(optimal);
				//System.out.println(words_urdu);
				fw.write(optimal + " ");
			}
			fw.write("\n");
			//System.out.println();
		}
		br.close();
		fw.close();
		test_trans(new File(args[5]), new File(args[6]), new File(args[7]));
	}
	private static String split(String optimal)
	{

		//System.out.print(optimal.length() + " ");
		Double max = uni_probs.find_sent_prob(optimal, T.dist);
		if(max != 0)
			return optimal;
		Integer max_p = 0;
		for(int i = 2; i <= optimal.length() - 2; i++)
		{
			String s1 = optimal.substring(0, i);
			String s2 = optimal.substring(i);
			double bi_prob = sent_bi_probs.find_sent_prob(s1, s2, T.dist);
			if(bi_prob != 0)
			{
				if(max < bi_prob)
				{
					max = bi_prob;
					max_p = i;
				}
			}
			//System.out.println(i + " " + s1 + " " + s2 + " " + max + " " + sent_bi_probs.find_sent_prob(s1, s2, T.dist));
			/*if(max < sent_bi_probs.find_sent_prob(s1, s2, T.dist))
			{
				max = sent_bi_probs.find_sent_prob(s1, s2, T.dist);
				max_p = i;
			}*/
		}
		if(max_p != 0)
			return (optimal.substring(0, max_p) + " " + optimal.substring(max_p));
		/*for(int i = 2; i <= optimal.length() - 2; i++)
		{
			String s1 = optimal.substring(0, i);
			String s2 = optimal.substring(i);
			double bi_prob = uni_probs.find_sent_prob(s1, T.dist)*uni_probs.find_sent_prob(s2, T.dist);
			if(bi_prob != 0)
			{
				if(max < bi_prob)
				{
					max = bi_prob;
					max_p = i;
				}
			}
		}
		if(max_p != 0)
			return (optimal.substring(0, max_p) + " " + optimal.substring(max_p));*/
		for(int i = 2; i <= optimal.length() - 2; i++)
		{
			String s1 = optimal.substring(0, i);
			String s2 = optimal.substring(i);
			if(uni_probs.find_sent_prob(s1, T.dist) != 0)
			{
				double find = char_bi_probs.find_unsmoothed_char_prob(s2, T.h);
				if(find == 0)
					test(s1, s2, max, max_p, i);
			}
			else if(uni_probs.find_sent_prob(s2, T.dist) != 0)
			{
				double find = char_bi_probs.find_unsmoothed_char_prob(s1, T.h);
				if(find == 0)
					test(s1, s2, max, max_p, i);
			}
			else if(!(char_bi_probs.find_unsmoothed_char_prob(s1, T.h) != 0 && char_bi_probs.find_unsmoothed_char_prob(s2, T.h) != 0))
				test(s1, s2, max, max_p, i);
		}
		if(max_p != 0)
			return (optimal.substring(0, max_p) + " " + optimal.substring(max_p));
		return optimal;
	}
	private static void test(String s1, String s2, Double max, Integer max_p, int i)
	{
		String comp1 = s1.charAt(s1.length()-1) + " " + s2.charAt(0);
		String comp2 = s1.charAt(s1.length()-1) + "" + s2.charAt(0);
		double bi_prob = char_bi_probs.find_unsmoothed_char_prob(comp1, T.h);
		if(bi_prob > char_bi_probs.find_unsmoothed_char_prob(comp2, T.h))
			if(max < bi_prob)
			{
				max = bi_prob;
				max_p = i;
			}
	}
	static void Process_Urdu(ArrayList<ArrayList<String>> combos, int index, String built, ArrayList<String> words_urdu, ArrayList<String> all_words_urdu)
	{
		for(int i = 0; i < combos.get(index).size(); i++)
		{
			String[] poss = combos.get(index).get(i).split(" ");
			for(int j = 0; j < poss.length; j++)
				built+=(char)Integer.parseInt(poss[j]);
			if(index == combos.size()-1)
			{
				//System.out.println(built);
				all_words_urdu.add(built);
				if(T.search(built) == 1)
					words_urdu.add(built);
			}
			else
				Process_Urdu(combos, index+1, built, words_urdu, all_words_urdu);
		}
		//System.out.println();
		//return words_urdu;
	}
	static String most_optimum(ArrayList<String> words)
	{
		double max = 0;
		int max_p = 0;
		for(int k = 0; k < words.size(); k++)
			if(max < char_bi_probs.find_smoothed_char_prob(" " + words.get(k) + " ", T.h))
			{
				max = char_bi_probs.find_smoothed_char_prob(" " + words.get(k) + " ", T.h);
				max_p = k;
			}
		return words.get(max_p);
	}
	static void test_trans(File f1, File f2, File result) throws IOException
	{
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		FileWriter fw = new FileWriter(result);
		String input1;
		int c = 0;
		int tot = 0;
		while((input1 = br1.readLine()) !=  null)
		{
			tot++;
			String input2 = br2.readLine();
			if(input1.length() == 0 && input2.length() != 0 || input1.length() != 0 && input2.length() == 0)
			{
				fw.write(tot + "\t" + input1 + "\t" + input2);
				//c++;
			}
			else
			{	
				String[] words1 = input1.split(" ");
				String[] words2 = input2.split(" ");
				if(words1.length != words2.length)
				{
					fw.write(tot + "\t" + input1 + "\t" + input2);
					c++;
				}
				else 
					for(int i = 0; i < words1.length; i++)
						if(!words1[i].equals(words2[i]))
						{
							fw.write(tot + "\t" + words1[i] + "\t" + words2[i]);
							c++;
						}
			}
			fw.write("\n");
		}
		br1.close();
		br2.close();
		fw.close();
		System.out.println(c);
	}
}