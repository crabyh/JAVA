import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.*;

public class Log {

	static Map<String, Integer> IPMap = new HashMap();
	static Map<String, Integer> WebMap = new HashMap();
	static Map<Integer, Integer> DayMap = new HashMap();
	static Map<Integer, Integer> HourMap = new HashMap();
	static long totalTraffic = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("access.log");
		if (file.exists() && file.isFile()) {
			StringBuffer content = new StringBuffer();
			try {
				char[] temp = new char[1024];
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(
						fileInputStream, "GBK");
				while (inputStreamReader.read(temp) != -1) {
					content.append(new String(temp));
					temp = new char[1024];
				}
				fileInputStream.close();
				inputStreamReader.close();
				int begin = 0, end = 0;
				String record;
				end = content.indexOf("\n", begin + 1);
				while (end != -1) {
					record = content.substring(begin, end);
					analyseIP(record);
					analyseTime(record);
					analyseWeb(record);
					analyseTraffic(record);
					begin = end;
					end = content.indexOf("\n", begin + 1);
				}

				sortIP();
				sortWeb();
				System.out.println(IPMap);
				System.out.println(DayMap);
				System.out.println(HourMap);
				System.out.println(WebMap);
				System.out.println(totalTraffic);
				// for (Object o : WebMap.keySet()) {
				// System.out.println("URL=" + o + " Times=" + WebMap.get(o));
				// }
				// for (Object o : IPMap.keySet()) {
				// System.out.println("key=" + o + " value=" + IPMap.get(o));
				// }

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				OutputStream os = new FileOutputStream(
						"Traffic&Time_analyse.CSV");
				String line = "Total Traffic: " + totalTraffic + "\n";
				for (int x = 0; x < line.length(); x++) {
					os.write(line.charAt(x)); // writes the bytes
				}
				os.write('\n');
				line = "Hour,\tTimes\n";
				for (int x = 0; x < line.length(); x++) {
					os.write(line.charAt(x)); // writes the bytes
				}
				for (Object o : HourMap.keySet()) {
					line = o + ",\t" + HourMap.get(o) + "\n";
					for (int x = 0; x < line.length(); x++) {
						os.write(line.charAt(x)); // writes the bytes
					}
				}
				os.write('\n');
				line = "Day,\tTimes\n";
				for (int x = 0; x < line.length(); x++) {
					os.write(line.charAt(x)); // writes the bytes
				}
				for (Object o : DayMap.keySet()) {
					line = o + ",\t" + DayMap.get(o) + "\n";
					for (int x = 0; x < line.length(); x++) {
						os.write(line.charAt(x)); // writes the bytes
					}
				}
				os.close();
			} catch (FileNotFoundException e) {
				System.out.print("Exception");
			} catch (IOException e) {
				System.out.print("Exception");
			}
		}
	}

	public static void analyseIP(String record) {
		String IP;

		String pattern = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(record);

		// int index=-1;
		// if(record.indexOf(" - - ")!=-1)
		// index=record.indexOf(" - - ");
		// else if(record.indexOf(" - root ")!=-1)
		// index=record.indexOf(" - root ");
		// else if(record.indexOf(" - wengkai ")!=-1)
		// index=record.indexOf(" - wengkai ");
		// IP=record.substring(0,index);

		if (m.find()) {
			IP = m.group();
			// System.out.println(IP);
			if (IPMap.get(IP) == null) {
				IPMap.put(IP, 1);
			} else {
				IPMap.put(IP, IPMap.remove(IP) + 1);
			}
		}
	}

	public static void sortIP() {
		// 这里将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				IPMap.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// 升序排序
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});
		try {
			OutputStream os = new FileOutputStream("IP_analyse.CSV");
			String line = "IP,\tTimes\n";
			for (int x = 0; x < line.length(); x++) {
				os.write(line.charAt(x)); // writes the bytes
			}
			for (Map.Entry<String, Integer> mapping : list) {
				line = mapping.getKey() + ",\t"
						+ mapping.getValue() + "\n";
				for (int x = 0; x < line.length(); x++) {
					os.write(line.charAt(x)); // writes the bytes
				}
			}
			os.close();
		} catch (FileNotFoundException e) {
			System.out.print("Exception");
		} catch (IOException e) {
			System.out.print("Exception");
		}

	}

	public static void analyseTime(String record) {
		String time;
		String pattern = "(\\[.*\\])";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(record);

		if (m.find()) {
			time = m.group();

			int day = Integer.valueOf(time.substring(time.indexOf('[') + 1,
					time.indexOf('/')));
			// System.out.print(day);
			if (DayMap.get(day) == null) {
				DayMap.put(day, 1);
			} else {
				DayMap.put(day, DayMap.remove(day) + 1);
			}

			String pattern1 = "2012:\\d+\\:";
			Pattern r1 = Pattern.compile(pattern1);
			Matcher m1 = r1.matcher(time);
			if (m1.find()) {
				int hour = Integer.valueOf(m1.group().substring(
						m1.group().indexOf(':') + 1,
						m1.group().lastIndexOf(':')));
				if (HourMap.get(hour) == null) {
					HourMap.put(hour, 1);
				} else {
					HourMap.put(hour, HourMap.remove(hour) + 1);
				}
			}
		}
	}

	public static void analyseWeb(String record) {
		String web;
		String pattern = "(GET.+HTTP|POST.+HTTP)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(record);

		if (m.find()) {
			web = m.group();
			web = web.substring(4, web.indexOf("HTTP")).trim();
			if (web.indexOf('?') > 0) {
				web = web.substring(0, web.indexOf('?'));
			}
			// System.out.println(web);
			if (WebMap.get(web) == null) {
				WebMap.put(web, 1);
			} else {
				WebMap.put(web, WebMap.remove(web) + 1);
			}
		}
	}

	public static void sortWeb() {
		// 这里将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				WebMap.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// 升序排序
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		try {
			OutputStream os = new FileOutputStream("URL_analyse.CSV");
			String line = "URL,\tTimes\n";
			for (int x = 0; x < line.length(); x++) {
				os.write(line.charAt(x)); // writes the bytes
			}
			for (Map.Entry<String, Integer> mapping : list) {
				line = mapping.getKey() + ",\t"
						+ mapping.getValue() + "\n";
				for (int x = 0; x < line.length(); x++) {
					os.write(line.charAt(x)); // writes the bytes
				}
			}
			os.close();
		} catch (FileNotFoundException e) {
			System.out.print("Exception");
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	public static void analyseTraffic(String record) {
		String web;
		String pattern = "(\\d+$|-$)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(record);

		if (m.find()) {
			web = m.group();
			if (!web.equals("-")) {
				totalTraffic += Integer.valueOf(web);
			}
		}
	}

}
