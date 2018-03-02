package org.marco.service;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class WebCrawlerTest {
	
	@InjectMocks
	private WebCrawlerService webCrawlerService = new WebCrawlerService("sitemap.json");
	
	@Spy
	private NavigateService navigateService = new NavigateService(5000);
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void can_process_a_simple_domain() throws Exception {
		webCrawlerService.execute("http://www.example.com");
		//TODO readFile?
	}
	
	@Test(expected = MalformedURLException.class)
	public void do_nothing() throws Exception   {
		webCrawlerService.execute("http ://www.e xample.com");
	}
	
	@Test(expected = MalformedURLException.class)
	public void do_nothing_two() throws Exception   {
		webCrawlerService.execute(null);
	}
}
