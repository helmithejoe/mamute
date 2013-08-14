package br.com.caelum.brutal.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;

@Controller
public class VerifyUsersController {
	
	private Session session;
	private Logger LOG = Logger.getLogger(VerifyUsersController.class);
	
	@Deprecated
	//CDI eyes only
	public VerifyUsersController() {
	}

	@Inject
	public VerifyUsersController(Session session) {
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	@Post("/asjkdjnjsaknfknsdklglas")
	public void verify(Result r){
		List<User> users = session.createCriteria(User.class).list();
		List<User> invalid = new ArrayList<>();
		for (User user : users) {
			if (!isValid(user)) {
				invalid.add(user);
			}
		}
		r.include("invalid", invalid);
	}

	private boolean isValid(User user) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> errors = validator.validate(user);
		if(!errors.isEmpty()){
			LOG.warn("The user "+user.getName()+" with id "+user.getId()+" has the errors bellow:");
			for (ConstraintViolation<User> constraintViolation : errors) {
				Object constraint = constraintViolation.getConstraintDescriptor().getAnnotation();
				LOG.warn(constraint);
			}
			LOG.warn("------------------------------------------------");
			return false;
		}
		return true;
	}

}
