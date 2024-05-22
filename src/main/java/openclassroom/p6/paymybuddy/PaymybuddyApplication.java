package openclassroom.p6.paymybuddy;

import openclassroom.p6.paymybuddy.service.InitDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(PaymybuddyApplication.class);
	private static final String LOG_ID = "[paymybuddy]";

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private InitDB initializer;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	@Override
	public void run(String... args) {
		initializer.initTables();
		logger.info("{} - password: test - encoding: {}",LOG_ID, passwordEncoder.encode("test"));
	}
}
