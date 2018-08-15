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
	int bombnum=1;//���ͬʱ��Ͷը��������ʼ1
	int maxbombnum=6;//��ҿ�Ͷը�����������ֵ
	int bombexist=0;//�ִ��ը����
	int power=1;//��ҵ�ǰը����������ʼ1
	int maxpower=5;//���ը�������������ֵ
	
	/********************************AI************************************/
	List<box> canGo=new ArrayList<box>();
	//List<link> path=new ArrayList<link>();
	boolean isCheck[][]=new boolean[15][12];
	boolean canCalPath=false;
	boolean isMoving=false;
	                    /**V2.0**/
	List<Path> path=new ArrayList<Path>();
	List<link> way2Go=new ArrayList<link>();
	boolean isGetTreasure=false;//�Ƿ��õ��˱���
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

	public void AIgo() throws Exception
	{
		int n=999;
		do
		{
			if((n == 999 || ( this.x/80 == path.get(n).x && this.y/80 == path.get(n).y )))              //�Ѿ�����Ŀ�ĵ�
			{
				Thread.sleep(10);
				if(n!=999&&!isGetTreasure) {
					DropBomb();
				}
				isGetTreasure=false;
				findBlock(this.x/80,this.y/80);//�ҵ������ܵ���ĸ��ӣ������·��
				n=getOnePoint();//ȡһ��ȫ�ĵ�
				drawPath(n);//���·��
				move1Step();//�ƶ�һ��
			}
			else if(!GameFrame.thismap.getBoxByXY(path.get(n).x,path.get(n).y).isDangerArea)    //�㰲ȫ
			{
				Path ptemp = path.get(n);
				findBlock(this.x/80,this.y/80);//�ҵ������ܵ���ĸ��ӣ������·��
				//System.out.println(n+"."+path.get(n).x+","+path.get(n).y+"..."+this.x/80+","+this.y/80);
				int i;
				for(i=0;i<path.size();i++) //����Ѱ·
				{
					if(path.get(i).x==ptemp.x && path.get(i).y==ptemp.y) {
						n=i;
						break;
					}
				}
				if(i>=path.size()) //�㲻�ɴ�
				{
					System.out.print("�㲻�ɴ�");
					n=getOnePoint();
					drawPath(n);//ȡһ��ȫ�ĵ㲢���·��
					move1Step();//�ƶ�һ��
				}
				else  								//��ɴ�
				{
					System.out.print("��ɴ�");
					drawPath(n);//ȡһ��ȫ�ĵ㲢���·��
					move1Step();//�ƶ�һ��
				}
			}
			else                                                                                 //�㲻��ȫ
			{
				System.out.print("�㲻��ȫ");
				findBlock(this.x/80,this.y/80);//�ҵ������ܵ���ĸ��ӣ������·��
				n=getOnePoint();
				drawPath(n);//ȡһ��ȫ�ĵ㲢���·��
				move1Step();//�ƶ�һ��
			}
		}while(this.live>0);
	}
	
	public void DropBomb()
	{
		new Bomb(this);
		//System.out.println("��ը��");
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
				//��
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
				//��
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
				//��
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
				//��
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
			/*******************ȡ���ص�********************/
			for(int i=1;i<path.size();i++) {
				if(GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y).isdestroyshowT&&GameFrame.thismap.getBoxByXY(path.get(i).x,path.get(i).y).isExist) {
					n=i;
					isTreasure=true;
					isGetTreasure=true;
					break;
				}
			}
			/*******************ȡ��ҵ�********************/
			if(!isTreasure) {
				for(int i=1;i<path.size();i++) {
					if((path.get(i).x) == (GameFrame.player1.getX()/80) && path.get(i).y == (GameFrame.player1.getY()/80)) {
						n=i;
						isPlayer=true;
						break;
					}
				}
			}
			/*******************ȡ����**********************/
			
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
			/*******************���ȡ��********************/
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
				if(p2.y+1==p1.y) //��
				{
					way2Go.add(new link(1));
				}
				else if(p2.y-1==p1.y) //��
				{
					way2Go.add(new link(2));
				}
			}
			else if(p2.y==p1.y) {
				if(p2.x+1==p1.x) //��
				{
					way2Go.add(new link(3));
				}
				else if(p2.x-1==p1.x) //��
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
			//System.out.println("��֪��Ҫ��ɶ");
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
				System.out.print("ǰ����Σ��");
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
				if(this.thisbomb.isExistPlayer)//�ж�����Ƿ��ڵ�ǰը���ϣ�һ���뿪��ҵ�ǰ���õ�ը�����㲻���ٴ�վ��ȥ��
				{
					ontheboom();
				}
			}
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

