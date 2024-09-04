package Working;

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

public class WorkingUI extends JFrame implements ActionListener, MouseListener {
	private JButton previousBtn;
	private JButton addButton;
	private JButton deleteButton;
	private JButton saveButton;
	private int selectRow = -1;
	private JTable workingTimeTable;
	private DefaultTableModel tableModel;
	
	public WorkingUI() {
		setTitle("근무 현황");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        createWoringPanel();

        setVisible(true);
	}
	
	public void createWoringPanel() {
		JPanel workingTimePanel = new JPanel();
		
		workingTimePanel.setLayout(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new GridBagLayout());
		
		previousBtn = new JButton(new ImageIcon("img/previous.png"));
		previousBtn.setBorderPainted(false);
		previousBtn.setContentAreaFilled(false);
		previousBtn.addActionListener(this);
		previousBtn.setFocusPainted(false);
		
		GridBagConstraints gbc1 = new GridBagConstraints();			//그리드 레이아웃 제약조건 설정
		gbc1.weightx = 1;											//그리드 내 가로 비율 설정
		gbc1.fill = GridBagConstraints.BOTH;  						//그리드를 채우도록 설정
      
        JLabel title = new JLabel("WorkingTable");
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
        
        titlePanel.add(previousBtn, gbc1);
        titlePanel.add(title, gbc2);
        titlePanel.add(addButton);
        titlePanel.add(deleteButton);
        titlePanel.add(saveButton);
        
		workingTimePanel.add(titlePanel, BorderLayout.NORTH);

		// 표 헤더 설정
		String[] columnNames = {"Number", "Working Date", "Name", "Workingtime", "Additional Information"};
		
		// Table 설정(초기화: 행 없음)
		tableModel = new DefaultTableModel(columnNames, 0);

		// Employee DB에서 데이터 불러오기
        ConnDB db = new ConnDB();
        db.showData(columnNames.length, tableModel, "worktable");
		db = null;
		
		//불러온 데이터를 세팅한 디자인으로 재정의
        workingTimeTable = new JTable(tableModel);
        workingTimeTable.setFont(new Font("ROKAF SLAB SERIF MEDIUM", 0, 15));
        workingTimeTable.setRowHeight(30);		//행 높이 설정
        workingTimeTable.addMouseListener(this);

        TableColumnModel columnModel = workingTimeTable.getColumnModel();			//TableColumnModel : 열의 속성 조정하기 위한 객체
        
        int[] colWidth = {10, 80, 80, 80, 300};
        TableColumn column;
        
        for(int i = 0; i < columnNames.length; i++) {
        	column = columnModel.getColumn(i);
        	column.setPreferredWidth(colWidth[i]);							//지정한 너비대로 각 열 너비 설정
        }
        
        // DefaultTableCellRender로 표의 각 셀에 대해 서식 지정 가능 = 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < workingTimeTable.getColumnCount(); i++) {
            workingTimeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(workingTimeTable);		//스크롤 지정
        workingTimePanel.add(scrollPane, BorderLayout.CENTER);
		
		add(workingTimePanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == previousBtn) {						//뒤로가기 버튼 = MainFrame 열기
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
			String filePath = "csv/worktable_data.csv";		//파일 경로 지정

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
		            for (int col = 0; col < tableModel.getColumnCount(); col++) {		//해당되는 셀의 데이터 갖고옴
		                Object value = tableModel.getValueAt(row, col);
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
		else if(obj == deleteButton) {
			if(selectRow != -1) {
				ConnDB db = new ConnDB();
				Object number = tableModel.getValueAt(selectRow, 0);
				if(db.deleteData(number, "worktable")) {
					JOptionPane.showMessageDialog(this, "삭제가 완료되었습니다.");
					
					WorkingUI worktable = new WorkingUI();
					worktable.setLocationRelativeTo(this);
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
	public void mouseClicked(MouseEvent e) {		//열 삭제시 해당 열 지정
		if (e.getSource() == workingTimeTable) {
            if(workingTimeTable.getSelectedRow() != -1)
            	selectRow = workingTimeTable.getSelectedRow();
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
