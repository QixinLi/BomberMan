import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Player extends JLabel implements KeyListener{
	private boolean bL = false, bU = false, bD = false, bR = false;//玩家键盘控制参数，初始均为false
	int live=4;//玩家当前生命值，初始4
	boolean isalive=true;//玩家是否存活
	int speed=10;//玩家当前速度，初始10
	int maxspeed=20;//玩家速度限制最大值
	int bombnum=1;//玩家同时可投炸弹数，初始1
	int maxbombnum=6;//玩家可投炸弹数限制最大值
	int bombexist=0;//现存的炸弹数
	int power=1;//玩家当前炸弹威力，初始1
	int maxpower=5;//玩家炸弹威力限制最大值
	
	/********************************AI************************************/
	List<box> canGo=new ArrayList<box>();
	//List<link> path=new ArrayList<link>();
	boolean isCheck[][]=new boolean[15][12];
	boolean canCalPath=false;
	boolean isMoving=false;
	                    /**V2.0**/
	List<Path> path=new ArrayList<Path>();
	List<link> way2Go=new ArrayList<link>();
	boolean isGetTreasure=false;//是否拿到了宝物
	/**********************************************************************/
	
	PlayerAttribute pla;//玩家属性面板
	
	int x,y;//玩家当前location坐标
	int oldx,oldy;//玩家上一坐标
	int index=1;//1-一号玩家，2-二号玩家
	String IMGpath;//玩家图片本地地址
	//boolean onthebomb=false;//玩家是否站在炸弹上
	box thisbomb;//玩家目前所放置的炸弹
	
	boolean invincible=false;//无敌状态
	
	public enum Direction {
		L, D, U, R, STOP
	}//枚举类型，分别代表玩家移动方向：左、下、上、右、停止
	
	public Player(String path,int x,int y,int index)//初始化玩家信息
	{
		IMGpath=path;//图片地址
		this.index=index;//玩家编号
		x*=80;
		y*=80;
		this.x=x;
		this.y=y;
		this.oldx=x;
		this.oldy=y;
		this.setBounds(this.x,this.y,80,80);//设置玩家坐标、大小
		setIcon(path,this);//设置玩家图片
	}
	
	private Direction dir = Direction.STOP;//玩家移动方向参数，初始为STOP
	
	public void move() {
		this.oldx = x;//定义上一位置
		this.oldy = y;//定义上一位置
		//在不遇到(碰撞物、边界、炸弹)的情况下，每40μs移动speed个像素，一共移动80像素(地图上一个格子的宽度)
		switch (dir) {
		case L://向左移动
			int l;
			for(l=0;l<=80;l+=speed)
			{
				this.setLocation(x-l,y);
				if(meetbox())//如果遇到碰撞物，则不移动
				{
					break;
				}
				if(l<80)
				{
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(l!=80)//如果循环后最后移动值不为80，则修复。强制用户每次只能移动一格的距离
			{
				l=80;
				this.setLocation(x-l,y);
			}
			break;
		case U://向上移动
			int u;
			for(u=0;u<=80;u+=speed)
			{
				this.setLocation(x,y-u);
				if(meetbox())
				{
					break;
				}
				if(u<80)
				{
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(u!=80)
			{
				u=80;
				this.setLocation(x,y-u);
			}
			break;
		case R://向右移动
			int r;
			for(r=0;r<=80;r+=speed)
			{
				this.setLocation(x+r,y);
				if(meetbox())
				{
					break;
				}
				if(r<80)
				{
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(r!=80)
			{
				r=80;
				this.setLocation(x+r,y);
			}
			break;
		case D://向下移动
			int d;
			for(d=0;d<=80;d+=speed)
			{
				this.setLocation(x,y+d);
				if(meetbox())
				{
					break;
				}
				if(d<80)
				{
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(d!=80)
			{
				d=80;
				this.setLocation(x,y+d);
			}
			break;
		case STOP://停止
			break;
		}
		//更新x,y坐标值
		this.x=this.getX();
		this.y=this.getY();
	}
	
	boolean meetbox()//判断玩家是否遇到碰撞物
	{
		if(this.getX()<0||this.getX()>1120||this.getY()<0||this.getY()>880)//出地图边境
		{
			return true;
		}
		for(int i=0;i<15;i++)
		{	
			for(int j=0;j<12;j++)
			{
				box temp=GameFrame.thismap.getBoxByXY(i,j);
				if(temp.getRect().intersects(this.getRect())&&temp.isExist)
				{
					if(!temp.isdestroyshowT)//遇到箱子
					{
						return true;
					}
				}
				if(temp.isExistBomb&&!temp.isExistPlayer&&temp.getRect().intersects(this.getRect()))//遇到炸弹
				{
					return true;
				}
			}
		}
		return false;
	}
	
	boolean isbox(int x,int y)//判断玩家是否遇到碰撞物
	{
		if(x<0||x>=15||y<0||y>=12)//出地图边境
		{
			return true;
		}
		box temp=GameFrame.thismap.getBoxByXY(x,y);
		if(temp.isExist&&!temp.isdestroyshowT)//遇到箱子
		{
			return true;
		}
		if(temp.isExistBomb&&!temp.isExistPlayer)//遇到炸弹
		{
			return true;
		}
		return false;
	}
	
	public void Back()//返回玩家上一坐标
	{
		//回调x,y坐标值
		x=oldx;
		y=oldy;
		this.setLocation(x,y);//重设玩家location
	}
	
	public void beInjured()//玩家被炸到(受伤)
	{
		this.live--;//生命值-1
		this.pla.setJL("生命值:"+this.live,this.pla.live);//设置玩家属性面板的生命值
		if(live==0)//如果死了(生命=0)
		{
			this.isalive=false;//玩家死亡
			setIcon("images/player"+this.index+"DIED.png",this.pla.photo);//玩家属性面板头像更换
			setIcon("images/player"+this.index+"DIED.png",this);//玩家图片更换
			JOptionPane.showMessageDialog(this,"玩家"+this.index+"阵亡！","GameOver",2); //提示消息
			GameFrame.NumofAlive--;//总生命值减1
			if(GameFrame.NumofAlive==1)//如果总存活数为1，则游戏结束
			{
				JOptionPane.showMessageDialog(this,"游戏结束！单机确认退出~","GameOver",2);//提示消息
				System.exit(0);//显示主界面
			}
		}
		else
		{
			Thread th=new Thread(){//定义玩家无敌的线程
				public void run(){
					invincible=true;//无敌时间开始
					for(int i=0;i<20;i++)//获得2秒的闪烁无敌时间
					{
						setIcon("images/default.png");//每0.1秒闪烁一个循环，一共闪烁20次
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setIcon(IMGpath);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					setIcon(IMGpath);//设置玩家图片
					invincible=false;//无敌时间结束
				}
			};
			th.start();
		}
	}

	public void AIgo() throws Exception
	{
		int n=999;
		do
		{
			if((n == 999 || ( this.x/80 == path.get(n).x && this.y/80 == path.get(n).y )))              //已经到达目的地
			{
				Thread.sleep(10);
				if(n!=999&&!isGetTreasure) {
					DropBomb();
				}
				isGetTreasure=false;
				findBlock(this.x/80,this.y/80);//找到所有能到达的格子，并描绘路径
				n=getOnePoint();//取一安全的点
				drawPath(n);//描绘路径
				move1Step();//移动一步
			}
			else if(!GameFrame.thismap.getBoxByXY(path.get(n).x,path.get(n).y).isDangerArea)    //点安全
			{
				Path ptemp = path.get(n);
				findBlock(this.x/80,this.y/80);//找到所有能到达的格子，并描绘路径
				//System.out.println(n+"."+path.get(n).x+","+path.get(n).y+"..."+this.x/80+","+this.y/80);
				int i;
				for(i=0;i<path.size();i++) //重新寻路
				{
					if(path.get(i).x==ptemp.x && path.get(i).y==ptemp.y) {
						n=i;
						break;
					}
				}
				if(i>=path.size()) //点不可达
				{
					System.out.print("点不可达");
					n=getOnePoint();
					drawPath(n);//取一安全的点并描绘路径
					move1Step();//移动一步
				}
				else  								//点可达
				{
					System.out.print("点可达");
					drawPath(n);//取一安全的点并描绘路径
					move1Step();//移动一步
				}
			}
			else                                                                                 //点不安全
			{
				System.out.print("点不安全");
				findBlock(this.x/80,this.y/80);//找到所有能到达的格子，并描绘路径
				n=getOnePoint();
				drawPath(n);//取一安全的点并描绘路径
				move1Step();//移动一步
			}
		}while(this.live>0);
	}
	
	public void DropBomb()
	{
		new Bomb(this);
		//System.out.println("丢炸弹");
	}
	
	public void findBlock(int x,int y) {
		boolean isSet=true;
		path.clear();
		List<Path> temp=new ArrayList<Path>();
		
		for(int u=0;u<15;u++)
			for(int t=0;t<12;t++)
				isCheck[u][t]=false;
		
		isCheck[x][y]=true;
		Path firstpath=new Path(x,y);
		firstpath.beforePath=new Path(999,999);
		path.add(firstpath);
		int i=0;
		while(isSet) 
		{
			temp.clear();
			isSet=false;
			for(;i<path.size();i++) {
				int nowX=path.get(i).x;
				int nowY=path.get(i).y;
				//上
				if(nowX>=0 && nowX<15 && (nowY-1)>=0 && (nowY-1)<12) {
					if(!(GameFrame.thismap.getBoxByXY(nowX,nowY-1).isExist&&!GameFrame.thismap.getBoxByXY(nowX,nowY-1).isdestroyshowT)
							&&!GameFrame.thismap.getBoxByXY(nowX,nowY-1).isExistBomb
							&&!isCheck[nowX][nowY-1]) {
						Path temppath = new Path(nowX,nowY-1);
						temppath.beforePath = path.get(i);
						isCheck[nowX][nowY-1] = true;
						temp.add(temppath);
						//System.out.println("add:"+(nowX)+","+(nowY-1));
						isSet=true;
					}
				}
				//下
				if(nowX>=0 && nowX<15 && (nowY+1)>=0 && (nowY+1)<12) {
					if(!(GameFrame.thismap.getBoxByXY(nowX,nowY+1).isExist&&!GameFrame.thismap.getBoxByXY(nowX,nowY+1).isdestroyshowT)
							&&!GameFrame.thismap.getBoxByXY(nowX,nowY+1).isExistBomb
							&&!isCheck[nowX][nowY+1]) {
						Path temppath = new Path(nowX,nowY+1);
						temppath.beforePath = path.get(i);
						isCheck[nowX][nowY+1] = true;
						temp.add(temppath);
						isSet=true;
						//System.out.println("add:"+(nowX)+","+(nowY+1));
					}
				}
				//左
				if((nowX-1)>=0 && (nowX-1)<15 && nowY>=0 && nowY<12) {
					if(!(GameFrame.thismap.getBoxByXY(nowX-1,nowY).isExist&&!GameFrame.thismap.getBoxByXY(nowX-1,nowY).isdestroyshowT)
							&&!GameFrame.thismap.getBoxByXY(nowX-1,nowY).isExistBomb
							&&!isCheck[nowX-1][nowY]) {
						Path temppath = new Path(nowX-1,nowY);
						temppath.beforePath = path.get(i);
						isCheck[nowX-1][nowY] = true;
						temp.add(temppath);
						isSet=true;
						//System.out.println("add:"+(nowX-1)+","+(nowY));
					}
				}
				//右
				if((nowX+1)>=0 && (nowX+1)<15 && nowY>=0 && nowY<12) {
					if(!(GameFrame.thismap.getBoxByXY(nowX+1,nowY).isExist&&!GameFrame.thismap.getBoxByXY(nowX+1,nowY).isdestroyshowT)
							&&!GameFrame.thismap.getBoxByXY(nowX+1,nowY).isExistBomb
							&&!isCheck[nowX+1][nowY]) {
						Path temppath = new Path(nowX+1,nowY);
						temppath.beforePath = path.get(i);
						isCheck[nowX+1][nowY] = true;
						temp.add(temppath);
						isSet=true;
						//System.out.println("add:"+(nowX+1)+","+(nowY));
					}
				}
			}
			path.addAll(temp);
			temp.clear();
		}
	}
	
	public int getOnePoint() {
		Random ra=new Random();
		
		for(int i=1;i<path.size();) {
			if(GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y).isDangerArea) {
				path.remove(i);
			}
			else {
				i++;
			}
		}
		int n=0;
		if(path.size()==1) {
			return n;
		}
		else {
			boolean isTreasure=false,isPlayer=false,isBox=false;
			/*******************取宝藏点********************/
			for(int i=1;i<path.size();i++) {
				if(GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y).isdestroyshowT&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y).isExist) {
					n=i;
					isTreasure=true;
					isGetTreasure=true;
					break;
				}
			}
			/*******************取玩家点********************/
			if(!isTreasure) {
				for(int i=1;i<path.size();i++) {
					if((path.get(i).x) == (GameFrame.player1.getX()/80) && path.get(i).y == (GameFrame.player1.getY()/80)) {
						n=i;
						isPlayer=true;
						break;
					}
				}
			}
			/*******************取箱子**********************/
			
			if(!isTreasure&&!isPlayer) {
				List<Path> boxpath=new ArrayList<Path>();
				for(int i=0;i<path.size();i++) {
					if(path.get(i).x-1>=0) {
						if(!GameFrame.thismap.getBoxByXY(path.get(i).x-1,path.get(i).y).isdestroyshowT
								&&GameFrame.thismap.getBoxByXY(path.get(i).x-1,path.get(i).y).isExist
								&&GameFrame.thismap.getBoxByXY(path.get(i).x-1,path.get(i).y).candestroy) {
							n=i;
							boxpath.add(path.get(i));
						}
					}
					if(path.get(i).x+1<15) {
						if(!GameFrame.thismap.getBoxByXY(path.get(i).x+1,path.get(i).y).isdestroyshowT
								&&GameFrame.thismap.getBoxByXY(path.get(i).x+1,path.get(i).y).isExist
								&&GameFrame.thismap.getBoxByXY(path.get(i).x+1,path.get(i).y).candestroy) {
							n=i;
							boxpath.add(path.get(i));
						}
					}
					if(path.get(i).y-1>=0) {
						if(!GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y-1).isdestroyshowT
								&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y-1).isExist
								&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y-1).candestroy) {
							n=i;
							boxpath.add(path.get(i));
						}
					}
					if(path.get(i).y+1<12) {
						if(!GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y+1).isdestroyshowT
								&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y+1).isExist
								&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y+1).candestroy) {
							n=i;
							boxpath.add(path.get(i));
						}
					}
				}
				if(!boxpath.isEmpty()) {
					isBox=true;
					Random rad=new Random();
					n=rad.nextInt(boxpath.size());
					for(int j=0;j<path.size();j++) {
						if(path.get(j).x==boxpath.get(n).x&&path.get(j).y==boxpath.get(n).y) {
							n=j;
							break;
						}
					}
				}
			}
			/*******************随机取点********************/
			if(!isTreasure&&!isPlayer&&!isBox) {
				do {
					n=ra.nextInt(path.size());
				}while(n==0);
			}
		}
		return n;
	}
	
	public void drawPath(int n) throws Exception {
		way2Go.clear();
		SqStack sqStack=new SqStack(100);
		for(Path p=path.get(n);;p=p.beforePath) {
			sqStack.push(p);
			if(p.beforePath.x==999&&p.beforePath.y==999) {
				break;
			}
		}
		Path p1=sqStack.pop();
		Path p2;
		int length=sqStack.length();
		for(int i=0;i<length;i++) {
			p2=sqStack.pop();
			if(p2.x==p1.x) {
				if(p2.y+1==p1.y) //上
				{
					way2Go.add(new link(1));
				}
				else if(p2.y-1==p1.y) //下
				{
					way2Go.add(new link(2));
				}
			}
			else if(p2.y==p1.y) {
				if(p2.x+1==p1.x) //左
				{
					way2Go.add(new link(3));
				}
				else if(p2.x-1==p1.x) //右
				{
					way2Go.add(new link(4));
				}
			}
			p1=p2;
		}
		if(GameFrame.thismap.getBoxByXY(p1.x,p1.y).isDangerArea) 
		{
			way2Go.clear();
		}
	}
	
	public void move1Step() {
		boolean isDangerAround=true;
		if(way2Go.size()==0) {
			//System.out.println("不知道要干啥");
			return;
		}
		else {
			switch(way2Go.get(0).dirindex)
			{
			case 1:
				dir=Direction.U;
				if(this.y/80-1>=0) {
					if(!GameFrame.thismap.getBoxByXY(this.x/80,this.y/80-1).isDangerArea) {
						isDangerAround=false;
					}
				}
				break;
			case 2:
				dir=Direction.D;
				if(this.y/80+1<12) {
					if(!GameFrame.thismap.getBoxByXY(this.x/80,this.y/80+1).isDangerArea) {
						isDangerAround=false;
					}
				}
				break;
			case 3:
				dir=Direction.L;
				if(this.x/80-1>=0) {
					if(!GameFrame.thismap.getBoxByXY(this.x/80-1,this.y/80).isDangerArea) {
						isDangerAround=false;
					}
				}
				break;
			case 4:
				dir=Direction.R;
				if(this.x/80+1<15) {
					if(!GameFrame.thismap.getBoxByXY(this.x/80+1,this.y/80).isDangerArea) {
						isDangerAround=false;
					}
				}
				break;
			default:
				break;
			}
			if(isDangerAround) {
				System.out.print("前方有危险");
				if( this.y/80-1>=0 && !GameFrame.thismap.getBoxByXY(this.x/80,this.y/80-1).isDangerArea) {
					dir=Direction.U;
				}
				else if( this.y/80+1<12 && !GameFrame.thismap.getBoxByXY(this.x/80,this.y/80+1).isDangerArea) {
					dir=Direction.D;
				}
				else if( this.x/80-1>=0 && !GameFrame.thismap.getBoxByXY(this.x/80-1,this.y/80).isDangerArea) {
					dir=Direction.L;
				}
				else if( this.x/80+1<15 && !GameFrame.thismap.getBoxByXY(this.x/80+1,this.y/80).isDangerArea) {
					dir=Direction.R;
				}
			}
			System.out.println(dir);
			move();
			GameFrame.meetbox(this);
			if(this.thisbomb!=null)
			{
				if(this.thisbomb.isExistPlayer)//判断玩家是否在当前炸弹上（一旦离开玩家当前放置的炸弹，便不可再次站上去）
				{
					ontheboom();
				}
			}
		}
	}

	public void setIcon(String file)//设置图片自适应窗口大小（用于线程）
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(this.getWidth(),this.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  this.setIcon(ico);
	}
	
	public void setIcon(String file,JLabel com)//设置图片自适应窗口大小
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
	
	public Rectangle getRect()//获取玩家label的大小，并返回一个矩形
	{
		Rectangle rec=new Rectangle();
		rec.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		return rec;
	}

	public void locateDirection() //定义方向
	{
		if (bL)
			dir = Direction.L;
		else if (bU)
			dir = Direction.U;
		else if (bD)
			dir = Direction.D;
		else if (bR)
			dir = Direction.R;
		else
			dir = Direction.STOP;
	}
	
	public void keyPressed(KeyEvent e) //键盘按下事件，设置键盘参数
	{
		int key = e.getKeyCode();
		if (index == 2) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_LEFT:
				bL = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break;
			}
		} 
		else if (index == 1) {
			switch (key) {
			case KeyEvent.VK_D:
				bR = true;
				break;
			case KeyEvent.VK_A:
				bL = true;
				break;
			case KeyEvent.VK_W:
				bU = true;
				break;
			case KeyEvent.VK_S:
				bD = true;
				break;
			}
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) //键盘抬起事件，还原键盘参数
	{
		int key = e.getKeyCode();
		if (index == 2) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
			case KeyEvent.VK_L:
				new Bomb(this);
				break;

			}
		} else if (index == 1) {
			switch (key) {
			case KeyEvent.VK_D:
				bR = false;
				break;
			case KeyEvent.VK_A:
				bL = false;
				break;
			case KeyEvent.VK_W:
				bU = false;
				break;
			case KeyEvent.VK_S:
				bD = false;
				break;
			case KeyEvent.VK_V:
				new Bomb(this);
				break;
			}
		}
		locateDirection();
	}
	
	public void ontheboom()//当玩家不在当前炸弹上后，参数设置false。
	{
		if(this.thisbomb!=null){
			if(!this.thisbomb.getRect().intersects(GameFrame.player1.getRect())&&!this.thisbomb.getRect().intersects(GameFrame.player2.getRect()))
			{
				this.thisbomb.isExistPlayer=false;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	static class StopMsgException extends RuntimeException {  
	}
}

