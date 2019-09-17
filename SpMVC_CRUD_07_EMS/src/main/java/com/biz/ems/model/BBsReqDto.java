package com.biz.ems.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BBsReqDto {

	/*
	 * validation에서 사용할수 있는 유효성 검사 항목들
	 * @NotBlank:빈칸 이어서는 안된다.
	 * @NotNull: Null값이어서는 안된다.
	 * @ Null :반드시 NULL이어야한다
	 * @Size(min=x, max=y):개수가 x개 부터 y개까지만 인정
	 * @Max(x) :숫자가 x이상이면 안된다.
	 * @Min(x) : 숫자가 y이하이면 안된다.
	 * 
	 * @DacimaMax(x) 10진수 x이상이면안된다.
	 * @DacimaMin(x) :10진수 X이상이면 안된다.
	 * 
	 * @Email :이메일 형식이 아니면안된다.
	 * 
	 */
	private long bbs_seq;// NUMBER PRIMARY KEY,
	private long bbs_main_seq;// NUMBER,
	private int bbs_layer;// NUMBER,
	private String bbs_date;// VARCHAR2(10),
	private String bbs_time;// VARCHAR2(20),
	
	@NotBlank(message = "사용자 이름을 입력해야합니다")
	@Email(message = " * 사용자이름은 이메일 형식이어야합니다 ")
	private String bbs_auth;// nVARCHAR2(50),
	private String bbs_title;// nVARCHAR2(255),
	private String bbs_content;// nVARCHAR2(1000)

	private List<String> bbs_files;
	private List<String> bbs_otigin_files;

}
