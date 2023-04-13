/**
 * 
 */
package jp.sample.postal.code.domain.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.common.enums.ErrorType;
import jp.sample.postal.code.common.enums.ExtensionType;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Service
@Log4j2
public class PostalCodeService {

    /** 全国一括ダウンロードzipURL */
    @Value("${jp.sample.postal.ken.all}")
    private String kenAll;
    /** 新規追加データ.zipURLのテンプレート */
    @Value("${jp.sample.postal.temp.add}")
    private String tempAdd;
    /** 廃止データ.zipURLのテンプレート */
    @Value("${jp.sample.postal.temp.del}")
    private String tempDel;
    /** ダウンロードURLのベース部分「{target}」を置換 */
    @Value("${jp.sample.postal.base.url}")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    /** 日付フォーマット：yyMM */
    private final DateTimeFormatter yyMM = DateTimeFormatter.ofPattern("yyMM");

    /**
     * 全国一括データ取得
     * @return
     * @throws PostalCodeException
     */
    public List<PostalCodeCsv> getPostalCodeAllList() throws PostalCodeException {
        return getPostalCode(kenAll);
    }

    /**
     * 新規追加データの差分データ取得
     * @return
     * @throws PostalCodeException
     */
    public List<PostalCodeCsv> getPostalCodeAddList() throws PostalCodeException {
        LocalDate now = LocalDate.now().minusMonths(1);
        return getPostalCode(tempAdd.replace("{YYMM}", now.format(yyMM)));
    }

    /**
     * 廃止データの差分データ取得
     * @return
     * @throws PostalCodeException
     */
    public List<PostalCodeCsv> getPostalCodeDelList() throws PostalCodeException {
        LocalDate now = LocalDate.now().minusMonths(1);
        return getPostalCode(tempDel.replace("{YYMM}", now.format(yyMM)));
    }

    /**
     * ほかのURLのダウンロード
     * @return
     * @throws PostalCodeException
     */
    public List<PostalCodeCsv> getPostalCodeList(String fileName) throws PostalCodeException {
        return getPostalCode(baseUrl.replace("{target}", fileName));
    }

    /**
     * 郵便番号の情報取得
     * @param url
     * @return
     * @throws PostalCodeException
     */
    private List<PostalCodeCsv> getPostalCode(String url) throws PostalCodeException {
        File res = callApi(url);
        File csv = unzip(res);
        File shift_jis = charactorConvertor(csv);
        List<PostalCodeCsv> list = csv(shift_jis);
        res.deleteOnExit();
        csv.deleteOnExit();
        shift_jis.deleteOnExit();
        return list;
    }

    /**
     * 一時ファイルの作成
     * 
     * @return
     * @throws ReportException
     */
    private File makeTempFile(String fileName) throws PostalCodeException {
        try {
            File tempFile = File.createTempFile("Temp_", fileName);
            log.debug(String.format("temp file = %s", tempFile.getAbsolutePath()));
            return tempFile;
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.COULD_NOT_BE_CREATED, e);
        }
    }

    /**
     * 指定URLをダウンロード
     * 
     * @return
     * @throws ReportException
     */
    private File callApi(String url) throws PostalCodeException {
        byte[] fileBytes = restTemplate.getForObject(url, byte[].class);
        File tempFile = makeTempFile(String.format(".%s", ExtensionType.ZIP.getExtension()));
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            outputStream.write(fileBytes);
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.CSV_READING, e);
        }
        return tempFile;

    }

    /**
     * zip解凍
     * @param zipFile
     * @return
     * @throws PostalCodeException
     */
    private File unzip(File zipFile) throws PostalCodeException {
        File tempFile = null;
        Charset shiftJis = Charset.forName("Shift-JIS");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFile));
                ZipArchiveInputStream zais = new ZipArchiveInputStream(bis, shiftJis.name())) {
            ZipArchiveEntry entry = null;
            while ((entry = (ZipArchiveEntry) zais.getNextEntry()) != null) {
                tempFile = makeTempFile(entry.getName());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zais.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                }
            }
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.FILE_READING, e);
        }
        return tempFile;
    }

    /**
     * 文字コード変換
     * @param csvFile
     * @return
     * @throws PostalCodeException
     */
    private File charactorConvertor(File csvFile) throws PostalCodeException {
        File tempFile = makeTempFile(String.format("%s.%s", "_shift_jis", "csv"));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "Shift-JIS"));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.FILE_READING, e);
        }
        return tempFile;
    }

    /**
     * 文字列をCSVにする
     * 
     * @param fileResource
     * @return
     * @throws ReportException
     */
    private List<PostalCodeCsv> csv(File fileResource) throws PostalCodeException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(PostalCodeCsv.class).withHeader();
        try {
            MappingIterator<PostalCodeCsv> objectMappingIterator =
                    csvMapper.readerFor(PostalCodeCsv.class).with(csvSchema).readValues(fileResource);
            List<PostalCodeCsv> retList = new ArrayList<>();
            while (objectMappingIterator.hasNext()) {
                retList.add(objectMappingIterator.next());
            }
            return retList;
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.CSV_READING, e);
        }
    }

}
