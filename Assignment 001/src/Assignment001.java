import java.util.Scanner;
import java.util.Stack;  

public class Assignment001 {
	public static void main(String[] args) {
		Stack<Character> Soperator = new Stack<Character>(); //用来储存运算符的栈
		Stack<Integer> Soperand = new Stack<Integer>(); //用来储存算子的栈
		Scanner in=new Scanner(System.in);
		String s=in.nextLine();//读取一行数据
		s+="\n";//加上结尾标记符
		while(!s.equals("\n")){
			int i = 0;
			int operand;
			char operator;
			while(s.charAt(i)!='\n'){//跳过空格
				while(s.charAt(i) == ' '){
					++i;
				}
				operand = 0;
				operator = 0;
				if(s.charAt(i)<='9'&&s.charAt(i)>='0'){//读取一个数
					while(s.charAt(i)<='9'&&s.charAt(i)>='0'){
						operand = operand*10+s.charAt(i)-'0';
						++i;
					}
					Soperand.push(operand);
				}
				if(s.charAt(i) == '(') {//左括号压栈
					Soperator.push(s.charAt(i));
					++i;
				}
				else if(s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '%'){//乘除法直接计算
					operator = s.charAt(i);
					++i;
					while(s.charAt(i) == ' ')	++i;//跳过空格
					if(s.charAt(i) == '(') {//如果乘除右边是括号，压栈
						Soperator.push(operator);
						Soperator.push(s.charAt(i));
						++i;
					}
					else{
						operand = 0;
						while(s.charAt(i) == ' ')	++i;//跳过空格
						while(s.charAt(i)<='9'&&s.charAt(i)>='0'){
							operand = operand*10+s.charAt(i)-'0';
							++i;
						}
						int result = calculate(operand,Soperand.pop(),operator);
						Soperand.push(result);
					}
				}
				else if(s.charAt(i) == '+' || s.charAt(i) == '-'){//碰到加减则运算前一个加减（如果有的话）
					operator = s.charAt(i);
					if(!Soperator.empty()){
						int result = 0;
						if(Soperator.lastElement() == '+' || Soperator.lastElement() == '-'){
							result = calculate(Soperand.pop(),Soperand.pop(),Soperator.pop());
							Soperand.push(result);
						}
					}
					Soperator.push(operator);
					++i;
				}
				else if(s.charAt(i) == ')'){//碰到右括号将栈里面到左括号位置的计算全部完成
					++i;
					operator = Soperator.pop();
					while(operator != '('){
						int result = calculate(Soperand.pop(),Soperand.pop(),operator);
						Soperand.push(result);
						operator = Soperator.pop();
					}
					if(!Soperator.empty()){//如果左括号左边还有未计算的乘除，计算它
						if(Soperator.lastElement() == '*' || Soperator.lastElement() == '/' || Soperator.lastElement() == '%'){
							int result = calculate(Soperand.pop(),Soperand.pop(),Soperator.pop());
							Soperand.push(result);
						}
					}
				}
			}
			while(!Soperator.empty()){//栈中还有未计算的操作
				int result = calculate(Soperand.pop(),Soperand.pop(),Soperator.pop());
				Soperand.push(result);
			}
			if(!Soperand.empty())
				System.out.println(Soperand.pop());//输出结果
			//重新初始化
			s=in.nextLine();
			s+="\n";
			Soperator.clear();
			Soperand.clear();
		}
	  
	}
	private static int calculate(int y, int x, char operator){
		int result = 0;
		switch (operator){
		case '+':result = x + y; break;
		case '-':result = x - y; break;
		case '*':result = x * y; break;
		case '/':result = x / y; break;
		case '%':result = x % y; break;
		}
		return result;
	}
}