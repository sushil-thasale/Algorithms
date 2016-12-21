import java.io.ObjectInputStream.GetField;
import java.util.Scanner;

public class BitTorrentKnapsack {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int files = sc.nextInt();
		int piece_size = sc.nextInt();
		int max_download = sc.nextInt();
		int file_sizes[] = new int[files];
		int package_size = 0;
		for (int i = 0; i < files; i++) {
			file_sizes[i] = sc.nextInt();
			package_size += file_sizes[i];
		}
		
		int pieces = (int)Math.ceil((float)package_size/piece_size);
		
		int file_info[][] = new int[files][2];
		int file_start=0;
		for(int i=0;i<files;i++)
		{
			file_info[i][0] = file_start;
			file_info[i][1] = file_start + file_sizes[i];
			file_start = file_info[i][1];
			//System.out.println(i+"->"+file_info[i][0]+" "+file_info[i][1]);
		}
		
		int piece_info[][] = new int[pieces][2];
		int piece_start=0;
		for(int i=0;i<pieces-1;i++)
		{
			piece_info[i][0] = piece_start;
			piece_info[i][1] = piece_start + piece_size;
			piece_start = piece_info[i][1];
			//System.out.println(i+"->"+piece_info[i][0]+" "+piece_info[i][1]);
		}
		piece_info[pieces-1][0] = piece_start;
		piece_info[pieces-1][1] = package_size;
		//System.out.println(pieces-1+" -> "+piece_start+" "+package_size);
		
		//System.out.println();
		
		double piece_value[] = new double[pieces];
		for(int i=0;i<pieces;i++)
		{
			int p_start = piece_info[i][0];
			int p_end = piece_info[i][1];
								
			double value=0;
			
			for(int j=0;j<files;j++)
			{
				int f_start = file_info[j][0];
				int f_end = file_info[j][1];
				int f_size=file_sizes[j];
				
				if(f_start<p_start && f_end<p_start)
				{}
				else if(f_start<p_start && f_end>p_start && f_end<=p_end)
				{
					value += (double)(f_end - p_start)/f_size;
					}
				else if(f_start<p_start && f_end>p_end)
				{
					value += (double)(p_end - p_start)/f_size;
				}
				else if(f_start>=p_start && f_end<=p_end)
				{
					value += 1.0;
				}
				else if(f_start>=p_start && f_start<p_end && f_end>p_end)
				{
					value += (double)(p_end - f_start)/f_size;
				}
				else if(f_start>p_end)
				{}
					
			}
			piece_value[i] = value;
			//System.out.println("piece "+i+"->"+value);
		}
		//System.out.println("piece "+(pieces-1)+"->"+piece_value[pieces-1]);
		
		
		double DP[][] = new double[max_download+1][pieces+1];
		String direction[][] = new String[max_download+1][pieces+1];
		int p_size;
		
		for(int i=0;i<=max_download;i++)
			DP[i][pieces]=0.0;
		
        for(int j=pieces-1; j >=0; j--){
        	        	
        	p_size=piece_info[j][1] - piece_info[j][0];
        	//System.out.println(p_size);
        	        	
            for(int i=0; i <= max_download ; i++){
            	
            	if(i-p_size>=0){            	
            		//System.out.println(DP[i-p_size][j+1] + piece_value[j]);
            		if(DP[i-p_size][j+1] + piece_value[j] > DP[i][j+1])
            		{
            			DP[i][j] = DP[i-p_size][j+1] + piece_value[j];
            			direction[i][j] = "up";
            		}
            		else if(DP[i-p_size][j+1] + piece_value[j] < DP[i][j+1])
            		{
            			DP[i][j] = DP[i][j+1];
            			direction[i][j] = "left";
            		}                    
            		else
            		{
            			//DP[i][j] = DP[i][j+1];
            			//direction[i][j] = "equal";
            			int p_list[] = getPiecesList(pieces, piece_info, i-p_size,j+1, DP, direction); 
            			int p_list1[] =new int[p_list.length+1];
            			p_list[0]=j;
            			for(int w=0;w<p_list.length;w++)
            			{
            				p_list1[w+1]=p_list[w];
            			}            			
            			
            		    int f_count1=getCount(files, file_info, piece_info, p_list1);            		    
            		    
            		    int p_list2[] = getPiecesList(pieces, piece_info, i,j+1, DP, direction);               	        
            		    int f_count2=getCount(files, file_info, piece_info, p_list2);
            		    
            		    System.out.println(j+"-"+i);
            		    System.out.println("DP is equal");
            		    System.out.println(f_count1);
            		    System.out.println(f_count2);
            		    
            			if(f_count1>f_count2)
            			{
            				DP[i][j] = DP[i-p_size][j+1] + piece_value[j];
            			}
            			else
            			{
            				DP[i][j] = DP[i][j+1];
            			}
            			
            		}
                }else{
                    DP[i][j] = DP[i][j+1];
                    direction[i][j] = "left";
                }
            	//System.out.println(i+" "+j+" "+DP[i][j]);
            	
            }
        }
        //System.out.println(DP[max_download][0]);
        
        
    int pieces_downloaded[] = getPiecesList(pieces, piece_info, max_download,0, DP, direction);   
        
    int count=getCount(files, file_info, piece_info, pieces_downloaded);
    System.out.println(count);
        	}
	
public static int getCount(int files,int [][]file_info,int [][]piece_info,int[]pieces_downloaded)	
{
	int file_count = 0;
	for(int file=0;file<files;file++)
    {        	
    	int f_start=file_info[file][0];
    	int f_end=file_info[file][1];
    	int f_size=f_end-f_start;
    	int file_downloaded_size=0;
    	
    	for(int p=0;p<pieces_downloaded.length;p++)
    	{
    		int p1=pieces_downloaded[p];
    		int p_start=piece_info[p1][0];
    		int p_end=piece_info[p1][1];
    		
    		if(p_start<f_start && p_end>f_end)
    		{
    			file_downloaded_size=f_size;
    			}
    		else if (p_start<f_start && p_end > f_start && p_end<=f_end)
    		{
    			file_downloaded_size += p_end-f_start;
    		}
    		else if (p_start>=f_start && p_end<=f_end)
    		{
    			file_downloaded_size += p_end-p_start;
    		}
    		else if (p_start>=f_start && p_start<f_end && p_end>f_end)
    		{
    			file_downloaded_size += f_end-p_start;
    		}
    		if(file==0)
    		{
    			//System.out.println(p1+"->"+p_start+" "+p_end+"->"+file_downloaded_size);
    		}
    	}
    	//System.out.println(file_downloaded_size);
    	if(file_downloaded_size>=f_size)
    	{file_count++;}
    	
    }
   // System.out.println(file_count);

	return file_count;
	
}

public static int[] getPiecesList(int pieces, int[][] piece_info,int max_download,int piece_pos,double[][] DP,String[][] direction)
{

    int pieces_downloaded[] = new int[pieces];
    int j=piece_pos;
    int k=0;
    for(int i=max_download;i>=0;)
    {
    	if(DP[i][j]==0)
    		break;
    	if(direction[i][j]=="up")
    	{
    		pieces_downloaded[k]=j;
    		k++;        		
    		i-=piece_info[j][1]-piece_info[j][0];
    		j++;
    	}
    	else
    	{        		
    		j++;
    	}
    }
    
    
    int piece_download_count=0;
    for(int i=0;i<pieces;)
    {
    	piece_download_count++;
    	i++;
    	if(pieces_downloaded[i]==0)
    		break;
    }
    
    //System.out.println(piece_download_count);
    int pieces_downloaded2[] = new int[piece_download_count]; 
    for(int i=0;i<piece_download_count;i++)
    {
    	pieces_downloaded2[i] = pieces_downloaded[i];
    }
    
    return pieces_downloaded2;
    }
}
