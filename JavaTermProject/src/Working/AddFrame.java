package Working;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.ConnDB;
import Employee.EmployeeUI;

public class AddFrame extends JFrame implements ActionListener{
	private JButton checkButton;
	private JButton cancleButton;
	private Font textFont;
	private JTextField[] textFields;
	private WorkingUI tmpWorktable;
	private Font inputFont;
	
	public AddFrame(WorkingUI worktable) {
		setTitle("근무 추가");
        setSize(600, 330);
        setResizable(false);
        setLayout(new BorderLayout());
        
        textFont = new Font("ROKAF SLAB SERIF MEDIUM", Font.BOLD, 20);		//해당 데이터 헤더 폰트 설정
        inputFont = new Font("ROKAF SLAB SERIF MEDIUM", 0, 15);				//입력 텍스트필드 폰트 설정
        tmpWorktable = worktable;
        
        createNorthPanel();
        createCenterPanel();
        
        setVisible(true);
	}

	private void createNorthPanel() {								//버튼 패널	
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));	//오른쪽 정렬
		
		northPanel.setPreferredSize(new Dimension(800, 90));

		checkButton = new JButton(new ImageIcon("img/addData.png"));
		checkButton.setBorderPainted(false);
		checkButton.setContentAreaFilled(false);
		checkButton.setFocusPainted(false);
		checkButton.setBorder(new EmptyBorder(10, 0, 0, 0));
		checkButton.addActionListener(this);
		
		cancleButton = new JButton(new ImageIcon("img/cancle.png"));
		cancleButton.setBorderPainted(false);
		cancleButton.setContentAreaFilled(false);
		cancleButton.setFocusPainted(false);
		cancleButton.setBorder(new EmptyBorder(10, 10, 0, 10));
		cancleButton.addActionListener(this);
		
		northPanel.add(checkButton);
		northPanel.add(cancleButton);
		
		add(northPanel, BorderLayout.NORTH);
	}
	
	private void createCenterPanel() {				//데이터 입력 패널
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(null);
		centerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));			//테두리 여백 설정
		add(centerPanel, BorderLayout.CENTER);
		
		String[] columnNames = {"Working Date", "Name", "Workingtime", "Additional Information"};
		int len = columnNames.length;
		
		textFields = new JTextField[len];			//입력받을 항목 데이터 배열
		JLabel[] textLabels = new JLabel[len];		//각 항목 제목라벨
		JLabel[] textFieldImg = new JLabel[len];	//각 항목별 TextField 배경 이미지
		
		int lbl_x = 30, lbl_y = 5, tf_x = 330, tf_y = 31, img_x = 185, img_y = 21;		//기본적으로 정렬할 픽셀 기준(lbl은 제목라벨, tf는 텍스트필드)
		
		for(int i = 0; i < columnNames.length; i++, lbl_y+=39, tf_y+=40, img_y+=40) {	//위의 기준 좌표로 일정한 간격으로 입력 텍스트필드 생성
			textLabels[i] = new JLabel(columnNames[i]);
			textLabels[i].setFont(textFont);
			textLabels[i].setBounds(lbl_x, lbl_y, 250, 100);
			centerPanel.add(textLabels[i]);

			textFields[i] = new JTextField();
			textFields[i].setBounds(tf_x, tf_y, 215, 28);
			textFields[i].setFont(inputFont);
			textFields[i].setBorder(BorderFactory.createEmptyBorder());
			centerPanel.add(textFields[i]);
			
			textFieldImg[i] = new JLabel(new ImageIcon("img/textFieldImg.png"));
			textFieldImg[i].setBounds(img_x, img_y, 500, 50);
			centerPanel.add(textFieldImg[i]);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == checkButton) {
			boolean flag = true;								// DB 접속 Flag 설정 = 쿼리문 준비된 경우에만 호출
			String[] data = new String[textFields.length];		//DB에 입력할 각 속성 배열
			
			for(int i = 0; i < textFields.length; i++) {
				if(!textFields[i].getText().equals(""))			//공백이 없으면(모든 데이터가 채워진 경우) 입력
					data[i] = textFields[i].getText();
				else {
					JOptionPane.showMessageDialog(
							this, "모든 값을 입력하세요.", "입력값 오류", JOptionPane.ERROR_MESSAGE);
					flag = false;
					break;
				}
			}
			
			if(flag) {
				ConnDB db = new ConnDB();
				if(db.insertData(data, "worktable")) {
					JOptionPane.showMessageDialog(this, "추가가 완료되었습니다.");
					
					WorkingUI worktable = new WorkingUI();
					worktable.setLocationRelativeTo(this);
					this.dispose();
					this.tmpWorktable.dispose();
				}
			}
			
		} else if(obj == cancleButton) {
			this.dispose();
		}
	}
}
