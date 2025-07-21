package employeemanager;
import java.io.*;
import java.util.*;

public class CSVReader {
	public static List<String[]> readCSV(String filePath) throws IOException{
		List<String[]> data=new ArrayList<>();
		BufferedReader reader=null;
		String line="";
		try {
			reader=new BufferedReader(new FileReader(filePath));
			while((line=reader.readLine())!=null) {
				String [] row=line.split(",");
				data.add(row);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			reader.close();
		}
		return data;
	}
}
