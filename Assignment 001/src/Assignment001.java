import java.util.Scanner;
import java.util.Stack;  

public class Assignment001 {
	public static void main(String[] args) {
		Stack<Character> Soperator = new Stack<Character>(); //���������������ջ
		Stack<Integer> Soperand = new Stack<Integer>(); //�����������ӵ�ջ
		Scanner in=new Scanner(System.in);
		String s=in.nextLine();//��ȡһ������
		s+="\n";//���Ͻ�β��Ƿ�
		while(!s.equals("\n")){
			int i = 0;
			int operand;
			char operator;
			while(s.charAt(i)!='\n'){//�����ո�
				while(s.charAt(i) == ' '){
					++i;
				}
				operand = 0;
				operator = 0;
				if(s.charAt(i)<='9'&&s.charAt(i)>='0'){//��ȡһ����
					while(s.charAt(i)<='9'&&s.charAt(i)>='0'){
						operand = operand*10+s.charAt(i)-'0';
						++i;
					}
					Soperand.push(operand);
				}
				if(s.charAt(i) == '(') {//������ѹջ
					Soperator.push(s.charAt(i));
					++i;
				}
				else if(s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '%'){//�˳���ֱ�Ӽ���
					operator = s.charAt(i);
					++i;
					while(s.charAt(i) == ' ')	++i;//�����ո�
					if(s.charAt(i) == '(') {//����˳��ұ������ţ�ѹջ
						Soperator.push(operator);
						Soperator.push(s.charAt(i));
						++i;
					}
					else{
						operand = 0;
						while(s.charAt(i) == ' ')	++i;//�����ո�
						while(s.charAt(i)<='9'&&s.charAt(i)>='0'){
							operand = operand*10+s.charAt(i)-'0';
							++i;
						}
						int result = calculate(operand,Soperand.pop(),operator);
						Soperand.push(result);
					}
				}
				else if(s.charAt(i) == '+' || s.charAt(i) == '-'){//�����Ӽ�������ǰһ���Ӽ�������еĻ���
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
				else if(s.charAt(i) == ')'){//���������Ž�ջ���浽������λ�õļ���ȫ�����
					++i;
					operator = Soperator.pop();
					while(operator != '('){
						int result = calculate(Soperand.pop(),Soperand.pop(),operator);
						Soperand.push(result);
						operator = Soperator.pop();
					}
					if(!Soperator.empty()){//�����������߻���δ����ĳ˳���������
						if(Soperator.lastElement() == '*' || Soperator.lastElement() == '/' || Soperator.lastElement() == '%'){
							int result = calculate(Soperand.pop(),Soperand.pop(),Soperator.pop());
							Soperand.push(result);
						}
					}
				}
			}
			while(!Soperator.empty()){//ջ�л���δ����Ĳ���
				int result = calculate(Soperand.pop(),Soperand.pop(),Soperator.pop());
				Soperand.push(result);
			}
			if(!Soperand.empty())
				System.out.println(Soperand.pop());//������
			//���³�ʼ��
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