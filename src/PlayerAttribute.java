import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PlayerAttribute extends JPanel{
	Player myP;//���
	JLabel photo=new JLabel();//���ͷ��
	JPanel attrJP=new JPanel();//�������
	JLabel live=new JLabel();//�������ֵ
	JLabel speed=new JLabel();//����ٶ�
	JLabel bombnum=new JLabel();//���ը����
	JLabel power=new JLabel();//�������
	
	public PlayerAttribute(Player pl)//���캯��
	{
		this.myP=pl;
		myP.pla=this;
		this.setOpaque(false);//������屳��Ϊ͸��
		this.setLayout(new GridLayout(2,1));//���øô�����Ϊ2:1��GridLayout
		
		photo.setSize(100,100);//������Ƭ��С
		setIcon(myP.IMGpath,photo);//����ͷ��ͼƬ
		this.add(photo);//�����ͷ����뵽�����
		
		attrJP.setOpaque(false);//��������������Ϊ͸��
		attrJP.setLayout(new GridLayout(4,1));//��������������Ϊ4:1��GridLayout
		
		setJL("����ֵ:"+myP.live,live);//��ʼ������ֵ����
		setJL("�ٶ�:"+myP.speed,speed);//��ʼ���ٶ�����
		setJL("������:"+myP.bombnum,bombnum);//��ʼ��ը��������
		setJL("����:"+myP.power,power);//��ʼ����������
		
		//������������ӵ��������
		attrJP.add(live);
		attrJP.add(speed);
		attrJP.add(bombnum);
		attrJP.add(power);
		
		//�����������뵽�����
		this.add(attrJP);
		
		
	}
	
	public void plusspeed()//���ٶ�
	{
		if(myP.speed<myP.maxspeed)//�����ǰ�ٶ�С������ٶ�
		{
			myP.speed+=2;//�ٶȼӶ�
			setJL("�ٶ�:"+myP.speed,speed);//�����ٶ�����
		}
	}
	
	public void plusbombnum()//��ը����
	{
		if(myP.bombnum<myP.maxbombnum)//�����ǰը����С�����ը����
		{
			myP.bombnum++;//ը������һ
			setJL("������:"+myP.bombnum,bombnum);//����ը������
		}
	}
	
	public void pluspower()//������
	{
		if(myP.power<myP.maxpower)//�����ǰ����С���������
		{
			myP.power++;//������һ
			setJL("����:"+myP.power,power);//������������
		}
	}
	
	public void setJL(String str,JLabel jl)//ͳһ���ñ�ǩ(�����ʽ����С����ɫ��)
	{
		jl.setText(str);
		jl.setFont(new Font("����",Font.BOLD,25));
		jl.setForeground(Color.white);
	}
	
	public void setIcon(String file,JLabel com)//��ͼƬ����Ӧlabel��ǩ��С
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
}
