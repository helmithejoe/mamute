package br.com.caelum.brutal.infra;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.core.RequestInfo;
import br.com.caelum.vraptor4.core.OverrideComponent;
import br.com.caelum.vraptor4.restfulie.controller.ControllerNotFoundHandler;

@OverrideComponent
public class BrutalResourceNotFoundHandler implements ControllerNotFoundHandler {
	
	@Inject private MenuInfo menuInfo;

	private static final Logger LOG = Logger.getLogger(BrutalResourceNotFoundHandler.class);
	
	@Override
	public void couldntFind(RequestInfo request) {
		menuInfo.include();
		LOG.warn("Got 404 at url:" + request.getRequestedUri());
		FilterChain chain = request.getChain();
		try {
			chain.doFilter(request.getRequest(), request.getResponse());
		} catch (IOException e) {
			throw new InterceptionException(e);
		} catch (ServletException e) {
			throw new InterceptionException(e);
		}
	}

}
