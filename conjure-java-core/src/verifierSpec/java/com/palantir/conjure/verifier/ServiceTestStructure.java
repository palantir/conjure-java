package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = ServiceTestStructure.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ServiceTestStructure {
    private final Optional<String> auth;

    private final Optional<String> cookie;

    private final Optional<Map<String, String>> headerParams;

    private final Optional<Map<String, Object>> pathParams;

    private final Optional<Object> body;

    private volatile int memoizedHashCode;

    private ServiceTestStructure(
            Optional<String> auth,
            Optional<String> cookie,
            Optional<Map<String, String>> headerParams,
            Optional<Map<String, Object>> pathParams,
            Optional<Object> body) {
        validateFields(auth, cookie, headerParams, pathParams, body);
        this.auth = auth;
        this.cookie = cookie;
        this.headerParams = headerParams;
        this.pathParams = pathParams;
        this.body = body;
    }

    @JsonProperty("auth")
    public Optional<String> getAuth() {
        return this.auth;
    }

    @JsonProperty("cookie")
    public Optional<String> getCookie() {
        return this.cookie;
    }

    @JsonProperty("header-params")
    public Optional<Map<String, String>> getHeaderParams() {
        return this.headerParams;
    }

    @JsonProperty("path-params")
    public Optional<Map<String, Object>> getPathParams() {
        return this.pathParams;
    }

    @JsonProperty("body")
    public Optional<Object> getBody() {
        return this.body;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ServiceTestStructure && equalTo((ServiceTestStructure) other));
    }

    private boolean equalTo(ServiceTestStructure other) {
        return this.auth.equals(other.auth)
                && this.cookie.equals(other.cookie)
                && this.headerParams.equals(other.headerParams)
                && this.pathParams.equals(other.pathParams)
                && this.body.equals(other.body);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(auth, cookie, headerParams, pathParams, body);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("ServiceTestStructure")
                .append("{")
                .append("auth")
                .append(": ")
                .append(auth)
                .append(", ")
                .append("cookie")
                .append(": ")
                .append(cookie)
                .append(", ")
                .append("header-params")
                .append(": ")
                .append(headerParams)
                .append(", ")
                .append("path-params")
                .append(": ")
                .append(pathParams)
                .append(", ")
                .append("body")
                .append(": ")
                .append(body)
                .append("}")
                .toString();
    }

    private static void validateFields(
            Optional<String> auth,
            Optional<String> cookie,
            Optional<Map<String, String>> headerParams,
            Optional<Map<String, Object>> pathParams,
            Optional<Object> body) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, auth, "auth");
        missingFields = addFieldIfMissing(missingFields, cookie, "cookie");
        missingFields = addFieldIfMissing(missingFields, headerParams, "header-params");
        missingFields = addFieldIfMissing(missingFields, pathParams, "path-params");
        missingFields = addFieldIfMissing(missingFields, body, "body");
        if (missingFields != null) {
            throw new IllegalArgumentException(
                    "Some required fields have not been set: " + missingFields);
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(5);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private Optional<String> auth = Optional.empty();

        private Optional<String> cookie = Optional.empty();

        private Optional<Map<String, String>> headerParams = Optional.empty();

        private Optional<Map<String, Object>> pathParams = Optional.empty();

        private Optional<Object> body = Optional.empty();

        private Builder() {}

        public Builder from(ServiceTestStructure other) {
            auth(other.getAuth());
            cookie(other.getCookie());
            headerParams(other.getHeaderParams());
            pathParams(other.getPathParams());
            body(other.getBody());
            return this;
        }

        @JsonSetter("auth")
        public Builder auth(Optional<String> auth) {
            this.auth = Objects.requireNonNull(auth, "auth cannot be null");
            return this;
        }

        public Builder auth(String auth) {
            this.auth = Optional.of(Objects.requireNonNull(auth, "auth cannot be null"));
            return this;
        }

        @JsonSetter("cookie")
        public Builder cookie(Optional<String> cookie) {
            this.cookie = Objects.requireNonNull(cookie, "cookie cannot be null");
            return this;
        }

        public Builder cookie(String cookie) {
            this.cookie = Optional.of(Objects.requireNonNull(cookie, "cookie cannot be null"));
            return this;
        }

        @JsonSetter("header-params")
        public Builder headerParams(Optional<Map<String, String>> headerParams) {
            this.headerParams =
                    Objects.requireNonNull(headerParams, "header-params cannot be null");
            return this;
        }

        public Builder headerParams(Map<String, String> headerParams) {
            this.headerParams =
                    Optional.of(
                            Objects.requireNonNull(headerParams, "header-params cannot be null"));
            return this;
        }

        @JsonSetter("path-params")
        public Builder pathParams(Optional<Map<String, Object>> pathParams) {
            this.pathParams = Objects.requireNonNull(pathParams, "path-params cannot be null");
            return this;
        }

        public Builder pathParams(Map<String, Object> pathParams) {
            this.pathParams =
                    Optional.of(Objects.requireNonNull(pathParams, "path-params cannot be null"));
            return this;
        }

        @JsonSetter("body")
        public Builder body(Optional<Object> body) {
            this.body = Objects.requireNonNull(body, "body cannot be null");
            return this;
        }

        public Builder body(Object body) {
            this.body = Optional.of(Objects.requireNonNull(body, "body cannot be null"));
            return this;
        }

        public ServiceTestStructure build() {
            return new ServiceTestStructure(auth, cookie, headerParams, pathParams, body);
        }
    }
}
