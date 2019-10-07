package com.biz.ems.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.biz.ems.mapper.EmailDao;
import com.biz.ems.model.EmailVO;

@Service
public class SendMailService {

	@Autowired
	EmailDao eDao;
	@Autowired
	JavaMailSender xMail; 
	
	@Autowired
	ServletContext context;
	
	private final String uploadFolder="C:/bizwork/upload/";
	
	public void sendMail(EmailVO emailVO) {
	      
	      String from_email = emailVO.getEms_from_email();
	     String from_name =emailVO.getEms_from_name();
	      String to_email = emailVO.getEms_to_email();
	      String subject = emailVO.getEms_subject();
	      String content = emailVO.getEms_content();
	      
	      String file1 = emailVO.getEms_file1();
	      String file2 = emailVO.getEms_file2();
	      // Email에 사용되는 문서구조 : MimeMessage 라고한다
	      MimeMessage message = xMail.createMimeMessage();
	      
	      // 메일을 보낼때 사용할 보조도구 선언
	      MimeMessageHelper mHelper;

	      
	      try {
	    	  
	    	  // MimeMessageHelper를 만들때
	    	  //2번째 항목을 true하면 메일에 파일을 첨부할 수 있고,
	    	  // false로 설정하면 순수하면 text만 전송이 된다.
	         mHelper = new MimeMessageHelper(message,true,"UTF-8");
	         mHelper.setFrom(from_email,from_name);
	         mHelper.setTo(to_email);
	         mHelper.setSubject(subject);
	         FileSystemResource fr =null;
	         mHelper.setText(content,true);
	         if(!file1.isEmpty()) {
	         fr=new FileSystemResource(new File(uploadFolder,file1));
	         mHelper.addAttachment(file1, fr);
	         }
	         
	         if(!file2.isEmpty()) {
		         fr=new FileSystemResource(new File(uploadFolder,file2));
		         mHelper.addAttachment(file2, fr);
		         }
	         // fr 파일 리소스를 file1의 이름 으로 첨부하겠다.
	         
	         xMail.send(message);
	      } catch (MessagingException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	   }

	public List<EmailVO> emailList() {
		List<EmailVO> emailList = eDao.selectAll();
		return emailList;
	}

	public int insert(EmailVO emailVO) {
		int ret = eDao.insert(emailVO);
		return ret;
	}

	public EmailVO getContent(long ems_seq) {
		EmailVO eVO = eDao.findBySeq(ems_seq);
		return eVO;
	}

	public int update(EmailVO emailVO) {
		int ret =eDao.update(emailVO);
		return ret;
	}

	public int delete(long ems_seq) {
		int ret = eDao.delete(ems_seq);
		return ret;
	}

	public int countArticle(String search_option, String keyword) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
