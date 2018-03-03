package org.marco.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.marco.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class WebCrawlerService {
	private String exportTo;
	private NavigateService navigateService;

	@Autowired
	public WebCrawlerService(NavigateService navigateService, @Value("${webcrawler.export.to:sitemap.json}") String exportTo) {
		this.exportTo = exportTo;
		this.navigateService = navigateService;
	}
	
	/**
     * Discover all pages related with domain and export it 
     * in a sitemap that uses json format
     *
     * @param   domain
     *          the url domain to discover all pages related
     *
     * @return  none
     *
     * @throws  Exception
     *          if an error occurs an exception will be handle
     */
	public void execute(String domain) throws Exception {
		exportSitemap(navigateService.execute(domain));
	}

	private void exportSitemap(List<Link> links) throws IOException {
		Files.write(Paths.get(exportTo), new Gson().toJson(links).getBytes());
	}
}
