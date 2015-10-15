import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Compute {
	private int n, t;
	private String mFilename= null;
	private ArrayList<Term> functionterm= new ArrayList<Term>();
//	private ArrayList<Variable> var= new ArrayList<Variable>();
	private ArrayList<String> var= new ArrayList<String>();
	private int[][] mat= null;
	private int[] coeff= null;
	private int[] order= null;
	
	public Compute(String function, String filename){
		mFilename= filename;
		function= function.substring(2);
		parse(function);
		makeMatrix(n);
		compute();
	}
	
	private void parse(String ft){
		StringTokenizer token= new StringTokenizer(ft, "+");
		while(token.hasMoreTokens()){
			StringTokenizer term= new StringTokenizer(token.nextToken(), "v");
			String var1= "v"+term.nextToken(), var2= "v"+term.nextToken();
			int var1ind= -1, var2ind= -1;
			
			for(int j=0; j<var.size(); j++){
//				Variable tempvar= var.get(j);
				String tempvar= var.get(j);
//				if(tempvar.getName().equals(var1)){
//					tempvar.incIntersect();
//					var1ind= j;
//				}
//				if(tempvar.getName().equals(var2)){
//					tempvar.incIntersect();
//					var2ind= j;
//				}
				if(tempvar.equals(var1)) var1ind= j;
				if(tempvar.equals(var2)) var2ind= j;
			}
			
			if(var1ind == -1){
//				Variable variable= new Variable(var1);
				var1ind= var.size();
//				var.add(variable);
				var.add(var1);
			}
			if(var2ind == -1){
//				Variable variable= new Variable(var2);
				var2ind= var.size();
//				var.add(variable);
				var.add(var2);
			}
			
			Term current= new Term();
			current.setVar(var1, var1ind, true);
			current.setVar(var2, var2ind, false);
			functionterm.add(current);
			t++;
		}
		
		n= (int)Math.pow(2, var.size());
	}
	
	private void makeMatrix(int size){
		order= new int[n];
		coeff= new int[n];
		int nVar= var.size();
		
		mat= new int[n][n];
		for(int i=0; i<n; i++) order[i]= i;
		for(int i=0; i<n; i++){
			int smallest= getOne(order[i], nVar), ind= i;
			for(int j=i+1; j<n; j++){
				int current= getOne(order[j], nVar);
				if(smallest > current){
					smallest= current;
					ind= j;
				} else if(smallest == current && order[j] < order[ind]) ind= j;
			}
			if(i != ind){
				order[i] ^= order[ind];
				order[ind] ^= order[i];
				order[i] ^= order[ind];
			}
		}
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++) mat[i][j]= 1;
			if(i!=0){
				for(int j=1; j<n; j++){
					int cnt= 0;
					for(int k=0; k<nVar; k++)
						if(checkZero(order[j]&(1<<k))) cnt+=(checkZero(order[i]&(1<<k))?1:0);
					mat[i][j]= (int)Math.pow(-1, cnt);
				}
			}
		}
		for(int i=0; i<n; i++){
			int[] temp= new int[nVar];
			int result= 0;
			for(int j=0; j<var.size(); j++) temp[j]= checkZero(order[i]&(1<<j))?1:0;
			for(int j=0; j<functionterm.size(); j++) result += (temp[functionterm.get(j).getVarInd(true)]*temp[functionterm.get(j).getVarInd(false)])%2;
			coeff[i]= (int)Math.pow(-1, result);
		}
	}
	private boolean checkZero(int n){ return n==0?false:true; }
	private int getOne(int n, int len){
		int cnt= 0;
		for(int i=0; i<len; i++)
			if(checkZero(n&(1<<i))) cnt++;
//			cnt += (checkZero(n&(1<<i))?1:0);
		return cnt;
	}
	private int[] matrix_square_to_strip(int[][] a, int[] g){
		int[] res= new int[n];
		for(int i=0; i<n; i++){
			res[i]= 0;
			for(int j=0; j<n; j++) res[i] += a[i][j]*g[j];
		}
		return res;
	}
	private int getW(int[] x, int[] a){
		int w= 0;
		for(int i=0; i<x.length; i++) w += x[i]*a[i];
		return w;
	}
	private boolean findValue(ArrayList<Integer> arr, int value){
		for(int i=0; i<arr.size(); i++)
			if(arr.get(i) == value) return true;
		return false;
	}

	public void compute(){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(mFilename));
			for(int i=0; i<Math.pow(2, n); i++){
				String resultString= "";
				int[] g= new int[n], gind= new int[n], a, x;
				ArrayList<Integer> c= new ArrayList<Integer>();
				for(int j=0; j<n; j++){
					gind[j]= checkZero(i&(1<<j))?1:0;
					g[j]= (int)Math.pow(-1, gind[j]);
				}
				a= matrix_square_to_strip(mat, g);
				
				for(int j=0; j<n; j++){
					x= new int[n];
					for(int k=0; k<n; k++) x[k]= coeff[k]*mat[j][k];
					int w= getW(x, a);
					if(!findValue(c, w)) c.add(w);
				}
				if(c.size() == 2){
					c.remove(Integer.valueOf(n));
					c.remove(Integer.valueOf(-n));
					if(c.size() == 0){
						resultString+="bent      :: ";
						resultString+=Integer.toString(gind[0]);
						for(int j=1; j<n; j++) resultString+=", "+Integer.toString(gind[j]);
						out.write(resultString);
						out.newLine();
					}
				} else if(c.size() == 3){
					c.remove(Integer.valueOf(n*2));
					c.remove(Integer.valueOf(-n*2));
					c.remove(Integer.valueOf(0));
					if(c.size() == 0){
						resultString+="semi-bent :: ";
						resultString+=Integer.toString(gind[0]);
						for(int j=1; j<n; j++) resultString+=", "+Integer.toString(gind[j]);
						out.write(resultString);
						out.newLine();
					}
				}
			}
			System.out.println("program finished");
			out.write("-------------------------------------------------------------------program finished");
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
