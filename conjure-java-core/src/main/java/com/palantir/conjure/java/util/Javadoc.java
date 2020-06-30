/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.util;

import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.Documentation;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.CoreHtmlNodeRenderer;
import org.commonmark.renderer.html.HtmlRenderer;

/** Provides utility functionality for rendering javadoc. */
public final class Javadoc {

    private static final Parser parser = Parser.builder().build();
    private static final Renderer renderer = HtmlRenderer.builder()
            // Escaping existing HTML may break docs that rendered fine previously.
            .escapeHtml(false)
            // Disable paragraph tags, prefer raw text instead. Otherwise
            // all javadoc will be wrapped in paragraphs.
            .nodeRendererFactory(context -> new CoreHtmlNodeRenderer(context) {
                private boolean firstParagraph = true;

                @Override
                public void visit(Paragraph paragraph) {
                    // Exclude the first set of paragraph tags for javadoc style.
                    if (firstParagraph) {
                        firstParagraph = false;
                        visitChildren(paragraph);
                    } else {
                        super.visit(paragraph);
                    }
                }
            })
            .build();

    public static String render(Documentation documentation) {
        String rawDocumentation = StringUtils.stripToEmpty(documentation.get());
        if (StringUtils.isBlank(rawDocumentation)) {
            return "";
        }
        String renderedHtml = renderer.render(parser.parse(rawDocumentation));
        return StringUtils.appendIfMissing(renderedHtml, "\n");
    }

    public static Optional<String> render(Optional<Documentation> documentation, Optional<Documentation> deprecation) {
        if (!documentation.isPresent() && !deprecation.isPresent()) {
            return Optional.empty();
        }
        StringBuilder sb = new StringBuilder();
        documentation.map(Javadoc::render).ifPresent(sb::append);
        deprecation.map(Javadoc::getDeprecatedJavadoc).ifPresent(sb::append);
        return sb.length() > 0 ? Optional.of(sb.toString()) : Optional.empty();
    }

    public static Optional<String> getParameterJavadoc(ArgumentDefinition argument, EndpointDefinition endpoint) {
        return argument.getDocs()
                .map(docs -> "@param "
                        + JavaNameSanitizer.sanitizeParameterName(
                                argument.getArgName().get(), endpoint)
                        + " "
                        + Javadoc.render(argument.getDocs().get()));
    }

    public static String getRequestLine(HttpMethod httpMethod, HttpPath httpPath) {
        return String.format("@apiNote {@code %s %s}\n", httpMethod, httpPath);
    }

    public static String getDeprecatedJavadoc(Documentation deprecationDocs) {
        return "@deprecated " + render(deprecationDocs);
    }

    private Javadoc() {}
}
