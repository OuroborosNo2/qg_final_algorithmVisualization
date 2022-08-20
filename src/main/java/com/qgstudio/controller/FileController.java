package com.qgstudio.controller;

import com.alibaba.fastjson.JSONObject;
import com.qgstudio.constant.ResultEnum;
import com.qgstudio.service.HttpService;
import com.qgstudio.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: OuroborosNo2
 * @create: 2022-08-17
 **/
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpService httpService;

    @Value("${qg-final-algorithm.resources-path}")
    private String resourcesPath;
    @Value("${qg-final-algorithm.to-send-url}")
    private String toSendUrl;
    @Value("${qg-final-algorithm.static-path}")
    private String staticPath;

    /**上传图片
     * @param f1 上传的文件
     * @param algorithm 算法
     * @param isDP 是否使用DP
     * @param rc rc参数
     * @return 结果集
     * */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file1") MultipartFile f1, @RequestParam String algorithm, @RequestParam boolean isDP, @RequestParam String rc){
        JSONObject result;
        try {
            switch(algorithm){
                case "HSB":
                case "DSG":
                case "MWMS-J":
                case "MWMS-S":
                case "RSRSP":
                    break;
                default:
                    return new Result(400,"无此算法");
            }

            String originalFilename = f1.getOriginalFilename();
            //文件后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            /*switch (suffix){
                case ".csv":
                case ".npy":
                case ".mat":
                case ".txt":
                case ".xlsx":
                case ".xls":
                    break;
                default:
                    return new Result(400,"文件格式错误");
            }*/

            String fileName = "custom" + suffix;
            String destFilePath = resourcesPath + fileName;
            File destFile = new File(resourcesPath);

            if(destFile.exists()) {
                //先删除目录下文件
                File[] files = destFile.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }else{
                destFile.mkdirs();
            }
            destFile = new File(destFilePath);
            //调用transferTo将上传的文件保存到指定的地址
            f1.transferTo(destFile);

            //发送http请求给人工智能组处理
            Map<String,Object> params = new HashMap<>();
            params.put("file",destFilePath);
            params.put("algorithm",algorithm);
            params.put("DP",isDP);
            params.put("rc",rc);
            result = JSONObject.parseObject(httpService.sendRequest(toSendUrl, params));
        }catch (IOException e){
            e.printStackTrace();
            return new Result(ResultEnum.SYSTEM_ERR);
        }catch (IllegalStateException ex){
            ex.printStackTrace();
            return new Result(500,"Calculator does not start");
        }
        return new Result(ResultEnum.SUCCESS,result);

    }

    @GetMapping("/list")
    public Result getFileList(){
        List<String> result = new ArrayList<>();
        File f = new File(staticPath);
        File[] files = f.listFiles();
        if(files!=null) {
            for (File file : files) {
                if(file.isFile()){
                    result.add(file.getName());
                }
            }
            return new Result(result);
        }else{
            return new Result("");
        }
    }
}
