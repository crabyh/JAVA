import java.io.IOException;
import java.util.ArrayList;

public class Student {
	
	public String name;
	public ArrayList<Record> records = new ArrayList();
	
	public Student(String name){
		this.name=name;
	}
	
	public void insert(Record record){
		records.add(record);
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length!=1){
			System.out.println("Input \"java Student <name>\"");
			System.exit(0);
		}
		DB d=new DB();
		d.print_student(args[0]);
	}
}
