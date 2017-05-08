package infoeval.main.services;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
@RestController 
public class InfoevalServiceImp implements InfoevalService {
	@Override
	@RequestMapping("/test")
	public List<String> getBornIn(String place, int year) {
		List<String> $=new ArrayList<String>();
		$.add("Yevgenia Shandalov");
		$.add("Moshiko Elisof");
		$.add("Netanel Felcher");
		$.add("Osher Hajaj");
		return $;
	}

	@Override
	public List<String> differentDeathPlace() {
		// TODO Auto-generated method stub
		return null;
	}

}
