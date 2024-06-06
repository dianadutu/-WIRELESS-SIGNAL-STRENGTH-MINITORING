/*historycontroller*/

package com.licenta.wireless.Controller;

import com.licenta.wireless.Model.HistoryModel;
import com.licenta.wireless.util.HistoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private HistoryUtil historyUtil;

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<HistoryModel> userProfiles = historyUtil.parseHtml();
        model.addAttribute("userProfiles", userProfiles);
        return "history";
    }


    @GetMapping("/api/history")
    public List<HistoryModel> getHistory() {
        return historyUtil.parseHtml();
    }
}
