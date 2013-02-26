
import java.io.*;
import java.util.*;

public class Trie 
{
	static Trie_Node t;
	Hashtable<Character, Integer> h;
	ArrayList<String> dist_words;
	ArrayList<String> dist_chars;
	Hashtable<String, Integer> dist;
	//double[] uni_pbt;
	public Trie(File dictionary, File corpus) throws IOException
	{
		h = new Hashtable<Character, Integer>();
		dist = new Hashtable<String, Integer>();
		dist_chars = new ArrayList<String>();
		initialize(dictionary);
		//System.out.println("initialized");
		dist_words = new ArrayList<String>();	
		//System.out.println("initialized");
		t = new Trie_Node();
		t.child = new Trie_Node[h.size()];
		//uni_pbt = new double[h.size()];

		String s;
		String str[];
		BufferedReader urdu_text = new BufferedReader(new FileReader(corpus));
		//int c = 0;
		while((s = urdu_text.readLine()) != null)
		{
			//System.out.println(s);
			s = s.replaceAll("[.*']", "");
			//s = s.replaceAll("-", " ");
			//System.out.println("this.s is "+s);
			str = s.split(" ");
			//System.out.println(str.length);
			while(str != null && str.length > 0)
			{
				//c++;
				//System.out.println("i "+str.length+"i\t"+str[str.length-1]+" "+str[str.length-1].length());
				//System.out.println(str[str.length-1]+'$');
				insert(str[str.length-1]+'$', h, dist_words, dist);
				str = Arrays.copyOf(str, str.length-1);
				//System.out.println("str left "+str.length);
				//insert("گوگل کے ترجمہ کا");
			}
			//System.out.println(dist_words.size());
			//System.out.println("done");
		}
		//System.out.println(c);
		//System.out.println("doneBuild\n");
		urdu_text.close();
		//System.out.println("Trie created");
	}
	public int search(String s)
	{
		//System.out.println(s);
		int x = 0, index;
		Trie_Node current = t;
		s = s+ '$';
		//System.out.println(s);
		while(x < s.length())
		{
			//System.out.println((int)s.charAt(x));
			index = (Integer)h.get(s.charAt(x));
			//System.out.println("index "+index);
			/*if(current.child[1] != null)
				System.out.println("see "+current.child[1].trie_node);*/
			if(index == -1 || current.child[index] == null || current.child[index].trie_node != true)
			{
				//System.out.println("1");
				return 0;
			}
			else
			{
				//System.out.println("2");
				current = current.child[index];
				x++;
			}
			//System.out.println("see2 "+current.trie_node);
		}
		return 1;
	}

	public void initialize(File corpus) throws IOException
	{
		//System.out.println(corpus);
		BufferedReader urdu_utf8 = new BufferedReader(new FileReader(corpus));
		StringBuilder s2 = new StringBuilder();
		String s, s1;
		//System.out.println("mnx");
		while((s1 = urdu_utf8.readLine()) != null)
			s2.append(s1);
		//System.out.println(s);
		urdu_utf8.close();
		//s = s2.toString();
		s2 = replaceAll(s2, "[.*']", "");
		//System.out.println("done");
		//s = s.replaceAll("-", " ");
		//System.out.println(s);
		//s = s.replaceAll((String.format("(.)(?<=(?:(?=\\1).).{0,%d}(?:(?=\\1).))", s.length())), "");
		//System.out.println(s);
		s2.append('$');
		//System.out.println(s);
		/*for(int i=0; i<s.length(); i++)
			h.put(s.charAt(i), i);*/
		/*char[] c = new char[co];
		for(int i = 0; i < s2.; i++)
			c[i] = s2.charAt(i);*/
		//s = s2.toString();
		s = removeDupes(s2);
		//System.out.println(s);
		//System.out.println("hgf");
		for(int i=0; i<s.length(); i++)
		{
			h.put(s.charAt(i), i);
			//System.out.println(s.charAt(i));
			dist_chars.add(Character.toString(s.charAt(i)));
		}
		//System.out.println(h.size());
	}

	public static void insert(String s, Hashtable<Character, Integer> h, ArrayList<String> dist_words, Hashtable<String, Integer> dist)
	{
		
		//System.out.println(s);
		int x=0, index;
		Trie_Node current = t;
		//System.out.println("s is "+s+"\t"+s.length());
		while(x < s.length())
		{
			//System.out.println("sss "+s.charAt(x));
			index = (Integer)h.get(s.charAt(x));
			//System.out.println("index "+index);
			if(current.child[index] == null)		
			{
				current.child[index] = new Trie_Node();
				current.child[index].child = new Trie_Node[h.size()];
				if(x == s.length() - 1)
				{
					//System.out.println(s.substring(0, s.length() - 1) + " " + dist_words.size()+1);
					dist_words.add(s.substring(0, s.length() - 1));
					dist.put(s.substring(0, s.length() - 1), dist.size());
				}
			}
			if(current.child[index].trie_node != true)
				current.child[index].trie_node = true;
			current = current.child[index];
			x++;
		}
	}

	public static String  removeDupes(StringBuilder array)
	{
		int NewLength = 1, i, j;
		int Length = array.length();
		for(i=1; i< Length; i++)
		{
			for(j=0; j< NewLength ; j++)
			{

				if(array.charAt(i) == array.charAt(j))
				{
					break;
				}
			}

			if (j==NewLength )
			{
				array.replace(NewLength, NewLength+1, Character.toString(array.charAt(i)));
				NewLength++;
			}
		}
		//array = Arrays.copyOf(array, NewLength);
		String array2 = array.substring(0, NewLength);
		//System.out.println(array2.length());
		return array2;
	}
	public static StringBuilder replaceAll(StringBuilder builder, String from, String to)
	{
	    int index = builder.indexOf(from);
	    while (index != -1)
	    {
	        builder.replace(index, index + from.length(), to);
	        index += to.length(); // Move to the end of the replacement
	        index = builder.indexOf(from, index);
	    }
	    return builder;
	}
}
