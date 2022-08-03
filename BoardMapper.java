package org.zerock.myapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.myapp.domain.BoardVO;

// MyBatis가 수행할 SQL 문장을 저장하는 자바 인터페이스 => Mapper Interface라고 부름
public interface BoardMapper {

	// MyBatis가 제공하는 Annotation의 속성에 SQL 문장을 저장
	// 마이바티스의 SQL 문장에서, 바인드 변수는 #{변수명} 형식으로 기재함
	// @Param (바인드 변수명) 형태로 사용해서 바인드 변수에 값 전달
	@Select("SELECT * FROM tbl_board WHERE bno > #{theBno} AND title LIKE '%'||#{search}||'%'")
	public abstract List<BoardVO> selectAllBoards(@Param("theBno")Integer bno, @Param("search")String title); // 여러 값이니까 List 객체이용
	
	@Select("SELECT * FROM tbl_board WHERE bno = #{theBno}")
	public abstract BoardVO selectBoard(@Param("theBno") Integer bno);
	
} // end interface
