package com.biz.ems.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.biz.ems.model.EmailVO;
import com.biz.ems.service.FileService;
import com.biz.ems.service.SendMailService;

import oracle.net.aso.l;

@Controller
@RequestMapping(value = "/ems")
public class EmailController {
	
	
	
	@Autowired
	SendMailService xMailService;
	
	@Autowired
	FileService fService;
	
	@ModelAttribute("emailVO")
	public EmailVO newEmilVO() {
		return new EmailVO();
	}

	@RequestMapping(value = "/list",method = RequestMethod.POST)
	public String list(EmailVO emailVO,Model model) {
		List<EmailVO> emailList = xMailService.emailList();
		
		model.addAttribute("LIST", emailList);
		model.addAttribute("BODY", "EMAIL_LIST");
		return	"home";
	}
	
	@RequestMapping(value = "/write",method = RequestMethod.GET)
	public String write(@ModelAttribute("emailVO") EmailVO emailVO, Model model) {
		LocalDateTime ldt = LocalDateTime.now();
		String ems_send_date = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

		String ems_send_time = ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();

		emailVO.setEms_send_date(ems_send_date);
		emailVO.setEms_send_time(ems_send_time);
		emailVO.setEms_from_email("cally91@naver.com");
		emailVO.setEms_from_name("냥");
		
		model.addAttribute("emailVO", emailVO);
		model.addAttribute("BODY","WRITE");
		return "home";
	}
	
	@RequestMapping(value = "/write",method = RequestMethod.POST)
	public String write (@ModelAttribute("emailVO") EmailVO emailVO,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2, BindingResult result,Model model) {
		
		String file_name1 = fService.fileUp(file1);
		emailVO.setEms_file1(file_name1);
		String file_name2 = fService.fileUp(file2);
		emailVO.setEms_file2(file_name2);
		xMailService.sendMail(emailVO);
		int ret =xMailService.insert(emailVO);
		return "home";
		
	}
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(@RequestParam("ems_seq") long ems_seq, Model model) {

		EmailVO eVO = xMailService.getContent(ems_seq);

		model.addAttribute("EMS", eVO);
		model.addAttribute("BODY", "EMS_VIEW");

		return "home";

	}
	@RequestMapping(value = "/update",method = RequestMethod.GET)
	public String update(@RequestParam("ems_seq") long ems_seq,Model model) {
		EmailVO emailVO = xMailService.getContent(ems_seq);
		
		model.addAttribute("emailVO", emailVO);
		model.addAttribute("BODY", "WRITE");
		return "home";
	}
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(@ModelAttribute("emailVO") EmailVO emailVO, Model model) {
		int ret = xMailService.update(emailVO);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/delete/{ems_seq}", method = RequestMethod.GET)
	public String delete(@PathVariable long ems_seq, Model model,HttpSession httpSession) {
		int ret =xMailService.delete(ems_seq);
		return"redirect:/";
	}
}
