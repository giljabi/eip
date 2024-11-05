package kr.giljabi.eip.controller;

import kr.giljabi.eip.dto.request.UserResultDTO;
import kr.giljabi.eip.service.ResultsService;
import kr.giljabi.eip.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class ResultsController {

    private final ResultsService resultService;

    @Autowired
    public ResultsController(ResultsService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/results/{qid}")
    public String getResultByUuid(@PathVariable Integer qid,
                                  HttpServletRequest request,
                                  Model model) {
        String uuid = CommonUtils.getCookieValue(request, CommonUtils.UUID_COOKIE_NAME);
        List<UserResultDTO> results = resultService.findByUuid(uuid, qid);
        model.addAttribute("results", results);
        return "questions/results";
    }
/*
    @PostMapping("/results/save")
    public Results createResult(@RequestBody Results result) {
        return resultService.save(result);
    }

    @PutMapping("/results/{id}")
    public ResponseEntity<Results> updateResult(@PathVariable Long id, @RequestBody Results updatedResult) {
        Optional<Results> existingResult = resultService.findById(id);

        if (existingResult.isPresent()) {
            Results result = existingResult.get();
            result.setUuid(updatedResult.getUuid());
            result.setQuestionId(updatedResult.getQuestionId());
            result.setAnswerNo(updatedResult.getAnswerNo());
            result.setCorrectflag(updatedResult.getCorrectflag());

            return ResponseEntity.ok(resultService.save(result));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        if (resultService.findById(id).isPresent()) {
            resultService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

 */
}
