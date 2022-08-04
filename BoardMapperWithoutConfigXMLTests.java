package org.zerock.myapp.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
import org.zerock.myapp.mapper.BoardMapper;
import org.zerock.myapp.mapper.TimeMapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardMapperWithoutConfigXMLTests {
   
   // 후에라도 동적으로 Mapper Interface or XML 파일등을 등록할 때 필요.
   private Configuration myBatisConfig;
   
   private SqlSessionFactory sqlSessionFactory;
   
   
   // 선처리작업(1회성)으로 마이바티스의 설정을 동적으로 생성(XML설정파일없이...)
   @BeforeAll
   void beforeAll() {
      log.trace("beforeAll() invoked.");
      
      // ------------------------------------
      // Step1. Connection Pool을 제공하는 DataSource(javax.sql.DataSource 규격을 준수하는)를 생성
      // ------------------------------------
      HikariConfig hikariConfig = new HikariConfig();
      
      // 1. 기본적인 JDBC Connection 생성을 위한 연결정보 4가지 설정
      hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@db20220510181503_high?TNS_ADMIN=C:/opt/OracleCloudWallet/ATP");
      hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
      hikariConfig.setUsername("ADMIN");
      hikariConfig.setPassword("Oracle12345678");
      
      // 2. Connection Pool과 관련된 설정
      hikariConfig.setMaximumPoolSize(7);
      hikariConfig.setMinimumIdle(3);
      hikariConfig.setConnectionTimeout(1000 * 3);
      hikariConfig.setConnectionTestQuery("SELECT 1 FROM dual");
      
      log.info("\t+ 1. hikariConfig: {}", hikariConfig);

      // ------------------------------------
      // Step2. Hikari DataSource 생성
      // ------------------------------------
      HikariDataSource dataSource = new HikariDataSource(hikariConfig);   // 자원객체임
      
      Objects.requireNonNull(dataSource);
      log.info("\t+ 2. dataSource: {}, type: {}", dataSource, dataSource.getClass().getName());

      // ------------------------------------
      // Step3. TX 관리자 생성
      // ------------------------------------
      TransactionFactory txFactory = new JdbcTransactionFactory();
      assertNotNull(txFactory);

      // ------------------------------------
      // Step4. 실행환경(Environment) 1개 생성
      // ------------------------------------
      Environment env = new Environment("development", txFactory, dataSource);
      assertNotNull(env);

      // ------------------------------------
      // Step5. Configuration 생성
      // ------------------------------------
      Configuration conf = new Configuration(env);
      assertNotNull(conf);

      // ------------------------------------
      // Step6. Configuration 에 Mapper Interface와 Mapper XML파일 등록
      // ------------------------------------
      conf.addMappers("org.zerock.myapp.mapper");
            
      this.myBatisConfig = conf;

      // ------------------------------------
      // Step7. SqlSessionFactory 생성
      // ------------------------------------
      SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
      this.sqlSessionFactory = builder.build(myBatisConfig);
   } // beforeAll
   
   
//   @Disabled
   @Test
   @Order(1)
   @DisplayName("1. testGetCurrentTime1")
   @Timeout(value=3, unit=TimeUnit.SECONDS)
   void testGetCurrentTime1() {
      log.trace("testGetCurrentTime1() invoked.");
      
//      @Cleanup
      SqlSession sqlSession = this.sqlSessionFactory.openSession();
      
      try (sqlSession) {
         // Dynamic Proxy API로, 지정된 Mapper Interface에 대한 구현객체 획득
         TimeMapper mapper = sqlSession.<TimeMapper>getMapper(TimeMapper.class);
         
         Objects.requireNonNull(mapper);
         log.info("\t+ mapper: {}", mapper);
         
         String now = mapper.getCurrentTime1();
         log.info("\t+ now: {}", now);
      } // try-with-resources
   } // testGetCurrentTime1
   
   
//  @Disabled
  @Test
  @Order(2)
  @DisplayName("2. testSelectAllBoards")
  @Timeout(unit = TimeUnit.SECONDS, value = 30)
  void testSelectAllBoards() {
     log.trace("testSelectAllBoards() invoked.");
 
    @Cleanup
    SqlSession sqlSession = this.sqlSessionFactory.openSession();
   
    BoardMapper mapper = sqlSession.<BoardMapper>getMapper(BoardMapper.class);
    
    List<BoardVO> list = mapper.selectAllBoards(200, "0");
//    log.info("\t+ list: {}", list);
    list.forEach(log::info);
  } // testSelectAllBoards
  
  
  @Test
  @Order(3)
  @DisplayName("3. testSelectBoard")
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void testSelectBoard() {
     log.trace("testSelectBoard() invoked.");
     
     SqlSession sqlSession = this.sqlSessionFactory.openSession();
     
     try(sqlSession) {        
        BoardMapper mapper = sqlSession.<BoardMapper>getMapper(BoardMapper.class);
        BoardVO board = mapper.selectBoard(77);
        
        Objects.requireNonNull(board);
        log.info("\t+ board : {}", board);
     } // try-with-resources
  } // testSelectBoard
  
} // end class
