package org.bench4Q.agent.rbe;

import java.util.ArrayList;
import java.util.Date;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.TestPhase;

public class RBE {

	private Args m_args;
	private ArrayList<Workers> m_workers;
	private long TestInterval;

	public RBE() {
		m_workers = new ArrayList<Workers>();
		TestInterval = 0;
	}
	
	public RBE(Args arg) {
		this();
		m_args = arg;
	}



	public void startWorkers() {
		long startTime = System.currentTimeMillis();
		
		long prepairTime = m_args.getPrepair();
		long cooldown = m_args.getCooldown();
		long testInterval = 0;
		int testPhaseEndTime;
		for (TestPhase testPhase : m_args.getEbs()) {
			testPhaseEndTime = testPhase.getTriggerTime() + testPhase.getStdyTime();
			if (testPhaseEndTime > testInterval) {
				testInterval = testPhaseEndTime;
			}
		}
		
		EBStats.getEBStats().init(startTime, prepairTime, testInterval, cooldown);
		
		if (m_args.getRbetype().equalsIgnoreCase("closed")) {
			for (TestPhase testPhase : m_args.getEbs()) {
				m_workers.add(new Workers_Closed(startTime, testPhase.getTriggerTime(), testPhase
						.getStdyTime(), testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate() ,m_args));
			}
		} else if (m_args.getRbetype().equalsIgnoreCase("EBOpen")) {
			for (TestPhase testPhase : m_args.getEbs()) {
				m_workers.add(new Workers_EBOpen(startTime, testPhase.getTriggerTime(), testPhase
						.getStdyTime(), testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate() ,m_args));
			}
		}else if (m_args.getRbetype().equalsIgnoreCase("FullOpen")) {
			for (TestPhase testPhase : m_args.getEbs()) {
				m_workers.add(new Workers_ReqOpen(startTime, testPhase.getTriggerTime(), testPhase
						.getStdyTime(), testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate() ,m_args));
			}
		}else{
			System.out.println("Error parameter.");	
			System.out.println("Start closed as default.");
			for (TestPhase testPhase : m_args.getEbs()) {
				m_workers.add(new Workers_Closed(startTime, testPhase.getTriggerTime(), testPhase
						.getStdyTime(), testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate() ,m_args));
			}
		}
		
		for (Workers worker : m_workers) {
			worker.setDaemon(true);
			worker.start();
		}
		
		
		
		TestInterval = calculateTestInterval();
		long endTime = startTime +  TestInterval * 1000L;
		
		try {
			Thread.sleep(TestInterval * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean flag = true;
		
		while (((System.currentTimeMillis() - endTime) < 0) && flag) {
			flag = false;
			for (Workers worker : m_workers) {
				if (worker.isAlive()) {
					flag = true;
					worker.stop();
				}			
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		clear();
	}
	
	private void clear() {
		m_workers = null;
	}

	private long calculateTestInterval(){
		int max = 0;
		int workerEndTime;
		
		for (TestPhase testPhase : m_args.getEbs()) {
			workerEndTime = testPhase.getStdyTime() + testPhase.getTriggerTime();
			if(workerEndTime > max){
				max = workerEndTime;
			}
		}
		return max;
	}

	public Args getArgs() {
		return m_args;
	}

}
