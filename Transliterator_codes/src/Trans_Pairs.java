import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Trans_Pairs
{
	public static void main(String args[]) throws IOException
	{
		String pair = args[1]+"_"+args[2];
		String input = args[0]+pair+"/"+pair+".A3.final";
		String output = args[0]+pair+"/"+pair+".trans";
		BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		FileWriter fw = new FileWriter(new File(output));
		String s;
		ArrayList<String> pairs = new ArrayList<String>();
		while((s = br.readLine()) != null)
		{
			s = br.readLine();
			String splsrc[] = s.split(" ");
			s = br.readLine();
			//String spltgt[] = s.split(" \\(\\{ [0-9 ]*\\}\\) ");
			String spltgt[] = s.split(" \\}\\) ");
			//System.out.println(splsrc.length + " " + spltgt.length);
			String spltrg[] = new String[splsrc.length];
			/*for(int i = 0; i < splsrc.length; i++)
				System.out.println(splsrc[i]);*/
			for(int i = 0; i < spltgt.length; i++)
				if(Character.isDigit(spltgt[i].charAt(spltgt[i].length() - 1)))
					spltrg[Integer.parseInt(spltgt[i].substring(spltgt[i].lastIndexOf(" ") + 1)) - 1] = spltgt[i].substring(0, spltgt[i].indexOf(" "));
			for(int i = 0; i < spltrg.length; i++)
				if(!(splsrc[i] == null || spltrg[i] == null || splsrc[i].equalsIgnoreCase("null") || spltrg[i].equalsIgnoreCase("null")))
					if(!pairs.contains(splsrc[i] + "\t" + spltrg[i]))
						pairs.add(splsrc[i] + "\t" + spltrg[i]);
		}
		for(int i = 0; i < pairs.size(); i++)
			fw.write(pairs.get(i) + "\n");
		br.close();
		fw.close();
	}
}
