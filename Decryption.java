
public class Decryption {
	
	public static boolean isSubSequence(String source , String target)
	{
		int i,index;
		index = 0;
		for(i=0; i<target.length(); i++)
		{
			if(target.charAt(i) == source.charAt(index))
			{
				index++;
				if(index == source.length())
					break;
			}
		}
		if(index == source.length())
			return true;
		else 
			return false;						
		}
				
	public static void main(String args[])
	{
		String target = "a12b12c12de";
		String source = "abc";
		Boolean ans = isSubSequence(source, target);
		System.out.println(ans);
	}
}
