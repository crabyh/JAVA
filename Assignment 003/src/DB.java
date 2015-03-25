import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class DB {
	public ArrayList<Record> records=new ArrayList();
	public ArrayList<Student> students=new ArrayList();
	public ArrayList<Course> courses=new ArrayList();
	
	public DB() throws IOException{
		FileInputStream fip = new FileInputStream("save.CSV");		
		InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
		StringBuffer sb = new StringBuffer();
		String input[]=new String[3];
		int i=0;
		while (reader.ready()) {
			char ch=(char) reader.read();
			if(ch==','){
				input[i]=sb.toString();
				sb.delete(0, sb.length());
				++i;
			}
			else if(ch=='\n'){
				input[i]=sb.toString().trim();
				insert_record(input[0],input[1],Integer.valueOf(input[2]));
				sb.delete(0, sb.length());
				i=0;
			}
			else if(ch==' '){
			}
			else
				sb.append(ch);
		}
		String s=sb.toString();
		reader.close();		
		fip.close();
	}
	
	public void save() throws IOException{
		OutputStream fop = new FileOutputStream("save.CSV");
		OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
		for(Record r : records)
		{
			writer.append(r.name+", "+r.course_name+", "+r.score+"\n");
		}
		writer.close();
		fop.close();
	}

	
	public int find_record(String name,String course_name){
		int index;
		for(Record r : records)
		{
			if(r.name.equals(name)&&r.course_name.equals(course_name)){
				return records.indexOf(r);
			}
		}
		return -1;
	}
	
	public void insert_record(String name,String course_name,int score){
		int index;
		boolean flag=false;
		Record r=new Record(name,course_name,score);
		records.add(r);
		for(Student s : students)
		{
			if(s.name.equals(name)){
				s.records.add(r);
				flag=true;
				break;
			}
		}
		if(flag==false){
			Student s=new Student(name);
			students.add(s);
			s.records.add(r);
		}
		flag=false;
		for(Course c : courses)
		{
			if(c.course_name.equals(course_name)){
				c.records.add(r);
				flag=true;
				break;
			}
		}	
		if(flag==false){
			Course c=new Course(course_name);
			courses.add(c);
			c.records.add(r);
		}
	}
	
	public void print_student(String name){
		System.out.println("Name      \tCourse     \tScore");
		int sum=0,n=0;
		for(Student s : students)
		{
			if(s.name.equals(name)){
				for(Record r : s.records){
					sum+=r.score;
					++n;
					System.out.println(getEquelsLength(r.name,10)+"\t"+getEquelsLength(r.course_name,10)+"\t"+r.score);
				}
				System.out.println("    \tTotal Score:\t"+sum);
				System.out.println("    \tAverage Score:\t"+sum/n);
				break;
			}
		}
	}
	
	public void print_course(String course_name){
		System.out.println("Name      \tCourse     \tScore");
		int sum=0,n=0;
		for(Course c : courses)
		{
			if(c.course_name.equals(course_name)){
				for(Record r : c.records){
					sum+=r.score;
					++n;
					System.out.println(getEquelsLength(r.name,10)+"\t"+getEquelsLength(r.course_name,10)+"\t"+r.score);
				}
				System.out.println("    \tAverage Score:\t"+sum/n);
				break;
			}
		}
	}
	
	public String  getEquelsLength(String a,int length){
        int aLength = a.length();
        if(length>aLength){
            int i = length-aLength;
            for (int j = 0; j < i; j++) {
                a +=" ";
            }
        }
        return a;
	}

}
