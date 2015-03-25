import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Execfile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length!=1){
			System.out.println("Input \"java Execfile <filename>\"");
			System.exit(0);
		}
		DB d=new DB();
		FileInputStream fip = new FileInputStream(args[0]);		
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
				input[i]=sb.toString();
				input[2]=input[2].trim();
				d.insert_record(input[0],input[1],Integer.valueOf(input[2]));
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
		
		d.save();
		System.out.println("File Executed");
	}

}
