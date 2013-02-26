import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;


public class Uni_gram
{
	double[] sent_unigram;
	public Uni_gram(File corpus, Hashtable<String, Integer> dist) throws IOException
	{
		sent_unigram = new double[dist.size()];
		int total = 0;
		BufferedReader urdu_text = new BufferedReader(new FileReader(corpus));
		//int c = 0;
		String s;
		String[] str;
		while((s = urdu_text.readLine()) != null)
		{
			s = s.replaceAll("[.*']", "");
			str = s.split(" ");
			for(int i = 0; i < str.length; i++)
				sent_unigram[dist.get(str[i])]++;
			total += str.length;
		}
		urdu_text.close();
		for(int i = 0; i < dist.size(); i++)
			sent_unigram[i] /= total;
	}
	public double find_sent_prob(String word, Hashtable<String, Integer> dist)
	{
		if(dist.get(word) == null)
			return 0.0000000001;
		return sent_unigram[dist.get(word)];
	}
	public void prnt()
	{
		for(int i = 0; i < sent_unigram.length; i++)
			System.out.println(sent_unigram[i]);
	}
}
