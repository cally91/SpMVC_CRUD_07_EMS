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
public class BBsDto {

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

	private List<FileVO> bbs_files;
	private List<BBsReqDto> bbs_rep_list;
}
