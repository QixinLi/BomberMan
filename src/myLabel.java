import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


class box extends JLabel{
	boolean isExist=true;//�Ƿ����
	/***************************BOMB******************************/
	boolean isExistBomb=false;//�Ƿ����ը��
	boolean needDetonate=false;//�Ƿ���Ҫ����
	boolean isExistPlayer=true;//*ը��*�Ƿ�վ����
	boolean isBoomArea=false;//�˴��Ƿ��Ǳ�ը����
	Bomb boom;//�˴���ը������ 
	/*************************************************************/
	boolean candestroy;//�Ƿ���ƻ�
	boolean isdestroyshowT=false;//�Ƿ����ƻ���false-box״̬��true-treasure״̬
	int TreasureIndex=0;//0-�ޣ�1-�ٶȣ�2-���ݣ�3-����
	String Tpath;//����ͼƬ��ַ
	public box(int x,int y,int n,int index,boolean candestroy)//*����*
	{
		this.candestroy=candestroy;
		this.setBounds(80*x,80*y,80,80);//���ø�box��С��λ��
		setIcon("images/"+n+"/"+index+".png",this);//���ݴ���Ĳ�������ͼƬ
		if(candestroy)//�����box�����ƻ�
		{
			setTreasure();//���ø�box�ڲصı���
		}
	}
	
	public box(int x,int y)//*������*
	{
		isExist=false;//���ø�boxΪ������
		this.setBounds(80*x,80*y,80,80);//���ø�box��С��λ��
		setIcon("images/default.png",this);//���ø�box��ͼƬΪĬ�ϵ�ͼƬ��͸����
	}
	
	public void setTreasure()//����ÿ�������ڵı���
	{
		Random ra=new Random();//����һ���������
		int isT=ra.nextInt(10)+1;//��ȡһ��1-10�������
		if(isT>6)//�Ǳ���ļ���Ϊ40%
		{
			int Tindex=ra.nextInt(3)+1;//��ȡһ��1-3�������
			switch(Tindex)
			{
			//�ٶ�+1
			case 1:
				TreasureIndex=1;
				Tpath="images/speed+.gif";
				break;
			//����
			case 2:
				TreasureIndex=2;
				Tpath="images/bombnum+.gif";
				break;
			//����
			case 3:
				TreasureIndex=3;
				Tpath="images/power+.gif";
				break;
			}
		}
	}
	
	public void setIcon(String file,JLabel com)//��ͼƬ����Ӧlabel��ǩ��С
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
	
	public Rectangle getRect()//��ȡ��box�Ĵ�С��λ�ã�����ֵΪһ������
	{
		Rectangle rec=new Rectangle();
		rec.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		return rec;
	}
}


class link
{
	int dirindex;//1-�ϣ�2-�£�3-��4-��
	public link(int dir)
	{
		this.dirindex=dir;
	}
}

