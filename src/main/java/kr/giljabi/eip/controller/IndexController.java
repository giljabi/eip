package kr.giljabi.eip.controller;

import kr.giljabi.eip.dto.request.SubjectQuestionDTO;
import kr.giljabi.eip.service.QNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
    private final QNameService qNameService;

    @Autowired
    public IndexController(QNameService qNameService) {
        this.qNameService = qNameService;
    }

    @RequestMapping("/")
    public String index(Model model){
        List<SubjectQuestionDTO> questions = qNameService.findByQnameCount();
        model.addAttribute("quizzes", questions);
        return "index";
    }
}





