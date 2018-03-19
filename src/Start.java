import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Start extends JFrame implements MouseListener{
	JPanel jp=new JPanel();//主面板

	JLabel single=new JLabel();//单人游戏按钮
	JLabel multi=new JLabel();//多人游戏按钮
	JLabel exit=new JLabel();//退出按钮

	public Start(){
		jp.setLayout(null);//清除主面板默认布局
		
		single.setBounds(568,430,354,47);//设置单人游戏按钮大小、位置
		single.addMouseListener(this);//为单人游戏按钮添加鼠标点击事件
		setIcon("images/singleplayer.png",single);//为单人游戏按钮添加图片
		jp.add(single);//将单人游戏按钮添加进主面板
		
		multi.setBounds(568,561,354,47);//设置多人游戏按钮大小、位置
		multi.addMouseListener(this);//为多人游戏按钮添加鼠标点击事件
		setIcon("images/multiplayer.png",multi);//为多人游戏按钮添加图片
		jp.add(multi);//将多人游戏按钮添加进主面板
		
		exit.setBounds(568,687,354,47);//设置退出按钮大小、位置
		exit.addMouseListener(this);//为退出按钮添加鼠标点击事件
		setIcon("images/Exit.png",exit);//为退出按钮添加图片
		jp.add(exit);//将退出按钮添加进主面板
		
		
		 //设置窗口（大小、背景、音乐等）
        ImageIcon background = new ImageIcon("images/menubg.jpg");  
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
	}

	public void setIcon(String file,JLabel com)//令图片自适应label标签大小
	{
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}

	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getSource()==single)//如果鼠标点击事件响应为单人游戏按钮
		{
			JOptionPane.showMessageDialog(this,"玩家1:\nW-上，S-下，A-左，D-右，V-放炸弹\n\n","操作说明",2); //弹出信息框
			this.setVisible(false);
			new GameFrame(1);//新建一个游戏窗口对象
		}
		if(e.getSource()==multi)//如果鼠标点击事件响应为多人游戏按钮
		{
			JOptionPane.showMessageDialog(this,"玩家1:\nW-上，S-下，A-左，D-右，V-放炸弹\n\n"
					+ "玩家2:\n↑-上，↓-下，←-左，→-右，L-放炸弹","操作说明",2); //弹出信息框
			this.setVisible(false);
			new GameFrame();//新建一个游戏窗口对象
		}
		if(e.getSource()==exit)//如果鼠标点击事件响应为退出按钮
		{
			this.dispose();//注销窗口
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
