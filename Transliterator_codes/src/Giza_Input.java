import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Giza_Input
{
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		FileWriter fw1 = new FileWriter(new File(args[1]));
		FileWriter fw2 = new FileWriter(new File(args[2]));
		String s;
		String write, spl[];
		while((s = br.readLine()) != null)
		{
			spl = s.split("\t");
			write = "";
			for(int i = 0; i < spl[0].length(); i++)
				write += Character.toLowerCase(spl[0].charAt(i)) + " ";
			write = write.substring(0, write.length() - 1);
			fw1.write(write + "\n");
			write = "";
			for(int i = 0; i < spl[1].length(); i++)
				write += Character.toLowerCase(spl[1].charAt(i)) + " ";
			write = write.substring(0, write.length() - 1);
			fw2.write(write + "\n");
		}
		fw1.close();
		fw2.close();
		br.close();
	}
}
