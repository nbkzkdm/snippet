/**
 * 
 */
package jp.sample.holiday.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nbkzk
 *
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "Hello 国民の休日!!");
        return "index";
    }

}
