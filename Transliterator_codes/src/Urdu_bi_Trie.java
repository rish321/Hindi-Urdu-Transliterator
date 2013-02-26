import java.io.File;
import java.io.IOException;


public class Urdu_bi_Trie
{
	static Trie T;
	public static void main(String args[]) throws IOException
	{
		File corpus = new File(args[0]);
		T = new Trie(corpus, corpus);
	}
}
