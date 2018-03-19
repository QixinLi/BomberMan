import java.awt.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Map extends JPanel{
	int n=1;//地图编号,默认1
	box BOX[][]=new box[15][12];//存储地图的15*12个方格
	public Map(int n)//初始化地图面板
	{
		this.n=n;//设置地图编号
		this.setLayout(null);//取消默认布局
		this.setBounds(0,0,1200,960);//设置该面板大小、位置
	}
	
	public void add(box b,int x,int y)//重写add方法，添加新参数x,y
	{
		this.add(b);//将box添加进地图
		BOX[x][y]=b;//保存box
	}
	
	public box getBoxByXY(int x,int y)//返回x,y坐标处的box值
	{
		return BOX[x][y];
	}
	
	public void setMap()//设置地图
	{
		
		File file = new File("map/"+n+".txt");//读取编号为n的地图文件
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            int j=0;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	String [] arr = s.split("\\s+");//通过空格分割每个字符
            	int i=0;
            	for(String ss : arr){//遍历每一行
            	    int ctemp=Integer.parseInt(ss);
            	    if(ctemp==0){//如果是0，则表示此处没有物体
            	    	box temp=new box(i,j);
            	    	this.add(temp,i,j);
            	    }
            	    if(1<=ctemp&&ctemp<=5)//1~5表示此处为可破坏的实体
            	    {
            	    	box temp=new box(i,j,n,ctemp,true);
            	    	this.add(temp,i,j);
            	    }
            	    if(6<=ctemp&&ctemp<=10)//6~10表示此处为不可破坏的实体
            	    {
            	    	box temp=new box(i,j,n,ctemp,false);
            	    	this.add(temp,i,j);
            	    }
            	    i++;
            	}
            	j++;
            }
            br.close();//关闭文件流
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	public void setIcon(String file,JLabel com)//使图片自适应label标签大小
	{
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
}
