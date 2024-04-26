package com.itwill.jdbc01;

import static com.itwill.jdbc.OracleJdbc.*;
import static com.itwill.jdbc.model.Blog.Entity.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.itwill.jdbc.model.Blog;

import oracle.jdbc.OracleDriver;




/*
 * JDBC (Java Database Connectivity):
 * 자바 프로그램에서 데이터베이스를 연결하고, SQL 문장들을 실행해서 그 결과를 처리. 
 * 
 * 1. Oracle JDBC 라이브러리를 다운로드하고 이클립스 프로젝트의 빌드패스에 추가. 
 *   (1) https://central.sonatype.com (메이븐 중앙 저장소)에서 ojdbc11 검색.
 *   	--> com/oracle/database/jdbc/ojdbc11/23.3.0.23.09 라이브러리 다운로드. 
 *   	--> ojdbc11-23.3.0.23.09.jar
 *   (2) 이클립스 프로젝트에 lib 폴더를 만들고, jar 파일 붙여넣기. 
 *   (3) jar 파일 오른쪽 클릭 --> build path --> add to build path
 *   
 * 2. 데이터베이스에 접속하기 위한 정보들 (URL, USER, PASSWORD, ...)을 상수로 정의.
 * 3. 프로그램에서 오라클 JDBC 라이브러리를 메모리에 로딩(등록).
 * 4. 오라클 데이터베이스에 접속
 * 5. Statement 타입의 객체 생성 - DB에서 실행할 SQL 문장을 작성하고 실행할 수 있는 객체
 * 6. Statement 타입 인스턴스에서 메서드를 호출에서 SQL 문장을 실행. 
 *   (1) executeQuery(): DQL(select 문장)
 *   (2) executeUpdate(): DML(insert, update, delete 문장)
 * 7. 메서드 실행 결과(리턴 값)을 처리.
 * 8. 사용했었던 모든 리소스들을 해제(close 메서드 호출).
 */

public class JdbcMain01 {

	public static void main(String[] args) throws SQLException {
		// select 문장 실행 & 결과 출력. 
		
		// 3. 오라클 JDBC 라이브러리를 사용할 수 있도록 등록.
		DriverManager.registerDriver(new OracleDriver());
		System.out.println("오라클 드라이버 등록 성공");
		
		// 4. 오라클 데이터베이스에 접속(오라클 세션 생성)
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		System.out.println("오라클 접속 성공");
		
		// 5. Statement 타입의 객체 생성
		final String sql = "select * from blogs order by id desc";
		// --> Statement 객체에서 사용하는 SQL 문장은 세미콜론(;)을 사용하지 않음. 
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		// 6-7. SQL 문장 실행 & 결과 처리 
		ResultSet rs = stmt.executeQuery(); // 실행
		
		while (rs.next()) { // --> ResultSet(테이블)에서 행(row)이 있는지 검사.
			// 그 행의 컬럼들에 있는 값을 읽음:
			int id = rs.getInt(col_id); // id 컬럼 값을 읽음.
			String title = rs.getString(col_title); // 테이블의 title 컬럼 값을 읽음. 
			String content = rs.getString(col_content);
			String writer = rs.getString(col_writer);
			LocalDateTime createdtime = rs.getTimestamp(col_created_time).toLocalDateTime();
			LocalDateTime modifiedtime = rs.getTimestamp(col_modified_time).toLocalDateTime();
		
		Blog blog = new Blog(id, title, content, writer, createdtime, modifiedtime);
		System.out.println(blog);
		
		}
		
		// 8. DB 관련 리소스(Connection, Statement, ResultSet) 해제.
		// 리소스를 해제할 때에는 리소스가 생성된 순서의 반대로.
		rs.close(); // ResultSet 해제.
		stmt.close(); // Statement 해제.
		conn.close(); // 오라클 접속 끊기.
		System.out.println("오라클 접속 해제");
		
	}

}
