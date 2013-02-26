import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Transliterate
{
	static Trie T;
	static Uni_gram uni_probs;
	static Bi_gram sent_bi_probs;
	static Tri_gram sent_tri_probs;
	static Hash_Map mapping;
	public static void main(String args[]) throws IOException
	{
		//String path = "/home/rishabh/Documents/Trans/";
		File corpus = new File(args[0]);
		T = new Trie(corpus, corpus);
		File char_list = new File(args[1]);
		File word_list = new File(args[2]);
		FileWriter fw = new FileWriter(char_list);
		for(int i = 0; i < T.dist_chars.size(); i++)
			fw.write(T.dist_chars.get(i) + "\n");
		fw.close();
		uni_probs = new Uni_gram(corpus, T.dist);
		fw = new FileWriter(word_list);
		for(int i = 0; i < T.dist_words.size(); i++)
			fw.write(T.dist_words.get(i) + "\t" + Math.log(uni_probs.find_sent_prob(T.dist_words.get(i), T.dist)) + "\n");
		fw.close();
		sent_bi_probs = new Bi_gram(T.h, corpus, T.dist_chars);
		//System.out.println(sent_bi_probs.u_bi_gram_h.get(sent_bi_probs.unique_char_bigrams.get(1)));
		sent_tri_probs = new Tri_gram(T.h, corpus, sent_bi_probs.u_bi_gram_h);
		//System.out.println(Math.log(sent_tri_probs.find_unsmoothed_char_prob("भारतीय", T.h, sent_bi_probs.u_bi_gram_h))/6);
		System.out.println("Hindi_dict initialization finished");
		File mapper = new File(args[3]);
		File mapped = new File(args[4]);
		File pbt = new File(args[5]);
		mapping = new Hash_Map(mapper, mapped, pbt, 28);
		System.out.println("Parallel Corpora Mapping finished");
		File perc = new File(args[6]);
		File a = new File(args[7]);
		fw = new FileWriter(perc);
		//Testing module
		BufferedReader br_source = new BufferedReader(new FileReader(new File(args[8])));
		BufferedReader br_target = new BufferedReader(new FileReader(new File(args[9])));
		String source = "";
		int tot_found = 0;
		int c = 0;
		while((source = br_source.readLine()) != null)
		{
			String target = br_target.readLine();
			/*if(target.equals("बशर्ते-कि") || target.equals("खुद-ब-खुद"))
				continue;*/
			System.out.print(target + " "/* + target.length() + " " + T.dist.get(target) + " "*/);
			ArrayList<ArrayList<String>> generated = Process_Transliteration.Process_Target(source, mapping.Hindi_Urdu);
			if(generated.size() == 0)
				continue;
			if(Calc_Lambda.fill_perc(source, target, perc, a, generated, uni_probs, sent_tri_probs, mapping.Mapping_pbt, T.dist, T.h, sent_bi_probs.u_bi_gram_h, 10) != 0)
				tot_found++;
			c++;
			System.out.println((double)tot_found/(double)c*100);
		}
		System.out.println((double)tot_found/(double)c*100 + " " + c);
		System.out.println("results in perc");
		fw.close();
		System.out.println(Accuracy.accurate(perc));
		br_source.close();
		br_target.close();
	}
}