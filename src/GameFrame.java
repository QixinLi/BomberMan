
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame extends JFrame implements KeyListener{
	static JPanel jp=new JPanel();//主面板
	JPanel map=new JPanel();//地图
	JPanel bg=new JPanel();//背景
	JPanel attribute=new JPanel();//属性总面板
	JPanel pa=new JPanel();//玩家属性面板
	
	public static Player player1=new Player("images/player1.gif",0,0,1);//玩家一(静态全局变量)
	public static Player player2=new Player("images/player2.gif",14,11,2);//玩家二(静态全局变量)
	
	public static int NumofAlive=2;//目前存活的玩家数量
	
	public static Map thismap;//当前地图
	
	/*public static List<Bomb> mybomb=new ArrayList<Bomb>();*/
	
	public PlayerAttribute plA1;//玩家一属性面板
	public PlayerAttribute plA2;//玩家二属性面板
	
	public GameFrame()
	{
		jp.setLayout(null);//取消默认布局
		
		map.setBounds(20,20,1200,960);//设置地图面板大小、位置
		map.setLayout(null);//取消默认布局
		map.setOpaque(false);//设置面板背景为透明
		
		thismap=new Map(1);//生成地图（Demo版为1号地图）
		thismap.setOpaque(false);//设置面板背景为透明
		thismap.add(player1);//地图中加入玩家一
		thismap.add(player2);//地图中加入玩家二
		thismap.setMap();//加载地图
		
		map.add(thismap);//将加载好的地图加入map面板
		
		jp.add(map);//将map面板加入主面板
		
		setBackGround();//设置地图中的背景
		
		jp.add(bg);//将背景加入地图
		
		attribute.setBounds(1240,20,240,960);//设置玩家属性面板大小、位置
		attribute.setOpaque(false);//设置面板背景为透明
		attribute.setLayout(null);//取消默认布局
		
		pa.setBounds(45,50,150,860);//pa面板用来放置玩家的详细属性面板
		pa.setOpaque(false);//设置面板背景为透明
		pa.setLayout(new GridLayout(4,1));//设置面板布局为4:1的GridLayout
		
		plA1=new PlayerAttribute(player1);//初始化玩家一的属性
		pa.add(plA1);//将玩家一的属性添加至pa面板
		plA2=new PlayerAttribute(player2);//初始化玩家二的属性
		pa.add(plA2);//将玩家二的属性添加至pa面板
		
		attribute.add(pa);//将pa面板添加至属性面板
		
		JLabel attrbg=new JLabel();//定义属性面板的背景标签
		attrbg.setBounds(0,0,240,960);//设置背景标签的大小、位置
		setIcon("images/attrbg.png",attrbg);//添加背景图片
		attribute.add(attrbg);//将背景插入属性面板
		jp.add(attribute);//将属性面板添加进总面板
		
		this.addKeyListener(this);//给该窗口添加键盘监听器
		
		 //设置窗口（大小、背景等）
        ImageIcon background = new ImageIcon("images/gamebg.jpg");  
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int windowsWedth = 1500;
		int windowsHeight= 1000;
		this.setBounds((width - windowsWedth) / 2,(height - windowsHeight) / 2-25, windowsWedth, windowsHeight);
		setSize(windowsWedth,windowsHeight);
		Image temp=background.getImage().getScaledInstance(this.getWidth(),this.getHeight(),background.getImage().SCALE_DEFAULT);  
		background=new ImageIcon(temp);  
		JLabel label = new JLabel(background); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		label.setBounds(0, 0, this.getWidth(), this.getHeight());  
		jp.add(label);
	    this.add(jp);
	    setVisible(true);
	    new Thread(new PaintThread1()).start();//键盘监听线程启动
	    new Thread(new PaintThread2()).start();//键盘监听线程启动
	}
	
	public GameFrame(int i)//单人游戏
	{
		jp.setLayout(null);//取消默认布局
		
		map.setBounds(20,20,1200,960);//设置地图面板大小、位置
		map.setLayout(null);//取消默认布局
		map.setOpaque(false);//设置面板背景为透明
		
		thismap=new Map(1);//生成地图（Demo版为1号地图）
		thismap.setOpaque(false);//设置面板背景为透明
		thismap.add(player1);//地图中加入玩家一
		thismap.add(player2);//地图中加入玩家二
		thismap.setMap();//加载地图
		
		map.add(thismap);//将加载好的地图加入map面板
		
		jp.add(map);//将map面板加入主面板
		
		setBackGround();//设置地图中的背景
		
		jp.add(bg);//将背景加入地图
		
		attribute.setBounds(1240,20,240,960);//设置玩家属性面板大小、位置
		attribute.setOpaque(false);//设置面板背景为透明
		attribute.setLayout(null);//取消默认布局
		
		pa.setBounds(45,50,150,860);//pa面板用来放置玩家的详细属性面板
		pa.setOpaque(false);//设置面板背景为透明
		pa.setLayout(new GridLayout(4,1));//设置面板布局为4:1的GridLayout
		
		plA1=new PlayerAttribute(player1);//初始化玩家一的属性
		pa.add(plA1);//将玩家一的属性添加至pa面板
		plA2=new PlayerAttribute(player2);//初始化玩家二的属性
		pa.add(plA2);//将玩家二的属性添加至pa面板
		
		attribute.add(pa);//将pa面板添加至属性面板
		
		JLabel attrbg=new JLabel();//定义属性面板的背景标签
		attrbg.setBounds(0,0,240,960);//设置背景标签的大小、位置
		setIcon("images/attrbg.png",attrbg);//添加背景图片
		attribute.add(attrbg);//将背景插入属性面板
		jp.add(attribute);//将属性面板添加进总面板
		
		this.addKeyListener(this);//给该窗口添加键盘监听器
		
		 //设置窗口（大小、背景等）
        ImageIcon background = new ImageIcon("images/gamebg.jpg");  
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int windowsWedth = 1500;
		int windowsHeight= 1000;
		this.setBounds((width - windowsWedth) / 2,(height - windowsHeight) / 2-25, windowsWedth, windowsHeight);
		setSize(windowsWedth,windowsHeight);
		Image temp=background.getImage().getScaledInstance(this.getWidth(),this.getHeight(),background.getImage().SCALE_DEFAULT);  
		background=new ImageIcon(temp);  
		JLabel label = new JLabel(background); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		label.setBounds(0, 0, this.getWidth(), this.getHeight());  
		jp.add(label);
	    this.add(jp);
	    setVisible(true);
	    //new Thread(new PaintThread1()).start();//键盘监听线程启动
	    // new Thread(new PaintThread2()).start();//键盘监听线程启动
	    new Thread(new PaintThreadAI2()).start();
	}
	
	static void meetbox(Player pl)
	{
		if(pl.getX()<0||pl.getX()>1120||pl.getY()<0||pl.getY()>880)//出地图边境
		{
			pl.Back();
			return;
		}
		for(int i=0;i<15;i++)
			for(int j=0;j<12;j++)
			{
				box temp=thismap.getBoxByXY(i,j);
				if(temp.getRect().intersects(pl.getRect())&&temp.isExist)
				{
					if(!temp.isdestroyshowT)//遇到箱子
					{
						pl.Back();
						return;
					}
					else{//遇到宝物
						switch(temp.TreasureIndex){
						case 1:
							pl.pla.plusspeed();//吃掉加速
							break;
						case 2:
							pl.pla.plusbombnum();//吃掉加泡泡
							break;
						case 3:
							pl.pla.pluspower();//吃掉加能量
							break;
						}
						temp.isExist=false;//设置当前方格为[不存在]
						setIcon("images/default.png",temp);//将当前方格设置为隐藏
					}
				}
				
				
				if(temp.isExistBomb&&!temp.isExistPlayer&&temp.getRect().intersects(pl.getRect()))//遇到炸弹
				{
					pl.Back();
					return;
				}
			}

	}
	
	public void setBackGround()//设置地图的背景
	{
		bg.setLayout(null);//取消默认布局
		bg.setBounds(20,20,1200,960);//设置面板大小、位置
		bg.setOpaque(false);//设置面板背景为透明
		for(int i=0;i<15;i++)//设置背景
		{
			for(int j=0;j<12;j++)
			{
				JLabel temp=new JLabel();//声明临时的label变量
				temp.setBounds(i*80,j*80,80,80);//设置当前临时图片的大小、位置
				if((i+j)%2==0)
				{
					setIcon("images/草地1.jpg",temp);//偶数格设置草地1图片
				}
				else{
					setIcon("images/草地2.jpg",temp);//奇数格设置草地2图片
				}
				bg.add(temp);//添加图片
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {//键盘按下事件
		// TODO Auto-generated method stub
		player1.keyPressed(e);
		player2.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {//键盘弹起事件
		// TODO Auto-generated method stub
		player1.keyReleased(e);
		player2.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class PaintThread1 implements Runnable {
		public void run() {
			while (true) {//设置一个死循环，保持程序对键盘事件的监听
				if(player1.isalive){//在玩家一存活的情况下才能移动
					player1.move();//移动玩家一
					meetbox(player1);//在遇到碰撞物的情况下返回
					if(player1.thisbomb!=null)
					{
						if(player1.thisbomb.isExistPlayer)//判断玩家是否在当前炸弹上（一旦离开玩家当前放置的炸弹，便不可再次站上去）
						{
							player1.ontheboom();
						}
					}
				}
				/*try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
	private class PaintThread2 implements Runnable {
		public void run() {
			while (true) {//设置一个死循环，保持程序对键盘事件的监听
				if(player2.isalive){//在玩家二存活的情况下才能移动
					player2.move();//移动玩家二
					meetbox(player2);//在遇到碰撞物的情况下返回
					if(player2.thisbomb!=null)
					{
						if(player2.thisbomb.isExistPlayer)//判断玩家是否在当前炸弹上（一旦离开玩家当前放置的炸弹，便不可再次站上去）
						{
							player2.ontheboom();
						}
					}
				}
				/*try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
	
	private class PaintThreadAI2 implements Runnable {
		public void run() {
			while (player2.isalive) {//设置一个死循环，保持程序对键盘事件的监听
				//meetbox(player2);//在遇到碰撞物的情况下返回
				if(player2.thisbomb!=null)
				{
					if(player2.thisbomb.isExistPlayer)//判断玩家是否在当前炸弹上（一旦离开玩家当前放置的炸弹，便不可再次站上去）
					{
						player2.ontheboom();
					}
				}
				if(!player2.isMoving)
				{
					
					player2.isMoving=true;
					player2.AIgo();
					player2.DropBomb();
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void setIcon(String file,JLabel com)//令图片自适应label标签大小
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}

}