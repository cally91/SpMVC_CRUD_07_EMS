package com.biz.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.biz.ems.mapper.MemberDao;
import com.biz.ems.model.MemberVO;

@Service
public class MemberService {

	
	@Autowired
	MemberDao mDao;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public MemberVO login_id_check(String m_userid,String m_password) {
		// TODO Auto-generated method stub
		
		MemberVO memberVO =mDao.login_id_check(m_userid);
		if(memberVO != null) {
			//DB에 저장된 암호화된 비밀번호
			String cryptPassword=memberVO.getM_password();
			if(passwordEncoder.matches(m_password, cryptPassword)) {
				//암호가 일치하면 VO를 리턴
				return memberVO;
			}
		}
		return null;
	}

	public int insert(MemberVO memberVO) {
		// plan text : 평문
				String strPassword=memberVO.getM_password();
				//crypt text:암호화된 문자열
				String cryptPassword =passwordEncoder.encode(strPassword);
				
				
				memberVO.setM_password(cryptPassword);
				/*
				 * 최초로 가입되는 회원에게 ADMIN 권한을 부여하고
				 * 이후 부터는 
				 */
				
				List<MemberVO> memList=mDao.selectAll();
				if(memList.size()>0) memberVO.setM_role("USER");
				else memberVO.setM_role("ADMIN");
				int ret=mDao.insert(memberVO);
				return 0;
		
	}
}
