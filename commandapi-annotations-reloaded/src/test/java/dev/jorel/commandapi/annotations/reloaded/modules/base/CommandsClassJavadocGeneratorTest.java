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
package dev.jorel.commandapi.annotations.reloaded.modules.base;

import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.modules.commands.CommandRegisterMethodGeneratorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandsClassJavadocGeneratorTest {

	CommandsClassJavadocGenerator generator;

	@BeforeEach
	void setUp() {
		generator = new CommandsClassJavadocGenerator();
	}

	@Test
	void generate(
		@Mock IndentedWriter out,
		@Captor ArgumentCaptor<String> outputLines,
		@Mock CommandsClassGeneratorContext context,
		@Mock CommandRegisterMethodGeneratorContext method1,
		@Mock CommandRegisterMethodGeneratorContext method2,
		@Mock CommandRegisterMethodGeneratorContext method3
	) {
		var generatorStarted = ZonedDateTime.of(2024, Month.APRIL.getValue(), 20, 17, 57, 23, 123456789, ZoneOffset.UTC);
		doReturn(generatorStarted).when(context).generatorStarted();
		doReturn(List.of(method1, method2, method3)).when(context).registerMethods();
		doReturn("my.package.name.MyCommand").when(method1).qualifiedCommandClassName();
		doReturn("my.package.name.MyOtherCommand").when(method2).qualifiedCommandClassName();
		doReturn("my.package.name.other.SomeCommand").when(method3).qualifiedCommandClassName();
		generator.generate(out, context);
		verify(out, times(8)).printOnNewLine(outputLines.capture());
		var outputsIterator = outputLines.getAllValues().iterator();
		assertEquals("/**", outputsIterator.next());
		assertEquals(" * This class was automatically generated by the CommandAPI", outputsIterator.next());
		assertEquals(" * Generation time: Sat Apr 20 17:57:23 Z 2024", outputsIterator.next());
		assertEquals(" * @Command classes used:", outputsIterator.next());
		assertEquals(" * - {@link my.package.name.MyCommand}", outputsIterator.next());
		assertEquals(" * - {@link my.package.name.MyOtherCommand}", outputsIterator.next());
		assertEquals(" * - {@link my.package.name.other.SomeCommand}", outputsIterator.next());
		assertEquals(" */", outputsIterator.next());
	}
}