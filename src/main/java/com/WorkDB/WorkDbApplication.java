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
	public static final List<Picture> completedPictures = new ArrayList<>();


	public static void main(String[] args) throws InterruptedException {

		EmailThread emailThread = new EmailThread();

		Thread thread = new Thread(emailThread);
		thread.start();
		Thread thread2 = new Thread(emailThread);
		thread2.start();
		Thread thread3 = new Thread(emailThread);
		thread3.start();


		Thread pictureThread = new Thread(new PictureThread());
		pictureThread.start();
		Thread pictureThread2 = new Thread(new PictureThread());
		pictureThread2.start();
		Thread pictureThread3 = new Thread(new PictureThread());
		pictureThread3.start();

	}

}
