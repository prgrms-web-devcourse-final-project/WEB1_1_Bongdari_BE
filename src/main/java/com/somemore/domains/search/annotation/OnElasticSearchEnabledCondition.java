package com.somemore.domains.search.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

public class OnElasticSearchEnabledCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(
                ConditionalOnElasticSearchEnabled.class.getName());
        String propertyName = (String) attrs.getFirst("propertyName");
        String havingValue = (String) attrs.getFirst("havingValue");

        String propertyValue = context.getEnvironment().getProperty(propertyName);
        return havingValue.equals(propertyValue);
    }
}