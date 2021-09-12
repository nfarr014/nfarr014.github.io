package Assingment_1;

public class RPN {
	
	public static void main(String[] arg)
	{
		//variable
		String string[] = {"5 + ) * ( 2", " 2 + ( - 3 * 5 )", "(( 2 + 3 ) * 5 ) * 8", "5 * 10 + ( 15 - 20 ) ) - 25",
						   " 5 + ( 5 * 10 + ( 15 - 20 ) - 25 ) * 9"};
		
		for (int i = 0; i < string.length; i++) //goes through the length of string array
		{
			//call Arithmetic class
			Arithmetic ari = new Arithmetic(string[i]);
			
			if (ari.isBalance())
			{
				System.out.println("Expression " + string[i] + " is balanced\n");
				ari.postFixExpression();
				System.out.println("The post fixed expression is " );
			}
			else
			{
				System.out.println("Expression is not balanced\n");
			}
		}
	}

}
