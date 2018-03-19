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
	JPanel jp=new JPanel();//�����

	JLabel single=new JLabel();//������Ϸ��ť
	JLabel multi=new JLabel();//������Ϸ��ť
	JLabel exit=new JLabel();//�˳���ť

	public Start(){
		jp.setLayout(null);//��������Ĭ�ϲ���
		
		single.setBounds(568,430,354,47);//���õ�����Ϸ��ť��С��λ��
		single.addMouseListener(this);//Ϊ������Ϸ��ť���������¼�
		setIcon("images/singleplayer.png",single);//Ϊ������Ϸ��ť���ͼƬ
		jp.add(single);//��������Ϸ��ť��ӽ������
		
		multi.setBounds(568,561,354,47);//���ö�����Ϸ��ť��С��λ��
		multi.addMouseListener(this);//Ϊ������Ϸ��ť���������¼�
		setIcon("images/multiplayer.png",multi);//Ϊ������Ϸ��ť���ͼƬ
		jp.add(multi);//��������Ϸ��ť��ӽ������
		
		exit.setBounds(568,687,354,47);//�����˳���ť��С��λ��
		exit.addMouseListener(this);//Ϊ�˳���ť���������¼�
		setIcon("images/Exit.png",exit);//Ϊ�˳���ť���ͼƬ
		jp.add(exit);//���˳���ť��ӽ������
		
		
		 //���ô��ڣ���С�����������ֵȣ�
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

	public void setIcon(String file,JLabel com)//��ͼƬ����Ӧlabel��ǩ��С
	{
		  ImageIcon ico=new ImageIcon(file);  
		  Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		  ico=new ImageIcon(temp);  
		  com.setIcon(ico);  
	}

	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getSource()==single)//���������¼���ӦΪ������Ϸ��ť
		{
			JOptionPane.showMessageDialog(this,"���1:\nW-�ϣ�S-�£�A-��D-�ң�V-��ը��\n\n","����˵��",2); //������Ϣ��
			this.setVisible(false);
			new GameFrame(1);//�½�һ����Ϸ���ڶ���
		}
		if(e.getSource()==multi)//���������¼���ӦΪ������Ϸ��ť
		{
			JOptionPane.showMessageDialog(this,"���1:\nW-�ϣ�S-�£�A-��D-�ң�V-��ը��\n\n"
					+ "���2:\n��-�ϣ���-�£���-�󣬡�-�ң�L-��ը��","����˵��",2); //������Ϣ��
			this.setVisible(false);
			new GameFrame();//�½�һ����Ϸ���ڶ���
		}
		if(e.getSource()==exit)//���������¼���ӦΪ�˳���ť
		{
			this.dispose();//ע������
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
