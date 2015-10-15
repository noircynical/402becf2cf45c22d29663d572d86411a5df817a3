
public class Term {
	private String var1, var2;
	private int var1ind, var2ind;
	private boolean mSign= true;
	
	public void setVar(String name, int ind, boolean type){
		if(type){
			var1= name; var1ind= ind;	
		} else {
			var2= name; var2ind= ind;
		}
	}
	public void setSign(boolean sign){ mSign= sign; }
	public String getVar(boolean type){ return type?var1:var2; }
	public int getVarInd(boolean type){ return type?var1ind:var2ind; }
	public boolean getSign(){ return mSign; }
}
