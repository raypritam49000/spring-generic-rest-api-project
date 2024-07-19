package com.generic.rest.api.project.utility;

import io.jsonwebtoken.lang.Classes;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EnumClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {


    EnumClassPathScanningCandidateComponentProvider() {
        super(false);
        addIncludeFilter(new IsEnumFilter());
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

    private static class IsEnumFilter implements TypeFilter {

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            String className = metadataReader.getClassMetadata().getClassName();
            Class<Enum> clazz = Classes.forName(className);
            return clazz.isEnum();
        }
    }

}
