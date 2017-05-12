package infoeval.main.services;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
    @RequestMapping("/test")
    public int test() {
    	return 42;
    }
}
