/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年9月4日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.common.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年9月4日
* @功能描述: 
 */
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	public static boolean deleteDirectoryFile(String dir, String excludeFile) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            log.info("删除目录失败：{}不存在！", dir);
            return false;
        }

        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
        	if(files[i].isFile()) {
        		if(files[i].getName().equals(excludeFile)) {
            		//不需要删除的文件
            		continue;
            	}
                // 删除文件
                if (files[i].isFile()) {
                    files[i].delete();
                }
        	}
        }
        return true;
    }
	
}
