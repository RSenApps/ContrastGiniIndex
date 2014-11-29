import java.util.Scanner;


public class main {
	   public static void main(String[] args) {
		   Scanner scanner = new Scanner(System.in);
			 System.out.print("Please enter file path: ");
	          ContrastGini demo = new ContrastGini(scanner.next());
	          scanner.close();
	          demo.pack();
	          demo.setVisible(true);
	      }
		
}
