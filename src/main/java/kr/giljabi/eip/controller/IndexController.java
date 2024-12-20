package kr.giljabi.eip.controller;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.service.QNameService;
import kr.giljabi.eip.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class IndexController {
    private final QNameService qNameService;

    @Autowired
    public IndexController(QNameService qNameService) {
        this.qNameService = qNameService;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model){
        log.info("index ip: {}", CommonUtils.getClientIp(request));
        List<SubjectQuestionDTO> questions = qNameService.findByQnameCount();
        model.addAttribute("quizzes", questions);
        return "index";
    }
}





