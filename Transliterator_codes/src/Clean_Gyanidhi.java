import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Clean_Gyanidhi
{
	public static void main(String args[]) throws IOException
	{
		String file = "/home/rishabh/Documents/Trans/Gyanishi_all_text12.txt";
		String file2 = "/home/rishabh/Documents/Trans/Gyanishi_all_text13.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		FileWriter fw = new FileWriter(new File(file2));
		String x = "‘’‍!/8|═𸮘^$";
		char exceptions[] = x.toCharArray();
		String s;
		//int c = 0;
		while((s = br.readLine()) != null)
		{
			//System.out.println(s);
			/*for(int i = s.length() - 1; i >= 0; i--)
				if(s.charAt(i) != ' ')
				{
					fw.write(s.substring(0, i+1) + "\n");
					break;
				}*/
			/*if(!s.endsWith("।"))
				System.out.println(s);
			break;*/
			/*String split[] = s.split("। ");
			for(int i = 0; i < split.length; i++)
				fw.write(split[i] + "।\n");*/
			//s = s.replaceAll("।।", "।");
			//fw.write(s+"\n");
			/*if(s.endsWith("।।"))
				s = s.replaceAll("।।", "।");
			fw.write(s + "\n");*/
			/*String num = "";
			int i;
			for(i = s.length() - 2; i > 0; i--)
				if(s.charAt(i) == '।')
				{
					num = s.substring(i+1, s.length() - 1);
					break;
				}
			if(s.contains("  ") && ((Character.isDigit(s.charAt(0)) || s.matches(".*\\d.*"))))
			{
				s = s.substring(s.lastIndexOf("  ") + 2);
			}
			else
			{
				try
				{
					Integer.parseInt(num);
					s = s.substring(0, i+1);
				}
				catch(NumberFormatException e)
				{

				}
			}
			fw.write(s + "\n");*/
			/*s = s.replaceAll("  ", " ");
			s = s.replaceAll("  ", " ");
			s = s.replaceAll("  ", " ");
			s = s.replaceAll("  ", " ");
			s = s.replaceAll("  ", " ");
			s = s.replaceAll("  ", " ");
			if(s.startsWith(" "))
				s = s.substring(1);
			fw.write(s+"\n");*/
			/*System.out.println(++c + " " + s.charAt(0));
			if(c == 47)
				break;*/
			/*c++;
			s = s.replaceAll("", "");
			s = s.replaceAll("", "");
			//s = s.replaceAll("", "");
			System.out.println(c + " " + s);
			fw.write(s+"\n");*/
			for(int i = 0; i < exceptions.length; i++)
			{
				s = s.replaceAll(Character.toString(exceptions[i]), "");
				//s = s.replaceAll("\\\\", "");
				//s = s.replaceAll("'", "");
				//s = s.replaceAll(";", "");
			}
			/*if(s.contains("  "))
				System.out.println(s);*/
			s = s.replaceAll("\\+", "");
			s = s.replaceAll("\\.", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("\\*", "");
			s = s.replaceAll("\\[", "");
			s = s.replaceAll("\\]", "");
			/*if(s.contains("  "))
				System.out.println(s);*/
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("      ", "");
			s = s.replaceAll("     ", "");
			s = s.replaceAll("     ", "");
			s = s.replaceAll("      ", "");
			s = s.replaceAll("                      ", "");
			s = s.replaceAll("      ", "");
			s = s.replaceAll("       ", "");
			s = s.replaceAll("                                                                                                                      ", "");
			s = s.replaceAll("       ", "");
			s = s.replaceAll("        ", "");
			s = s.replaceAll("     ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("    ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("  ", "");
			s = s.replaceAll("          ", "");
			s = s.replaceAll("\t", "");
			if(s.contains("  "))
				System.out.println(s);
			if(s.startsWith(" "))
				s = s.substring(1);
			fw.write(s+"\n");
		}
		fw.close();
		br.close();
	}
}
