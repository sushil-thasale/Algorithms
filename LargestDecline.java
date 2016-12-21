import java.util.Scanner;


public class LargestDecline {

	public static int largestDrop(int p, int a,int b, int c,int d, int n){		
        double max_price = calculate_price(p, a, b, c, d, 0);
        double drop = -1000;
         for(int i=1; i<n ; i++){
        	double price = calculate_price(p, a, b, c, d, i);
            if(price > max_price){
                max_price = price;
            }
            else
            {
                if(max_price - price > drop){
                    drop = max_price - price;
                }
            }
        }
        return (int)drop;
     }
	
	public static double calculate_price(int p, int a,int b, int c,int d, int k)
	{
		double price = (p*(Math.sin(a*k + b) + Math.cos(c*k + d) + 2));
		return price;
	}
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		int p = Integer.parseInt(sc.next());
		int a = Integer.parseInt(sc.next());
		int b = Integer.parseInt(sc.next());
		int c = Integer.parseInt(sc.next());
		int d = Integer.parseInt(sc.next());
		int n = Integer.parseInt(sc.next());
	
		int drop = (int)largestDrop(p,a,b,c,d,n);
		System.out.println(drop);
	}
}
