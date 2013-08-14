package br.com.caelum.brutal.validators;

import javax.inject.Inject;

import br.com.caelum.brutal.controller.TagPageController;
import br.com.caelum.brutal.dao.TagPageDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.TagPage;
import br.com.caelum.vraptor4.Validator;

public class TagPageValidator {
	private Validator validator;
	private MessageFactory messageFactory;
	private TagPageDAO tagPages;

	@Deprecated
	public TagPageValidator() {
	}

	@Inject
	public TagPageValidator(Validator validator, MessageFactory messageFactory, TagPageDAO tagPages) {
		this.validator = validator;
		this.messageFactory = messageFactory;
		this.tagPages = tagPages;
	}
	
	public boolean validateCreationWithTag(String tagName){
		if(tagPages.existsOfTag(tagName)){
			validator.add(messageFactory.build("error", "tag_page.errors.already_exists", tagName));
		}
		validator.onErrorRedirectTo(TagPageController.class).showTagPage(tagName);
		return !validator.hasErrors();
	}

	public boolean validate(TagPage tagPage) {
		validator.validate(tagPage);
		return !validator.hasErrors();
	}
	
	public <T> T onErrorRedirectTo(Class<T> controller) {
		return validator.onErrorRedirectTo(controller);
	}

}
