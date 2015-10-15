import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class main {
	public static void main(String[] args){
		Scanner scanner= new Scanner(System.in);
		String filename= null;
		System.out.print("input the filename :: ");
		filename=scanner.nextLine();
		System.out.print("input the function :: ");
		Compute com= new Compute(scanner.nextLine(), filename);
	}
}
