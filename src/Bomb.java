import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bomb{
	int x,y;//ը��������ֵ
	Player myplayer;//Ͷ�Ÿ�ը�������
	box mybox;//ը�����ڵ�box
	int power;//��ҵ�ǰ������
	List<box> explodeCache=new ArrayList<box>();//��ը����Ŀռ���
	List<box> explodeBomb=new ArrayList<box>();//������ը
	List<box> bombarea=new ArrayList<box>();
	
	public Bomb(Player pl)
	{
		myplayer=pl;
		power=myplayer.power;//��ȡ��ҵ�ǰ������
		x=(int) Math.rint((pl.getX()+40)/80);//��õ�ǰxֵ
		y=(int) Math.rint((pl.getY()+40)/80);//��õ�ǰyֵ
		mybox=GameFrame.thismap.getBoxByXY(x,y);//��ȡ��ǰ���ڵ�box
		pl.thisbomb=mybox;//������ҵ�ǰը��Ϊ��ǰ����box
		mybox.boom=this;//���õ�ǰ����box����boomֵ
		//�����ǰλ��û��ը�������������ը����Ҳû�дﵽ���ޣ������һ��ը��
		if(!mybox.isExistBomb&&myplayer.bombexist<myplayer.bombnum)
		{
			setIcon("images/bomb.gif",mybox);//����ǰboxͼƬ����Ϊը��
			setBoomArea();
			Thread th=new Thread(){//����һ���µ��߳�
				public void run(){
					//��mybox������ը���Ĳ�����ֵtrue
					mybox.isExistBomb=true;
					mybox.needDetonate=true;
					mybox.isExistPlayer=true;
					myplayer.bombexist++;//��ҵ�ǰ���ڵ�ը����+1
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(mybox.needDetonate)//���ը����Ҫ����������������
					{
						E_V();
					}
				}
			};
			th.start();
		}
	}
	
	public void E_V(){//��ը-��ʧ
		Explode();//��ը
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vanish();//��ʧ
	}
	
	public void setBoomArea()
	{
		bombarea.clear();
		box thistemp=GameFrame.thismap.getBoxByXY(x,y);
		thistemp.isBoomArea=true;
		bombarea.add(thistemp);
		int power=myplayer.power;//��ȡ��ҵ�ǰ������
		int u,d,l,r;
		for(u=1;u<=power;u++)//���ϱ�ը
		{
			int x=this.x;
			int y=this.y-u;
			if(y>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//����Ƿ��飬����ֹ��ը
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//���ô���ӵ���ը������
			}
		}
		for(d=1;d<=power;d++)//���±�ը
		{
			int x=this.x;
			int y=this.y+d;
			if(y<12)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//����Ƿ��飬����ֹ��ը
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//���ô���ӵ���ը������
			}
		}
		for(l=1;l<=power;l++)//����ը
		{
			int x=this.x-l;
			int y=this.y;
			if(x>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//����Ƿ��飬����ֹ��ը
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//���ô���ӵ���ը������
			}
		}
		for(r=1;r<=power;r++)//���ұ�ը
		{
			int x=this.x+r;
			int y=this.y;
			if(x<15)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//����Ƿ��飬����ֹ��ը
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//���ô���ӵ���ը������
			}
		}
	}

	public void removeBoomArea()
	{
		for(int i=0;i<bombarea.size();i++)
		{
			bombarea.get(i).isBoomArea=false;
		}
	}
	
	public void Explode()//��ը
	{
		//��mybox������ը���Ĳ�����ֵfalse
		//mybox.isExist=false;
		mybox.isExistBomb=false;
		mybox.needDetonate=false;
		myplayer.bombexist--;//��ҵ�ǰը����-1
		
		setIcon("images/UDLR.png",mybox);//����ը�����ĵ�ͼƬ
		explodePlayer(mybox);//�ж��Ƿ�ը�������
		int u,d,l,r;
		for(u=1;u<=power;u++)//���ϱ�ը
		{
			int x=this.x;
			int y=this.y-u;
			if(y>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isExist=false;//������ʧ
					}
					else//����Ƿ��飬����ֹ��ը
					{
						showTreasure(temp);//��ʾ��box�����صı���
						break;
					}
				}
				else if(temp.isExistBomb)//����ñ�ը����ը��
				{
					temp.boom.Explode();//�����ô���ը��
					explodeBomb.add(temp);//����ը����ӵ����������б���
				}
				explodePlayer(temp);//�ж��Ƿ�ը�������
				explodeCache.add(temp);//���ô���ӵ���ը������
				setIcon("images/UD.png",temp);//���øô��ı�ըͼƬ
			}
		}
		for(d=1;d<=power;d++)//���±�ը
		{
			int x=this.x;
			int y=this.y+d;
			if(y<12)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isExist=false;//������ʧ
					}
					else//����Ƿ��飬����ֹ��ը
					{
						showTreasure(temp);//��ʾ��box�����صı���
						break;
					}
				}
				else if(temp.isExistBomb)//����ñ�ը����ը��
				{
					temp.boom.Explode();//�����ô���ը��
					explodeBomb.add(temp);//����ը����ӵ����������б���
				}
				explodePlayer(temp);//�ж��Ƿ�ը�������
				explodeCache.add(temp);//���ô���ӵ���ը������
				setIcon("images/UD.png",temp);//���øô��ı�ըͼƬ
			}
		}
		for(l=1;l<=power;l++)//����ը
		{
			int x=this.x-l;
			int y=this.y;
			if(x>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isExist=false;//������ʧ
					}
					else//����Ƿ��飬����ֹ��ը
					{
						showTreasure(temp);//��ʾ��box�����صı���
						break;
					}
				}
				else if(temp.isExistBomb)//����ñ�ը����ը��
				{
					temp.boom.Explode();//�����ô���ը��
					explodeBomb.add(temp);//����ը����ӵ����������б���
				}
				explodePlayer(temp);//�ж��Ƿ�ը�������
				explodeCache.add(temp);//���ô���ӵ���ը������
				setIcon("images/LR.png",temp);//���øô��ı�ըͼƬ
			}
		}
		for(r=1;r<=power;r++)//���ұ�ը
		{
			int x=this.x+r;
			int y=this.y;
			if(x<15)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//�����ը��·��������������
				{
					if(temp.isdestroyshowT)//����Ǳ���
					{
						temp.isExist=false;//������ʧ
					}
					else//����Ƿ��飬����ֹ��ը
					{
						showTreasure(temp);//��ʾ��box�����صı���
						break;
					}
				}
				else if(temp.isExistBomb)//����ñ�ը����ը��
				{
					temp.boom.Explode();//�����ô���ը��
					explodeBomb.add(temp);//����ը����ӵ����������б���
				}
				explodePlayer(temp);//�ж��Ƿ�ը�������
				explodeCache.add(temp);//���ô���ӵ���ը������
				setIcon("images/LR.png",temp);//���øô��ı�ըͼƬ
			}
		}
		removeBoomArea();
	}
	
	public void showTreasure(box temp)//��ʾ�˴�box�����صı���
	{
		if(temp.candestroy)//�����box���ƻ�
		{
			if(temp.TreasureIndex==0)//����ó�û�б���
			{
				temp.isExist=false;//���ø�boxΪ������
				setIcon("images/default.png",temp);//����ô�ͼƬ
			}
			else
			{
				temp.isdestroyshowT=true;//���øô�box���ƻ�����ʾ������
				switch(temp.TreasureIndex)//�ֱ���ʾ��ͬ�ı���
				{
				case 1:
					setIcon("images/speed+.gif",temp);//�����ٶ�ͼƬ
					break;
				case 2:
					setIcon("images/bombnum+.gif",temp);//����ը������ͼƬ
					break;
				case 3:
					setIcon("images/power+.gif",temp);//��������ͼƬ
					break;
				}
			}
		}
	}
	
	public void Vanish()//�����ը����
	{
		setIcon("images/default.png",mybox);//������ı�ը��
		for(int i=0;i<explodeCache.size();i++)//�����ը�����еı�ը��
		{
			setIcon("images/default.png",explodeCache.get(i));//���������·���ϵı�ը��ָ���͸����
		}
		for(int i=0;i<explodeBomb.size();i++)//���·���ϱ������ı�ը��
		{
			explodeBomb.get(i).boom.Vanish();//����·����ÿ���������ı�ը����������
		}
	}
	
	void explodePlayer(box b)//�ж��Ƿ�ը�����
	{
		if(b.getRect().intersects(GameFrame.player1.getRect())
				&&!GameFrame.player1.invincible
				&&GameFrame.player1.isalive)//�ж����һ�Ƿ�ͱ�ը���ص����������޵�״̬�����Ҵ��
		{
			GameFrame.player1.beInjured();//���һ�ܵ�1���˺�
		}
		if(b.getRect().intersects(GameFrame.player2.getRect())
				&&!GameFrame.player2.invincible
				&&GameFrame.player2.isalive)//�ж���Ҷ��Ƿ�ͱ�ը���ص����������޵�״̬�����Ҵ��
		{
			GameFrame.player2.beInjured();//��Ҷ��ܵ�1���˺�
		}
	}
	
	public void setIcon(String file,JLabel com)//��ͼƬ����Ӧlabel��ǩ��С
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico); 
	}
}
