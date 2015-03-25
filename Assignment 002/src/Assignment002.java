import java.util.Scanner;

public class Assignment002 {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		String line = in.nextLine();//读入一行
		line = line.trim();
		line = line.replaceAll("[\\p{Punct}]+", ""); //去除符号
		int answer = 0;
		while(!line.equals("")){
			int index = line.indexOf(' ');
			while(index != -1){
				String word = line.substring(0, index);//读取一个词
				answer+=keyword(word);//对应输出并记录输出次数
				line = line.substring(index);
				line = line.trim();
				index = line.indexOf(' ');
			}
			answer+=keyword(line);//最后一个词
			if(answer == 0)
				System.out.println("Sorry, I can't understand.");
			answer = 0;
			line = in.nextLine();
			line = line.trim();
			line = line.replaceAll("[\\p{Punct}]+", ""); 
		}
	}
	
	private static int keyword(String word){
		if(word.equalsIgnoreCase("hello")){//判断关键词
			if((int)(Math.random()+0.5) == 0)//产生随即回复
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
		//还可以添加各种关键词
		return 0;
	}
}
