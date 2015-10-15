
public class Variable {
	private String mName= null;
	private int mIntersect= 0;
	
	public Variable(){}
	public Variable(String name){ mName= name; }
	
	public void setIntersect(int intersect){ mIntersect= intersect; }
	public int getIntersect(){ return mIntersect; }
	public String getName(){ return mName; }
	
	public void incIntersect(){ mIntersect++; }
}
