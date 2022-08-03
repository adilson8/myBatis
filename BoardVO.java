package org.zerock.myapp.domain;

import lombok.Value;

//2. 직접 작성하지 말고 롬복 어노테이션으로 만들자. 
//@EqualsAndHashCode                 // 중복판단
//@ToString
//@Getter(lombok.AccessLevel.PUBLIC) // 접근제한자를 public으로 설정해준다.
//@AllArgsConstructor

//3. 위에 것들 한번에 만들어주자. cf) DTO는 @Data
@Value 
public class BoardVO {

	// 0) 테이블의 컬럼의 순서와 필드의 순서를 동일하게 작성한다.
	// 1) VO class의 필드는 외부에서 접근하지 못하게 private으로 한다.
	// 2) 테이블의 컬럼은 NULL (결측치)가 있을 수 있기 때문에 기본 타입인 int가 아니고 
	//    결측치를 표현할 수 있는 Integer로 필드를 선언해야한다.
	// 3) INSERT_TS, UPDATE_TS는 한번 작성하면 건드리면 안 되기에 만들지 말자
//	private Integer BNO;
//	private String  TITLE;
//	private String  CONTENT;
//	private String  WRITER;
	
	// 자바 식별자 규칙에 따르면 파이널 상수만이 모두 대문자로 표기하기 때문에 소문자로 바꿔주자
	private Integer bno;
	private String  title;
	private String  content;
	private String  writer;
	

 // 1. 생성자를 직접 작성해보자
//	public BoardVO(Integer BNO, String TITLE, String CONTENT, String WRITER) {
//		this.BNO = BNO;
//		this.TITLE = TITLE;
//		this.CONTENT = CONTENT;
//		this.WRITER = WRITER;
//	} // constructor
//
//	2. 수정불가능(Immutable)한 읽기 전용 객체여야 하기 때문에 (테이블의 값을 조회하는데 값을 수정하면 안 되잖슴)
//	   Setter는 말고 Getter만 만들어준다 (상단탭-source-generate getters and setters)
//	public Integer getBNO() {
//		return BNO;
//	} // getBNO
//
//	public String getTITLE() {
//		return TITLE;
//	} // getTITLE
//
//	public String getCONTENT() {
//		return CONTENT;
//	} // getCONTENT
//
//	public String getWRITER() {
//		return WRITER;
//	} // getWRITER
	
} // end class
