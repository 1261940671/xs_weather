package xh.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xh.model.WeatherInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author xiehuang
 * @date 2022/05/07 0:06
 */
@RestController
@RequestMapping("download")
public class FileDownloadController {
    @Value("${web.upload-path}")
    private String uploadPath;
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public static RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(30000);
        requestFactory.setReadTimeout(30000);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @RequestMapping(value = "downloadFile", method = RequestMethod.GET)
    public void downloadFile(String name, HttpServletResponse response) {
        if (name == null || name.equals("null") || name.equals("")) {
            return;
        }
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        String filename = uploadPath + name;
        File file = new File(filename);
        // 处理Excel类型的文件
        if (suffix.equals("xlsx")) {
            handleExcel(name);
            filename = uploadPath + "output.xlsx";
        }
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        response.addHeader("Content-Length", "" + file.length());
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            os.write(buffer);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExcel(String name) {
        String filename = uploadPath + name;

        File file = new File(filename);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                XSSFRow row = sheet.getRow(i);
                XSSFCell cell = row.getCell(0);// 第1列：地区编码
                // 过滤掉为空的内容
                if (cell == null || cell.getCellType().equals("") || cell.getCellType() == CellType.BLANK) {
                    continue;
                }
                cell.setCellType(CellType.STRING);
                String url = String.format("http://www.weather.com.cn/data/sk/%s.html", cell.getStringCellValue());
                ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);// 请求数据接口
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WeatherInfo weather = objectMapper.readerFor(WeatherInfo.class).readValue(result.getBody());
                    row.getCell(2).setCellValue(weather.getWeather().getTemp());// 第3列：天气
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(new FileOutputStream(file));// 保存
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
