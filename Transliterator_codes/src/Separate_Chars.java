import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Separate_Chars
{
	public static void main(String args[]) throws IOException
	{
		File inp = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(inp));
		FileWriter fw = new FileWriter(new File(args[0] + "spl"));
		String s;
		while((s = br.readLine()) != null)
		{
			//String str[] = s.split("\t");
			String spl[] = s.split("");
			String s1 = "";
			for(int i = 0; i < spl.length; i++)
				s1 += spl[i] + " ";
			fw.write(s1.substring(1, s1.length() - 1) + "\n");
		}
		br.close();
		fw.close();
	}
}
