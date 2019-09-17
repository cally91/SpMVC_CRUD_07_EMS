package com.biz.ems.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.biz.ems.model.BBsDto;
import com.biz.ems.model.BBsReqDto;
import com.biz.ems.model.MemberVO;
import com.biz.ems.service.AjaxFileService;
import com.biz.ems.service.BBsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({"bbsReqDto"})
@RequestMapping(value = "/bbs")
public class BBsController {

	@Autowired
	BBsService bbsService;

	
	@Autowired
	AjaxFileService aFService;

	/*
	 * 현재의 controller내의 어떤 메서드에서 BBsVO를 객체로 취급하여 값을 setter, getter 하려고 시도할때 만약 객체가
	 * 초기화 되지 않아 NullPointException이 발생하려고 하면 이 메서드가 자동으로 호출되어 bbsVO 객체를 생성 및 초기화 한다
	 */
	@ModelAttribute("bbsReqDto")
	public BBsReqDto newBBsVO() {
		return new BBsReqDto();
	}

	@RequestMapping(value = "/free", method = RequestMethod.GET)
	public String list(Model model) {

		// List<BBsVO> bbsList = bbsService.bbsList();

		List<BBsDto> bbsList = bbsService.bbsList();
		model.addAttribute("LIST", bbsList);
		model.addAttribute("BODY", "BBS_LIST");
		return "home";

	}

	@RequestMapping(value = "/album", method = RequestMethod.GET)
	public String album(Model model) {

		List<BBsDto> bbsList = bbsService.bbsListForFile();

		model.addAttribute("LIST", bbsList);
		model.addAttribute("BODY", "BBS_ALBUM");
		return "home";

	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@ModelAttribute("bbsReqDto") BBsReqDto bbsReqDto, Model model, HttpSession httpSession) {

		MemberVO memberVO = (MemberVO) httpSession.getAttribute("LOGIN");
		if(memberVO != null) bbsReqDto.setBbs_auth(memberVO.getM_userid());
		
		LocalDateTime ldt = LocalDateTime.now();
		String curDate = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

		String curTime = ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();

		bbsReqDto.setBbs_date(curDate);
		bbsReqDto.setBbs_time(curTime);
		model.addAttribute("bbsReqDto", bbsReqDto);

		model.addAttribute("BODY", "BBS_WRITE");
		return "home";

	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write_up(@ModelAttribute("bbsReqDto") @Valid BBsReqDto bbsReqDto,BindingResult result, SessionStatus sStatus, Model model) {


		
		if(result.hasErrors()) {
			model.addAttribute("BODY", "BBS_WRITE");
			return "home";
		}
		int ret = bbsService.insert(bbsReqDto);
		
		sStatus.setComplete();
		return "redirect:/free";

	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(@RequestParam long bbs_seq, Model model) {

		BBsDto bbsDto = bbsService.getContent(bbs_seq);

		model.addAttribute("BBS", bbsDto);
		model.addAttribute("BODY", "BBS_VIEW");

		return "home";

	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam long bbs_seq, Model model,HttpSession httpSession) {
		
		BBsDto bbsDto=bbsService.getContent(bbs_seq);
		model.addAttribute("bbsReqDto", bbsDto);
		model.addAttribute("BODY", "BBS_WRITE");		
		return "home";

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("bbsReqDto") BBsReqDto bbsReqDto,SessionStatus sStatus,  Model model) {

		int ret = bbsService.update(bbsReqDto);
		sStatus.setComplete();
		return "redirect:/free";

	}


	@RequestMapping(value = "/reply/{bbs_seq}", method = RequestMethod.GET)
	public String reply(
			@PathVariable long bbs_seq,
			@ModelAttribute("bbsReqDto") BBsReqDto bbsReqDto, Model model,HttpSession httpSession) {

		BBsDto bbsDto = bbsService.getContent(bbs_seq);

		MemberVO memberVO =(MemberVO)httpSession.getAttribute("LOGIN");
		if(memberVO==null) {
			model.addAttribute("LOGIN_MSG", "LOGIN");
			return"redirect:/member/login";
		}
		 bbsDto=bbsService.getContent(bbs_seq);
		if (!memberVO.getM_userid().equalsIgnoreCase(bbsDto.getBbs_auth())) {
			model.addAttribute("LOGIN_MSG", "REPLY");
			return"redirect:/member/login";
		}
		
		
		LocalDateTime ldt = LocalDateTime.now();
		String curDate = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

		String curTime = ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();

		bbsReqDto.setBbs_date(curDate);
		bbsReqDto.setBbs_time(curTime);

		bbsReqDto.setBbs_main_seq(bbs_seq);
		bbsReqDto.setBbs_title("re " + bbsDto.getBbs_title());

		

		model.addAttribute("bbsReqDto", bbsReqDto);
		model.addAttribute("BODY", "BBS_WRITE");

		return "home";
	}

	@RequestMapping(value = "/reply/{bbs_seq}", method = RequestMethod.POST)
	public String reply_write(
			@ModelAttribute("bbsReqDto") BBsReqDto bbsReqDto,SessionStatus sStatus,  Model model) {
		
		
		bbsService.insert(bbsReqDto);
		sStatus.setComplete();
		return "redirect:/free";

	}
	
	
	@RequestMapping(value = "/delete/{bbs_seq}", method = RequestMethod.GET)
	public String delete(@PathVariable long bbs_seq, Model model,HttpSession httpSession) {
		int ret =bbsService.delete(bbs_seq);
		return"redirect:/free";

	}

}
