package br.com.caelum.brutal.interceptors;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.http.MutableResponse;
import br.com.caelum.vraptor4.http.MutableResponse.RedirectListener;
import br.com.caelum.vraptor4.interceptor.Interceptor;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;

@Intercepts
public class RedirectCommitInterceptor implements Interceptor {
	@Inject private Session session;
	@Inject private MutableResponse response;
	@Inject private Validator validator;

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object instance) {
		response.addRedirectListener(new RedirectListener() {
			@Override
			public void beforeRedirect() {
				Transaction transaction = session.getTransaction();
				if (!validator.hasErrors() && transaction.isActive()) {
					session.flush();
				}
			}
		});
		stack.next(method, instance);
	}

}