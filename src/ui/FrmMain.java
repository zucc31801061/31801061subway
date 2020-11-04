package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mysubway.Dijkstra;
import mysubway.Station;

public class FrmMain extends JDialog implements ActionListener {
	//定义程序序列化ID
		private static final long serialVersionUID = 1L;
		//创建底部面板
		private JPanel toolBar = new JPanel();
		//创建主面板
		private JPanel workPane = new JPanel();
		//创建下拉列表
		private JComboBox<String> line = null;
		//创建单行文本框
		private JTextField start = new JTextField(10);
		private JTextField end = new JTextField(10);
		//创建提示标签
		private JLabel chooseLab = new JLabel("请选择要查看的线路：");
		private JLabel startLab = new JLabel("请输入起始站点：");
		private JLabel endLab = new JLabel("请输入目的站点：");
		//创建多行文本框
		private JTextArea station = new JTextArea();
		private JTextArea route = new JTextArea();
		//创建按钮
		private JButton btnOk = new JButton("确定");
		private JButton btnCancel = new JButton("重置");
		//创建滚动条
		private JScrollPane scrollpane1 = new JScrollPane(station);
		private JScrollPane scrollpane2 = new JScrollPane(route);
		
		private List<String> lines = null;
		private Map<String, List<Station>> map = null;
		private List<Station> allStations = null;
		private int[][] graph = null;

		public FrmMain(Map<String, List<Station>> map, List<Station> allStations, int[][] graph) {
			this.lines = new LinkedList<String>(map.keySet());
			this.allStations = allStations;
			this.map = map;
			this.graph = graph;
			//初始化
			initialize();
			//设置窗口标题
			this.setTitle("北京地铁线路查询");
			toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
			toolBar.add(btnOk);
			toolBar.add(btnCancel);
			this.getContentPane().add(toolBar, BorderLayout.SOUTH);
			workPane.add(chooseLab);
			workPane.add(line);
			workPane.add(station);
			workPane.add(scrollpane1);
			scrollpane1.setViewportView(station);
			workPane.add(startLab);
			workPane.add(endLab);
			workPane.add(start);
			workPane.add(end);
			workPane.add(route);
			workPane.add(scrollpane2);
			scrollpane2.setViewportView(route);
			this.workPane.setLayout(null);
			//获取显示屏大小
			double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			//设定窗口大小
			this.setSize((int) width * 3 / 5, (int) height * 3 / 4);
			//窗口居中显示
			this.setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);
			//添加鼠标监听器
			this.btnOk.addActionListener(this);
			this.btnCancel.addActionListener(this);
			//添加文本框样式
			this.station.setFont(new Font("宋体", Font.BOLD, 14));
			this.route.setFont(new Font("宋体", Font.BOLD, 14));
			//让窗口响应大小改变事件
			this.workPane.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					int inWidth = workPane.getWidth();// 获取窗口宽度
					int inHeight = workPane.getHeight();// 获取窗口高度
					chooseLab.setBounds(inWidth / 50, inHeight / 160 + 10, inWidth / 7, inHeight / 20);
					line.setBounds(inWidth / 50, inHeight / 20 + 20, inWidth / 7, inHeight / 20);
					station.setBounds(inWidth / 50, inHeight / 8 + 30, inWidth * 9 / 20, inHeight * 4 / 5);
					scrollpane1.setBounds(inWidth / 50, inHeight / 8 + 30, inWidth * 9 / 20, inHeight * 4 / 5);
					start.setBounds(inWidth / 2 + 10, inHeight / 20 + 20, inWidth / 5, inHeight / 20);
					end.setBounds(inWidth * 3 / 4 + 10, inHeight / 20 + 20, inWidth / 5, inHeight / 20);
					route.setBounds(inWidth / 2 + 10, inHeight / 8 + 30, inWidth * 9 / 20, inHeight * 4 / 5 - 10);
					scrollpane2.setBounds(inWidth / 2 + 10, inHeight / 8 + 30, inWidth * 9 / 20, inHeight * 4 / 5 - 10);
					startLab.setBounds(inWidth / 2 + 10, inHeight / 160 + 10, inWidth / 5, inHeight / 20);
					endLab.setBounds(inWidth * 3 / 4 + 10, inHeight / 160 + 10, inWidth / 5, inHeight / 20);
				}
			});
			this.getContentPane().add(workPane, BorderLayout.CENTER);
			this.validate();
			//关闭当前窗口则退出整个程序
			this.addWindowListener(new WindowAdapter(){   
		    	public void windowClosing(WindowEvent e){ 
		    		System.exit(0);
	             }
	        });
			this.setVisible(true);
	}

	private void initialize() {
		//初始化line下拉列表
		String[] lineStaions = new String[this.lines.size() + 1];
		((LinkedList<String>) this.lines).addFirst("请选择地铁线路");
		((LinkedList<String>) this.lines).toArray(lineStaions);
		this.line = new JComboBox<>(lineStaions);
		this.validate();
		//对选择的选项运行outputStation
		this.line.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String linename = line.getSelectedItem().toString();
					//运行结果输出到station多行文本框
					station.setText(outputStation(linename));
				}
			}
		});
	}
	
	public String outputStation(String linename) {
		//获取map中linename对应的站点序列
		List<Station> chooseStations = this.map.get(linename);
		//新建String数组保存
		int size = 0;
		if (chooseStations != null) {
			size = chooseStations.size();
		}
		String[] stationname = new String[size];
		for (int i = 0; i < size; i++) {
			stationname[i] = chooseStations.get(i).getStaName();
		}
		//转为String输出
		String result = "";
		for (int i = 0; i < size; i++) {
			result += stationname[i] + "\n";
		}
		return result;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String startSta = null;
		String endSta = null;
		//点击重置按钮清空Start、end、route中的内容
		if (e.getSource() == this.btnCancel) {
			this.start.setText(null);
			this.end.setText(null);
			this.route.setText(null);
		}
		//点击确定按钮，判断start和end的内容是否为空
		else if(e.getSource() == this.btnOk) {
			//为空则弹出提示框（包括各种空白字符）
			if (this.start.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入起始站点", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//读取start中输入的内容
			startSta = this.start.getText();
			if (this.end.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入目的站点", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//读取end中输入的内容
			endSta = this.end.getText();
			//判断起始站和终点站是否存在，若不存在则弹出提示框
			boolean flag = false;
			for(int i = 0; i < this.allStations.size(); i++) {
				flag = false;
				String staname = this.allStations.get(i).getStaName();
				if(startSta.equals(staname)) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				JOptionPane.showMessageDialog(null, "起始站不存在", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for(int i = 0; i < this.allStations.size(); i++) {
				flag = false;
				String endname = this.allStations.get(i).getStaName();
				if(endSta.equals(endname)) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				JOptionPane.showMessageDialog(null, "终点站不存在", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//若start中内容与end中内容相同则弹出提示框
			if(startSta.equals(endSta)) {
				JOptionPane.showMessageDialog(null, "起始站和终点站相同", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//存储所有的起始站和终点站id
			int[] startsta = new int[10];
			int[] endsta = new int[10];
			//初始化为-1
			for(int i = 0; i < 10; i++) {
				startsta[i] = -1;
				endsta[i] = -1;
			}
			//遍历map存入数组
			int i = 0, j = 0;
			int startid = -1, endid = -1;
			for(String st : this.map.keySet()) {
				for (Station s : map.get(st)) {
					String temp = s.getStaName();
					if(startSta.equals(temp)) {
						startid = s.getStaId();
						if(startid >= 0) {
							startsta[i++] = startid;
						}
					}
					if(endSta.equals(temp)) {
						endid = s.getStaId();
						if(endid >= 0) {
							endsta[j++] = endid;
						}
					}
				}
			}
			//数组一一对应查找最短路径
			String all = null;
			String result = null;
			//存储所有最短路径中最短的路径
			int count = this.allStations.size();
			Dijkstra dijkstra = new Dijkstra(allStations.size(), this.map);
			for(i = 0; i < 10;i++) {
				int x = startsta[i];
				if(x < 0) {
					break;
				}
				for(j = 0; j < 10;j++) {
					int y = endsta[j];
					if(y < 0) {
						break;
					}
					all = dijkstra.dijkstra(graph, allStations, x, y);
					//查看当前最短路径的输出行数
					String[] line = all.split("\n");
					//若比存储的路径短，则替代最短路径
					if(count > line.length) {
						count = line.length;
						result = all;
					}
				}
			}
			//输出结果到route文本框
			this.route.setText(result);
		}
	}
}
