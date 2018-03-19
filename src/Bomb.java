import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bomb{
	int x,y;//炸弹的坐标值
	Player myplayer;//投放该炸弹的玩家
	box mybox;//炸弹所在的box
	int power;//玩家当前的威力
	List<box> explodeCache=new ArrayList<box>();//爆炸缓存的空间树
	List<box> explodeBomb=new ArrayList<box>();//连续爆炸
	List<box> bombarea=new ArrayList<box>();
	
	public Bomb(Player pl)
	{
		myplayer=pl;
		power=myplayer.power;//获取玩家当前的威力
		x=(int) Math.rint((pl.getX()+40)/80);//获得当前x值
		y=(int) Math.rint((pl.getY()+40)/80);//获得当前y值
		mybox=GameFrame.thismap.getBoxByXY(x,y);//获取当前所在的box
		pl.thisbomb=mybox;//设置玩家当前炸弹为当前所在box
		mybox.boom=this;//设置当前所在box处的boom值
		//如果当前位置没有炸弹并且玩家所放炸弹数也没有达到上限，便放置一颗炸弹
		if(!mybox.isExistBomb&&myplayer.bombexist<myplayer.bombnum)
		{
			setIcon("images/bomb.gif",mybox);//将当前box图片设置为炸弹
			setBoomArea();
			Thread th=new Thread(){//定义一个新的线程
				public void run(){
					//将mybox处定义炸弹的参数赋值true
					mybox.isExistBomb=true;
					mybox.needDetonate=true;
					mybox.isExistPlayer=true;
					myplayer.bombexist++;//玩家当前存在的炸弹数+1
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(mybox.needDetonate)//如果炸弹需要自行引爆，则引爆
					{
						E_V();
					}
				}
			};
			th.start();
		}
	}
	
	public void E_V(){//爆炸-消失
		Explode();//爆炸
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vanish();//消失
	}
	
	public void setBoomArea()
	{
		bombarea.clear();
		box thistemp=GameFrame.thismap.getBoxByXY(x,y);
		thistemp.isBoomArea=true;
		bombarea.add(thistemp);
		int power=myplayer.power;//获取玩家当前的威力
		int u,d,l,r;
		for(u=1;u<=power;u++)//向上爆炸
		{
			int x=this.x;
			int y=this.y-u;
			if(y>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//如果是方块，则终止爆炸
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//将该处添加到爆炸缓存中
			}
		}
		for(d=1;d<=power;d++)//向下爆炸
		{
			int x=this.x;
			int y=this.y+d;
			if(y<12)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//如果是方块，则终止爆炸
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//将该处添加到爆炸缓存中
			}
		}
		for(l=1;l<=power;l++)//向左爆炸
		{
			int x=this.x-l;
			int y=this.y;
			if(x>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//如果是方块，则终止爆炸
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//将该处添加到爆炸缓存中
			}
		}
		for(r=1;r<=power;r++)//向右爆炸
		{
			int x=this.x+r;
			int y=this.y;
			if(x<15)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isBoomArea=true;
						bombarea.add(temp);
					}
					else//如果是方块，则终止爆炸
					{
						break;
					}
				}
				temp.isBoomArea=true;
				bombarea.add(temp);//将该处添加到爆炸缓存中
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
	
	public void Explode()//爆炸
	{
		//将mybox处定义炸弹的参数赋值false
		//mybox.isExist=false;
		mybox.isExistBomb=false;
		mybox.needDetonate=false;
		myplayer.bombexist--;//玩家当前炸弹数-1
		
		setIcon("images/UDLR.png",mybox);//设置炸弹中心的图片
		explodePlayer(mybox);//判断是否炸到了玩家
		int u,d,l,r;
		for(u=1;u<=power;u++)//向上爆炸
		{
			int x=this.x;
			int y=this.y-u;
			if(y>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isExist=false;//宝物消失
					}
					else//如果是方块，则终止爆炸
					{
						showTreasure(temp);//显示该box处隐藏的宝物
						break;
					}
				}
				else if(temp.isExistBomb)//如果该爆炸处有炸弹
				{
					temp.boom.Explode();//引爆该处的炸弹
					explodeBomb.add(temp);//将该炸弹添加到被引爆的列表里
				}
				explodePlayer(temp);//判断是否炸到了玩家
				explodeCache.add(temp);//将该处添加到爆炸缓存中
				setIcon("images/UD.png",temp);//设置该处的爆炸图片
			}
		}
		for(d=1;d<=power;d++)//向下爆炸
		{
			int x=this.x;
			int y=this.y+d;
			if(y<12)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isExist=false;//宝物消失
					}
					else//如果是方块，则终止爆炸
					{
						showTreasure(temp);//显示该box处隐藏的宝物
						break;
					}
				}
				else if(temp.isExistBomb)//如果该爆炸处有炸弹
				{
					temp.boom.Explode();//引爆该处的炸弹
					explodeBomb.add(temp);//将该炸弹添加到被引爆的列表里
				}
				explodePlayer(temp);//判断是否炸到了玩家
				explodeCache.add(temp);//将该处添加到爆炸缓存中
				setIcon("images/UD.png",temp);//设置该处的爆炸图片
			}
		}
		for(l=1;l<=power;l++)//向左爆炸
		{
			int x=this.x-l;
			int y=this.y;
			if(x>=0)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isExist=false;//宝物消失
					}
					else//如果是方块，则终止爆炸
					{
						showTreasure(temp);//显示该box处隐藏的宝物
						break;
					}
				}
				else if(temp.isExistBomb)//如果该爆炸处有炸弹
				{
					temp.boom.Explode();//引爆该处的炸弹
					explodeBomb.add(temp);//将该炸弹添加到被引爆的列表里
				}
				explodePlayer(temp);//判断是否炸到了玩家
				explodeCache.add(temp);//将该处添加到爆炸缓存中
				setIcon("images/LR.png",temp);//设置该处的爆炸图片
			}
		}
		for(r=1;r<=power;r++)//向右爆炸
		{
			int x=this.x+r;
			int y=this.y;
			if(x<15)
			{
				box temp=GameFrame.thismap.getBoxByXY(x,y);
				if(temp.isExist)//如果爆炸的路线上遇到方块了
				{
					if(temp.isdestroyshowT)//如果是宝物
					{
						temp.isExist=false;//宝物消失
					}
					else//如果是方块，则终止爆炸
					{
						showTreasure(temp);//显示该box处隐藏的宝物
						break;
					}
				}
				else if(temp.isExistBomb)//如果该爆炸处有炸弹
				{
					temp.boom.Explode();//引爆该处的炸弹
					explodeBomb.add(temp);//将该炸弹添加到被引爆的列表里
				}
				explodePlayer(temp);//判断是否炸到了玩家
				explodeCache.add(temp);//将该处添加到爆炸缓存中
				setIcon("images/LR.png",temp);//设置该处的爆炸图片
			}
		}
		removeBoomArea();
	}
	
	public void showTreasure(box temp)//显示此处box中隐藏的宝物
	{
		if(temp.candestroy)//如果该box可破坏
		{
			if(temp.TreasureIndex==0)//如果该出没有宝物
			{
				temp.isExist=false;//设置该box为不存在
				setIcon("images/default.png",temp);//清除该处图片
			}
			else
			{
				temp.isdestroyshowT=true;//设置该处box被破坏，显示出宝物
				switch(temp.TreasureIndex)//分别显示不同的宝物
				{
				case 1:
					setIcon("images/speed+.gif",temp);//设置速度图片
					break;
				case 2:
					setIcon("images/bombnum+.gif",temp);//设置炸弹泡泡图片
					break;
				case 3:
					setIcon("images/power+.gif",temp);//设置威力图片
					break;
				}
			}
		}
	}
	
	public void Vanish()//清除爆炸波纹
	{
		setIcon("images/default.png",mybox);//清除中心爆炸块
		for(int i=0;i<explodeCache.size();i++)//清除爆炸缓存中的爆炸块
		{
			setIcon("images/default.png",explodeCache.get(i));//将被清除的路径上的爆炸块恢复成透明块
		}
		for(int i=0;i<explodeBomb.size();i++)//清除路径上被引爆的爆炸块
		{
			explodeBomb.get(i).boom.Vanish();//调用路径上每个被引爆的爆炸块的清除方法
		}
	}
	
	void explodePlayer(box b)//判断是否炸到玩家
	{
		if(b.getRect().intersects(GameFrame.player1.getRect())
				&&!GameFrame.player1.invincible
				&&GameFrame.player1.isalive)//判断玩家一是否和爆炸块重叠，不处于无敌状态，并且存活
		{
			GameFrame.player1.beInjured();//玩家一受到1点伤害
		}
		if(b.getRect().intersects(GameFrame.player2.getRect())
				&&!GameFrame.player2.invincible
				&&GameFrame.player2.isalive)//判断玩家二是否和爆炸块重叠，不处于无敌状态，并且存活
		{
			GameFrame.player2.beInjured();//玩家二受到1点伤害
		}
	}
	
	public void setIcon(String file,JLabel com)//令图片自适应label标签大小
	{  
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico); 
	}
}
