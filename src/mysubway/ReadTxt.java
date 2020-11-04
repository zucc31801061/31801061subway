package mysubway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ui.FrmMain;

public class ReadTxt {
	static Set<String> lines = null;//存储地铁线路名
	static List<Station> allStations = null;//存储所有站点信息（编号，站点名，所属线路），其中换乘站应有多条信息
	static Map<String, List<Station>> map = new LinkedHashMap<>();//存储线路信息，线路名，线路所含站点信息
	static int[][] graph = null;//存储站点间的连通信息
	
	public ReadTxt(String fileName) {
		readFileContent(fileName);
		initialize();
		new FrmMain(map, allStations, graph);
	}
	//读取文件
	private void readFileContent(String fileName) {
		File file = new File(fileName);
		InputStreamReader read = null;
		BufferedReader br = null;
		lines = new LinkedHashSet<>();
		allStations = new LinkedList<>();
		List<Station> line_stations = null;
		try {
			if(file.isFile() && file.exists()){//判断文件是否存在
				read = new InputStreamReader(new FileInputStream(file), "UTF-8");
				br = new BufferedReader(read);
				String tempStr = null;
				int staId = 0;//站点编号
				while ((tempStr = br.readLine())!= null) {//使用readLine方法，一次读一行，判断是否读到文件末尾
					line_stations = new LinkedList<>();
					String[] message = tempStr.split("\\s+");//分割线路站点信息
					lines.add(message[0]);//读取地铁线路名
					for(int i=1; i<message.length; i++) {
						Station station = new Station(staId++, message[i], message[0]);//创建站点对象
						allStations.add(station);
						line_stations.add(station);//将站点加入当前线路
					}
					map.put(message[0], line_stations);//将路线加入map
				}
				br.close();
			}
			else{
            	System.out.println("找不到指定的文件");
            }
		} catch (IOException e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
	}
	//初始化
	private void initialize() {
		int count = 0;//站点数量
		allStations = new LinkedList<>();
		
		for (String st : map.keySet()) {//遍历map中地铁线路
			count += map.get(st).size();
			for (Station s : map.get(st)) {//遍历当前线路站点
				allStations.add(s);
			}
		}
		graph = new int[count][count];
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count; j++)
				graph[i][j] = -1;
		}
		for (int i = 0; i < count; i++) {
			String name = allStations.get(i).getStaName();
			graph[i][i] = 0;
			for (Station s : allStations) {
				if (s.getStaName().equals(name)) {//可换乘站和环线起止站的连通
					int id = s.getStaId();
					if (id - 1 >= 0) {
						if (allStations.get(id - 1).getLine().equals(allStations.get(id).getLine())) {
							graph[i][id - 1] = 1;
							graph[id - 1][i] = 1;
						}
					}
					if (id + 1 < count) {
						if (allStations.get(id + 1).getLine().equals(allStations.get(id).getLine())) {
							graph[i][id + 1] = 1;
							graph[id + 1][i] = 1;
						}
					}
				}
			}
		}
	}
}
