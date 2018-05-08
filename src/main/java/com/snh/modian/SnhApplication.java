package com.snh.modian;

import com.snh.modian.domain.modian.Order;
import com.snh.modian.task.ModianTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SnhApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SnhApplication.class, args);
		ModianTask modianTask = context.getBean(ModianTask.class);
		new Thread(modianTask).start();
	}
}
