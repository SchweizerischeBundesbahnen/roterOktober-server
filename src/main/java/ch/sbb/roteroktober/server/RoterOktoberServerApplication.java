package ch.sbb.roteroktober.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
@EnableSwagger2
public class RoterOktoberServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoterOktoberServerApplication.class, args);
	}
}
