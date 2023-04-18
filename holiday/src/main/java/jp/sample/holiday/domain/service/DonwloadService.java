/**
 * 
 */
package jp.sample.holiday.domain.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jp.sample.holiday.common.HolidayException;
import jp.sample.holiday.common.enums.ErrorType;
import jp.sample.holiday.domain.model.HolidayCsv;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Service
@Log4j2
public class DonwloadService {

    /** 内閣府の国民の休日のURL */
    @Value("${jp.sample.cabinet.office.shukujitsu}")
    private String url;

    private RestTemplate restTemplate = new RestTemplate();;

    /**
     * <code>https://www8.cao.go.jp/chosei/shukujitsu/syukujitsu.csv</code>をダウンロードして、文字列にする。
     * @return
     * @throws ReportException
     */
    public String callApi() throws HolidayException {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        try {
            return new String(response.getBody().getBytes("ISO-8859-1"), "Shift-JIS");
        }
        catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new HolidayException(jp.sample.holiday.common.enums.ErrorType.UNSUPPORTED_ENCODING, e);
        }
    }

    /**
     * 文字列をCSVにする
     * @param csvString
     * @return
     * @throws ReportException
     */
    public List<HolidayCsv> csv(String csvString) throws HolidayException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(HolidayCsv.class).withHeader();
        try {
            MappingIterator<HolidayCsv> objectMappingIterator =
                    csvMapper.readerFor(HolidayCsv.class).with(csvSchema).readValues(csvString);
            List<HolidayCsv> retList = new ArrayList<>();
            while (objectMappingIterator.hasNext()) {
                retList.add(objectMappingIterator.next());
            }
            return retList;
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HolidayException(ErrorType.CSV_READING, e);
        }
    }
}
