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
	int n=1;//��ͼ���,Ĭ��1
	box BOX[][]=new box[15][12];//�洢��ͼ��15*12������
	public Map(int n)//��ʼ����ͼ���
	{
		this.n=n;//���õ�ͼ���
		this.setLayout(null);//ȡ��Ĭ�ϲ���
		this.setBounds(0,0,1200,960);//���ø�����С��λ��
	}
	
	public void add(box b,int x,int y)//��дadd����������²���x,y
	{
		this.add(b);//��box��ӽ���ͼ
		BOX[x][y]=b;//����box
	}
	
	public box getBoxByXY(int x,int y)//����x,y���괦��boxֵ
	{
		return BOX[x][y];
	}
	
	public void setMap()//���õ�ͼ
	{
		
		File file = new File("map/"+n+".txt");//��ȡ���Ϊn�ĵ�ͼ�ļ�
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
            String s = null;
            int j=0;
            while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
            	String [] arr = s.split("\\s+");//ͨ���ո�ָ�ÿ���ַ�
            	int i=0;
            	for(String ss : arr){//����ÿһ��
            	    int ctemp=Integer.parseInt(ss);
            	    if(ctemp==0){//�����0�����ʾ�˴�û������
            	    	box temp=new box(i,j);
            	    	this.add(temp,i,j);
            	    }
            	    if(1<=ctemp&&ctemp<=5)//1~5��ʾ�˴�Ϊ���ƻ���ʵ��
            	    {
            	    	box temp=new box(i,j,n,ctemp,true);
            	    	this.add(temp,i,j);
            	    }
            	    if(6<=ctemp&&ctemp<=10)//6~10��ʾ�˴�Ϊ�����ƻ���ʵ��
            	    {
            	    	box temp=new box(i,j,n,ctemp,false);
            	    	this.add(temp,i,j);
            	    }
            	    i++;
            	}
            	j++;
            }
            br.close();//�ر��ļ���
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	public void setIcon(String file,JLabel com)//ʹͼƬ����Ӧlabel��ǩ��С
	{
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}
}
