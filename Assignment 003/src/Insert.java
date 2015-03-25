import java.io.IOException;


public class Insert {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length!=3){
			System.out.println("Input \"java Insert <name> <course_name> <score>\"");
			System.exit(0);
		}
		
		DB d = new DB();
		int index=d.find_record(args[0],args[1]);
		if(index!=-1){
			Record r=d.records.get(index);
			r.score=Integer.valueOf(args[2]);
		}
		else{
			d.insert_record(args[0],args[1],Integer.valueOf(args[2]));
		}
		
		d.save();
		System.out.println("Record Updated");
	}

}
