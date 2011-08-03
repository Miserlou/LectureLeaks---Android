package org.ale.lectureleaks;

interface recordService {
    
	void start();
	void stop();
	boolean running();
	String getPath();

}