import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SAT_Solver 
{
	ArrayList<Integer[]> clause = new ArrayList<Integer[]> () ;
	ArrayList<Integer> symbol = new ArrayList<Integer> ();
	int num = 0;//number of nodes
	int max_c =0; // max number of satisfied clauses
	int flips = 0; // number of flips
	public SAT_Solver()
	{
		
	}
	public int getNum() {
		return num;
	}

	public int getMax_c() {
		return max_c;
	}

	/* DPLL_sat: use DPLL to solve SAT problem
	 * filename: the file that contains all the clauses
	 * true/false: true if the CNF sentences is satisfiable false otherwise
	 */
	public boolean DPLL_sat(File file)
	{
		readFile(file);
		ArrayList<Integer> model = new ArrayList<Integer> ();
		return DPLL(clause,symbol,model);
	}
	/* is_false: used to check if one clause is already false
	 * m: current model
	 * return true if a clause is already false, otherwise return false
	 */
	public boolean is_false(ArrayList<Integer> m)
	{
		for(int i = 0; i<clause.size();i++)
		{
			int f = 0;
			for(int j = 0; j<clause.get(i).length-2; j++)
			{
				//a clause is false when all the symbols are false
				if(m.contains(-clause.get(i)[j]))
				{
					f++;
				}
			}
			if(f==clause.get(i).length-2)
			{
				return true;
			}
		}
		return false;
	}
	/* is_true: check if all clauses are true
	 * m: current model
	 * return true if all clause are true, false otherwise
	 */
	public boolean is_true(ArrayList<Integer> m)
	{
		for(int i = 0; i<clause.size();i++)
		{
			
			int f = 0;
			for(int j = 0; j<clause.get(i).length-2; j++)
			{
				if(m.contains(clause.get(i)[j]))
				{
					break;
				}
				if(!m.contains(clause.get(i)[j]))
				{
					f++;
				}
			}
			if(f==clause.get(i).length-2)
			{
				return false;
			}
		}
		return true;
	}
	/* DPLL: recursive DPLL search
	 * c: arraylist of all clauses
	 * s: array of unassigned symbols
	 * m: current model
	 * true/false: true if satisfiable false otherwise
	 */
	public boolean DPLL(ArrayList<Integer[]> c, ArrayList<Integer> s, ArrayList<Integer> m)
	{
		num++;
		//base cases 
		if(is_true(m))
		{	
			System.out.println(m);
			return true;
		}
		if (is_false(m))
		{
			return false;
		}
		//find pure symbols 
		int pure = find_pure_symbol(s,c);
		if( pure != Integer.MAX_VALUE)
		{
			//add pure symbol to the model and remove it from the symbol arraylist			
			int ind = s.indexOf(Math.abs(pure));	
			s.remove(ind);
			m.add(pure);
			return DPLL(c,s,m);
		}
		int unit = find_unit_clause(c,s);
		if(unit != Integer.MAX_VALUE)
		{
			//add literal in the unit clause to model and remove it from the symbol arraylist
			int in = s.indexOf(Math.abs(unit));
			s.remove(in);			
			m.add(unit);
			return DPLL(c,s,m);
		}

		int p = s.get(0); 
		ArrayList<Integer> rest = new ArrayList<Integer>();
		for(int i = 1; i<s.size();i++)
		{
			rest.add(s.get(i));
		}
		ArrayList<Integer> m2 = new ArrayList<Integer>();
		for(int i = 0; i<m.size();i++)
		{
			m2.add(m.get(i));
		}
		m.add(p);
		m2.add(-p);
		return (DPLL(c,rest,m) ||DPLL(c,rest,m2));
	}

	/* find_pure_symbol: go through all the clauses to find the pure symbol
	 * s: arraylist of symbol
	 * c: arraylist of clauses
	 * return a symbol if there is a pure symbol, return the max integer if no pure symbol exists
	 */
	public int find_pure_symbol(ArrayList<Integer> s, ArrayList<Integer[]> c)
	{
		
		for(int i = 0; i < s.size(); i++)
		{
			int temp = s.get(i);
			boolean first = true;
			boolean p = true;
			outerloop:
			for (int j = 0; j< c.size(); j++)
			{
				//check if the clause is already true
				//System.out.println(c[j][c[j].length-2]);
				if(c.get(j)[c.get(j).length-2] == Integer.MAX_VALUE)
				{	
					for (int l = 0; l<c.get(j).length-2; l++)
					{ 
						//check if the literal is the symbol we are checking, if so set the 
						//variable temp to be the variale. 
						if(Math.abs(c.get(j)[l]) == Math.abs(temp) && first)	
						{
							temp = c.get(j)[l];
							first = false;
						}
						//if there is literal different from temp, then the symbol is not pure, 
						//break the loop and then check for next symbol.
						else if (Math.abs(c.get(j)[l]) == Math.abs(temp))
						{
							if(temp != c.get(j)[l])
							{
								p = false;
								break outerloop;
							}
						}					
					}
				}
			}
			//set the flag to negative in order to ignore the clause that are already true. 
			if(p)
			{
				for(int m = 0; m<c.size();m++)
				{
					for(int n = 0; n<c.get(m).length-2; n++)
					{
						if(c.get(m)[n] == temp)
						{
							c.get(m)[c.get(m).length-2] = -Integer.MAX_VALUE;
							break;
						}
					}
				}
				return temp;
			}
			
		}
		return Integer.MAX_VALUE;
	}
	/* find_unit_clause: find unit clause in the clause array
	 * clause: arraylist of clauses
	 * s: arraylist of all the symbols
	 * return the symbol if a unit clause exists, otherwise return Max_integer
	 */
	public int find_unit_clause(ArrayList<Integer[]> clause, ArrayList<Integer> s)
	{
		
		for(int j = 0; j<clause.size();j++)
		{
			//check if the clause has already been checked
			if(clause.get(j)[clause.get(j).length-1] == Integer.MAX_VALUE)
			{
				int temp = clause.get(j)[0];
				int inc = 1;
				for(int m = 1; m<clause.get(j).length-2;m++)
				{
					if(clause.get(j)[m] == temp)
					{	
						inc ++;						
					}				
				}
				if(inc == clause.get(j).length-2 && s.contains(Math.abs(temp)))
				{
					//set checked flag to negative
					clause.get(j)[clause.get(j).length-1] = -Integer.MAX_VALUE;
					return temp;
				}
			}
		}
		return Integer.MAX_VALUE;
	}
	/* WalkSAT_sat: function that is used to call WalkSAT
	 * filename: name of the file that contains all the clauses
	 */
	public boolean WalkSAT_sat(File file, double p, int max)
	{
		readFile(file);
		ArrayList<Integer> sat = WalkSAT(p,max);
		if(sat != null)
		{
			System.out.println(sat);
			return true;
		}
		return false;
	}
	/* WalkSAT: use WalkSAT algorithm to solve SAT problems
	 * p: probability to choose between random walk and minimization
	 * max_f: the max number of flips
	 * return model or failure;
	 */
	public ArrayList<Integer> WalkSAT(double p, int max_f) 
	{
		
		Random rand = new Random(); 
		Random rand_d = new Random(); 
		ArrayList<Integer> model = new ArrayList<Integer>();
		for(int i =0; i<symbol.size(); i++)
		{
			//randomly assign each symbol
			int ran = rand.nextInt(2);
			if(ran == 0)
			{
				model.add(-symbol.get(i));
			}
			else if (ran == 1)
			{
				model.add(symbol.get(i));
			}
		}
		
		//check each clause use the early_termination function 
		for(int j = 0; j < max_f; j++)
		{
			flips++;
			if(is_true(model))
			{
				return model;
			}
			//get an arraylist of false clauses
			ArrayList<Integer[]> falseC = findfalse(model);
			//find the max number of satisfied clauses
			int c_max = clause.size() - falseC.size();
			if(c_max > max_c)
			{
				max_c = c_max;
			}
			//select a false clause randomly 
			Random rand_d2 = new Random();
			Integer cls_ind = rand_d2.nextInt(falseC.size());
			Integer[] cls = falseC.get(cls_ind);
			//get probability 
			Float ran_d = rand_d.nextFloat();	
			
			if(ran_d <= p)
			{
				//flip a symbol randomly 
				int ran_sym = rand.nextInt(cls.length-2);
				Integer sym = cls[ran_sym];
				int in;
				if(model.contains(sym))
				{
					in = model.indexOf(sym);
				}
				else
				{
					in = model.indexOf(-sym);
				}
				model.set(in, -model.get(in));
			}
			else
			{	
				//find the symbol that maximize the number of satisfied clauses
				int flip_ind = Max_sat(cls,model);
				//flip the symbol
				int flip = model.get(flip_ind);
				model.set(flip_ind, -flip);
				
			}
			
		}
		return null;
	}
	public int getFlips() {
		return flips;
	}
	/* Max_sat: find the symbol that maximize the number of satisfied clauses
	 * c: selected clause
	 * model: current model
	 * return the symbol
	 */
	public int Max_sat(Integer[] c, ArrayList<Integer> model) 
	{
		int max_sat = 0;
		int sati_sym_ind = 0;
		for(int m = 0; m<c.length-2;m++)
		{   
			int tr = c[m];
			int ind;
			int find;
			//get the index of the symbol in the model
			if(model.contains(tr))
			{
				ind = model.indexOf(tr); 
			}
			else
			{
				ind = model.indexOf(-tr);
			}
			find = model.get(ind);
			model.set(ind, -find);
			int sati = 0; 
			for (int i = 0; i<clause.size(); i++)
			{
				for(int j = 0; j<clause.get(i).length-2; j++)
				{
					if(model.contains(clause.get(i)[j]))
					{
						sati++;							
						break;
					}	
				}
			}			
			//find the maximum
			if(sati > max_sat)
			{
				max_sat = sati;
				sati_sym_ind = ind;
			}
			//flip the symbol back
			model.set(ind, find);
		}

		return sati_sym_ind;		 
	}
	/* findfalse: find an arraylist of false clauses 
	 * model: current model
	 * return a list of false clauses
	 */
	public ArrayList<Integer[]> findfalse(ArrayList<Integer> model)
	{
		ArrayList<Integer[]> falseClause = new ArrayList<Integer[]> ();
		for(int i =0; i<clause.size();i++)
		{
			int f = 0;
			for(int j = 0; j<clause.get(i).length-2; j++)
			{
				if(!model.contains(clause.get(i)[j]))
				{
					f++;
				}
			}
			//add the false clause to the arraylist 
			if(f==clause.get(i).length-2)
			{
				Integer[] temp = new Integer[clause.get(i).length-2];
				for(int l = 0; l<clause.get(i).length-2; l++)
				{
					//System.out.println(l);
					temp[l] = clause.get(i)[l];
				}
				falseClause.add(temp);
			}
		}
		return falseClause;
	}
	
	/* readFile: read files that contain all the clauses
	 * filename: the name of the file that will be read
	 */
	public void readFile(File file)
	{
	    Scanner scanner;	   
		try {
			scanner = new Scanner(file);
			String data = scanner.nextLine();
			while(data.charAt(0)!= 'p')
			{	
				data = scanner.nextLine();
			}
			String[] temp = data.trim().split("\\s");
			//size is the size of the 2D array of clause
			//size2 is the size of symbols that is read from the file 
			int size2 = Integer.parseInt(temp[2]);
			int i = 0; 
			while(scanner.hasNextLine())
			{
				data = scanner.nextLine();
				temp = data.trim().split("\\s");
				if(data.charAt(0) != 'c')
				{
					int size = temp.length+1;
					Integer[] i_temp = new Integer[size];
					int j = 0; 
					while(j<size-2)
					{
						i_temp[j] = Integer.parseInt(temp[j]);
						j++;
					}
					//set flags for pure symbol and unit clause
					i_temp[j] = Integer.MAX_VALUE;
					i_temp[j+1] = Integer.MAX_VALUE;
					
					clause.add(i, i_temp);
					i++;
				}
			}
			
			//create arraylist of symbols
			for(int ind = 0; ind<size2; ind++)
			{
				symbol.add(ind,ind+1);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}
		//System.out.println(clause);
		//System.out.println(symbol);
	    
	}
}
