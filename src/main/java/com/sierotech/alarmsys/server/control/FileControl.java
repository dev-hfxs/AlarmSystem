/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月15日
* @修改人: 
* @修改日期：
* @描述: 文件简要描述
 */
package com.sierotech.alarmsys.server.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sierotech.alarmsys.common.BusinessException;
import com.sierotech.alarmsys.context.AppContext;


/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年4月15日
* @功能描述: 文件上传响应类
 */
@Controller
@RequestMapping("/file")
@Scope("request")
public class FileControl {
	static final Logger log = LoggerFactory.getLogger(FileControl.class);

	
	/*
	 * 上传报警图片文件
	 * */
	@RequestMapping(value = "/upload/alarmImage", method = RequestMethod.POST)
	@ResponseBody
	public String uploadTemp(HttpServletRequest request) {
		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest ) request;
		if(mreq.getFileMap()!=null) {
			for(String key: mreq.getFileMap().keySet()) {
				MultipartFile fileT = mreq.getFileMap().get(key);
				
				String targetDir = AppContext.getAlarmImageDir();

				// 如果文件不为空，写入上传路径
				if (!fileT.isEmpty()) {
					// 上传文件名
					String filename = fileT.getOriginalFilename();
					String alarmId = request.getParameter("alarmId");
					if(alarmId == null) {
						throw new BusinessException("上传报警图片失败, 缺少报警ID.");
					}
					int index = filename.lastIndexOf(".");
					String postfix = filename.substring(index + 1);
					if (!("jpg".equals(postfix) || "png".equals(postfix))) {
						throw new BusinessException("文件类型不正确, 请选择jpg、jpg文件.");
					}
					String newFileName = alarmId + File.separator + filename;					
					File filepath = new File(targetDir, newFileName);
					// 判断路径是否存在，如果不存在就创建一个
					if (!filepath.getParentFile().exists()) {
						filepath.getParentFile().mkdirs();
					}
					
					// 将上传文件保存到目标文件当中
					try {
						fileT.transferTo(new File(targetDir + File.separator + newFileName));
					} catch (Exception e) {
						throw new BusinessException("文件存储错误.");
					}
				}
			}	
		}
		return "success";
	}
	
	
	/**
     * 报警图片下载
     */
    @RequestMapping(value = "/download/alarmimage", method = RequestMethod.GET)
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
    	String alarmId = request.getParameter("alarmId");
    	String fileName = request.getParameter("fileName");
    	
        if (alarmId != null && fileName != null) {
        	
            String realPath = AppContext.getAlarmImageDir();
            String fullPath = realPath + File.separator + alarmId + File.separator + fileName;
            File file = new File(fullPath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                String encodeFileName = "";
				try {
					encodeFileName = URLEncoder.encode(fileName,"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					log.info(e1.getMessage());
				}
				response.addHeader("Content-Disposition","attachment;filename=" + encodeFileName );// 设置文件名
				byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                        	log.info(e.getMessage());
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                        	log.info(e.getMessage());
                        }
                    }
                }
            }else {
            	request.setAttribute("errorInfo", "访问的文件："+fileName+"不存在!");
            	return "error/resIsvalid";
            }
        }
        return null;
    }
}
