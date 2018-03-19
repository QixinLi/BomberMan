import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PlayerAttribute extends JPanel{
	Player myP;//玩家
	JLabel photo=new JLabel();//玩家头像
	JPanel attrJP=new JPanel();//属性面板
	JLabel live=new JLabel();//玩家生命值
	JLabel speed=new JLabel();//玩家速度
	JLabel bombnum=new JLabel();//玩家炸弹数
	JLabel power=new JLabel();//玩家威力
	
	public PlayerAttribute(Player pl)//构造函数
	{
		this.myP=pl;
		myP.pla=this;
		this.setOpaque(false);//设置面板背景为透明
		this.setLayout(new GridLayout(2,1));//设置该处布局为2:1的GridLayout
		
		photo.setSize(100,100);//设置照片大小
		setIcon(myP.IMGpath,photo);//插入头像图片
		this.add(photo);//将玩家头像插入到该面板
		
		attrJP.setOpaque(false);//设置属性栏背景为透明
		attrJP.setLayout(new GridLayout(4,1));//设置属性栏布局为4:1的GridLayout
		
		setJL("生命值:"+myP.live,live);//初始化生命值数据
		setJL("速度:"+myP.speed,speed);//初始化速度数据
		setJL("泡泡数:"+myP.bombnum,bombnum);//初始化炸弹数数据
		setJL("威力:"+myP.power,power);//初始化威力数据
		
		//将各类属性添加到属性面板
		attrJP.add(live);
		attrJP.add(speed);
		attrJP.add(bombnum);
		attrJP.add(power);
		
		//将属性面板插入到该面板
		this.add(attrJP);
		
		
	}
	
	public void plusspeed()//加速度
	{
		if(myP.speed<myP.maxspeed)//如果当前速度小于最高速度
		{
			myP.speed+=2;//速度加二
			setJL("速度:"+myP.speed,speed);//更新速度数据
		}
	}
	
	public void plusbombnum()//加炸弹数
	{
		if(myP.bombnum<myP.maxbombnum)//如果当前炸弹数小于最高炸弹数
		{
			myP.bombnum++;//炸弹数加一
			setJL("泡泡数:"+myP.bombnum,bombnum);//更新炸弹数据
		}
	}
	
	public void pluspower()//加威力
	{
		if(myP.power<myP.maxpower)//如果当前威力小于最高威力
		{
			myP.power++;//威力加一
			setJL("威力:"+myP.power,power);//更新威力数据
		}
	}
	
	public void setJL(String str,JLabel jl)//统一设置标签(字体格式、大小、颜色等)
	{
		jl.setText(str);
		jl.setFont(new Font("黑体",Font.BOLD,25));
		jl.setForeground(Color.white);
	}
	
	public void setIcon(String file,JLabel com)//令图片自适应label标签大小
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
}
