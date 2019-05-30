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

import com.palantir.conjure.spec.Documentation;
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
                @Override
                public void visit(Paragraph paragraph) {
                    visitChildren(paragraph);
                }
            })
            .build();

    public static String render(Documentation documentation) {
        String rawDocumentation = documentation.get();
        if (StringUtils.isBlank(rawDocumentation)) {
            return "";
        }
        String renderedHtml = renderer.render(parser.parse(rawDocumentation));
        return StringUtils.appendIfMissing(renderedHtml, "\n");
    }

    private Javadoc() {}
}
