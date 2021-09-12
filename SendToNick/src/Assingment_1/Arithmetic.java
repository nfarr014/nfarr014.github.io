/* STEP 1: Verify the expression [COMPLETE]
 * 1. [COMPLETE] Create empty stack to hold left parenthesis ONLY
 * 2. [COMPLETE] Scan the arithmetic expression from left to right, one character at a time
 * 				- if char left parenthesis (, push it onto stack
 * 				- if char right parenthesis ), pop top element from off the stack.
 * 				  All other characters ignored.
 * 3. [COMPLETE] If stack contains any element at end of reading infix expression, conclusion is parentheses were not balanced.
 * 				 (In RPN class).
 * 
 * STEP 2: Convert infixed expression to postfix [COMPLETE]
 * 1. [COMPLETE] Create an empty stack to hold any arithmetic operators (+, -, *, /) and left parenthesis ONLY
 * 2. [COMPLETE] Establish a string to store the postfix expression- the output from the conversion
 * 3. [COMPLETE] Scan the arithmetic expression from left to right.
 * 		After symbol is scanned, there are 5 rules to observe:
 * 			- if symbol operand (#), write it to the output string
 * 			- if symbol left parenthesis (, push it into stack
 * 			- if symbol right parenthesis ):
 * 				- pop everything from operator stack down to the FIRST left parenthesis (.
 * 					- write each item popped from the stack to the output string, but do not write either of the parentheses
 * 					  on the ouput string. Discard them.
 * 			- if symbol scanned is arithmetic operator, check:
 * 				- if operator on top of stack is higher or = precedence (>=) than one currently read, operator popped from stack
 * 				  & written to the output string & current operator is placed on stack.
 * 				  if not the case, nothing removed from stack but the currently read operator is placed on stack.
 * 4. [COMPLETE] After arithmetic expression exhausted, any operator remaining on stack is popped from off the stack &
 *    			 written to output string.
 *    
 * STEP 3: Evaluate the post fix expression
 * -This step requires a stack for storing operands (#s) ONLY.
 * -Every time operator (+, -, *, /) encountered from input string (postfix expression),
 *  stack is re-visited & only 2 topmost operands are removed from stack for calculation.
 *  1. [COMPLETE] Initialize an empty stack to store operands (#s) ONLY.
 *  2. Read the input string (the postfix expression)
 *  	- if token is an operand (#), push it onto the stack.
 *  	- if token is an operator (+, -, *, /):
 *  		a. Pop the two topmost values from the stack & store them in the variables
 *  		   t1, the topmost & t2, the second value.
 *  		b. Calculate the partial result in the following order: t2 operator t1.
 *  		c. Push the result of this calculation onto the stack.
 *  		NOTE: if stack doesn't have two operands, a malformed postfix expression has occured, & evaluation
 *  			  should be terminated.
 *  3. When the end of the input string is encountered, the result of the expression is popped from the stack.
 *     NOTE: if the stack is empty, or if it has more than one operand remaining, the result is unreliable.
 */
package Assingment_1;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Arithmetic {
	
	//variables
	private Stack<Object> stack; //empty stack to store left parenthesis
	private String stringExpression;
	private int length; //length of stringExpression
	
	public Arithmetic(String string) //constructor
	{
		stack = new Stack<Object>();
		this.stringExpression = string; //setting stringExpression to string as user input
		this.length = stringExpression.length(); //length of stringExpression
	}

	/* STEP 1: Verify the expression
	 * determines if parentheses are balanced
	 */
	boolean isBalance()
	{
		//variable
		int index = 0;
		boolean fail = false;
		
		try
		{
			while (index < length && !fail) //index < length of stringExpression & is true
			{
				//variable
				char ch = stringExpression.charAt(index); //scans expression from left to right one char at a time
				
				switch(ch)
				{
					case Constants.LEFT_PARENTHESIS:
						stack.push(index); //pushes object to top of stack
						break;
						
					case Constants.RIGHT_PARENTHESIS:
						stack.pop(); //remove object on top of stack
						break;
						
					default:
						break;
				}
				
				index++; //increment index in while loop to go onto next char
			}
		} catch (EmptyStackException e) //catch exception for try
		{
			System.out.println(e.toString());
			fail = true;
		}
		
		return (stack.empty() && !fail);
	}
	
	/* STEP 2: Convert infixed expression to postfix
	 * converts infix expression to postfix expression
	 */
	public String postFixExpression()
	{
		//variables
		String postfix = "";
		Scanner scan = new Scanner(stringExpression); //scanner class
		char current;
		boolean fail = false;
		
		while (scan.hasNext() && !fail) //while scan has another token in its input & is true
		{
			String token = scan.next(); //returns next complete token from scan
			
			if (isNumber(token)) //if token is a number *****************************************(BULLET #1)
			{
				postfix = postfix + token + Constants.A_SPACE; //string = string + token (#) + space (" ")
			}
			else
			{
				current = token.charAt(0); //char = token at char(0)
				
				if (current == Constants.LEFT_PARENTHESIS) //****************************************(BULLET #2)
				{
					stack.push(current); //push into stack
				}
				
				else if (current == Constants.RIGHT_PARENTHESIS) //********************************(BULLET #3)
				{
					try
					{
						//variables
						Character topmost = (Character)stack.pop(); //topmost (wraps value of char in an object) = pop of stack
						char top = Character.valueOf(topmost); //char top = value on top of stack
						
						while (top != Constants.LEFT_PARENTHESIS)
						{
							postfix = postfix + top + Constants.A_SPACE; //string = string + topmost value of stack + space (" ")
							top = (Character)stack.pop(); //char top = pop of stack
						}
						
					} catch (EmptyStackException e)
					{
						fail = true;
					}
				}
				
				else if (isOperator(current)) //************************************************(BULLET #4)
				{
					try
					{
						//variables
						char top = (Character)stack.peek(); //looks at object on top of stack
						boolean higher = hasHigherPrecedence(top, current);
						
						while (top != Constants.LEFT_PARENTHESIS && higher) //while top doesn't equal to ( & is higher
						{
							postfix = postfix + stack.pop() + Constants.A_SPACE; //string = string + pop of stack + space (" ")
							top = (Character)stack.peek(); //looks at object on top of stack
						}
						
						stack.push(current); //push char current to top of stack
						
					} catch(EmptyStackException e)
					{
						stack.push(current); //push char current to top of stack
					}
				}
			}
		}
		
		try
		{
			
			while (!stack.empty()) //************************************************************(BULLET #5)
			{
				postfix = postfix + stack.pop() + Constants.A_SPACE; //string = string + pop of stack + space (" ")
			}
			
		} catch(EmptyStackException e)
		{
			e.printStackTrace(); //prints to standard error stream (while program is running)
		}
		
		return postfix;
	}
	
	/* STEP 3: Evaluate the postfixed expression, if possible
	 * evaluates postfix expression
	 */
	public String evaluateRPN(String postFixExpression)
	{
		
		
		
		
		
		return postFixExpression;
	}
	
	
	
	
	/*
	 * used for postFixExpression()
	 * determine if token is an integer
	 */
	boolean isNumber(String s)
	{
		//variable
		boolean number = true;
		
		try
		{
			Integer.parseInt(s); //returns an integer as a decimal
			
		} catch (NumberFormatException e)
		{
			number = false;
		}
		
		
		return number;
	}

	
	/*
	 * used for postFixExpression
	 * Determines if token is one of the four arithmetic operators
	 */
	boolean isOperator(char ch)
	{
		//variable
		boolean operator;
		
		switch(ch)
		{
			case Constants.MULTIPLICATION:
			case Constants.DIVISION:
			case Constants.ADDITION:
			case Constants.SUBTRACTION:
				operator = true;
				break;
			default:
				operator = false;
				break;
		}
		
		return operator;
	}

	/*
	 * used for postFixExpression
	 * Determine which token has higher precedence
	 */
	boolean hasHigherPrecedence(char top, char current)
	{
		//variable
		boolean higher;
		
		switch(top)
		{
			//if * or /, do switch statement for current
			case Constants.MULTIPLICATION: //multiplication & division/ have higher precedence over addition & subtraction
			case Constants.DIVISION:
				switch(current)
				{
					//if + or -
					case Constants.ADDITION:
					case Constants.SUBTRACTION:
						higher = true;
						break;
						
					//else	
					default:
						higher = false;
						break;
				}
				
			//else, higher = false
			default:
				higher = false;
				break;
		}
		
		return higher;
	}

}
