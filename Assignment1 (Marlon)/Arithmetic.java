package assignment1;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Arithmetic {
    //variables
    private Stack<Object> stack; //empty stack to store left parenthesis
    private String stringExpression;
    private int length; //length of stringExpression
    private String postfixExpression;
    private String RPNResult;

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

                else //************************************************(BULLET #4)
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
        postfixExpression = postfix;
        return postfix;
    }

    /* STEP 3: Evaluate the postfixed expression, if possible
     * evaluates postfix expression
     */
    public void evaluateRPN()
    {
        //variables
        //String resultString = "";
        Scanner scan = new Scanner(postfixExpression); //scanner class
        char current;
        boolean fail = false;
        Stack<Double> operands = new Stack<Double>();
        Double ans = 0.0;
        int index = 0;

        while (scan.hasNext() && !fail) //while scan has another token in its input & is true
        {
            try{
            String token = scan.next(); //returns next complete token from scan

            if (isNumber(token)) {
                Double tokenDouble = Double.parseDouble(token);
                operands.push(tokenDouble);
                //System.out.println("Index: " + index + ", token: " + token);
            }else{
                if(isOperator(token.charAt(0))){
                    Double t1 = operands.pop();
                    Double t2 = 0.0;
                    //System.out.println("Index: " + index + ", t1: " + t1);
                    if(!operands.isEmpty()){
                        t2 = operands.pop();
                        //System.out.println("Index: " + index + ", t2: " + t2);
                    }else{
                        throw new Exception();
                    }
                    Double result = evaluateSimple(t2, t1, token);
                    operands.push(result);
                    //System.out.println("Index: " + index + ", result: " + result);
                }else{
                    System.out.println("Character: "+token+" is not an operator");
                }
            }
            index++;

        }catch(Exception e){
            System.out.println("The postfix expression is malformed");
            fail = true;
        }

        }
        if(operands.isEmpty() || operands.size() > 1){
            System.out.println("The postfix expression is malformed");
        }else{
        ans = operands.pop();
    }
        //set the postfix to the evaluated result
        RPNResult = Double.toString(ans);

    }

    //To get the postfix result
    //@param none
    //@return postfix expression
    public String getPostfix(){
        return postfixExpression;
    }

    //To get the RPN result
    //@param none
    //@return RPN result
    public String getRPNResult(){
        return RPNResult;
    }


    //Helper function to evaluate simple arithmetic expression with 2 operands
    //@param t1 first operand
    //@param t2 second operand
    //@param operator the operator
    //@return result
    private double evaluateSimple(Double t1, Double t2, String operator){
        double ans = 0;
        char operatorChar = operator.charAt(0);
        switch(operatorChar){
            case Constants.ADDITION:
                ans = t1 + t2;
                break;
            case Constants.SUBTRACTION:
                ans = t1 - t2;
                break;
            case Constants.MULTIPLICATION:
                ans = t1 * t2;
                break;
            case Constants.DIVISION:
                ans = t1 / t2;
                break;
            case Constants.MODULO:
                ans = t1 % t2;
                break;
        }
        return ans;
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
