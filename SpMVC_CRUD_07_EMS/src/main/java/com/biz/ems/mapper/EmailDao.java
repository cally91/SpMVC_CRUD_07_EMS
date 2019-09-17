package com.biz.ems.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;

import com.biz.ems.model.EmailVO;

public interface EmailDao {

	@Select(" SELECT * FROM tbl_ems ORDER BY ems_seq  DESC")
	public List<EmailVO> selectAll();
	@Select(" SELECT * FROM tbl_ems WHERE ems_seq=#{ems_seq} ")
	public EmailVO findBySeq(long ems_seq);
	
	public List<EmailVO> fileByFrom(String ems_from);
	public List<EmailVO> fileByTo(String ems_email);
	
	/*
	 * 매개변수가 2개 이상일경우는
	 * 반드기 @param으로 변수 이름을 명시해 주어야한다.
	 */
	public List<EmailVO> fileByFromAndTo(@Param("ems_from_email")String ems_from,@Param("ems_to_email") String ems_to_email);
	
	@InsertProvider(type = EmailSQL.class,method = "ems_insert_sql")
	@SelectKey(keyProperty = "ems_seq",statement= " SELECT SEQ_EMS.NEXTVAL FROM DUAL ",resultType = Long.class,before = true )
	public int insert(EmailVO emailVO);
	@UpdateProvider(type = EmailSQL.class,method = "ems_update_sql")
	public int update(EmailVO emailVO);
	@Delete("DELETE FROM tbl_ems WHERE ems_seq =#{ems_seq}")
	public int delete(long ems_seq);
}
