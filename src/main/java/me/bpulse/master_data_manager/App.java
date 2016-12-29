package me.bpulse.master_data_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
	
	@Autowired
	private MasterDataManagerService masterDataManagerService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
	
	public void run(String... args) {
		masterDataManagerService.startProcess();
	}
	
}