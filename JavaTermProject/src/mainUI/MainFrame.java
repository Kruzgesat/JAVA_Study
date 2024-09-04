package mainUI;

import javax.swing.*;

import Employee.EmployeeUI;
import Income.IncomeUI;
import Working.WorkingUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JButton employeeBtn;
    private JButton workingtimeBtn;
    private JButton incomeValueBtn;
	private Font mainFont;

    public MainFrame() {
        setTitle("급여지급 ERP");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(this);
        
        mainFont = new Font("ROKAF SLAB SERIF MEDIUM", Font.BOLD ,16);			//폰트 설정
        mainPanel = new JPanel();

        createMainPanel();

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createMainPanel() {
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        
        JLabel logo = new JLabel(new ImageIcon("img/Logo.png"));
        logo.setBounds(20,20,350,100);
        mainPanel.add(logo);
        
        employeeBtn = new JButton(new ImageIcon("img/employee.png"));
        employeeBtn.setBounds(70, 150, 200, 200);
        employeeBtn.setBorderPainted(false);			//버튼 외곽선 삭제
        employeeBtn.setContentAreaFilled(false);		//버튼 배경 삭제
        employeeBtn.setFocusPainted(false);				//버튼 포커스(점선) 삭제
        employeeBtn.addActionListener(this);
        employeeBtn.setPressedIcon(new ImageIcon("img/push_employee.png"));
       
        workingtimeBtn = new JButton(new ImageIcon("img/workingtime.png"));
        workingtimeBtn.setBounds(300, 150, 200, 200);
        workingtimeBtn.setBorderPainted(false);
        workingtimeBtn.addActionListener(this);
        workingtimeBtn.setContentAreaFilled(false);
        workingtimeBtn.setFocusPainted(false);
        workingtimeBtn.setPressedIcon(new ImageIcon("img/push_workingtime.png"));
        
        incomeValueBtn = new JButton(new ImageIcon("img/incomevalue.png"));
        incomeValueBtn.setBounds(530, 150, 200, 200);
        incomeValueBtn.setBorderPainted(false);
        incomeValueBtn.setContentAreaFilled(false);
        incomeValueBtn.addActionListener(this);
        incomeValueBtn.setFocusPainted(false);
        incomeValueBtn.setPressedIcon(new ImageIcon("img/push_incomevalue.png"));
        
        JLabel Employee = new JLabel("Employee");				//각 버튼별 이름 설정
        Employee.setBounds(130, 320, 100, 100);
        Employee.setFont(mainFont);
        
        JLabel worktable = new JLabel("Working Time");
        worktable.setBounds(350, 320, 150, 100);
        worktable.setFont(mainFont);

        JLabel income = new JLabel("Payment");
        income.setBounds(600, 320, 150, 100);
        income.setFont(mainFont);
        
        mainPanel.add(employeeBtn);
        mainPanel.add(workingtimeBtn);
        mainPanel.add(incomeValueBtn);
        mainPanel.add(Employee);
        mainPanel.add(income);
        mainPanel.add(worktable);
    }

    
    public static void main(String[] args) {
           MainFrame mf =  new MainFrame();
    }

	@Override
	public void actionPerformed(ActionEvent e) {			//각 기능 Frame 열기
		Object obj = e.getSource();
		
		if(obj == employeeBtn) {
			EmployeeUI employee = new EmployeeUI();
			employee.setLocationRelativeTo(this);
			this.dispose();								//MainFrame 닫고 기능 Frame 열기로 UI 변경 효과
		}
		else if(obj == workingtimeBtn) {
			WorkingUI workingTable = new WorkingUI();
			workingTable.setLocationRelativeTo(this);
			this.dispose();
		}
		else if(obj == incomeValueBtn) {
			IncomeUI imconeTable = new IncomeUI();
			imconeTable.setLocationRelativeTo(this);
			this.dispose();
		}
	}
}
