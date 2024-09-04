package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

public class ConnDB {
    private Connection conn;			
    private PreparedStatement pstmt;
    private ResultSet rs;

    public ConnDB() {
    	try {
    		// SQL Loading
    		Class.forName("com.mysql.cj.jdbc.Driver");		
    		conn = DriverManager.getConnection("jdbc:mysql://localhost/erp?serverTimezone=Asia/Seoul","erp","erp1234"); //ID로 mySQL의 erp DB 로그인 후 conn 변수에 객체 연결
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public void showData(int rowCnt, DefaultTableModel table, String tableName) {		//각 기능 프레임에서 전달받은 table 이름을 통해서 Employee, Income, Working Table 불러오기
    	try {
		pstmt = conn.prepareStatement("select * from " + tableName);	//prepareStatement -> SQL쿼리 입력 = Employee, Income, Working Table 가져오기
      	rs = pstmt.executeQuery();										//Table 저장
      	
      	while(rs.next()) {										//해당 DB 열 끝까지 불러오기
      		Object[] rowData = new Object[rowCnt];				//각 Frame(Employee, Income, Working)의 열 갯수 불러오기
      		
      		for(int i = 1; i <= rowCnt; i++) {		
      			rowData[i - 1] = rs.getString(i);				//행 데이터 문자열로 Parse
      		}
      		
      		table.addRow(rowData);			
      	}
      } catch(Exception e) {
      	System.out.println("Table Data Read Error!");
      	e.printStackTrace();
      } finally {
		try {
			//전체 쿼리 종료
			rs.close();						
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
      }
    }
    
    //각 Frame별 addFrame에서 들어온 정보 DB에 Insert
    public boolean insertData(String[] data, String tableName) {			
    	String query = null;
    	
    	if(tableName.equals("employee")) {													//쿼리문 작성
    		query = "insert into " + tableName + 
    				" (name, age, gender, address, workingspace, workingtime, salary, additional) "
    				+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
    	}
    	else if(tableName.equals("worktable")) {
    		query = "insert into " + tableName +
    				" (workingdate, name, workingtime, additioal) values (?, ?, ?, ?)";
    	}
    	else if(tableName.equals("income")) {
    		query = "insert into " + tableName + 
    				" (name, workingtime, salary, payout, status) values (?, ?, ?, ?, 'Unpaid')";		//급여 지급 현황은 UnPaid로 기본 지정
    	}
    	
    	try {
			pstmt = conn.prepareStatement(query);			//DB Table Update
			
			for(int i = 0; i < data.length; i++)
				pstmt.setString(i+1, data[i]);
			
			pstmt.executeUpdate();
			
			return true;									//작업 성공시 Finally 실행
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    //행 삭제
    public boolean deleteData(Object number, String tableName) {		
    	String query = "delete from " + tableName + " where number=" + number;
    			
    	try {
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			
			return true;									//작업 성공시 Finally 실행
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    //Income Frame에서 UnPaid 항목을 Paid 로 변경
    public boolean payment(Object number) {
    	String query = "update income set status='Paid' where number=" + number;		
    	
    	try {
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		}
    	
    }
}
