package Income;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import DB.ConnDB;
import mainUI.MainFrame;

public class IncomeUI extends JFrame implements ActionListener, MouseListener {
	private JButton previousBtn;
	private JButton addButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton payoutNotificationButton;
	private DefaultTableModel tableModel;
	private JTable incomeValueTable;
	private int selectRow = -1;

	public IncomeUI() {
		setTitle("급여 지급 현황");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        createIncomePanel();

        setVisible(true);
	}
	
	public void createIncomePanel() {
		JPanel incomeValuePanel = new JPanel();
		incomeValuePanel.setLayout(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new GridBagLayout());
		
		previousBtn = new JButton(new ImageIcon("img/previous.png"));
		previousBtn.setContentAreaFilled(false);
		previousBtn.addActionListener(this);
		
		GridBagConstraints gbc1 = new GridBagConstraints();			//그리드 레이아웃 제약조건 설정	
		gbc1.weightx = 1;											//그리드 내 가로 비율 설정
		gbc1.fill = GridBagConstraints.BOTH;  						// 그리드를 채우도록 설정
      
        JLabel title = new JLabel("Payment");
        title.setFont(new Font("ROKAF SLAB SERIF MEDIUM", Font.BOLD, 30));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.weightx = 9;
        gbc2.fill = GridBagConstraints.BOTH;
      
        addButton = new JButton(new ImageIcon("img/add.png"));
        addButton.setBorderPainted(false);
        addButton.addActionListener(this);
        addButton.setContentAreaFilled(false);
        addButton.setFocusPainted(false);
        
        deleteButton = new JButton(new ImageIcon("img/delete.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.addActionListener(this);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);
        
        saveButton = new JButton(new ImageIcon("img/save.png"));
        saveButton.setBorderPainted(false);
        saveButton.addActionListener(this);
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusPainted(false);
        
        payoutNotificationButton = new JButton(new ImageIcon("img/notification.png"));
        payoutNotificationButton.setBorderPainted(false);
        payoutNotificationButton.addActionListener(this);
        payoutNotificationButton.setContentAreaFilled(false);
        
        titlePanel.add(previousBtn, gbc1);
        titlePanel.add(title, gbc2);
        titlePanel.add(addButton);
        titlePanel.add(deleteButton);
        titlePanel.add(saveButton);
        titlePanel.add(payoutNotificationButton);
        
		incomeValuePanel.add(titlePanel, BorderLayout.NORTH);
		
		// 표 헤더 설정
		String[] columnNames = {"Number", "Name", "Workingtime", "Salary per hour", "Salary Payout Date", "Payout Status"};
		
		// Table 설정(초기화: 행 없음)
		tableModel = new DefaultTableModel(columnNames, 0);
		
		// Employee DB에서 데이터 불러오기
        ConnDB db = new ConnDB();
        db.showData(columnNames.length, tableModel, "income");	
        db = null;
        
      //불러온 데이터를 세팅한 디자인으로 재정의
		incomeValueTable = new JTable(tableModel);
		incomeValueTable.setFont(new Font("ROKAF SLAB SERIF MEDIUM", 0, 15));
		incomeValueTable.setRowHeight(30);		//행 높이 설정
		incomeValueTable.addMouseListener(this);
		
		TableColumnModel columnModel = incomeValueTable.getColumnModel();			//TableColumnModel : 열의 속성 조정하기 위한 객체
        
        int[] colWidth = {10, 80, 80, 80, 80, 80};
        TableColumn column;
        
        for(int i = 0; i < columnNames.length; i++) {
        	column = columnModel.getColumn(i);
        	column.setPreferredWidth(colWidth[i]);							//지정한 너비대로 각 열 너비 설정
        }
		
     // DefaultTableCellRender로 표의 각 셀에 대해 서식 지정 가능 = 가운데 정렬
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < incomeValueTable.getColumnCount(); i++) {
			incomeValueTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		JScrollPane scrollPane = new JScrollPane(incomeValueTable);		//스크롤 지정
		incomeValuePanel.add(scrollPane, BorderLayout.CENTER);
		
        add(incomeValuePanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {						//뒤로가기 버튼 = MainFrame 열기
		Object obj = e.getSource();
		
		if(obj == previousBtn) {
			MainFrame mf = new MainFrame();
			mf.setLocationRelativeTo(this);
			this.dispose();							//창 끄기
		}
		else if(obj == addButton) {					//데이터 추가 창 열기
			AddFrame add = new AddFrame(this);
			add.setLocationRelativeTo(this);
		}
		
		//현재 보이는 표를 csv파일로 저장
		else if(obj == saveButton) {
			String filePath = "csv/income_data.csv";		//파일 경로 지정

		    try (FileWriter writer = new FileWriter(filePath)) {
		    	
		        StringBuilder header = new StringBuilder();				//문자열 버퍼 = 표 헤더 저장
		        for (int i = 0; i < tableModel.getColumnCount(); i++) {		//전체 행 갯수만큼 반복
		            header.append(tableModel.getColumnName(i));				//표 헤더 입력
		            if (i < tableModel.getColumnCount() - 1) {				// 맨 마지막 데이터 제외 ',' 입력
		                header.append(",");
		            }
		        }
		        writer.write(header.toString());						//String으로 입력
		        writer.write("\n");										//CR로 표 헤더 작성 종료

		        for (int row = 0; row < tableModel.getRowCount(); row++) {		//표 내의 각 데이터 입력(열과 행 반복)
		            StringBuilder rowStr = new StringBuilder();
		            for (int col = 0; col < tableModel.getColumnCount(); col++) {
		                Object value = tableModel.getValueAt(row, col);		//해당되는 셀의 데이터 갖고옴
		                if (value != null) {
		                    rowStr.append(value.toString());
		                }
		                if (col < tableModel.getColumnCount() - 1) {		//맨 마지막 데이터 제외 ','입력
		                    rowStr.append(",");
		                }
		            }
		            writer.write(rowStr.toString());
		            writer.write("\n");
		        }

		        JOptionPane.showMessageDialog(this, "데이터가 성공적으로 저장되었습니다.");
		    } catch (IOException ex) {
		        JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
		    }
		}
		else if(obj == payoutNotificationButton) {					//급여지급 버튼 알림
			ConnDB db = new ConnDB();
			Object number = tableModel.getValueAt(selectRow, 0);		//해당하는 열의 DB UnPaid를 Paid로 변경
			if(db.payment(number)) {
				JOptionPane.showMessageDialog(this, "급여 지급처리가 되었습니다. 문자 알림을 전송합니다.");
				
				IncomeUI income = new IncomeUI();
				income.setLocationRelativeTo(this);
				this.dispose();
			}
			db = null;
		}
		else if(obj == deleteButton) {
			if(selectRow != -1) {
				ConnDB db = new ConnDB();
				Object number = tableModel.getValueAt(selectRow, 0);
				if(db.deleteData(number, "income")) {
					JOptionPane.showMessageDialog(this, "삭제가 완료되었습니다.");
					
					IncomeUI income = new IncomeUI();
					income.setLocationRelativeTo(this);
					this.dispose();
				}
				else
					JOptionPane.showMessageDialog(
							this, "삭제가 실패했습니다.", "데이터 삭제 오류", JOptionPane.ERROR_MESSAGE);
				db = null;
			}
			else
				JOptionPane.showMessageDialog(
						this, "행을 선택하세요.", "데이터 선택 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {		//열 삭제 혹은 급여지급 알람 선택 시 해당 열 지정
		if (e.getSource() == incomeValueTable) {
            if(incomeValueTable.getSelectedRow() != -1)
            	selectRow = incomeValueTable.getSelectedRow();
        }
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
