package org.marco.service;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.marco.model.Link;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class NavigateServiceTest {
	
	@InjectMocks
	private NavigateService navigateService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void can_process_a_simple_domain() throws Exception {
		List<Link> lst = navigateService.execute("http://www.example.com");
		assertEquals(1, lst.size());
		assertEquals(1, lst.get(0).getChildrens().size());
	}
	
	@Test(expected = MalformedURLException.class)
	public void wrong_domain() throws Exception   {
		navigateService.execute("http ://www.ex amp le.com");
	}
	
	@Test
	public void domain_not_found() throws Exception  {
		List<Link> lst = navigateService.execute("http://www.examp .com");
		assertEquals(0, lst.size());
	}
	
	@Test(expected = MalformedURLException.class)
	public void null_domain() throws Exception   {
		navigateService.execute(null);
	}
}
