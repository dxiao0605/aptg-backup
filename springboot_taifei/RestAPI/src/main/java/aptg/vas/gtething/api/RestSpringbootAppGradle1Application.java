package aptg.vas.gtething.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RestSpringbootAppGradle1Application {

	public static void main(String[] args) {

		SpringApplication.run(RestSpringbootAppGradle1Application.class, args);
		log.info("Ap Start......程式開始");
	}

}
