import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Unique_Introduced
{
	public static void main(String args[]) throws IOException
	{
		File in1 = new File("/home/rishabh/Documents/Trans/parallel_hindi");
		File in2 = new File("/home/rishabh/Documents/Trans/parallel_urdu");
		File out1 = new File("/home/rishabh/Documents/Trans/Creating_Resource/Uniq_Uni.hi");
		File out2 = new File("/home/rishabh/Documents/Trans/Creating_Resource/Uniq_Uni.ur");
		BufferedReader br1 = new BufferedReader(new FileReader(in1));
		BufferedReader br2 = new BufferedReader(new FileReader(in2));
		BufferedReader or1 = new BufferedReader(new FileReader(out1));
		BufferedReader or2 = new BufferedReader(new FileReader(out2));
		ArrayList<String> orig1 = new ArrayList<String>();
		ArrayList<String> orig2 = new ArrayList<String>();
		String s1;
		int c = 0;
		while((s1 = br1.readLine()) != null)
		{
			orig1.add(s1);
			orig2.add(br2.readLine());
		}
		while((s1 = or1.readLine()) != null)
		{
			if(check(orig1, orig2, s1, or2.readLine()) == 0)
				c++;
		}
		System.out.println(c);
		br1.close();
		br2.close();
		or1.close();
		or2.close();
	}
	public static int check(ArrayList<String> orig1, ArrayList<String> orig2, String s1, String s2)
	{
		int i = 0;
		for(i = 0; i < orig1.size(); i++)
		{
			if(orig1.get(i).equals(s1) && orig2.get(i).equals(s2))
			{
				System.out.println(orig1.get(i) + " " + s1 + " " + orig2.get(i) + " " + s2);
				break;
			}
		}
		if(i == orig1.size())
			return 0;
		return 1;
	}
}
