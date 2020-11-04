package mysubway;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Dijkstra {
	private Queue<Integer> visited = null;//已访问的站点
	int[] length = null;//路径长度
	private Map<String, List<Station>> map = null;
	static String nowLine = null;//当前地铁线
	static String[] nextLine = null;//当前节点的下一换乘地铁线
	static HashMap<Integer, String> route = null;//路径
	static List<Station> list = null;

	public Dijkstra(int len, Map<String, List<Station>> map) {
		this.visited = new LinkedList<Integer>();
		this.length = new int[len];
		this.map = map;
		Dijkstra.nextLine = new String[len];
	}

	private int getIndex(Queue<Integer> q, int[] length) {
		int result = -1;
		int min_num = Integer.MAX_VALUE;
		for (int i = 0; i < length.length; i++) {
			if (!q.contains(i)) {
				if (length[i] < min_num) {
					min_num = length[i];
					result = i;
				}
			}
		}
		return result;
	}

	public String shortestroute(int v, int dest) {
		String result = "系统暂时无法查询到结果";
		for (int h = 0; h < list.size(); h++) {
			if (h == dest) {
				result = "从" + list.get(v).getStaName() + "到" + list.get(h).getStaName() + "之间的具体换乘建议为：\n\n-->搭乘"
						+ list.get(v).getLine() + "出发" + "\n\t-->" + route.get(h).toString();
				System.out.println();
				break;
			}
		}
		return result;
	}

	public String dijkstra(int[][] weight, List<Station> list, int v, int dest) {
		Dijkstra.list = list;
		//路径HashMap route;
		route = new HashMap<Integer, String>();
		for (int i = 0; i < list.size(); i++) {
			route.put(i, "");
		}
		//初始化路径长度数组length
		for (int i = 0; i < list.size(); i++) {
			route.put(i, route.get(i) + "" + list.get(v).getStaName());
			if (i == v) {
				length[i] = 0;
			}
			//连通站点
			else if (weight[v][i] != -1) {
				length[i] = weight[v][i];
				//获取当前站点所属的线路
				nowLine = list.get(v).getLine();
				StringBuffer sbf = new StringBuffer();
				for (Station s : map.get(nowLine)) {
					sbf.append(s.getStaName());
				}
				//起点站和下一站点是否属于同一地铁线
				if (sbf.indexOf(list.get(i).getStaName()) != -1) {
					route.put(i, route.get(i) + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = nowLine;
				}
				else {
					route.put(i, route.get(i) + "\n-->换乘" + list.get(i).getLine() + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = list.get(i).getLine();
				}
			}
			//不连通
			else
				length[i] = Integer.MAX_VALUE;
		}
		visited.add(v);

		//迭代寻找最优线路
		while (visited.size() < list.size()) {
			int k = getIndex(visited, length);// 获取未访问点中距离源点最近的点
			visited.add(k);
			if (k != -1) {
				for (int j = 0; j < list.size(); j++) {
					if (weight[k][j] != -1) {// 判断k点能够直接到达的点
						//通过遍历各点，比较是否有比当前更短的路径，有的话，则更新length，并更新route。
						if (length[j] > length[k] + weight[k][j]) {
							length[j] = length[k] + weight[k][j];
							nowLine = nextLine[k];
							StringBuffer sbf = new StringBuffer();
							for (Station s : map.get(nowLine)) {
								sbf.append(s.getStaName());
							}
							//判断到下一站点是否需要换乘
							if (sbf.indexOf(list.get(j).getStaName()) != -1) {
								route.put(j, route.get(k) + "\n\t-->" + list.get(j).getStaName());
								nextLine[j] = nowLine;
							}
							else {
								StringBuffer temp = new StringBuffer();
								for (String str : map.keySet()) {
									temp = new StringBuffer();
									for (Station s : map.get(str)) {
										temp.append(s.getStaName());
									}
									if (temp.indexOf(list.get(j).getStaName()) != -1
											&& temp.indexOf(list.get(k).getStaName()) != -1) {
										route.put(j,route.get(k) + "\n-->换乘" + str + "\n\t-->" + list.get(j).getStaName());
										nextLine[j] = str;
									}
								}
							}
						}
					}
				}
			}
		}
		visited.clear();
		return this.shortestroute(v, dest);
	}
}

