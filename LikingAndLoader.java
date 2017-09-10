package sichm2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LikingAndLoader {
	
	static HashMap<String, Integer> SYMTAB = new HashMap<String, Integer>(); // hash use for LOAD MAP table
	static String filename1, filename2,filename3;
	
	static int PROGADDR;
	static int CSADDR;
	
	public static void main(String[] args)
	{
		filename1 = "p1.txt";
		filename2 = "p2.txt";
		filename3 = "p3.txt";
		
		System.out.println("Keyin the programme address: ");
		Scanner input = new Scanner(System.in);
		String str = input.nextLine();
		PROGADDR = Integer.valueOf(str,16);
		CSADDR = PROGADDR;
		System.out.println(PROGADDR);
		
		pass1();
		pass2();
	}
	
    static void printloadmap() throws IOException
    {
    	FileWriter fw = new FileWriter("loadmap.txt");
    	System.out.println("Symbol\tAddrees\t");
    	fw.write("Sysmbol\tAddress"+System.getProperty("line.separator"));
    	for(Object key:SYMTAB.keySet())
    	{
    		String sym = (String) key;
    		int addr = SYMTAB.get(key);
    		String saddr = Integer.toHexString(addr);
    		System.out.println(sym + " = "+saddr);
    		fw.write(key+" = "+saddr+System.getProperty("line.separator"));
    		//fw.write("\n");
    	}
    	
		fw.flush();
		fw.close();
    }
	
	public static void pass1()
	{
		String label, op, arg,address,name,tt,sstart,ssize;
		int size = 0,start = 0;
		
		try
		{
			BufferedReader src1 = new BufferedReader(new FileReader(filename1));
			BufferedReader src2 = new BufferedReader(new FileReader(filename2));
			BufferedReader src3 = new BufferedReader(new FileReader(filename3));
			
			System.out.println("Opening the destination file...");
			System.out.println("creating load map/symbol table");
			
			for(int i = 0;i<=2;i++)
			{
				System.out.println("Reading the 1st line of the source file...");
				String inline = null;
				if(i==0)
				{
					inline = src1.readLine();
				}
				if(i==1)
				{
					CSADDR = CSADDR + size;
					inline = src2.readLine();
				}
				if(i==2)
				{
					CSADDR = CSADDR + size;
					 inline = src3.readLine();
				}
				
				StringTokenizer line;
				line = new StringTokenizer(inline,"^");
				
				//parser header
				if(line.nextToken().equals("H"))
					System.out.println("Paring Header");
				else
					System.out.println("Error programme");
				
				if(line.countTokens()<=3)
				{
					
					name = line.nextToken();
					System.out.println("Found the programme name ");
					sstart = line.nextToken();
					start = Integer.valueOf(sstart,16);
					if(line.hasMoreTokens())
					{
						ssize = line.nextToken();
						size = Integer.valueOf(ssize,16);
					}
						else
							ssize = null;	
				}
				else
				{
					name = "Default";
					System.out.println("Not Found programming using default name ");
					sstart = line.nextToken();
					start = Integer.valueOf(sstart,16);
					if(line.hasMoreTokens())
					{
						ssize = line.nextToken();
						size = Integer.valueOf(ssize,16);
					}
						else
							ssize = null;	
				}
				
				if(i==0)
				{
					start = start + PROGADDR;
					CSADDR = start; 
				}
				if(i==1)
				{
					start = CSADDR;
					CSADDR = start;
				}
				if(i==2)
				{
					start = CSADDR;
					CSADDR = start;
				}
				String nam1 = name.trim();
				SYMTAB.put(nam1, new Integer(start));
				
				if(i==0)
				{
					 inline = src1.readLine();
				}
				if(i==1)
				{
					 inline = src2.readLine();
					 
				}
				if(i==2)
				{
					 inline = src3.readLine();
				}
				
				line = new StringTokenizer(inline,"^");
				tt = line.nextToken();
				//paser body
				while(true)
				{
					if(tt.equals("T"))
					{
						System.out.println("Text Record ");
					}
					if(tt.equals("D"))
					{
						System.out.println("Define Record ");
						String symname;
						String symaddr;
						int isymaddr;
						while(line.hasMoreTokens())
						{
							symname = line.nextToken();
							symaddr = line.nextToken();
							isymaddr = Integer.valueOf(symaddr,16);
							isymaddr = isymaddr + start;
							
							SYMTAB.put(symname.trim(), new Integer(isymaddr));
						}
					}
					if(tt.equals("R"))
					{
						System.out.println("Re-Locate ");
					}
					if(tt.equals("M"))
					{
						System.out.println("Modifi Record ");
					}
					if(tt.equals("E"))
						break;
					System.out.println("Next Line ");
					
					String t = null;
					if(i==0)
					{
						 t = src1.readLine();
					}
					if(i==1)
					{
						 t = src2.readLine();
					}
					if(i==2)
					{
						 t = src3.readLine();
					}
					System.out.println(t);
					line = new StringTokenizer(t,"^");
					tt = line.nextToken();
				}
			}
			src1.close();
			src2.close();
			src3.close();
			
			printloadmap();
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void pass2()
	{
		String label, op, arg,address,name,tt,sstart,ssize;
		int size = 0,start = 0;
		int[] memory;
		memory = new int[65536];
	
		try
		{
			BufferedReader src1 = new BufferedReader(new FileReader(filename1));
			BufferedReader src2 = new BufferedReader(new FileReader(filename2));
			BufferedReader src3 = new BufferedReader(new FileReader(filename3));
			
			System.out.println("Opening the destination file...");
			System.out.println("creating load map/symbol table");
			
			for(int i = 0;i<=2;i++)
			{
				System.out.println("Reading the 1st line of the source file...");
				String inline = null;
				if(i==0)
				{
					 inline = src1.readLine();
				}
				if(i==1)
				{
					
					start = size + start;
					CSADDR = start;
					 inline = src2.readLine();
				}
				if(i==2)
				{
					start = size + start;
					CSADDR = start; 
					 inline = src3.readLine();
				}
				
				StringTokenizer line;
				line = new StringTokenizer(inline,"^");
				
				//parser header
				if(line.nextToken().equals("H"))
					System.out.println("Paring Header");
				else
					System.out.println("Error programme");
				
				if(line.countTokens()<=3)
				{
					
					name = line.nextToken();
					System.out.println("Found the programme name ");
					sstart = line.nextToken();
					start = Integer.valueOf(sstart,16);
					if(line.hasMoreTokens())
					{
						ssize = line.nextToken();
						size = Integer.valueOf(ssize,16);
					}
						else
							ssize = null;	
				}
				else
				{
					name = "Default";
					System.out.println("Not Found programming using default name ");
					sstart = line.nextToken();
					start = Integer.valueOf(sstart,16);
					if(line.hasMoreTokens())
					{
						ssize = line.nextToken();
						size = Integer.valueOf(ssize,16);
					}
						else
							ssize = null;	
				}
				
				if(i==0)
				{
					start = start + PROGADDR;
					CSADDR = start; 
				}
				if(i==1)
				{
					start = CSADDR;
					CSADDR = start;
				}
				if(i==2)
				{
					start = CSADDR;
					CSADDR = start;
				}
				
				if(i==0)
				{
					 inline = src1.readLine();
				}
				if(i==1)
				{
					 inline = src2.readLine();
				}
				if(i==2)
				{
					 inline = src3.readLine();
				}
				
				line = new StringTokenizer(inline,"^");
				tt = line.nextToken();
				
				//paser body
				while(true)
				{
					

					if(tt.equals("T"))
					{
						String begin = null;
						String trecordSize = null;
						System.out.println("Text Record ");
						if(line.hasMoreTokens())
							 begin = line.nextToken();
						if(line.hasMoreTokens())
							 trecordSize = line.nextToken();
						String sdata;
						while(line.hasMoreTokens())
						{
							sdata = line.nextToken();
							
							int tomemdata = Integer.valueOf((String) sdata.substring(0, 2),16);
							memory[CSADDR] = tomemdata;
							CSADDR = CSADDR + 1;
							
							if(sdata.length()==8)
							{
								tomemdata = Integer.valueOf((String) sdata.substring(2, 4),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
								
								tomemdata = Integer.valueOf((String) sdata.substring(4, 6),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
								
								tomemdata = Integer.valueOf((String) sdata.substring(6, 8),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
							}
							if(sdata.length()==6)
							{	
								tomemdata = Integer.valueOf((String) sdata.substring(2, 4),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
								
								tomemdata = Integer.valueOf((String) sdata.substring(4, 6),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
							}
							if(sdata.length()==4)
							{
								tomemdata = Integer.valueOf((String) sdata.substring(2, 4),16);
								memory[CSADDR] = tomemdata;
								CSADDR = CSADDR + 1;
							}
						}
					}
					if(tt.equals("D"))
					{
						System.out.println("Define Record ");

					}
					if(tt.equals("R"))
					{
						System.out.println("Re-Locate ");
					}
					if(tt.equals("M"))
					{
						System.out.println("Modifi Record ");
						String place = null;
						String num = null;
						if(line.hasMoreTokens())
							 place = line.nextToken();
						if(line.hasMoreTokens())
							 num = line.nextToken();
							
						int tomemdata = Integer.valueOf(place,16);
						String halfbyte = null; 
						if(num.equals("05"))
						{
							halfbyte = Integer.toHexString(memory[start + tomemdata]);
							halfbyte = halfbyte.substring(1);
							halfbyte = halfbyte + Integer.toHexString(memory[start + tomemdata+1]);
							halfbyte = halfbyte + Integer.toHexString(memory[start + tomemdata+2]);
						}
						else
						{
							halfbyte = Integer.toHexString(memory[start + tomemdata]);
							halfbyte = halfbyte + Integer.toHexString(memory[start + tomemdata+1]);
							halfbyte = halfbyte + Integer.toHexString(memory[start + tomemdata+2]);
						}
						
						String doWhat = line.nextToken();
						String action = doWhat.substring(0,1);
						String paramenter = doWhat.substring(1);
						String buf1 = null;
						int v1 = Integer.valueOf(halfbyte, 16);
						if(action.equals("+"))
						{
							int symaddr = SYMTAB.get(paramenter);
							v1 = v1 + symaddr;
							buf1 = Integer.toHexString(v1);
						}
						else if(action.equals("-"))
						{
							int symaddr = SYMTAB.get(paramenter);
							v1 = v1 - symaddr;
							buf1 = Integer.toHexString(v1);
						}
						if(num.equals("05"))
						{
							String hb1 = Integer.toHexString(memory[start + tomemdata]);
							hb1 = hb1.substring(0,1);
							hb1 = hb1 + buf1;
							
							if(buf1.length()==0)
							{
								memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(0),16);
							}
							if(buf1.length()==1||buf1.length()==2)
							{
								memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(0),16);
							}
							if(buf1.length()==3||buf1.length()==4)
							{
								memory[start + tomemdata+1] = Integer.valueOf(buf1.substring(0,2),16);
								memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(2),16);
							}
							if(buf1.length()==5)
							{
								memory[start + tomemdata+1] = Integer.valueOf(buf1.substring(0,2),16);
								memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(2,4),16);
								memory[start + tomemdata] = Integer.valueOf(hb1.substring(2,4),16);
							}
						}
						else
						{
							if(num.equals("06"))
							{
								String hb1 = Integer.toHexString(memory[start + tomemdata]);
								hb1 = hb1.substring(0,1);
								hb1 = hb1 + buf1;
								if(buf1.length()==0)
								{
									memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(0),16);
								}
								if(buf1.length()==1||buf1.length()==2)
								{
									memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(0),16);
								}
								if(buf1.length()==3||buf1.length()==4)
								{
									memory[start + tomemdata+1] = Integer.valueOf(buf1.substring(0,2),16);
									memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(2),16);
								}
								if(buf1.length()==5||buf1.length()==5)
								{
									memory[start + tomemdata+2] = Integer.valueOf(buf1.substring(4),16);
									memory[start + tomemdata+1] = Integer.valueOf(buf1.substring(2,4),16);
									memory[start + tomemdata] = Integer.valueOf(hb1.substring(0,2),16);
								}
							}
						}
					}
					if(tt.equals("E"))
						break;
					System.out.println("Next Line ");
					
					String t = null;
					if(i==0)
					{
						 t = src1.readLine();
					}
					if(i==1)
					{
						 t = src2.readLine();
					}
					if(i==2)
					{
						 t = src3.readLine();
					}
					System.out.println(t);
					line = new StringTokenizer(t,"^");
					tt = line.nextToken();
				}
			}
			src1.close();
			src2.close();
			src3.close();
			printmem(memory);
			
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void printmem(int[] memory) throws IOException
	{
		FileWriter fw = new FileWriter("dumpmemorry.txt");
		System.out.println("Memory\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\tA\tB\tC\tD\tE\tF\t");
		fw.write("Memory\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\tA\tB\tC\tD\tE\tF\t"+System.getProperty("line.separator"));
		int i = PROGADDR;
		while(i<=CSADDR)
		{
			System.out.print(Integer.toHexString(i).toUpperCase()+"\t");
			fw.write(Integer.toHexString(i).toUpperCase()+"\t");
			for(int j = 0;j<=15;j++)
			{
				String plusZero  = "0";
				String data = Integer.toHexString(memory[i]);
				if(data.length()==1)
				{
					data = plusZero + data;
				}
				System.out.print(data.toUpperCase()+"\t");
				fw.write(data.toUpperCase()+"\t");
				i++;
			}
			System.out.println();
			fw.write(System.getProperty("line.separator"));
		}
		fw.flush();
		fw.close();
	}
}
