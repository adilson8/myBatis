package org.zerock.myapp.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.domain.BoardVO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DynamicSQLTests {
	
	// 1. SqlSessionFactory 객체 생성
	private SqlSessionFactory sqlSessionFactory;
	
	@BeforeAll // 1회성 전처리 작업 수행
	void beforeAll() throws IOException {
		log.trace("beforeAll() invoked.");
				
		// 2. 마이바티스의 설정파일(mybatis-config.xml)에 대한 입력스트림 객체 is를 생성하자
		
		// InputStream을 얻을 경로를 필드에 저장해주자		
		String path = "mybatis-config.xml";
		
		@Cleanup // 자원객체 썼으면 닫자
		InputStream is = Resources.getResourceAsStream(path);

		// 단 한번 SqlSessionFactory 공장객체를 생성해서, 필드에 저장
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		this.sqlSessionFactory = builder.build(is);
		
		// 객체가 null인지 여부 검증
		assert this.sqlSessionFactory != null;
		log.info("\t+ this.sqlSessionFactory : {}", this.sqlSessionFactory);
		
	} // beforeAll
	
	@Test
	@Order(1)
	@DisplayName("1.testFindBoardsByBno")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testFindBoardsByBno() {
		log.trace("testFindBoardsByBno() invoked.");
		
		SqlSession sqlSession = this. sqlSessionFactory.openSession();
		
		try(sqlSession){
			String namespace = "mappers.Board2Mapper";
			String id = "findBoardsByBno";
			String sql = namespace + "." + id;
			
			List<BoardVO> board = sqlSession.<BoardVO>selectList(sql, null);
			
			Objects.requireNonNull(board);
			log.info("\t+ board : {}", board);
		} // try-with-resources
		
	} // testFindBoardsByBno
	
	@Test
	@Order(2)
	@DisplayName("2.testFindBoardsByTitle")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testFindBoardsByTitle() {
		log.trace("testFindBoardsByTitle() invoked.");
		
		SqlSession sqlSession = this. sqlSessionFactory.openSession();
		
		try(sqlSession){
			String namespace = "mappers.Board2Mapper";
			String id = "findBoardsByTitle";
			String sql = namespace + "." + id;
			
			List<BoardVO> board = sqlSession.<BoardVO>selectList(sql, "3");
			
			Objects.requireNonNull(board);
			log.info("\t+ board : {}", board);
		} // try-with-resources
		
	} // testFindBoardsByTitle
	
	@Test
	@Order(3)
	@DisplayName("3.testFindBoardsByWriter")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testFindBoardsByWriter() {
		log.trace("testFindBoardsByTitle() invoked.");
		
		SqlSession sqlSession = this. sqlSessionFactory.openSession();
		
		try(sqlSession){
			String namespace = "mappers.Board2Mapper";
			String id = "findBoardsByWriter";
			String sql = namespace + "." + id;
			
			List<BoardVO> board = sqlSession.<BoardVO>selectList(sql, "9");
			
			Objects.requireNonNull(board);
			log.info("\t+ board : {}", board);
		} // try-with-resources
		
	} // testFindBoardsByWriter
	
	@Test
	@Order(4)
	@DisplayName("4.testFindBoardsByBnoAndTitle")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testFindBoardsByBnoAndTitle() {
		log.trace("testFindBoardsByTitle() invoked.");
		
		SqlSession sqlSession = this. sqlSessionFactory.openSession();
		
		try(sqlSession){
			String namespace = "mappers.Board2Mapper";
			String id = "findBoardsByBnoAndTitle";
			String sql = namespace + "." + id;
			
			Map<String, Object> params = new HashMap<>();
			params.put("bno", 100);
			params.put("title", "7");
			
			List<BoardVO> list = sqlSession.<BoardVO>selectList(sql, params);
			
			Objects.requireNonNull(list);
			log.info("\t+ board : {}", list);
		} // try-with-resources
		
	} // testFindBoardsByBnoAndTitle

}
