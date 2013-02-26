import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Calc_Lambda
{
	public static int fill_perc(String input, String deemed, File perc, File a, ArrayList<ArrayList<String>> process_Target, Uni_gram uni_probs2, Tri_gram sent_tri_probs2, HashMap<String, Double> mapping_pbt, Hashtable<String, Integer> dist, Hashtable<Character, Integer> h, Hashtable<String, Integer> u_bi_gram_h, double per) throws IOException
	{
		//System.out.println();
		/*BufferedReader br = new BufferedReader(new FileReader(a));
		String val = br.readLine();
		String comps[] = val.split("\t");
		System.out.println(comps[0] + " " + comps[1] + " " + comps[2] + " " + comps[3]);
		br.close();*/
		FileWriter fw = new FileWriter(perc, true);
		ArrayList<Double> sum = new ArrayList<Double>();
		ArrayList<String> gens = new ArrayList<String>();
		Double tot_mle = 0.0, tot_tri = 0.0, tot_uni = 0.0;
		for(int i = 0; i < process_Target.size(); i++)
		{		
			String current = process_Target.get(i).get(process_Target.get(i).size() - 1);
			/*if(current.equals("रजामंद"))
			{
				System.out.println(current);
				System.out.println(Double.parseDouble(find_unigram_train(current, uni_probs2, dist))*Double.parseDouble(comps[3]));
				System.out.println(find_mle_test(input, process_Target.get(i), mapping_pbt, comps[1]));
			}*/
			String mle = find_mle_train(input, process_Target.get(i), mapping_pbt);
			String tri_gram = find_trigram_train(current, sent_tri_probs2, h, u_bi_gram_h);
			String uni_gram = find_unigram_train(current, uni_probs2, dist);
			/*String mle = find_mle_test(input, process_Target.get(i), mapping_pbt, comps[1]);
			String tri_gram = find_trigram_test(current, sent_tri_probs2, h, u_bi_gram_h, comps[2]);
			String uni_gram = find_unigram_test(current, uni_probs2, dist, comps[3]);*/
			/*String value = "";
			if(deemed.equals(current))
				value = "1";
			else
				value = "-1";*/
			if(!(tri_gram.equals("NaN") || tri_gram.equals("-Infinity") || uni_gram.equals("-Infinity")))
			{
				tot_mle += Double.parseDouble(mle);
				tot_tri += Double.parseDouble(tri_gram);
				tot_uni += Double.parseDouble(uni_gram);
				//fw.write(/*current + "\t" + */Double.toString(Double.parseDouble(mle)/Double.parseDouble(uni_gram)) + "\t" + Double.toString(Double.parseDouble(tri_gram)/Double.parseDouble(uni_gram)) + "\t" + "1"/* + "\t" + (Double.parseDouble(mle) + Double.parseDouble(tri_gram) + Double.parseDouble(uni_gram))*/ + "\t" + value + "\n");
				//if(value.equals("1"))
				//	fw.write(/*current + "\t" + */Double.toString(Double.parseDouble(mle)/Double.parseDouble(uni_gram)) + "\t" + Double.toString(Double.parseDouble(tri_gram)/Double.parseDouble(uni_gram)) + "\t" + "1"/* + "\t" + (Double.parseDouble(mle) + Double.parseDouble(tri_gram) + Double.parseDouble(uni_gram))*/ + "\t" + value + "\n");
				//else if(value.equals("-1"))
				//	fw.write(/*current + "\t" + */Double.toString(-1*Double.parseDouble(mle)/Double.parseDouble(uni_gram)) + "\t" + Double.toString(-1*Double.parseDouble(tri_gram)/Double.parseDouble(uni_gram)) + "\t" + "1"/* + "\t" + (Double.parseDouble(mle) + Double.parseDouble(tri_gram) + Double.parseDouble(uni_gram))*/ + "\t" + value + "\n");
				sum.add(Double.parseDouble(mle) + Double.parseDouble(tri_gram) + Double.parseDouble(uni_gram));
				gens.add(current);
				/*String check = "";
				double tot = Double.parseDouble(comps[0]) + Double.parseDouble(mle) + Double.parseDouble(tri_gram) + Double.parseDouble(uni_gram);
				if(tot > 0)
					check = "1";
				else
					check = "0";
				if(check.equals("1"))
					fw.write(current + " " + value + " " + check + " " + tot + " " + comps[0] + " " + mle + " " + tri_gram + " " + uni_gram + " " + "\n");*/
			}
		}
		/*double avg = (tot_mle + tot_tri + tot_uni)/sum.size();
		//fw.write(deemed + " " + tot_mle/sum.size() + " " + tot_tri/sum.size() + " " + tot_uni/sum.size() + " " + avg + " ");
		double std = 0.0;
		for(int i = 0; i < sum.size(); i++)
			std += Math.pow((sum.get(i) - avg), 2);
		std = Math.sqrt(std/sum.size());
		//fw.write(std + "\n");
		//int cnt = 0;*/
		int found = 0;
		if(gens.size() == 0)
		{
			found = 1;
			fw.close();
			return found;
		}
		ascsort(sum, gens);
		if(gens.get(0).equals(deemed))
			found = 1;
		fw.write(input + "\t" + deemed + "\t" + gens.get(0) + "\n");
		/*for(int i = 0; i < Math.ceil((double)(sum.size()*per)/(double)100); i++)
		{
			//if(sum.get(i) > avg)
			{
				cnt++;
				fw.write(gens.get(i) + " " + sum.get(i) + " ");
				if(gens.get(i).equals(deemed))
				{
					found++;
					fw.write("1");
				}
				else
					fw.write("0");
				fw.write("\n");
			}
		}
		System.out.print(deemed + " " + sum.size() + " " + cnt + " " + found);*/
		//fw.write("\n");
		fw.close();
		return found;
		//return 0;
	}
	public static void ascsort(ArrayList<Double> sum, ArrayList<String> gens)
	{
		for(int i = 0; i < sum.size()-1; i++)
			for(int j = 0; j < sum.size(); j++)
				if(sum.get(i) > sum.get(j))
				{
					double temp = sum.get(i);
					sum.set(i, sum.get(j));
					sum.set(j, temp);
					String tmp = gens.get(i);
					gens.set(i, gens.get(j));
					gens.set(j, tmp);
				}
	}
	public static String find_mle_test(String input, ArrayList<String> string, HashMap<String, Double> mapping_pbt, String value)
	{
		double pbt = 1;
		for(int i = 0; i < input.length(); i++)
		{
			//System.out.print(mapping_pbt.get(input.charAt(i) + " " + string.get(i)));
			//System.out.println(input.charAt(i) + " " + string.get(i));
			if(mapping_pbt.get(input.charAt(i) + " " + string.get(i)) == null)
				pbt *= 0.0000000001;
			else
				pbt *= mapping_pbt.get(input.charAt(i) + " " + string.get(i));
		}
		//System.out.println(pbt);
		pbt = Math.log(pbt);
		pbt /= input.length();
		//System.out.println(pbt);
		return Double.toString(Double.parseDouble(value) * pbt);
	}
	public static String find_trigram_test(String current, Tri_gram sent_tri_probs2, Hashtable<Character, Integer> h,	Hashtable<String, Integer> u_bi_gram_h, String string)
	{
		return Double.toString(Double.parseDouble(string) * Math.log(sent_tri_probs2.find_unsmoothed_char_prob("  " + current, h, u_bi_gram_h))/(string.length()+2));
	}
	public static String find_unigram_test(String current, Uni_gram uni_probs2, Hashtable<String, Integer> dist, String string)
	{
		//System.out.println(uni_probs2.find_sent_prob(string, dist));
		return Double.toString(Double.parseDouble(string) * Math.log(uni_probs2.find_sent_prob(current, dist)));
	}
	public static String find_mle_train(String input, ArrayList<String> string, HashMap<String, Double> mapping_pbt)
	{
		double pbt = 1;
		for(int i = 0; i < input.length(); i++)
		{
			if(i >= string.size() || string.get(i) == null)
				continue;
			//System.out.println(string.get(i));
			if(mapping_pbt.get(input.charAt(i) + " " + string.get(i)) == null)
				pbt *= 0.0000000001;
			else
				pbt *= mapping_pbt.get(input.charAt(i) + " " + string.get(i));
		}
		pbt = Math.log(pbt);
		pbt /= input.length();
		return Double.toString(pbt);
	}
	public static String find_trigram_train(String string, Tri_gram sent_tri_probs2, Hashtable<Character, Integer> h, Hashtable<String, Integer> u_bi_gram_h)
	{
		return Double.toString(Math.log(sent_tri_probs2.find_unsmoothed_char_prob("  " + string, h, u_bi_gram_h))/(string.length()+2));
	}
	public static String find_unigram_train(String string, Uni_gram uni_probs2, Hashtable<String, Integer> dist)
	{
		return Double.toString(Math.log(uni_probs2.find_sent_prob(string, dist)));
	}
}
