
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
	static JPanel jp=new JPanel();//�����
	JPanel map=new JPanel();//��ͼ
	JPanel bg=new JPanel();//����
	JPanel attribute=new JPanel();//���������
	JPanel pa=new JPanel();//����������
	
	public static Player player1=new Player("images/player1.gif",0,0,1);//���һ(��̬ȫ�ֱ���)
	public static Player player2=new Player("images/player2.gif",14,11,2);//��Ҷ�(��̬ȫ�ֱ���)
	
	public static int NumofAlive=2;//Ŀǰ�����������
	
	public static Map thismap;//��ǰ��ͼ
	
	/*public static List<Bomb> mybomb=new ArrayList<Bomb>();*/
	
	public PlayerAttribute plA1;//���һ�������
	public PlayerAttribute plA2;//��Ҷ��������
	
	public GameFrame()
	{
		jp.setLayout(null);//ȡ��Ĭ�ϲ���
		
		map.setBounds(20,20,1200,960);//���õ�ͼ����С��λ��
		map.setLayout(null);//ȡ��Ĭ�ϲ���
		map.setOpaque(false);//������屳��Ϊ͸��
		
		thismap=new Map(1);//���ɵ�ͼ��Demo��Ϊ1�ŵ�ͼ��
		thismap.setOpaque(false);//������屳��Ϊ͸��
		thismap.add(player1);//��ͼ�м������һ
		thismap.add(player2);//��ͼ�м�����Ҷ�
		thismap.setMap();//���ص�ͼ
		
		map.add(thismap);//�����غõĵ�ͼ����map���
		
		jp.add(map);//��map�����������
		
		setBackGround();//���õ�ͼ�еı���
		
		jp.add(bg);//�����������ͼ
		
		attribute.setBounds(1240,20,240,960);//���������������С��λ��
		attribute.setOpaque(false);//������屳��Ϊ͸��
		attribute.setLayout(null);//ȡ��Ĭ�ϲ���
		
		pa.setBounds(45,50,150,860);//pa�������������ҵ���ϸ�������
		pa.setOpaque(false);//������屳��Ϊ͸��
		pa.setLayout(new GridLayout(4,1));//������岼��Ϊ4:1��GridLayout
		
		plA1=new PlayerAttribute(player1);//��ʼ�����һ������
		pa.add(plA1);//�����һ�����������pa���
		plA2=new PlayerAttribute(player2);//��ʼ����Ҷ�������
		pa.add(plA2);//����Ҷ������������pa���
		
		attribute.add(pa);//��pa���������������
		
		JLabel attrbg=new JLabel();//�����������ı�����ǩ
		attrbg.setBounds(0,0,240,960);//���ñ�����ǩ�Ĵ�С��λ��
		setIcon("images/attrbg.png",attrbg);//��ӱ���ͼƬ
		attribute.add(attrbg);//�����������������
		jp.add(attribute);//�����������ӽ������
		
		this.addKeyListener(this);//���ô�����Ӽ��̼�����
		
		 //���ô��ڣ���С�������ȣ�
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
	    new Thread(new PaintThread1()).start();//���̼����߳�����
	    new Thread(new PaintThread2()).start();//���̼����߳�����
	}
	
	public GameFrame(int i)//������Ϸ
	{
		jp.setLayout(null);//ȡ��Ĭ�ϲ���
		
		map.setBounds(20,20,1200,960);//���õ�ͼ����С��λ��
		map.setLayout(null);//ȡ��Ĭ�ϲ���
		map.setOpaque(false);//������屳��Ϊ͸��
		
		thismap=new Map(1);//���ɵ�ͼ��Demo��Ϊ1�ŵ�ͼ��
		thismap.setOpaque(false);//������屳��Ϊ͸��
		thismap.add(player1);//��ͼ�м������һ
		thismap.add(player2);//��ͼ�м�����Ҷ�
		thismap.setMap();//���ص�ͼ
		
		map.add(thismap);//�����غõĵ�ͼ����map���
		
		jp.add(map);//��map�����������
		
		setBackGround();//���õ�ͼ�еı���
		
		jp.add(bg);//�����������ͼ
		
		attribute.setBounds(1240,20,240,960);//���������������С��λ��
		attribute.setOpaque(false);//������屳��Ϊ͸��
		attribute.setLayout(null);//ȡ��Ĭ�ϲ���
		
		pa.setBounds(45,50,150,860);//pa�������������ҵ���ϸ�������
		pa.setOpaque(false);//������屳��Ϊ͸��
		pa.setLayout(new GridLayout(4,1));//������岼��Ϊ4:1��GridLayout
		
		plA1=new PlayerAttribute(player1);//��ʼ�����һ������
		pa.add(plA1);//�����һ�����������pa���
		plA2=new PlayerAttribute(player2);//��ʼ����Ҷ�������
		pa.add(plA2);//����Ҷ������������pa���
		
		attribute.add(pa);//��pa���������������
		
		JLabel attrbg=new JLabel();//�����������ı�����ǩ
		attrbg.setBounds(0,0,240,960);//���ñ�����ǩ�Ĵ�С��λ��
		setIcon("images/attrbg.png",attrbg);//��ӱ���ͼƬ
		attribute.add(attrbg);//�����������������
		jp.add(attribute);//�����������ӽ������
		
		this.addKeyListener(this);//���ô�����Ӽ��̼�����
		
		 //���ô��ڣ���С�������ȣ�
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
	    //new Thread(new PaintThread1()).start();//���̼����߳�����
	    // new Thread(new PaintThread2()).start();//���̼����߳�����
	    new Thread(new PaintThreadAI2()).start();
	}
	
	static void meetbox(Player pl)
	{
		if(pl.getX()<0||pl.getX()>1120||pl.getY()<0||pl.getY()>880)//����ͼ�߾�
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
					if(!temp.isdestroyshowT)//��������
					{
						pl.Back();
						return;
					}
					else{//��������
						switch(temp.TreasureIndex){
						case 1:
							pl.pla.plusspeed();//�Ե�����
							break;
						case 2:
							pl.pla.plusbombnum();//�Ե�������
							break;
						case 3:
							pl.pla.pluspower();//�Ե�������
							break;
						}
						temp.isExist=false;//���õ�ǰ����Ϊ[������]
						setIcon("images/default.png",temp);//����ǰ��������Ϊ����
					}
				}
				
				
				if(temp.isExistBomb&&!temp.isExistPlayer&&temp.getRect().intersects(pl.getRect()))//����ը��
				{
					pl.Back();
					return;
				}
			}

	}
	
	public void setBackGround()//���õ�ͼ�ı���
	{
		bg.setLayout(null);//ȡ��Ĭ�ϲ���
		bg.setBounds(20,20,1200,960);//��������С��λ��
		bg.setOpaque(false);//������屳��Ϊ͸��
		for(int i=0;i<15;i++)//���ñ���
		{
			for(int j=0;j<12;j++)
			{
				JLabel temp=new JLabel();//������ʱ��label����
				temp.setBounds(i*80,j*80,80,80);//���õ�ǰ��ʱͼƬ�Ĵ�С��λ��
				if((i+j)%2==0)
				{
					setIcon("images/�ݵ�1.jpg",temp);//ż�������òݵ�1ͼƬ
				}
				else{
					setIcon("images/�ݵ�2.jpg",temp);//���������òݵ�2ͼƬ
				}
				bg.add(temp);//���ͼƬ
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {//���̰����¼�
		// TODO Auto-generated method stub
		player1.keyPressed(e);
		player2.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {//���̵����¼�
		// TODO Auto-generated method stub
		player1.keyReleased(e);
		player2.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class PaintThread1 implements Runnable {
		public void run() {
			while (true) {//����һ����ѭ�������ֳ���Լ����¼��ļ���
				if(player1.isalive){//�����һ��������²����ƶ�
					player1.move();//�ƶ����һ
					meetbox(player1);//��������ײ�������·���
					if(player1.thisbomb!=null)
					{
						if(player1.thisbomb.isExistPlayer)//�ж�����Ƿ��ڵ�ǰը���ϣ�һ���뿪��ҵ�ǰ���õ�ը�����㲻���ٴ�վ��ȥ��
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
			while (true) {//����һ����ѭ�������ֳ���Լ����¼��ļ���
				if(player2.isalive){//����Ҷ���������²����ƶ�
					player2.move();//�ƶ���Ҷ�
					meetbox(player2);//��������ײ�������·���
					if(player2.thisbomb!=null)
					{
						if(player2.thisbomb.isExistPlayer)//�ж�����Ƿ��ڵ�ǰը���ϣ�һ���뿪��ҵ�ǰ���õ�ը�����㲻���ٴ�վ��ȥ��
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
			while (player2.isalive) {//����һ����ѭ�������ֳ���Լ����¼��ļ���
				//meetbox(player2);//��������ײ�������·���
				if(player2.thisbomb!=null)
				{
					if(player2.thisbomb.isExistPlayer)//�ж�����Ƿ��ڵ�ǰը���ϣ�һ���뿪��ҵ�ǰ���õ�ը�����㲻���ٴ�վ��ȥ��
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
	
	public static void setIcon(String file,JLabel com)//��ͼƬ����Ӧlabel��ǩ��С
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}

}