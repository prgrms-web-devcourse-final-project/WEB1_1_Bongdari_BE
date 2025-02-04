package com.somemore.domains.search.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class OnElasticSearchEnabledCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String propertyName = (String) Objects.requireNonNull(metadata.getAnnotationAttributes(
                ConditionalOnElasticSearchEnabled.class.getName())).get("propertyName");
        String havingValue = (String) Objects.requireNonNull(metadata.getAnnotationAttributes(
                ConditionalOnElasticSearchEnabled.class.getName())).get("havingValue");

        String propertyValue = context.getEnvironment().getProperty(propertyName);

        return havingValue.equals(propertyValue != null ? propertyValue : "");
    }
}
