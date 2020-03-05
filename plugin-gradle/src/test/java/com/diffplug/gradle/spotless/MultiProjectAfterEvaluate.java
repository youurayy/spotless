/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.gradle.spotless;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

/** Reproduces https://github.com/diffplug/spotless/issues/506 */
public class MultiProjectAfterEvaluate extends GradleIntegrationTest {
	@Test
	public void failureDoesntTriggerAll() throws IOException {
		setFile("settings.gradle").toLines("include 'sub'");
		setFile("build.gradle").toLines("buildscript { repositories { mavenCentral() }}");
		setFile("sub/build.gradle")
				.toLines(
						"plugins {",
						"  id 'com.diffplug.gradle.spotless'",
						"  id 'java'",
						"}",
						"spotless { java { googleJavaFormat() } }");
		String output = gradleRunner6().withArguments("spotlessApply", "--warning-mode", "all").build().getOutput().replace("\r\n", "\n");
		Assertions.assertThat(output).contains("Using method Project#afterEvaluate(Action) when the project is already evaluated has been deprecated.");
	}

	private final GradleRunner gradleRunner6() throws IOException {
		return gradleRunner().withGradleVersion("6.0");
	}
}
