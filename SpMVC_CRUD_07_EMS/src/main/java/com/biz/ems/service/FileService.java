package com.biz.ems.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sun.mail.handlers.multipart_mixed;

@Service
public class FileService {

	private final String uploadFolder="C:/bizwork/upload/";
	
	public String fileUp(MultipartFile file) {
		String FileName =file.getOriginalFilename();
		File saveFile =new File(uploadFolder,FileName);
		
		try {
			file.transferTo(saveFile);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FileName;
		
	}
}
