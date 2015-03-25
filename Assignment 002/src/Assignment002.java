import java.util.Scanner;

public class Assignment002 {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		String line = in.nextLine();//����һ��
		line = line.trim();
		line = line.replaceAll("[\\p{Punct}]+", ""); //ȥ������
		int answer = 0;
		while(!line.equals("")){
			int index = line.indexOf(' ');
			while(index != -1){
				String word = line.substring(0, index);//��ȡһ����
				answer+=keyword(word);//��Ӧ�������¼�������
				line = line.substring(index);
				line = line.trim();
				index = line.indexOf(' ');
			}
			answer+=keyword(line);//���һ����
			if(answer == 0)
				System.out.println("Sorry, I can't understand.");
			answer = 0;
			line = in.nextLine();
			line = line.trim();
			line = line.replaceAll("[\\p{Punct}]+", ""); 
		}
	}
	
	private static int keyword(String word){
		if(word.equalsIgnoreCase("hello")){//�жϹؼ���
			if((int)(Math.random()+0.5) == 0)//�����漴�ظ�
				System.out.println("Hello!");
			else
				System.out.println("Can I help you?");
			return 1;
		}
		if(word.equalsIgnoreCase("DodgySoft")){
			if((int)(Math.random()+0.2) == 0)
				System.out.println("DodgySoft is great!");
			else
				System.out.println("I won't tell you I'm a program :P");
			return 1;
		}if(word.equalsIgnoreCase("support")){
			if((int)(Math.random()+0.5) == 0)
				System.out.println("You need any support?");
			else
				System.out.println("Yep, I can help you!");
			return 1;
		}
		//��������Ӹ��ֹؼ���
		return 0;
	}
}
