/**
 * 
 */
package jp.sample.postal.code.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.sample.postal.code.api.process.CallProcess;
import jp.sample.postal.code.common.enums.ZipcodeUrl;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Controller
@RequestMapping("/view")
@Log4j2
public class ViewController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "Hello 郵便番号!!");
        model.addAttribute("prefectureList", ZipcodeUrl.getPrefectureList());
        return "index";
    }

}
