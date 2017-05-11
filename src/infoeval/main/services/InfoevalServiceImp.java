package infoeval.main.services;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@RestController 
@EnableAutoConfiguration
public class InfoevalServiceImp implements InfoevalService {
	@Override
	@RequestMapping("Queries/Query2")
	public List<String> getBornInPlaceYear() {
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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(InfoevalServiceImp.class, args);
    }


}

