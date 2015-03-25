import java.io.IOException;
import java.util.ArrayList;


public class Course {

	public String course_name;
	public ArrayList<Record> records=new ArrayList();
	
	public Course(String course_name) {
		this.course_name=course_name;
	}
	public void insert(Record record){
		records.add(record);
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length!=1){
			System.out.println("Input \"java Course <name>\"");
			System.exit(0);
		}
		DB d=new DB();
		d.print_course(args[0]);
	}
	
}
