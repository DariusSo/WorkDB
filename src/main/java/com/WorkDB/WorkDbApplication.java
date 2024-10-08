package com.WorkDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class WorkDbApplication {

	//public static final CopyOnWriteArrayList<Email> completedTasks = new CopyOnWriteArrayList<>();
	public static final List<Email> completedTasks = new ArrayList<>();
	public static final ReentrantLock lockEmailThread = new ReentrantLock();
	public static Condition lockCondition = lockEmailThread.newCondition();


	public static void main(String[] args) throws InterruptedException {

		Thread thread = new Thread(new EmailThread());
		thread.start();
		Thread thread2 = new Thread(new EmailThread());
		thread2.start();
		Thread thread3 = new Thread(new EmailThread());
		thread3.start();
		Thread thread4 = new Thread(new EmailThread());
		thread4.start();


	}

}
