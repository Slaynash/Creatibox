package fr.slaynash.creatibox.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogTimer {

	public static String getTimeForLog() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		return "["+dtf.format(LocalDateTime.now())+"]";
	}
	
}
