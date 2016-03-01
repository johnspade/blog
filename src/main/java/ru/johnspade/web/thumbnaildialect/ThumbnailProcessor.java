package ru.johnspade.web.thumbnaildialect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.attr.AbstractStandardSingleAttributeModifierAttrProcessor;

public class ThumbnailProcessor extends AbstractStandardSingleAttributeModifierAttrProcessor {

	public ThumbnailProcessor() {
		super("thumbnail");
	}

	@Override
	protected String getTargetAttributeName(Arguments arguments, Element element, String attributeName) {
		return "style";
	}

	@Override
	public int getPrecedence() {
		return 10000;
	}

	@Override
	protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName,
												   String newAttributeName) {
		return ModificationType.PREPEND_WITH_SPACE;
	}

	@Override
	protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName,
											 String newAttributeName) {
		return true;
	}

	@Override
	protected String getTargetAttributeValue(Arguments arguments, Element element, String attributeName) {
		String attributeValue = element.getAttributeValue(attributeName);
		Configuration configuration = arguments.getConfiguration();
		IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
		IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, attributeValue);
		Object result = expression.execute(configuration, arguments);
		Document doc = Jsoup.parse(result.toString());
		org.jsoup.nodes.Element image = doc.body().select("img").first();
		if (image != null) {
			String link = image.attr("src");
			if (link != null)
				return "background-image: url(" + link + ");";
		}
		return "";
	}
}
