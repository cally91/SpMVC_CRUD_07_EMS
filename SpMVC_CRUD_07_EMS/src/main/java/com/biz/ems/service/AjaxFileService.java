package com.biz.ems.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.biz.ems.mapper.FileDao;
import com.biz.ems.model.BBsReqDto;
import com.biz.ems.model.FileVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AjaxFileService {

	@Autowired
	FileDao fDao;
	/*
	 * 변수에 final키워드가 붙으면 이 변수의 값을 변경할 수가없다. 상수
	 */
	private final String upLoadFolder = "c:/bizwork/upload/";

	/*
	 * 여러개의 파일을 업로드 하는 method
	 */
	public List<FileVO> uploads(MultipartHttpServletRequest files) {
		List<MultipartFile> fileList = files.getFiles("files");
		List<FileVO> fileVOList = new ArrayList<FileVO>();

		for (MultipartFile file : fileList) {

			String saveName = this.upLoad(file);

			log.debug("FILENAME" + file.getOriginalFilename());
			fileVOList.add(FileVO.builder().file_origin_name(file.getOriginalFilename()).file_name(saveName).build());
		}
		return fileVOList;
	}

	/*
	 * 한개의 파일을 업로드하는 메소드
	 */
	public String upLoad(MultipartFile file) {

		// 업로드할 파일 정보가 없으면
		// 더이상 코드 진행금지.
		if (file.isEmpty() || file == null)
			return null;

		String originName = file.getOriginalFilename();
		String uuString = UUID.randomUUID().toString();
		String saveName = uuString + "_" + originName;

		// 파일을 업로드 하기전에
		// 저장할 폴더(디렉토리)가 없으면
		// 새로운 디렉토리를 생성
		File saveDir = new File(upLoadFolder);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		File saveFile = new File(upLoadFolder, saveName);
		log.debug("ORIGIN" + saveName);

		try {
			file.transferTo(saveFile);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveName;
	}

	public boolean file_delete(long file_seq) {

		// 1. 파일 정보 추출
		FileVO fileVO = fDao.findBySeq(file_seq);

		// 2. 파일의 물리적 경로 생성
		File delFile = new File(upLoadFolder, fileVO.getFile_name());

		// 3. 파일이 있는지 확인 한 후
		if (delFile.exists()) {

			// 4. 파일 삭제
			delFile.delete();

			// 5. DB 정보 삭제
			fDao.delete(file_seq);

			return true;
		}

		return false;

	}

	/*
	 * mybatis에서 transaction 방식으로 다중쿼리를 실행하라.
	 */
	@Transactional
	public int insert(BBsReqDto bbsReqDto) {
		List<String> bbs_files = bbsReqDto.getBbs_files();
		/*
		 * 파일을 drag upload를 하지않고 글저장 버튼을 클릭하면
		 * bbs_files에 아무런 값이 없어서 아래코드가 오휴를
		 * 일으킨다
		 * 이때 bbs_files가 없으면 코드를 중단하고록
		 */
		if (bbs_files ==null)return -1;
		for (String file_name : bbs_files) {

			// 1. 각항목을 변수에 담고 최종적으로 insert을 수행하기
			long bbs_seq = bbsReqDto.getBbs_seq();
			// uuid를 제거하고origin 이름을 추출
			String file_origin_name = file_name.substring(37);

			FileVO fVO = FileVO.builder().file_name(file_name).file_origin_name(file_origin_name).file_bbs_seq(bbs_seq)
					.build();
			fDao.insert(fVO);
		}
		return 0;
	}

	/*
	 * 첨부파일 삭제절차
	 * 1. tbl_bbs_file에서 첨부파일 목록 추출
	 * 2. 해당하는 실제 파일을 삭제
	 * 3. table에 해당 정보 삭제
	 */
	public int files_delete(long bbs_seq) {
		
		List<FileVO> fileList =fDao.findbbsSeq(bbs_seq);
		for(FileVO fileVO :fileList) {
			File delFile = new File(upLoadFolder,fileVO.getFile_name());
			if(delFile.exists()) delFile.delete();
		}
		int ret= fDao.deletes(bbs_seq);
		return ret;
	}
}
