package org.zerock.myapp.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.domain.BoardVO;
import org.zerock.myapp.mapper.BoardMapper;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)

public class BoardMapperTests {//	Mybatis Framework 객체는 바로 SqlSessionFactory 객체임
//	 1) SqlSessionFactoryBuilder를 통해 생성
//	 2) 이 Builder 객체를 통해 핵심객체인 SqlSessionFactory 객체를 생성하려면
//	    마이바티스의 설정파일 mybatis-config.xml에 대한 InputStream 객체가 필요함
	
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
	@DisplayName("1. testGetCurrentTime")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testGetCurrentTime( ) throws SQLException {
		log.trace("testGetCurrentTime() invoked.");
		
		// 얘는 밑에서 try-with-resources로 닫아보자
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// 마이바티스는 다수개의 Mapper XML 파일이 있을 때 아래와 같이 2개의 값을 이용해서 
		// 어떤 Mapper XML 파일을 사용할지 결정하고 (by "namespace")
		// 어떤 태그의 SQL문장을 사용할지 결정      (by "id")
		try (sqlSession) {
			String namespace = "BoardMapper";
			String sqlId = "getCurrentTime";			
			String sql = namespace + "." + sqlId; // Mapped Statement
			
			String now = sqlSession.<String>selectOne(sql);
			assert now != null;
			log.info("\t+ now : {}", now);
			
		} // try-with-resources (Auto-closeable 해야 사용가능)
		
	} // testGetCurrentTime
	
	@Test
	@Order(2)
	@DisplayName("2. testSelectAllBoards")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testSelectAllBoards(){
		log.trace("testSelectAllBoards() invoked.");
	
		@Cleanup // 이번에는 Cleanup으로 닫자
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		BoardMapper mapper = sqlSession.<BoardMapper>getMapper(BoardMapper.class);
		
		assertNotNull(mapper);
		log.info("\t+ mapper : {}", mapper);		
		
		List<BoardVO> list = mapper.selectAllBoards(200, "6");
		
		Objects.requireNonNull(list);
		list.forEach(log::info);
	} // testSelectAllBoards
	
	@Test
	@Order(3)
	@DisplayName("3. testSelectBoard")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testSelectBoard(){
		log.trace("testSelectBoard() invoked.");
	
		@Cleanup // 이번에는 Cleanup으로 닫자
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// 지정한 Mapper Interface의 구현객체인 MapperProxy 획득
		BoardMapper mapper = sqlSession.<BoardMapper>getMapper(BoardMapper.class);
		
		assertNotNull(mapper);
		log.info("\t+ mapper : {}", mapper);		
		
		BoardVO board = mapper.selectBoard(199);
		
		Objects.requireNonNull(board);
		log.info("\t+ board : {}", board);
	} // testSelectBoard
	
	@Test
	@Order(4)
	@DisplayName("4. testSelectAllBoardsByMapperXML")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void testSelectAllBoardsByMapperXML( ) throws SQLException {
		log.trace("testGetCurrentTime() invoked.");
		
		// 얘는 밑에서 try-with-resources로 닫아보자
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// 마이바티스는 다수개의 Mapper XML 파일이 있을 때 아래와 같이 2개의 값을 이용해서 
		// 어떤 Mapper XML 파일을 사용할지 결정하고 (by "namespace")
		// 어떤 태그의 SQL문장을 사용할지 결정      (by "id")
		try (sqlSession) {
			String namespace = "BoardMapper";
			String sqlId = "selectAllBoards";			
			String sql = namespace + "." + sqlId; // Mapped Statement
			
			Map<String, Object> params = new HashMap<>();
			params.put("theBno", 200);
			params.put("search", "7");
			
			List<BoardVO> list = sqlSession.<BoardVO>selectList(sql, params);
			assert list != null;
			list.forEach(log::info);
			
		} // try-with-resources (Auto-closeable 해야 사용가능)
		
	} // testGetCurrentTime

} // end class
