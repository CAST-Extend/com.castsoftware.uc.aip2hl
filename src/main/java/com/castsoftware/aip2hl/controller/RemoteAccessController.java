package com.castsoftware.aip2hl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.castsoftware.aip2hl.component.AnalysisComponent;
import com.castsoftware.aip2hl.config.AIPRestConfig;
import com.castsoftware.aip2hl.config.CodeSourceFolderDAO;
import com.castsoftware.aip2hl.model.AplicationDTO;
import com.castsoftware.aip2hl.model.ApplStatusDTO;
import com.castsoftware.aip2hl.model.CodeSourceFolderDTO;
import com.castsoftware.aip2hl.model.ProcessDetail;
import com.castsoftware.aip2hl.model.aad.AADApplication;
import com.castsoftware.aip2hl.model.hl.HLApplication;
import com.castsoftware.restapi.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Controller
public class RemoteAccessController {
	private static final Logger log = LoggerFactory.getLogger(RemoteAccessController.class);

	@Autowired
	private AIPRestConfig restConfig;

	@Autowired
	private MainController mainController;

	@Autowired
	private CodeSourceFolderDAO csfd;

	@Autowired
	public AnalysisComponent applAnalysis;

	@RequestMapping("/ping")
	@ResponseBody
	public JsonResponse ping() {
		return new JsonResponse(0, "pong");
	}

	@RequestMapping("/runHighlight")
	@ResponseBody
	public JsonResponse runHighlight(@ModelAttribute("applName") String applName) {
		// does the application exist in highlight?
		int code = 0;
		String message = "UNKNOWN ERROR!";
		AADApplication appl = null;

		HLApplication hlAppl = applInHL(applName);
		if (hlAppl == null) {
			message = "APPLICATION NOT FOUND IN HIGHLIGHT!";
			code = -1;
		} else {
			// does the application exist in aip, if so get the latest version?
			appl = getLatestCastAppl(applName);
			if (appl == null) {
				message = "APPLICATION NOT FOUND IN AIP!";
				code = -2;
			} else {
				// now get the source location
				List<String> source = getSourceLocation(appl);
				if (source == null || source.isEmpty()) {
					message = "SOURCE FOLDER NOT FOUND!";
					code = -3;
				} else if (source.size() > 1) {
					message = "MULTIPLE SOURCE LOCATIONS FOUND!";
					code = -4;
				} else {
					// we have all required information, trigger the HL run
					ProcessDetail pd = mainController.createAnalysis(applName, source.get(0), appl.getAdgVersion());
					applAnalysis.add(pd);
					message = "HIGHLIGHT ANALYSIS QUEUED";
					code = pd.getId();
				}
			}
		}

		return new JsonResponse(code, message);
	}

	@RequestMapping("/runStatus")
	@ResponseBody
	public JsonResponse runStatus(@ModelAttribute("runId") int runId) {
		int code = 0;
		String message = "UNKNOWN ERROR!";
		ProcessDetail pDetail = null;

		List<ProcessDetail> applList = applAnalysis.getApplAnalysisList();
		ProcessDetail status=null;
		for (ProcessDetail pd: applList)
		{
			if (pd.getId()==runId)
			{
				status=pd;
				break;
			}
		}
		
		if (status!=null)
		{
			message = status.getStep();
			if (status.getStatus().equals("Running"))
			{
				code = 0;
			} else if (status.getStatus().equals("Success")) {
				code = 1;
			} else if (status.getStatus().equals("Fail")) {
				code = -1;
			}
		} else {
			message = "UNKNOWN RUN ID!";
			code = -2;
		} 
		return new JsonResponse(code, message);
	}

	@RequestMapping("/allApplications")
	@ResponseBody
	public JsonResponse listCastApplications() {
		int code = 0;
		String message;

		List<AADApplication> applList = getAllCastApplications();
		List<String> list = new ArrayList<String>();
		if (applList.size() > 0) {
			for (AADApplication appl : applList) {
				list.add(appl.getName());
			}
		} else {
			code = -1;
		}
		message = new Gson().toJson(list.toArray());

		return new JsonResponse(code, message);
	}

	private List<String> getSourceLocation(AADApplication aipApp) {
		List rslt = new ArrayList<String>();
		AplicationDTO app = new AplicationDTO();
		app.setAipApp(aipApp);

		try {
			// get the application source folder(s)
			List<CodeSourceFolderDTO> src = csfd.getSource(aipApp.getMngtSchema());
			for (CodeSourceFolderDTO s : src) {
				rslt.add(s.getDeployPath());
			}
		} catch (UncategorizedSQLException e) {
			rslt = null;
			log.debug(String.format("No source code found for %s", aipApp.getName()));
		}
		return rslt;
	}

	private AADApplication getLatestCastAppl(String applName) {
		AADApplication rslt = null;
		List<AADApplication> castAppls = getAllCastApplications();
		List<AADApplication> shortList = new ArrayList<AADApplication>();

		for (AADApplication appl : castAppls) {
			if (appl.getName().equals(applName)) {
				shortList.add(appl);
			}
		}

		if (!shortList.isEmpty()) {
			shortList.sort(Comparator.comparing(AADApplication::getAdgVersion).reversed());
			rslt = shortList.get(0);
		}

		return rslt;
	}

	private List<AADApplication> getAllCastApplications() {
		String url = String.format("%sAAD/applications", restConfig.getAADBaseURL());
		ResponseEntity<List<AADApplication>> applResponse = restConfig.getAADRestTemplate().exchange(url,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<AADApplication>>() {
				});
		return applResponse.getBody();
	}

	private HLApplication applInHL(String applName) {
		List<HLApplication> hlApps = mainController.getAllHLApps();

		HLApplication found = null;
		for (HLApplication app : hlApps) {
			if (app.getName().equals(applName)) {
				found = app;
				break;
			}
		}
		return found;
	}

}
