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
	int bombnum=2;//玩家同时可投炸弹数，初始2
	int maxbombnum=6;//玩家可投炸弹数限制最大值
	int bombexist=0;//现存的炸弹数
	int power=1;//玩家当前炸弹威力，初始1
	int maxpower=5;//玩家炸弹威力限制最大值
	
	/********************************AI************************************/
	List<box> canGo=new ArrayList<box>();
	List<link> path=new ArrayList<link>();
	boolean isCheck[][]=new boolean[15][12];
	boolean canCalPath=false;
	boolean isMoving=false;
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

	public void AIgo()
	{
		isMoving=true;
		do
		{
			calCanGo();
		}while(!canCalPath);
		if(path.size()!=0)
		{
			System.out.println("开始移动");
			for(int i=0;i<path.size();i++)
			{
				switch(path.get(i).dirindex)
				{
				case 1:
					dir=Direction.U;
					break;
				case 2:
					dir=Direction.D;
					break;
				case 3:
					dir=Direction.L;
					break;
				case 4:
					dir=Direction.R;
					break;
				default:
					break;
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
			System.out.println("丢炸弹");
		}
		isMoving=false;
	}
	
	public void DropBomb()
	{
		new Bomb(this);
	}
	
	public void calCanGo2()
	{
		boolean isget=false;
		Random ra=new Random();
		int i,j;
		do{
			do{
				i=ra.nextInt(15);
				j=ra.nextInt(12);
			}while(((this.x/80)==i&&(this.y/80)==j)
						||GameFrame.thismap.getBoxByXY(i,j).isExist
							||GameFrame.thismap.getBoxByXY(i,j).isBoomArea
							||GameFrame.thismap.getBoxByXY(i,j).isExistBomb);
			int x=this.x/80;
			int y=this.y/80;
			for(int u=0;u<15;u++)
				for(int t=0;t<12;t++)
					isCheck[u][t]=false;
			path.clear();
			canCalPath=false;
			try {  
				calPath(x,y,i,j);
	        } catch (StopMsgException e) {  
	        }
			if(canCalPath)
			{
				isget=true;
			}
		}while(!isget);
	}
	
	public void calCanGo()
	{
		boolean isget=false;
		for(int j=0;j<12&&!isget;j++)
		{
			for(int i=0;i<15&&!isget;i++)
			{
				/*System.out.print(!GameFrame.thismap.getBoxByXY(i,j).isExist&&!GameFrame.thismap.getBoxByXY(i,j).isBoomArea&&!GameFrame.thismap.getBoxByXY(i,j).isExistBomb);
				System.out.print(" ");*/
				if((this.x/80)!=i||(this.y/80)!=j)
				{	
					if(!GameFrame.thismap.getBoxByXY(i,j).isExist
							&&!GameFrame.thismap.getBoxByXY(i,j).isBoomArea
							&&!GameFrame.thismap.getBoxByXY(i,j).isExistBomb)
					{
						int x=this.x/80;
						int y=this.y/80;
						for(int u=0;u<15;u++)
							for(int t=0;t<12;t++)
								isCheck[u][t]=false;
						path.clear();
						canCalPath=false;
						try {  
							calPath(x,y,i,j);
				        } catch (StopMsgException e) {  
				        }
						if(canCalPath)
						{
							isget=true;
						}
					}
				}
				
			}
			//System.out.println();
		}
		//System.out.println();
	}
	
	
	private void calPath(int x,int y,int targetx,int targety)
	{
		if(x==targetx&&y==targety)
		{
			System.out.println("-计算出路径-");
			canCalPath=true;
			throw new StopMsgException();
		}
		else if(isbox(x,y)||isCheck[x][y])
		{
			if(path.size()>=1)
			{
				path.remove(path.size()-1);//将顶层的结点移除
			}
		}
		else
		{
			isCheck[x][y]=true;
			//向上递归
			if(x-1>=0)
			{
				if(!isCheck[x-1][y])
				{
					path.add(new link(1));
					calPath(x-1,y,targetx,targety);
				}
			}
			//向下递归
			if(x+1<15)
			{
				if(!isCheck[x+1][y])
				{
					path.add(new link(2));
					calPath(x+1,y,targetx,targety);
				}
			}
			//向左递归
			if(y-1>=0)
			{
				if(!isCheck[x][y-1])
				{
					path.add(new link(3));
					calPath(x,y-1,targetx,targety);
				}
			}
			//向右递归
			if(y+1<12)
			{
				if(!isCheck[x][y+1])
				{
					path.add(new link(4));
					calPath(x,y+1,targetx,targety);
				}
			}
			isCheck[x][y]=false;
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
			if(!this.thisbomb.getRect().intersects(this.getRect()))
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

