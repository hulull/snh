package com.snh.modian;

import com.snh.modian.task.ModianPkTask;
import com.snh.modian.task.ModianTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SnhApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SnhApplication.class, args);
		ModianTask modianTask = context.getBean(ModianTask.class);
		Thread thread = new Thread(modianTask);
		thread.setName("ModianTask");
		thread.start();
		ModianPkTask modianPkTask = context.getBean(ModianPkTask.class);
		Thread pkThread = new Thread(modianPkTask);
		pkThread.setName("ModianPkTask");
		pkThread.start();
	}
}
