/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded.modules.subcommands;

import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRule;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRuleContext;
import dev.jorel.commandapi.annotations.reloaded.annotations.Subcommand;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Semantic check that sub-commands can only go on normal classes. I.e. not enums, records etc
 */
public class RuleTypeSubCommandsCanOnlyGoOnNormalClasses implements SemanticRule {

	@Override
	public boolean passes(SemanticRuleContext context) {
		boolean passes = true;
		for (Element element : context.roundEnv().getElementsAnnotatedWith(Subcommand.class)) {
			if (element instanceof TypeElement classElement) {
				if (classElement.getKind() != ElementKind.CLASS) {
					context.logging().complain(classElement,
						"@%s cannot go on %s types"
							.formatted(
								Subcommand.class.getSimpleName(),
								classElement.getKind()
							)
					);
					passes = false;
				}
			}
		}
		return passes;
	}
}