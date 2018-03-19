import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


class box extends JLabel{
	boolean isExist=true;//是否存在
	/***************************BOMB******************************/
	boolean isExistBomb=false;//是否存在炸弹
	boolean needDetonate=false;//是否需要引爆
	boolean isExistPlayer=true;//*炸弹*是否站了人
	boolean isBoomArea=false;//此处是否是爆炸区域
	Bomb boom;//此处的炸弹参数 
	/*************************************************************/
	boolean candestroy;//是否可破坏
	boolean isdestroyshowT=false;//是否已破坏，false-box状态，true-treasure状态
	int TreasureIndex=0;//0-无，1-速度，2-泡泡，3-威力
	String Tpath;//宝物图片地址
	public box(int x,int y,int n,int index,boolean candestroy)//*存在*
	{
		this.candestroy=candestroy;
		this.setBounds(80*x,80*y,80,80);//设置该box大小、位置
		setIcon("images/"+n+"/"+index+".png",this);//根据传入的参数设置图片
		if(candestroy)//如果该box可以破坏
		{
			setTreasure();//设置该box内藏的宝物
		}
	}
	
	public box(int x,int y)//*不存在*
	{
		isExist=false;//设置该box为不存在
		this.setBounds(80*x,80*y,80,80);//设置该box大小、位置
		setIcon("images/default.png",this);//设置该box处图片为默认的图片（透明）
	}
	
	public void setTreasure()//设置每个方块内的宝物
	{
		Random ra=new Random();//定义一个新随机数
		int isT=ra.nextInt(10)+1;//读取一个1-10的随机数
		if(isT>6)//是宝物的几率为40%
		{
			int Tindex=ra.nextInt(3)+1;//读取一个1-3的随机数
			switch(Tindex)
			{
			//速度+1
			case 1:
				TreasureIndex=1;
				Tpath="images/speed+.gif";
				break;
			//泡泡
			case 2:
				TreasureIndex=2;
				Tpath="images/bombnum+.gif";
				break;
			//威力
			case 3:
				TreasureIndex=3;
				Tpath="images/power+.gif";
				break;
			}
		}
	}
	
	public void setIcon(String file,JLabel com)//令图片自适应label标签大小
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
	
	public Rectangle getRect()//获取该box的大小、位置，返回值为一个矩形
	{
		Rectangle rec=new Rectangle();
		rec.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		return rec;
	}
}


class link
{
	int dirindex;//1-上，2-下，3-左，4-右
	public link(int dir)
	{
		this.dirindex=dir;
	}
}

