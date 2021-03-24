import java.io.File;
import java.util.Arrays;

public class A3 {

	public static void main(String[] args) {
		File folder2 = new File("src/main/java/A3Formulas");
		File[] listOfFiles2 = folder2.listFiles();
		Arrays.sort(listOfFiles2);
		System.out.println("DPLL");
		for (int i = 0; i < listOfFiles2.length; i++) 
		{
		    System.out.println(listOfFiles2[i].getName());
		    SAT_Solver s_d = new SAT_Solver();
		    long startTime = System.currentTimeMillis();
		    boolean sat = s_d.DPLL_sat(listOfFiles2[i]);
		    if(sat)
		    {
		    	System.out.println("The formula is satisfied.");
		    }
		    else
		    {
		    	System.out.println("The formula is unsatisfied.");
		    }
		    long endTime = System.currentTimeMillis();
		    long time = endTime - startTime;
			System.out.println("The time DPLL takes is "+ time + "ms");
			System.out.println("The number of node is " + s_d.getNum());
			System.out.println("===================================");
		}
		
		System.out.println("WalkSAT");
		for(int m = 0; m<listOfFiles2.length;m++)
		{
			System.out.println(listOfFiles2[m].getName());
			for(int j = 0; j<10; j++)
			{
				SAT_Solver s_dd = new SAT_Solver();
				long startTime = System.currentTimeMillis();
				boolean sat2 = s_dd.WalkSAT_sat(listOfFiles2[m], 0.5, 10000);
				long endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				System.out.println("The time WalkSAT takes is "+ time + "ms");
				
			    if(sat2)
			    {
			    	System.out.println("The formula is satisfied.");
			    	System.out.println("The number of flips is " + s_dd.getFlips());
			    }
			    else
			    {
			    	System.out.println("The formula is unsatisfied.");
			    	System.out.println("The number of satisfied clauses is " + s_dd.getMax_c());
			    }
			    System.out.println("===================================");
			}
			
		}
	}

}
