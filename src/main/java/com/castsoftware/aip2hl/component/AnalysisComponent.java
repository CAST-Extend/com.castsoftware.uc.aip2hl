package com.castsoftware.aip2hl.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.castsoftware.aip2hl.config.HLRestConfig;
import com.castsoftware.aip2hl.controller.GreetingController;
import com.castsoftware.aip2hl.model.ApplStatusDTO;
import com.castsoftware.aip2hl.model.ProcessDetail;
import com.castsoftware.aip2hl.model.hl.HLApplication;

@Component
public class AnalysisComponent implements DisposableBean, Runnable {
	private static final Logger log = LoggerFactory.getLogger(AnalysisComponent.class);
	private volatile boolean runBackgroundAnalysis = true;
	private Thread thread;

	@Autowired
	private HLRestConfig hlRestConfig;

	private List<ProcessDetail> applAnalysisList;

	public AnalysisComponent() {
		this.thread = new Thread(this);
		this.applAnalysisList = new ArrayList<ProcessDetail>();
		this.thread.start();
	}

	@Override
	public void run() {
		while (runBackgroundAnalysis) {
			try {
				for (int ii=applAnalysisList.size()-1;ii>=0;ii--) {
					ProcessDetail pd = applAnalysisList.get(ii);
					if (pd.getStatus().equals("Queue")) {
						runAnalysis(pd);
					}
				}
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					//nothing to do here
				}
			} catch (ConcurrentModificationException ex) {
				log.debug("New analysis added before completing the current" );
			}
			
		}
	}

	public void add(ProcessDetail pd) {
		boolean addAppl=true;
		for (ProcessDetail p: applAnalysisList)
		{
			if (p.equals(pd) && (p.getStatus().equals("Running") || p.getStatus().equals("Queue") ))
			{
				addAppl=false;
			} 
		}
		if (addAppl==true)
		{
			pd.setStatus("Queue");
			applAnalysisList.add(0,pd);	
		}
	
	}

	public List<ProcessDetail> getApplAnalysisList() {
		return applAnalysisList;
	}

	private void runAnalysis(ProcessDetail pd) {
		boolean status = false;
		Process p;
		pd.setStatus("Running");

		try {
			p = pd.getPb().start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					if (!pd.getStatus().equals("Success"))
					{
						pd.setStatus("Fail");
					}
					break;
				}
				switch (line) {
				case "Highlight automation completed successfully!":
					status = true;
					pd.setStatus("Success");
					break;
				case "Source directory does not exists":
					pd.setStatus(line);
					break;
				case "Preparing analysis...":
				case "Preparation stage completed.":
				case "Analysing...":
				case "Start discovering":
				case "Start analyzing":
				case "Analysis stage completed.":
				case "Uploading...":
				case "Upload stage completed.":
					pd.setStep(line);
				default:
					break;
				}
				log.info(line);
			}

		} catch (IOException e) {
			pd.setStatus(e.getMessage());
		}

	}

	@Override
	public void destroy() {
		runBackgroundAnalysis = false;
	}

}
