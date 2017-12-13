package com.castsoftware.aip2hl.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.castsoftware.aip2hl.component.AnalysisComponent;
import com.castsoftware.aip2hl.config.AIPRestConfig;
import com.castsoftware.aip2hl.config.CodeSourceFolderDAO;
import com.castsoftware.aip2hl.config.HLConfig;
import com.castsoftware.aip2hl.config.HLRestConfig;
import com.castsoftware.aip2hl.model.AnalysisDTO;
import com.castsoftware.aip2hl.model.AplicationDTO;
import com.castsoftware.aip2hl.model.ApplStatusDTO;
import com.castsoftware.aip2hl.model.CodeSourceFolderDTO;
import com.castsoftware.aip2hl.model.ProcessDetail;
import com.castsoftware.aip2hl.model.aad.AADApplication;
import com.castsoftware.aip2hl.model.hl.HLAppRequired;
import com.castsoftware.aip2hl.model.hl.HLApplRslt;
import com.castsoftware.aip2hl.model.hl.HLApplication;
import com.castsoftware.aip2hl.model.hl.HLDomain;

@Controller
public class GreetingController {
	private static final Logger log = LoggerFactory.getLogger(GreetingController.class);

	@Autowired
	private AIPRestConfig restConfig;
	@Autowired
	private HLRestConfig hlRestConfig;
	@Autowired
	private CodeSourceFolderDAO csfd;
	@Autowired
	public ApplStatusDTO applStatus;

	@Autowired
	public AnalysisComponent applAnalysis;

	private String version = null;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setAutoGrowCollectionLimit(768);
	}
	@RequestMapping({ "/", "/apps" })
	public String populateSeedStarters(Model model, @ModelAttribute("version") String pVersion,
			@ModelAttribute("haveSource") String haveSource) {

		AnalysisDTO analysisDTO = new AnalysisDTO();
		model.addAttribute("appls", analysisDTO);

		if (pVersion.length() == 0)
			version = "All";
		else
			version = pVersion;

		String url = String.format("%sAAD/applications", restConfig.getAADBaseURL());
		log.debug(url);
		ArrayList<AplicationDTO> allAppsFinal = new ArrayList<AplicationDTO>();
		;
		try {
			ResponseEntity<List<AADApplication>> applResponse = restConfig.getAADRestTemplate().exchange(url,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<AADApplication>>() {
					});

			List<HLApplication> hlApps = getAllHLApps();

			List<String> versions = new ArrayList();
			model.addAttribute("versionList", versions);
			versions.add("All");

			List<AADApplication> allApps = applResponse.getBody();
			StringBuffer finalSrc = new StringBuffer();

			for (AADApplication aipApp : allApps) {
				boolean versionFound = false;
				Collections.sort(versions);
				for (String v : versions) {
					if (v.equals(aipApp.getAdgVersion())) {
						versionFound = true;
						break;
					}
				}
				if (!versionFound) {
					versions.add(aipApp.getAdgVersion());
				}
				if (version == null || version.equalsIgnoreCase("all")
						|| aipApp.getAdgVersion().equalsIgnoreCase(version)) {
					AplicationDTO app = new AplicationDTO();

					app.setAipApp(aipApp);

					try {
						// get the application source folder(s)
						List<CodeSourceFolderDTO> src = csfd.getSource(aipApp.getMngtSchema());
						finalSrc.setLength(0);
						boolean second = false;
						for (CodeSourceFolderDTO s : src) {
							if (second)
								finalSrc.append(",");
							finalSrc.append(s.getDeployPath());
						}
						app.setSourceFolder(finalSrc.toString().replace("\\", "/"));
					} catch (UncategorizedSQLException e) {
						app.setSourceFolder(null);
						log.debug(String.format("No source code found for %s", aipApp.getName()));
					}

					if ((haveSource.equalsIgnoreCase("true") && app.getSourceFolder().equals("No"))
							|| (haveSource.equalsIgnoreCase("false") && !app.getSourceFolder().equals("No"))) {
						continue;
					}

					HLApplication thisApp = applInHL(aipApp.getName(), hlApps);
					app.setHlApp(thisApp);
					allAppsFinal.add(app);

				}
			}
		} catch (ResourceAccessException ex) {
			log.error("AAD not accessable", ex);

			if (allAppsFinal == null) {
				allAppsFinal = new ArrayList<AplicationDTO>(0);
			}
		}

		analysisDTO.setApplList(allAppsFinal);

		return "applications";
	}

	@RequestMapping(value = "/analysisStatus")
	public String analysisStatus(Model model) {
		model.addAttribute("applStatus", applAnalysis.getApplAnalysisList());

		return "results :: resultsList";
	}

	@RequestMapping(value = "/add")
	public String createApp(@ModelAttribute("applName") String applName, @ModelAttribute("version") String pVersion) {

		applStatus.setApplName(applName);
		applStatus.setVersionFilter(pVersion);

		if (applInHL(applName) != null) {
			applStatus.setMessage("%s already exists!");
		}

		// get domain for all appls
		String url = String.format("%sdomains/%d", hlRestConfig.getBaseURL(),
				hlRestConfig.getHLConfig().getAllApplDomain());
		RestTemplate restTemplate = hlRestConfig.getRestTemplate();
		HLDomain domainAllAppls = restTemplate.getForObject(url, HLDomain.class);

		// create the json object
		HLAppRequired[] json = new HLAppRequired[1];
		HLDomain[] domains = new HLDomain[1];

		HLAppRequired appl = new HLAppRequired();
		appl.setName(applName);
		domains[0] = domainAllAppls;
		appl.setDomains(domains);
		json[0] = appl;

		url = String.format("%sdomains/%d/applications", hlRestConfig.getBaseURL(),
				hlRestConfig.getHLConfig().getDefaultDomain());

		HttpEntity<HLAppRequired[]> request = new HttpEntity<>(json);
		restTemplate.postForObject(url, request, HLApplRslt.class);

		if (applInHL(applName) == null) {
			applStatus.setMessage("Error Creating Application in Highlight");
		} else {
			applStatus.setMessage(String.format("%s has been sucessfuly created", applName));
		}

		return String.format("redirect:/apps?version=%s", pVersion);
	}

	@RequestMapping("/analyze")
	public String analyze(@ModelAttribute("applName") String applName, @ModelAttribute("version") String pVersion,
			@ModelAttribute("source") String pSource, @ModelAttribute("adgVersion") String adgVersion) {

		applStatus.setApplName(applName);
		applStatus.setVersionFilter(pVersion);
		applStatus.setApplSource(pSource);

		ProcessDetail pd = createAnalysis(applName, pSource, adgVersion);
		applAnalysis.add(pd);
		applStatus.setMessage(String.format("%s queued for analysis", applName));

		return String.format("redirect:/apps?version=%s", pVersion);
	}

	private ProcessDetail createAnalysis(String applName, String applSource, String adgVersion) {
		HLConfig hlConfig = hlRestConfig.getHLConfig();

		File jar = new File(hlConfig.getJar());
		File resultsFolder = new File(String.format("%s/%s", hlConfig.getResultsFolder(), applName));
		File perlFolder = new File(hlConfig.getPerlFolder());
		File java = new File(String.format("%s/java.exe", hlConfig.getJavaBinFolder()));
		File sourceFolder = new File(applSource);

		HLApplication hlAppl = applInHL(applName);

		List<String> params = new ArrayList<String>();
		// params.add("cmd.exe"); params.add("/c");
		params.add(java.toString());

		if (hlConfig.getProxyHost().length() > 0) {
			params.add(String.format("-Dhttps.proxyHost=%s", hlConfig.getProxyHost()));
			params.add(String.format("-Dhttps.proxyPort=%s", hlConfig.getProxyPort()));
		}

		params.add("-jar");
		params.add(jar.toString());
		params.add("--login");
		params.add(hlConfig.getUserName());
		params.add("--password");
		params.add(hlConfig.getPassword());
		params.add("--companyId");
		params.add(String.format("%d", hlConfig.getDefaultDomain()));
		params.add("--applicationId");
		params.add(String.format("%d", hlAppl.getId()));
		params.add("--analyzerDir");
		params.add(perlFolder.toString());
		params.add("--workingDir");
		params.add(resultsFolder.toString());
		params.add("--sourceDir");
		params.add(sourceFolder.toString());
		params.add("--serverUrl");
		params.add(hlConfig.getServerUrl());

		ProcessBuilder builder = new ProcessBuilder(params);

		log.info(builder.command().stream().map(Object::toString).collect(Collectors.joining(", ")));

		return new ProcessDetail(applName, adgVersion, builder);

	}

	@RequestMapping(params = "checked", value = "/bulkAction", method = RequestMethod.POST)
	public String analyzeChecked(@ModelAttribute AnalysisDTO form, @ModelAttribute("version") String pVersion,
			Model model) {

		ArrayList<AplicationDTO> appList = form.getApplList();

		for (AplicationDTO appl : appList) {
			if (appl.isSelected()) {
				String applName = appl.getAipApp().getName();
				String applSource = appl.getSourceFolder();
				String adgVersion = appl.getAipApp().getAdgVersion();

				ProcessDetail pd = new ProcessDetail();

				if (applName == null || applName.length() == 0 || applSource == null || applSource.length() == 0) {
					pd = new ProcessDetail();
					pd.setApplName(applName);
					pd.setAdgVersion(adgVersion);
					pd.setStatus("Input Error");
				} else {
					pd = createAnalysis(applName, applSource, adgVersion);
				}

				applAnalysis.add(pd);
			}
		}

		return String.format("redirect:/apps?version=%s", pVersion);
	}

	@RequestMapping(params = "all", value = "/bulkAction", method = RequestMethod.POST)
	public String analyzeAll(@ModelAttribute AnalysisDTO form, @ModelAttribute("version") String pVersion,
			Model model) {
		ArrayList<AplicationDTO> appList = form.getApplList();

		return String.format("redirect:/apps?version=%s", pVersion);
	}

	@Bean(name = "applStatus")
	public ApplStatusDTO applStatus() {
		return new ApplStatusDTO();
	}

	private HLApplication applInHL(String applName, List<HLApplication> hlApps) {
		HLApplication found = null;
		for (HLApplication app : hlApps) {
			if (app.getName().equals(applName)) {
				found = app;
				break;
			}
		}
		return found;
	}

	private HLApplication applInHL(String applName) {
		List<HLApplication> hlApps = getAllHLApps();
		return applInHL(applName, hlApps);
	}

	private List<HLApplication> getAllHLApps() {
		String url = String.format("%sdomains/%d/applications?expand=domain", hlRestConfig.getBaseURL(),
				hlRestConfig.getHLConfig().getDefaultDomain());
		log.debug(url);
		HLApplication[] allApps = hlRestConfig.getRestTemplate().getForObject(url, HLApplication[].class);

		return Arrays.asList(allApps);
	}

}