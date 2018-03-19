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
	private boolean bL = false, bU = false, bD = false, bR = false;//��Ҽ��̿��Ʋ�������ʼ��Ϊfalse
	int live=4;//��ҵ�ǰ����ֵ����ʼ4
	boolean isalive=true;//����Ƿ���
	int speed=10;//��ҵ�ǰ�ٶȣ���ʼ10
	int maxspeed=20;//����ٶ��������ֵ
	int bombnum=2;//���ͬʱ��Ͷը��������ʼ2
	int maxbombnum=6;//��ҿ�Ͷը�����������ֵ
	int bombexist=0;//�ִ��ը����
	int power=1;//��ҵ�ǰը����������ʼ1
	int maxpower=5;//���ը�������������ֵ
	
	/********************************AI************************************/
	List<box> canGo=new ArrayList<box>();
	List<link> path=new ArrayList<link>();
	boolean isCheck[][]=new boolean[15][12];
	boolean canCalPath=false;
	boolean isMoving=false;
	/**********************************************************************/
	
	
	PlayerAttribute pla;//����������
	
	int x,y;//��ҵ�ǰlocation����
	int oldx,oldy;//�����һ����
	int index=1;//1-һ����ң�2-�������
	String IMGpath;//���ͼƬ���ص�ַ
	//boolean onthebomb=false;//����Ƿ�վ��ը����
	box thisbomb;//���Ŀǰ�����õ�ը��
	
	boolean invincible=false;//�޵�״̬
	
	public enum Direction {
		L, D, U, R, STOP
	}//ö�����ͣ��ֱ��������ƶ��������¡��ϡ��ҡ�ֹͣ
	
	public Player(String path,int x,int y,int index)//��ʼ�������Ϣ
	{
		IMGpath=path;//ͼƬ��ַ
		this.index=index;//��ұ��
		x*=80;
		y*=80;
		this.x=x;
		this.y=y;
		this.oldx=x;
		this.oldy=y;
		this.setBounds(this.x,this.y,80,80);//����������ꡢ��С
		setIcon(path,this);//�������ͼƬ
	}
	
	private Direction dir = Direction.STOP;//����ƶ������������ʼΪSTOP
	
	public void move() {
		this.oldx = x;//������һλ��
		this.oldy = y;//������һλ��
		//�ڲ�����(��ײ��߽硢ը��)������£�ÿ40��s�ƶ�speed�����أ�һ���ƶ�80����(��ͼ��һ�����ӵĿ��)
		switch (dir) {
		case L://�����ƶ�
			int l;
			for(l=0;l<=80;l+=speed)
			{
				this.setLocation(x-l,y);
				if(meetbox())//���������ײ����ƶ�
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
			if(l!=80)//���ѭ��������ƶ�ֵ��Ϊ80�����޸���ǿ���û�ÿ��ֻ���ƶ�һ��ľ���
			{
				l=80;
				this.setLocation(x-l,y);
			}
			break;
		case U://�����ƶ�
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
		case R://�����ƶ�
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
		case D://�����ƶ�
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
		case STOP://ֹͣ
			break;
		}
		//����x,y����ֵ
		this.x=this.getX();
		this.y=this.getY();
	}
	
	boolean meetbox()//�ж�����Ƿ�������ײ��
	{
		if(this.getX()<0||this.getX()>1120||this.getY()<0||this.getY()>880)//����ͼ�߾�
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
					if(!temp.isdestroyshowT)//��������
					{
						return true;
					}
				}
				if(temp.isExistBomb&&!temp.isExistPlayer&&temp.getRect().intersects(this.getRect()))//����ը��
				{
					return true;
				}
			}
		}
		return false;
	}
	
	boolean isbox(int x,int y)//�ж�����Ƿ�������ײ��
	{
		if(x<0||x>=15||y<0||y>=12)//����ͼ�߾�
		{
			return true;
		}
		box temp=GameFrame.thismap.getBoxByXY(x,y);
		if(temp.isExist&&!temp.isdestroyshowT)//��������
		{
			return true;
		}
		if(temp.isExistBomb&&!temp.isExistPlayer)//����ը��
		{
			return true;
		}
		return false;
	}
	
	public void Back()//���������һ����
	{
		//�ص�x,y����ֵ
		x=oldx;
		y=oldy;
		this.setLocation(x,y);//�������location
	}
	
	public void beInjured()//��ұ�ը��(����)
	{
		this.live--;//����ֵ-1
		this.pla.setJL("����ֵ:"+this.live,this.pla.live);//�������������������ֵ
		if(live==0)//�������(����=0)
		{
			this.isalive=false;//�������
			setIcon("images/player"+this.index+"DIED.png",this.pla.photo);//����������ͷ�����
			setIcon("images/player"+this.index+"DIED.png",this);//���ͼƬ����
			JOptionPane.showMessageDialog(this,"���"+this.index+"������","GameOver",2); //��ʾ��Ϣ
			GameFrame.NumofAlive--;//������ֵ��1
			if(GameFrame.NumofAlive==1)//����ܴ����Ϊ1������Ϸ����
			{
				JOptionPane.showMessageDialog(this,"��Ϸ����������ȷ���˳�~","GameOver",2);//��ʾ��Ϣ
				System.exit(0);//��ʾ������
			}
		}
		else
		{
			Thread th=new Thread(){//��������޵е��߳�
				public void run(){
					invincible=true;//�޵�ʱ�俪ʼ
					for(int i=0;i<20;i++)//���2�����˸�޵�ʱ��
					{
						setIcon("images/default.png");//ÿ0.1����˸һ��ѭ����һ����˸20��
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
					setIcon(IMGpath);//�������ͼƬ
					invincible=false;//�޵�ʱ�����
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
			System.out.println("��ʼ�ƶ�");
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
					if(this.thisbomb.isExistPlayer)//�ж�����Ƿ��ڵ�ǰը���ϣ�һ���뿪��ҵ�ǰ���õ�ը�����㲻���ٴ�վ��ȥ��
					{
						ontheboom();
					}
				}
			}
			System.out.println("��ը��");
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
			System.out.println("-�����·��-");
			canCalPath=true;
			throw new StopMsgException();
		}
		else if(isbox(x,y)||isCheck[x][y])
		{
			if(path.size()>=1)
			{
				path.remove(path.size()-1);//������Ľ���Ƴ�
			}
		}
		else
		{
			isCheck[x][y]=true;
			//���ϵݹ�
			if(x-1>=0)
			{
				if(!isCheck[x-1][y])
				{
					path.add(new link(1));
					calPath(x-1,y,targetx,targety);
				}
			}
			//���µݹ�
			if(x+1<15)
			{
				if(!isCheck[x+1][y])
				{
					path.add(new link(2));
					calPath(x+1,y,targetx,targety);
				}
			}
			//����ݹ�
			if(y-1>=0)
			{
				if(!isCheck[x][y-1])
				{
					path.add(new link(3));
					calPath(x,y-1,targetx,targety);
				}
			}
			//���ҵݹ�
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
	
	public void setIcon(String file)//����ͼƬ����Ӧ���ڴ�С�������̣߳�
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(this.getWidth(),this.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  this.setIcon(ico);
	}
	
	public void setIcon(String file,JLabel com)//����ͼƬ����Ӧ���ڴ�С
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
	
	public Rectangle getRect()//��ȡ���label�Ĵ�С��������һ������
	{
		Rectangle rec=new Rectangle();
		rec.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		return rec;
	}

	public void locateDirection() //���巽��
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
	
	public void keyPressed(KeyEvent e) //���̰����¼������ü��̲���
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
	
	public void keyReleased(KeyEvent e) //����̧���¼�����ԭ���̲���
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
	
	public void ontheboom()//����Ҳ��ڵ�ǰը���Ϻ󣬲�������false��
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

