package com.biz.ems.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.ScriptAssert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ScriptAssert(lang="javascript",
		script= "_this.m_password == this.m_re-password",
		reportOn= "m_re-password",
		message = "비밀번호와 비밀번호 확인이 일치하지않습니다.")
public class MemberVO {
	
	@NotBlank(message = "* 사용자 ID는 반드시 입력해야합니다." )
	@NotNull(message = "* 사용자 ID는 반드시 입력해야합니다.")	
	@Size(min=5,max=15,message ="* 사용자 id은 5자 이상 15자 이하만 " )
	private String m_userid; // varchar2(15 byte)
	private String m_password; // varchar2(64 byte)
	private String m_re_password;
	private String m_name; // nvarchar2(30 char)
	
	@Email(message = "* 이메일 형식을 확인하세요")
	private String m_email; // varchar2(25 byte)
	
	@Pattern(regexp = "\\d[1,15]",message = "* 전화번호는 숫자 1부터 15자리까지 가능" )
	private String m_tel; // varchar2(20 byte)
	private String m_role; // varchar2(5 byte)
}
