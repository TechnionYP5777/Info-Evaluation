package infoeval.main.services;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
    @RequestMapping("/test")
    public Test test() {
    	Test $ = new Test();
    	$.setNum(7);
    	$.setStr("Green");
    	return $;
    }
}
