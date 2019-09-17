package com.biz.ems.mapper;

import org.apache.ibatis.jdbc.SQL;

public class EmailSQL {

	public String ems_insert_sql() {

		SQL sql = new SQL() {
			{

				INSERT_INTO("tbl_ems");
				INTO_COLUMNS("ems_seq").INTO_VALUES("#{ems_seq}");
				INTO_COLUMNS("ems_to_email").INTO_VALUES("#{ems_to_email}");
				INTO_COLUMNS("ems_from_email").INTO_VALUES("#{ems_from_email}");
				INTO_COLUMNS("ems_to_name").INTO_VALUES("#{ems_to_name}");
				INTO_COLUMNS("ems_from_name").INTO_VALUES("#{ems_from_name}");
				INTO_COLUMNS("ems_send_date").INTO_VALUES("#{ems_send_date}");
				INTO_COLUMNS("ems_send_time").INTO_VALUES("#{ems_send_time}");
				INTO_COLUMNS("ems_subject").INTO_VALUES("#{ems_subject}");
				INTO_COLUMNS("ems_content").INTO_VALUES("#{ems_content}");
				INTO_COLUMNS("ems_file1").INTO_VALUES("#{ems_file1}");
				INTO_COLUMNS("ems_file2").INTO_VALUES("#{ems_file2}");

			}
		};

		return sql.toString();

	}

	public String ems_update_sql() {

		SQL sql = new SQL() {
			{

				UPDATE("tbl_ems");
				SET("ems_to_email = #{ems_to_email}");
				SET("ems_from_email = #{ems_from_email} ");
				SET("ems_to_name = #{ems_to_name}");
				SET("ems_from_name = #{ems_from_name}");
				SET("ems_send_date = #{ems_send_date}");
				SET("ems_send_time = #{ems_send_time}");
				SET("ems_subject = #{ems_subject}");
				SET("ems_content = #{ems_content}");
				SET("ems_file1 = #{ems_file1}");
				SET("ems_file2 = #{ems_file2}");
				WHERE("ems_seq = #{ems_seq}");

			}
		};

		return sql.toString();
	}
}
