/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.annotations;

import com.google.common.annotations.Beta;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an RPC endpoint.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Handle {

    /**
     * Generate annotation which is implied by default, however may be applied to an interface to
     * opt out of codegen using <code>@Handle.Generate(false)</code>, for instance to allow a subclass/implementor
     * to opt into code-generation. Implementors may annotate themselves with <code>@Handle.Generate</code>.
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @interface Generate {
        boolean value() default true;
    }

    HttpMethod method();

    /**
     * Request path.
     *
     * Follows conjure format. Path parameter names must correspond to parameters on the annotated method.
     *
     * @see
     * <a href="https://github.com/palantir/conjure/blob/master/docs/spec/conjure_definitions.md#pathstring">Path string</a>
     */
    String path();

    /**
     * Response body {@link SerializerFactory}.
     *
     * @return class that implements a zero-arg constructor to be used to serialize the response
     */
    Class<? extends SerializerFactory> produces() default DefaultSerDe.class;

    // TODO(ckozak): Custom exception handling? Not sure it should be necessary if we support custom response status
    //  codes.

    /** Conjure endpoint tags. */
    String[] tags() default {};

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface Body {

        /**
         * Custom body {@link DeserializerFactory}.
         *
         * @return class that implements a zero-arg constructor to be used to deserialize the body. Defaults to
         * {@link DefaultSerDe}
         */
        Class<? extends DeserializerFactory> value() default DefaultSerDe.class;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface Header {
        String value();

        /**
         * Decoder for deserializing the header value. If omitted, tries to use a default decoder in
         * {@link ParamDecoders}.
         */
        Class<? extends CollectionParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface PathParam {
        /**
         * Decoder for deserializing the path parameter. If omitted, tries to use a default decoder in
         * {@link ParamDecoders}.
         */
        Class<? extends ParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }

    /**
     * Only supported at the end of a path for the time being.
     * Unlike {@link PathParam}, values are represented to the decoder as a collection of strings, each
     * URL segment delimited by an unencoded slash is represented as an element. For example, {@code /foo/bar}
     * is surfaced as {@code ['foo', 'bar']} while {@code /foo%2Fbar} is surfaced as {@code ['foo/bar']}.
     */
    @Beta
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface PathMultiParam {
        /**
         * Decoder for deserializing a path parameter that may span several path segments.
         * If omitted, tries to use a default decoder in {@link ParamDecoders}.
         */
        Class<? extends CollectionParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface QueryParam {
        String value();

        /**
         * Decoder for deserializing query parameter value. If omitted, tries to use a default decoder in
         * {@link ParamDecoders}.
         */
        Class<? extends CollectionParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface FormParam {
        String value();

        /**
         * Decoder for deserializing form parameter value. If omitted, tries to use a default decoder in
         * {@link ParamDecoders}.
         */
        Class<? extends CollectionParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.PARAMETER)
    @interface Cookie {
        String value();

        /**
         * Decoder for deserializing the cookie value. If omitted, tries to use a default decoder in
         * {@link ParamDecoders}.
         *
         * Note that if the annotated argument is using {@link com.palantir.tokens.auth.BearerToken} as type, it
         * also delegates to the conjure {@link com.palantir.conjure.java.undertow.lib.AuthorizationExtractor}.
         */
        Class<? extends ParamDecoder<?>> decoder() default DefaultParamDecoder.class;
    }
}
