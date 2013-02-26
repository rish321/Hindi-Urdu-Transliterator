import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Edit_Distance {
	static int c;

	public static int computeDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}

	public static void printDistance(String s1, String s2, String s3) {
		if((double)computeDistance(s1, s2)/(double)s1.length()*100 <= 60)
		{
			System.out.println(s1 + "-->" + s2 + ": " + (double)computeDistance(s1, s2)/(double)s1.length()*100 + " " + s3);
			c++;
		}
	}

	public static void main(String[] args) throws IOException
	{

		/*printDistance("kitten", "sitting");
    	printDistance("rosettacode", "raisethysword");
    		printDistance(new StringBuilder("rosettacode").reverse().toString(), new StringBuilder("raisethysword").reverse().toString());*/
		/*for (int i = 1; i < args.length; i += 2)
      	printDistance(args[i - 1], args[i]);*/
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(args[1])));
		String s;
		while((s = br.readLine()) != null)
		{
			String s2 = br2.readLine();
			String str[] = s2.split("\t");
			printDistance(s, str[0], str[1]);
		}
		System.out.println(c);
		br2.close();
		br.close();
	}
}